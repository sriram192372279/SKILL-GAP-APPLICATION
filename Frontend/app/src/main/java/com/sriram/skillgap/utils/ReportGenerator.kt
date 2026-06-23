package com.sriram.skillgap.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import com.sriram.skillgap.viewmodel.AnalysisResult
import com.sriram.skillgap.data.model.User
import java.io.File
import java.io.FileOutputStream

object ReportGenerator {
    fun generateCareerReport(context: Context, user: User, analysis: AnalysisResult) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 Size
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        val paint = Paint()

        // Header
        paint.color = Color.parseColor("#0A0E21")
        canvas.drawRect(0f, 0f, 595f, 100f, paint)
        
        paint.color = Color.WHITE
        paint.textSize = 24f
        paint.isFakeBoldText = true
        canvas.drawText("AI Career Intelligence Report", 40f, 60f, paint)

        // User Info
        paint.color = Color.BLACK
        paint.textSize = 14f
        canvas.drawText("Name: ${user.name}", 40f, 130f, paint)
        canvas.drawText("Target Role: ${user.dreamJob}", 40f, 150f, paint)
        canvas.drawText("Readiness Score: ${user.employabilityScore}%", 40f, 170f, paint)

        // Gap Analysis
        paint.textSize = 18f
        paint.isFakeBoldText = true
        canvas.drawText("Skill Gap Analysis", 40f, 220f, paint)
        
        paint.textSize = 12f
        paint.isFakeBoldText = false
        var yPos = 250f
        canvas.drawText("Matching Skills:", 40f, yPos, paint)
        yPos += 20f
        analysis.matchingSkills.take(5).forEach {
            canvas.drawText("• $it", 60f, yPos, paint)
            yPos += 15f
        }

        yPos += 20f
        canvas.drawText("Missing Critical Skills:", 40f, yPos, paint)
        yPos += 20f
        analysis.missingSkills.take(5).forEach {
            canvas.drawText("! $it", 60f, yPos, paint)
            yPos += 15f
        }

        // Resume Suggestions
        yPos += 30f
        paint.textSize = 18f
        paint.isFakeBoldText = true
        canvas.drawText("Resume Optimization", 40f, yPos, paint)
        yPos += 25f
        paint.textSize = 12f
        paint.isFakeBoldText = false
        analysis.resumeIssues.forEach {
            canvas.drawText("${it.type}: ${it.description}", 40f, yPos, paint)
            yPos += 20f
        }

        pdfDocument.finishPage(page)

        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Career_Report.pdf")
        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(context, "Report saved to Documents", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        pdfDocument.close()
    }
}
