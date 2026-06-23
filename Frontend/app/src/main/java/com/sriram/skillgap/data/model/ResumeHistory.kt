package com.sriram.skillgap.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "resume_history")
data class ResumeHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String = "",
    val timestamp: Long = 0,
    val compatibilityScore: Int = 0,
    val atsScore: Int = 0,
    val readabilityScore: Int = 0,
    val projectStrengthScore: Int = 0,
    val keywordDensityScore: Int = 0,
    val fileName: String = ""
)
