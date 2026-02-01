package com.kath.chatbotmedicalapp.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.kath.chatbotmedicalapp.domain.model.ShowAppointmentSummary

@Composable
fun AppointmentSummaryDialog(
    summary: ShowAppointmentSummary,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmar cita") },
        text = {
            Text(
                "Especialidad: ${summary.specialty}\n" +
                        "Fecha: ${summary.date}\n" +
                        "Horario: ${summary.slot}\n\n" +
                        "¿Confirmas la reserva?"
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("Confirmar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
