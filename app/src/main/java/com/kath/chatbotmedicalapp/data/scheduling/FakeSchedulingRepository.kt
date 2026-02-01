package com.kath.chatbotmedicalapp.data.scheduling

data class Appointment(
    val id: String,
    val specialty: String,
    val date: String,
    val slot: String
)

class FakeSchedulingRepository {
    private val store = mutableMapOf<String, Appointment>()
    private var counter = 200

    fun createAppointment(specialty: String, date: String, slot: String): Appointment {
        val id = "APT-${counter++}"
        val appt = Appointment(id, specialty, date, slot)
        store[id] = appt
        return appt
    }

    fun modifyAppointment(appointmentId: String, newDate: String, newSlot: String): Boolean {
        val existing = store[appointmentId] ?: return false
        store[appointmentId] = existing.copy(date = newDate, slot = newSlot)
        return true
    }

    fun cancelAppointment(appointmentId: String): Boolean {
        return store.remove(appointmentId) != null
    }

    fun getAnyAppointment(): Appointment? = store.values.firstOrNull()
}
