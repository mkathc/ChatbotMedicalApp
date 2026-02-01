package com.kath.chatbotmedicalapp.data.bff

import com.kath.chatbotmedicalapp.domain.model.AssistantResponse

interface BffClient {
    fun sendMessage(
        conversationId: String,
        userText: String
    ): AssistantResponse
}