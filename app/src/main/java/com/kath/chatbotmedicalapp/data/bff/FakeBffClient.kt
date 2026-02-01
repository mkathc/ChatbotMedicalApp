package com.kath.chatbotmedicalapp.data.bff

import com.kath.chatbotmedicalapp.domain.model.AskCancelConfirmation
import com.kath.chatbotmedicalapp.domain.model.AssistantResponse
import com.kath.chatbotmedicalapp.domain.model.InfoCard
import com.kath.chatbotmedicalapp.domain.model.ShowAppointmentSummary

/**
 * Simula el BFF/orquestador.
 * - Interpreta intención con reglas simples (Intent Interpreter híbrido simplificado).
 * - Devuelve acciones estructuradas (para que la App ejecute UI determinística).
 */
class FakeBffClient : BffClient {

    override fun sendMessage(conversationId: String, userText: String): AssistantResponse {
        val text = userText.lowercase()

        return when {
            // Scheduling (crear)
            listOf("agenda", "agendar", "cita").any { text.contains(it) } ->
                AssistantResponse(
                    text = "Perfecto. Encontré disponibilidad para cardiología. ¿Confirmas la reserva?",
                    actions = listOf(
                        ShowAppointmentSummary(
                            specialty = "Cardiología",
                            date = "Jueves",
                            slot = "4:30 PM"
                        )
                    )
                )

            // Cancel
            text.contains("cancela") || text.contains("cancelar") ->
                AssistantResponse(
                    text = "Encontré una cita para cancelar. ¿Confirmas? (Sí/No)",
                    actions = listOf(
                        AskCancelConfirmation(
                            appointmentId = "APT-123",
                            humanReadable = "Cardiología - Jueves 4:30 PM"
                        )
                    )
                )

            // Medical info
            text.contains("recetas") || text.contains("historial") || text.contains("exámenes") || text.contains("examenes") ->
                AssistantResponse(
                    text = "Puedo mostrarte información médica. ¿Qué necesitas: recetas, historial o exámenes?",
                    actions = listOf(
                        InfoCard(
                            title = "Info médica (PoC)",
                            lines = listOf("Recetas", "Historial", "Exámenes")
                        )
                    )
                )

            // Reminders
            text.contains("recuérdame") || text.contains("recordatorio") ->
                AssistantResponse(
                    text = "Claro. Dime qué debo recordarte y a qué hora (ej: 'tomar medicina a las 8pm')."
                )

            else ->
                AssistantResponse(
                    text = "Puedo ayudarte con: síntomas (triaje), citas, información médica y recordatorios."
                )
        }
    }
}
