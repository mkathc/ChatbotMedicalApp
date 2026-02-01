package com.kath.chatbotmedicalapp.domain.model

sealed interface Command {
    val conversationId: String
}

data class RunTriageCommand(
    override val conversationId: String,
    val symptomsText: String
) : Command

data class ScheduleAppointmentCommand(
    override val conversationId: String,
    val specialty: String,
    val date: String,
    val slot: String
) : Command

data class ModifyAppointmentCommand(
    override val conversationId: String,
    val appointmentId: String,
    val newDate: String,
    val newSlot: String
) : Command

data class CancelAppointmentCommand(
    override val conversationId: String,
    val appointmentId: String
) : Command

data class GetMedicalInfoCommand(
    override val conversationId: String,
    val queryType: MedicalInfoType
) : Command

enum class MedicalInfoType { APPOINTMENTS, LAB_RESULTS, PRESCRIPTIONS, DIAGNOSES }

data class CreateReminderCommand(
    override val conversationId: String,
    val reminderText: String,
    val time: String
) : Command
