package com.kath.chatbotmedicalapp.app.intent

enum class UserIntent {
    TRIAGE,
    SCHEDULING,
    CANCEL,
    MEDICAL_INFO,
    REMINDERS,
    UNKNOWN
}

/**
 * Intent Router local (híbrido simplificado):
 * - Hace un match rápido (keywords).
 * - Si no sabe, deja que el "BFF" fake responda.
 */
class IntentRouter {
    fun route(text: String): UserIntent {
        val t = text.lowercase()
        return when {
            listOf("dolor", "fiebre", "pecho", "tos", "mareo", "síntoma", "sintoma").any { t.contains(it) } -> UserIntent.TRIAGE
            listOf("agenda", "agendar", "cita", "reservar", "disponibilidad").any { t.contains(it) } -> UserIntent.SCHEDULING
            listOf("cancela", "cancelar").any { t.contains(it) } -> UserIntent.CANCEL
            listOf("recetas", "historial", "exámenes", "examenes", "diagnóstico", "diagnostico").any { t.contains(it) } -> UserIntent.MEDICAL_INFO
            listOf("recuérdame", "recuerdame", "recordatorio").any { t.contains(it) } -> UserIntent.REMINDERS
            else -> UserIntent.UNKNOWN
        }
    }
}
