package com.kath.chatbotmedicalapp.domain.tools.reminders

import com.kath.chatbotmedicalapp.data.reminders.LocalRemindersRepository
import com.kath.chatbotmedicalapp.domain.model.Command
import com.kath.chatbotmedicalapp.domain.model.CreateReminderCommand
import com.kath.chatbotmedicalapp.domain.tools.Tool
import com.kath.chatbotmedicalapp.domain.tools.ToolExecution


class RemindersTool(
    private val repo: LocalRemindersRepository
) : Tool {
    override fun canHandle(command: Command): Boolean = command is CreateReminderCommand

    override fun execute(command: Command): ToolExecution {
        val c = command as CreateReminderCommand
        repo.saveReminder(c.reminderText, c.time)
        return ToolExecution("Listo ✅ Recordatorio creado para ${c.time}.")
    }
}
