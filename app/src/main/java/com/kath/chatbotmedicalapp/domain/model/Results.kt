package com.kath.chatbotmedicalapp.domain.model

data class AssistantResponse(
    val text: String,
    val actions: List<AssistantAction> = emptyList()
)

sealed interface AssistantAction

data class ShowAppointmentSummary(
    val specialty: String,
    val date: String,
    val slot: String
) : AssistantAction

data class AskCancelConfirmation(
    val appointmentId: String,
    val humanReadable: String
) : AssistantAction

data class InfoCard(
    val title: String,
    val lines: List<String>
) : AssistantAction
