package com.sriram.skillgap.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String = "",
    val name: String = "",
    val email: String = "",
    val dreamJob: String? = null,
    val technicalScore: Int = 0,
    val atsScore: Int = 0,
    val experienceScore: Int = 0,
    val employabilityScore: Int = 0,
    val level: Int = 1,
    val expPoints: Int = 0,
    val learningStreak: Int = 0,
    val lastActiveDate: Long = 0
)
