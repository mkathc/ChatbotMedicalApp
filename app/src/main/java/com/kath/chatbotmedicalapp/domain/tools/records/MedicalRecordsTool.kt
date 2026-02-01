package com.kath.chatbotmedicalapp.domain.tools.records

import com.kath.chatbotmedicalapp.data.records.FakeMedicalRecordsRepository
import com.kath.chatbotmedicalapp.domain.model.Command
import com.kath.chatbotmedicalapp.domain.model.GetMedicalInfoCommand
import com.kath.chatbotmedicalapp.domain.tools.Tool
import com.kath.chatbotmedicalapp.domain.tools.ToolExecution


class MedicalRecordsTool(
    private val repo: FakeMedicalRecordsRepository
) : Tool {

    override fun canHandle(command: Command): Boolean = command is GetMedicalInfoCommand

    override fun execute(command: Command): ToolExecution {
        val c = command as GetMedicalInfoCommand
        val lines = repo.getInfo(c.queryType)
        val msg = buildString {
            append("Aquí tienes la información solicitada:\n")
            lines.forEach { append("• ").append(it).append("\n") }
        }.trim()
        return ToolExecution(msg)
    }
}
