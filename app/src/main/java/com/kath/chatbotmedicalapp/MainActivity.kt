package com.kath.chatbotmedicalapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kath.chatbotmedicalapp.app.ConversationController
import com.kath.chatbotmedicalapp.app.intent.IntentRouter
import com.kath.chatbotmedicalapp.app.state.ConversationStateStore
import com.kath.chatbotmedicalapp.data.bff.FakeBffClient
import com.kath.chatbotmedicalapp.data.records.FakeMedicalRecordsRepository
import com.kath.chatbotmedicalapp.data.reminders.LocalRemindersRepository
import com.kath.chatbotmedicalapp.data.scheduling.FakeSchedulingRepository
import com.kath.chatbotmedicalapp.domain.tools.ToolDispatcher
import com.kath.chatbotmedicalapp.domain.tools.records.MedicalRecordsTool
import com.kath.chatbotmedicalapp.domain.tools.reminders.RemindersTool
import com.kath.chatbotmedicalapp.domain.tools.scheduling.SchedulingTool
import com.kath.chatbotmedicalapp.domain.tools.triaje.OfflineTriageTool
import com.kath.chatbotmedicalapp.ui.chat.ChatScreen
import com.kath.chatbotmedicalapp.ui.chat.ChatViewModel
import com.kath.chatbotmedicalapp.util.SystemClock
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // --- Wiring (manual DI / constructor injection) ---
        val stateStore = ConversationStateStore(clock = SystemClock)
        val intentRouter = IntentRouter()

        val schedulingRepo = FakeSchedulingRepository()
        val recordsRepo = FakeMedicalRecordsRepository()
        val remindersRepo = LocalRemindersRepository()

        val dispatcher = ToolDispatcher(
            tools = listOf(
                OfflineTriageTool(),
                SchedulingTool(schedulingRepo),
                MedicalRecordsTool(recordsRepo),
                RemindersTool(remindersRepo)
            )
        )

        val bffClient = FakeBffClient()

        val controller = ConversationController(
            stateStore = stateStore,
            intentRouter = intentRouter,
            dispatcher = dispatcher,
            bffClient = bffClient
        )

        val vmFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ChatViewModel(controller) as T
            }
        }

        setContent {
            val vm = viewModel<ChatViewModel>(factory = vmFactory)
            ChatScreen(vm)
        }
    }
}
