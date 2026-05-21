package com.shishusneh.app.utils

import com.shishusneh.app.data.local.entities.VaccineRecord
import java.util.*

object VaccinationScheduleGenerator {
    
    private val indianSchedule = listOf(
        VaccineTemplate("BCG", 0, listOf("Tuberculosis"), 1),
        VaccineTemplate("OPV 0", 0, listOf("Poliomyelitis"), 1),
        VaccineTemplate("Hepatitis B (Birth dose)", 0, listOf("Hepatitis B"), 1),
        VaccineTemplate("OPV 1", 6, listOf("Poliomyelitis"), 1),
        VaccineTemplate("Pentavalent 1", 6, listOf("Diphtheria", "Tetanus", "Pertussis", "Hepatitis B", "Hib"), 1),
        VaccineTemplate("Rotavirus 1", 6, listOf("Rotavirus diarrhea"), 1),
        VaccineTemplate("PCV 1", 6, listOf("Pneumococcal disease"), 1),
        VaccineTemplate("OPV 2", 10, listOf("Poliomyelitis"), 2),
        VaccineTemplate("Pentavalent 2", 10, listOf("Diphtheria", "Tetanus", "Pertussis", "Hepatitis B", "Hib"), 2),
        VaccineTemplate("Rotavirus 2", 10, listOf("Rotavirus diarrhea"), 2),
        VaccineTemplate("PCV 2", 10, listOf("Pneumococcal disease"), 2),
        VaccineTemplate("OPV 3", 14, listOf("Poliomyelitis"), 3),
        VaccineTemplate("Pentavalent 3", 14, listOf("Diphtheria", "Tetanus", "Pertussis", "Hepatitis B", "Hib"), 3),
        VaccineTemplate("Rotavirus 3", 14, listOf("Rotavirus diarrhea"), 3),
        VaccineTemplate("PCV 3", 14, listOf("Pneumococcal disease"), 3),
        VaccineTemplate("IPV", 14, listOf("Poliomyelitis"), 1),
        VaccineTemplate("MMR 1", 36, listOf("Measles", "Mumps", "Rubella"), 1),
        VaccineTemplate("Vitamin A (1st dose)", 36, listOf("Vitamin A deficiency"), 1),
        VaccineTemplate("DPT Booster 1", 64, listOf("Diphtheria", "Tetanus", "Pertussis"), 1),
        VaccineTemplate("MMR 2", 72, listOf("Measles", "Mumps", "Rubella"), 2),
        VaccineTemplate("DPT Booster 2", 80, listOf("Diphtheria", "Tetanus", "Pertussis"), 2)
    )
    
    data class VaccineTemplate(
        val name: String,
        val ageWeeks: Int,
        val diseases: List<String>,
        val dosageNumber: Int
    )
    
    fun generateSchedule(babyId: String, dateOfBirth: Date): List<VaccineRecord> {
        val calendar = Calendar.getInstance()
        
        return indianSchedule.map { template ->
            calendar.time = dateOfBirth
            calendar.add(Calendar.WEEK_OF_YEAR, template.ageWeeks)
            
            VaccineRecord(
                id = UUID.randomUUID().toString(),
                babyId = babyId,
                name = template.name,
                dueDate = calendar.time,
                givenDate = null,
                status = "PENDING",
                diseases = template.diseases.joinToString(", "),
                dosageNumber = template.dosageNumber,
                ageWeeks = template.ageWeeks,
                notes = null,
                createdAt = Date()
            )
        }
    }
}
