package com.sriram.skillgap.data.database

import androidx.room.*
import com.sriram.skillgap.data.model.ResumeHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface ResumeHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHistory(historyEntry: ResumeHistory)

    @Query("SELECT * FROM resume_history WHERE userId = :userId ORDER BY timestamp DESC")
    fun getHistoryByUserId(userId: String): Flow<List<ResumeHistory>>

    @Query("DELETE FROM resume_history WHERE userId = :userId")
    fun clearHistory(userId: String)
}
