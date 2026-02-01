package com.kath.chatbotmedicalapp.domain.tools

import com.kath.chatbotmedicalapp.domain.model.Command

interface Tool {
    fun canHandle(command: Command): Boolean
    fun execute(command: Command): ToolExecution
}

data class ToolExecution(
    val message: String,
    val actions: List<Any> = emptyList() // kept generic to stay lightweight in PoC
)
