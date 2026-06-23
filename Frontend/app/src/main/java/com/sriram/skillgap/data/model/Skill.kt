package com.sriram.skillgap.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_skills")
data class Skill(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String = "",
    val name: String = "",
    val level: String = "Beginner",
    val isExtracted: Boolean = false,
    val progress: Int = 0
)
