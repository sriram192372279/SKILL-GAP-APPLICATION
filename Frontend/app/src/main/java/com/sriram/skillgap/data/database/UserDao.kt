package com.sriram.skillgap.data.database

import androidx.room.*
import com.sriram.skillgap.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Update
    fun updateUser(user: User)

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: String): Flow<User?>

    @Query("SELECT * FROM users LIMIT 1")
    fun getActiveUser(): Flow<User?>

    @Query("DELETE FROM users")
    fun clearAll()
}
