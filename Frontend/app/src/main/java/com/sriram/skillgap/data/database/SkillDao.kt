package com.sriram.skillgap.data.database

import androidx.room.*
import com.sriram.skillgap.data.model.Skill
import kotlinx.coroutines.flow.Flow

@Dao
interface SkillDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSkills(skills: List<Skill>)

    @Query("SELECT * FROM user_skills WHERE userId = :userId")
    fun getSkillsByUserId(userId: String): Flow<List<Skill>>

    @Query("DELETE FROM user_skills WHERE userId = :userId AND isExtracted = 1")
    fun clearExtractedSkills(userId: String)
}
