package com.kath.chatbotmedicalapp.domain.tools.scheduling

import com.kath.chatbotmedicalapp.data.scheduling.FakeSchedulingRepository
import com.kath.chatbotmedicalapp.domain.model.CancelAppointmentCommand
import com.kath.chatbotmedicalapp.domain.model.Command
import com.kath.chatbotmedicalapp.domain.model.ModifyAppointmentCommand
import com.kath.chatbotmedicalapp.domain.model.ScheduleAppointmentCommand
import com.kath.chatbotmedicalapp.domain.tools.Tool
import com.kath.chatbotmedicalapp.domain.tools.ToolExecution

class SchedulingTool(
    private val repo: FakeSchedulingRepository
) : Tool {

    override fun canHandle(command: Command): Boolean =
        command is ScheduleAppointmentCommand ||
                command is ModifyAppointmentCommand ||
                command is CancelAppointmentCommand

    override fun execute(command: Command): ToolExecution {
        return when (command) {
            is ScheduleAppointmentCommand -> {
                val appt = repo.createAppointment(command.specialty, command.date, command.slot)
                ToolExecution("Cita reservada ✅ (${appt.id})")
            }
            is ModifyAppointmentCommand -> {
                val ok = repo.modifyAppointment(command.appointmentId, command.newDate, command.newSlot)
                ToolExecution(if (ok) "Cita modificada ✅" else "No encontré esa cita para modificar.")
            }
            is CancelAppointmentCommand -> {
                val ok = repo.cancelAppointment(command.appointmentId)
                ToolExecution(if (ok) "Cita cancelada ✅" else "No encontré esa cita para cancelar.")
            }
            else -> ToolExecution("Acción de citas no soportada.")
        }
    }
}
