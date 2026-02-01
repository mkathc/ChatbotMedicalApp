package com.kath.chatbotmedicalapp.domain.tools

import com.kath.chatbotmedicalapp.domain.model.Command

class ToolDispatcher(
    private val tools: List<Tool>
) {
    fun dispatch(command: Command): ToolExecution {
        val tool = tools.firstOrNull { it.canHandle(command) }
            ?: return ToolExecution("No puedo manejar esta acción todavía.")
        return tool.execute(command)
    }
}