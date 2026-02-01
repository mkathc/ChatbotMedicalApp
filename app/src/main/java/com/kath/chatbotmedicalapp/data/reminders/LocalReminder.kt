package com.kath.chatbotmedicalapp.data.reminders

data class LocalReminder(val text: String, val time: String)

class LocalRemindersRepository {
    private val reminders = mutableListOf<LocalReminder>()

    fun saveReminder(text: String, time: String) {
        reminders.add(LocalReminder(text, time))
    }

    fun list(): List<LocalReminder> = reminders.toList()
}
