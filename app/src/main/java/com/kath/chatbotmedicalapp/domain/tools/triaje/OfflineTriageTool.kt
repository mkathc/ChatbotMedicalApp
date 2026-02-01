package com.kath.chatbotmedicalapp.domain.tools.triaje

import com.kath.chatbotmedicalapp.domain.model.Command
import com.kath.chatbotmedicalapp.domain.model.RunTriageCommand
import com.kath.chatbotmedicalapp.domain.tools.Tool
import com.kath.chatbotmedicalapp.domain.tools.ToolExecution

class OfflineTriageTool : Tool {
    override fun canHandle(command: Command): Boolean = command is RunTriageCommand

    override fun execute(command: Command): ToolExecution {
        val c = command as RunTriageCommand
        val text = c.symptomsText.lowercase()

        // Reglas mínimas determinísticas (PoC)
        val isEmergency = listOf("pecho", "falta de aire", "desmayo", "sangrado").any { text.contains(it) }
        val needsAppointment = listOf("fiebre", "dolor", "tos", "mareo").any { text.contains(it) }

        val msg = when {
            isEmergency ->
                "Esto podría ser una urgencia. Te recomendamos acudir a emergencias o llamar a tu número local de urgencias. (Esto no es un diagnóstico)."
            needsAppointment ->
                "Te recomiendo agendar una cita médica para evaluación. (Esto no es un diagnóstico)."
            else ->
                "Parece un caso leve. Puedes continuar con autocuidado y observar evolución. (Esto no es un diagnóstico)."
        }
        return ToolExecution(msg)
    }
}