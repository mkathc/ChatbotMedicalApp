package com.kath.chatbotmedicalapp.app.state

import com.kath.chatbotmedicalapp.util.Clock
import java.util.UUID

data class ConversationState(
    val conversationId: String,
    val lastUpdatedMillis: Long,
    val pendingCancelAppointmentId: String? = null,
    val pendingCancelHumanReadable: String? = null
)

class ConversationStateStore(
    private val clock: Clock,
    private val ttlMillis: Long = 24L * 60 * 60 * 1000 // 24h
) {
    private var state: ConversationState? = null

    fun getOrCreate(): ConversationState {
        val now = clock.nowMillis()
        val current = state
        return if (current == null || isExpired(current, now)) {
            val fresh = ConversationState(
                conversationId = UUID.randomUUID().toString(),
                lastUpdatedMillis = now
            )
            state = fresh
            fresh
        } else {
            current
        }
    }

    fun update(transform: (ConversationState) -> ConversationState) {
        val now = clock.nowMillis()
        val current = getOrCreate()
        state = transform(current).copy(lastUpdatedMillis = now)
    }

    fun clearPendingCancel() {
        update { it.copy(pendingCancelAppointmentId = null, pendingCancelHumanReadable = null) }
    }

    private fun isExpired(s: ConversationState, now: Long): Boolean =
        (now - s.lastUpdatedMillis) > ttlMillis
}
