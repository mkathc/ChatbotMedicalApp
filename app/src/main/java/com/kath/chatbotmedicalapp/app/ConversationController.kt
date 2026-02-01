package com.kath.chatbotmedicalapp.app

import com.kath.chatbotmedicalapp.app.intent.IntentRouter
import com.kath.chatbotmedicalapp.app.intent.UserIntent
import com.kath.chatbotmedicalapp.app.state.ConversationStateStore
import com.kath.chatbotmedicalapp.data.bff.BffClient
import com.kath.chatbotmedicalapp.domain.model.AskCancelConfirmation
import com.kath.chatbotmedicalapp.domain.model.AssistantResponse
import com.kath.chatbotmedicalapp.domain.model.CancelAppointmentCommand
import com.kath.chatbotmedicalapp.domain.model.CreateReminderCommand
import com.kath.chatbotmedicalapp.domain.model.GetMedicalInfoCommand
import com.kath.chatbotmedicalapp.domain.model.MedicalInfoType
import com.kath.chatbotmedicalapp.domain.model.RunTriageCommand
import com.kath.chatbotmedicalapp.domain.model.ScheduleAppointmentCommand
import com.kath.chatbotmedicalapp.domain.tools.ToolDispatcher

class ConversationController(
    private val stateStore: ConversationStateStore,
    private val intentRouter: IntentRouter,
    private val dispatcher: ToolDispatcher,
    private val bffClient: BffClient
) {
    fun onUserText(userText: String): AssistantResponse {
        val state = stateStore.getOrCreate()
        val trimmed = userText.trim()

        // Manejo especial: confirmar cancelación por chat (Sí/No)
        val pendingCancelId = stateStore.getOrCreate().pendingCancelAppointmentId
        if (pendingCancelId != null) {
            val yes = trimmed.equals("sí", true) || trimmed.equals("si", true) || trimmed.equals("yes", true)
            val no = trimmed.equals("no", true)

            return if (yes) {
                val exec = dispatcher.dispatch(
                    CancelAppointmentCommand(
                        conversationId = state.conversationId,
                        appointmentId = pendingCancelId
                    )
                )
                stateStore.clearPendingCancel()
                AssistantResponse(exec.message)
            } else if (no) {
                stateStore.clearPendingCancel()
                AssistantResponse("Entendido. No se canceló ninguna cita.")
            } else {
                AssistantResponse("Por favor responde Sí o No para confirmar la cancelación.")
            }
        }

        return when (intentRouter.route(trimmed)) {
            UserIntent.TRIAGE -> {
                val exec = dispatcher.dispatch(
                    RunTriageCommand(conversationId = state.conversationId, symptomsText = trimmed)
                )
                AssistantResponse(exec.message)
            }

            // En scheduling/cancel/info dejamos que el BFF fake orqueste y devuelva acciones estructuradas
            UserIntent.SCHEDULING, UserIntent.CANCEL, UserIntent.MEDICAL_INFO, UserIntent.REMINDERS, UserIntent.UNKNOWN -> {
                val response = bffClient.sendMessage(state.conversationId, trimmed)

                // Si el BFF pide confirmación de cancelación, guardamos pending state local (24h)
                val cancelAction = response.actions.filterIsInstance<AskCancelConfirmation>().firstOrNull()
                if (cancelAction != null) {
                    stateStore.update {
                        it.copy(
                            pendingCancelAppointmentId = cancelAction.appointmentId,
                            pendingCancelHumanReadable = cancelAction.humanReadable
                        )
                    }
                    return response.copy(
                        text = "${response.text}\n\nCita: ${cancelAction.humanReadable}"
                    )
                }

                response
            }
        }
    }

    fun confirmScheduleFromUi(specialty: String, date: String, slot: String): AssistantResponse {
        val state = stateStore.getOrCreate()
        val exec = dispatcher.dispatch(
            ScheduleAppointmentCommand(
                conversationId = state.conversationId,
                specialty = specialty,
                date = date,
                slot = slot
            )
        )
        return AssistantResponse(exec.message)
    }

    fun createReminderFromText(reminderText: String, time: String): AssistantResponse {
        val state = stateStore.getOrCreate()
        val exec = dispatcher.dispatch(
            CreateReminderCommand(
                conversationId = state.conversationId,
                reminderText = reminderText,
                time = time
            )
        )
        return AssistantResponse(exec.message)
    }

    fun getMedicalInfo(type: MedicalInfoType): AssistantResponse {
        val state = stateStore.getOrCreate()
        val exec = dispatcher.dispatch(
            GetMedicalInfoCommand(conversationId = state.conversationId, queryType = type)
        )
        return AssistantResponse(exec.message)
    }
}
