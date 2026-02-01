package com.kath.chatbotmedicalapp.ui.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kath.chatbotmedicalapp.ui.components.AppointmentSummaryDialog

@Composable
fun ChatScreen(
    vm: ChatViewModel
) {
    val state by vm.state.collectAsState()
    var input by remember { mutableStateOf("") }

    if (state.pendingAppointmentSummary != null) {
        AppointmentSummaryDialog(
            summary = state.pendingAppointmentSummary!!,
            onConfirm = { vm.confirmAppointment(state.pendingAppointmentSummary!!) },
            onDismiss = { vm.dismissAppointmentDialog() }
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
        Text("Asistente de Salud (PoC)", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(state.messages) { msg ->
                val prefix = if (msg.fromUser) "Tú: " else "Bot: "
                Text(prefix + msg.text, style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(6.dp))
            }
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = input,
                onValueChange = { input = it },
                placeholder = { Text("Escribe: 'dolor en el pecho' o 'agenda cita'...") }
            )
            Spacer(Modifier.width(8.dp))
            Button(
                onClick = {
                    vm.sendUserMessage(input)
                    input = ""
                }
            ) { Text("Enviar") }
        }
    }
}
