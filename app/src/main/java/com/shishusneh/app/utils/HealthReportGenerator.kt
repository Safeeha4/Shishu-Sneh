package com.shishusneh.app.utils

import android.content.Context
import android.os.Environment
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.UnitValue
import com.shishusneh.app.data.local.entities.BabyProfile
import com.shishusneh.app.data.local.entities.GrowthEntry
import com.shishusneh.app.data.local.entities.VaccineRecord
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object HealthReportGenerator {

    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    fun generateReport(
        context: Context,
        baby: BabyProfile,
        growthHistory: List<GrowthEntry>,
        vaccines: List<VaccineRecord>
    ): File? {
        val fileName = "Health_Report_${baby.name.replace(" ", "_")}_${System.currentTimeMillis()}.pdf"
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)

        try {
            val writer = PdfWriter(filePath)
            val pdf = PdfDocument(writer)
            val document = Document(pdf)

            // Header
            document.add(Paragraph("Shishu-Sneh Health Report").setFontSize(20f).setBold())
            document.add(Paragraph("Generated on: ${dateFormat.format(Date())}"))
            document.add(Paragraph("\n"))

            // Baby Details
            document.add(Paragraph("Baby Information").setBold().setFontSize(16f))
            document.add(Paragraph("Name: ${baby.name}"))
            document.add(Paragraph("Date of Birth: ${dateFormat.format(baby.dateOfBirth)}"))
            document.add(Paragraph("Gender: ${baby.gender}"))
            document.add(Paragraph("Birth Weight: ${baby.birthWeight} kg"))
            baby.birthHeight?.let { document.add(Paragraph("Birth Height: $it cm")) }
            document.add(Paragraph("\n"))

            // Growth Summary
            if (growthHistory.isNotEmpty()) {
                document.add(Paragraph("Growth History").setBold().setFontSize(16f))
                val table = Table(UnitValue.createPercentArray(floatArrayOf(30f, 35f, 35f))).useAllAvailableWidth()
                table.addCell("Date")
                table.addCell("Weight (kg)")
                table.addCell("Height (cm)")

                growthHistory.sortedByDescending { it.date }.forEach { entry ->
                    table.addCell(dateFormat.format(entry.date))
                    table.addCell(entry.weight.toString())
                    table.addCell(entry.height?.toString() ?: "--")
                }
                document.add(table)
                document.add(Paragraph("\n"))
            }

            // Vaccination Summary
            if (vaccines.isNotEmpty()) {
                document.add(Paragraph("Vaccination Record").setBold().setFontSize(16f))
                val table = Table(UnitValue.createPercentArray(floatArrayOf(40f, 30f, 30f))).useAllAvailableWidth()
                table.addCell("Vaccine")
                table.addCell("Due Date")
                table.addCell("Status")

                vaccines.forEach { vaccine ->
                    table.addCell(vaccine.name)
                    table.addCell(dateFormat.format(vaccine.dueDate))
                    table.addCell(vaccine.status)
                }
                document.add(table)
            }

            document.close()
            return filePath
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}
