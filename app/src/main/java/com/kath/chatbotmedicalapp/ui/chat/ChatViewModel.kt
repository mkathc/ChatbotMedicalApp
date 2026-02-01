package com.kath.chatbotmedicalapp.ui.chat

import androidx.lifecycle.ViewModel
import com.kath.chatbotmedicalapp.app.ConversationController
import com.kath.chatbotmedicalapp.domain.model.AssistantAction
import com.kath.chatbotmedicalapp.domain.model.AssistantResponse
import com.kath.chatbotmedicalapp.domain.model.ShowAppointmentSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class ChatMessage(val fromUser: Boolean, val text: String)

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val pendingAppointmentSummary: ShowAppointmentSummary? = null,
    val lastActions: List<AssistantAction> = emptyList()
)

class ChatViewModel(
    private val controller: ConversationController
) : ViewModel() {

    private val _state = MutableStateFlow(ChatUiState())
    val state: StateFlow<ChatUiState> = _state

    fun sendUserMessage(text: String) {
        if (text.isBlank()) return

        _state.update { it.copy(messages = it.messages + ChatMessage(true, text)) }

        val response = controller.onUserText(text)
        applyAssistantResponse(response)
    }

    fun confirmAppointment(summary: ShowAppointmentSummary) {
        val response = controller.confirmScheduleFromUi(summary.specialty, summary.date, summary.slot)
        _state.update { it.copy(pendingAppointmentSummary = null) }
        applyAssistantResponse(response)
    }

    fun dismissAppointmentDialog() {
        _state.update { it.copy(pendingAppointmentSummary = null) }
        _state.update { it.copy(messages = it.messages + ChatMessage(false, "Entendido. No se confirmó la reserva.")) }
    }

    private fun applyAssistantResponse(response: AssistantResponse) {
        _state.update {
            it.copy(
                messages = it.messages + ChatMessage(false, response.text),
                lastActions = response.actions
            )
        }

        val summary = response.actions.filterIsInstance<ShowAppointmentSummary>().firstOrNull()
        if (summary != null) {
            _state.update { it.copy(pendingAppointmentSummary = summary) }
        }
    }
}
