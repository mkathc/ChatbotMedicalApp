package com.kath.chatbotmedicalapp.data.records

import com.kath.chatbotmedicalapp.domain.model.MedicalInfoType

class FakeMedicalRecordsRepository {
    fun getInfo(type: MedicalInfoType): List<String> = when (type) {
        MedicalInfoType.APPOINTMENTS -> listOf("Cita: Cardiología - 2026-02-03 16:30")
        MedicalInfoType.LAB_RESULTS -> listOf("Hemograma: Normal", "Glucosa: 92 mg/dL")
        MedicalInfoType.PRESCRIPTIONS -> listOf("Ibuprofeno 400mg (si dolor)", "Vitamina D 1000 IU")
        MedicalInfoType.DIAGNOSES -> listOf("Diagnóstico previo: Migraña (2025)")
    }
}
