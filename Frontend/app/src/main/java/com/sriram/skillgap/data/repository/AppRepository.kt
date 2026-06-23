package com.sriram.skillgap.data.repository

import com.sriram.skillgap.data.database.SkillDao
import com.sriram.skillgap.data.database.UserDao
import com.sriram.skillgap.data.database.ResumeHistoryDao
import com.sriram.skillgap.data.model.Skill
import com.sriram.skillgap.data.model.User
import com.sriram.skillgap.data.model.ResumeHistory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class AppRepository(
    private val userDao: UserDao,
    private val skillDao: SkillDao,
    private val resumeHistoryDao: ResumeHistoryDao
) {
    val activeUser: Flow<User?> = userDao.getActiveUser()

    fun getSkills(userId: String): Flow<List<Skill>> = skillDao.getSkillsByUserId(userId)

    fun getResumeHistory(userId: String): Flow<List<ResumeHistory>> = resumeHistoryDao.getHistoryByUserId(userId)

    suspend fun saveResumeHistory(history: ResumeHistory) = withContext(Dispatchers.IO) {
        resumeHistoryDao.insertHistory(history)
        pushResumeHistoryToCloud(history.userId, history)
    }

    suspend fun clearResumeHistory(userId: String) = withContext(Dispatchers.IO) {
        resumeHistoryDao.clearHistory(userId)
        try {
            com.google.firebase.database.FirebaseDatabase.getInstance()
                .getReference("resume_history")
                .child(userId)
                .removeValue()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun saveUser(user: User) = withContext(Dispatchers.IO) {
        userDao.insertUser(user)
        pushUserToCloud(user)
    }
    
    suspend fun updateUser(user: User) = withContext(Dispatchers.IO) {
        userDao.updateUser(user)
        pushUserToCloud(user)
    }

    suspend fun addSkills(skills: List<Skill>) = withContext(Dispatchers.IO) {
        skillDao.insertSkills(skills)
        if (skills.isNotEmpty()) {
            val userId = skills.first().userId
            val allSkills = skillDao.getSkillsByUserId(userId).firstOrNull() ?: skills
            pushSkillsToCloud(userId, allSkills)
        }
    }
    
    suspend fun clearExtractedSkills(userId: String) = withContext(Dispatchers.IO) {
        skillDao.clearExtractedSkills(userId)
        try {
            com.google.firebase.database.FirebaseDatabase.getInstance()
                .getReference("skills")
                .child(userId)
                .removeValue()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    suspend fun logout() = withContext(Dispatchers.IO) {
        userDao.clearAll()
    }

    // --- Firebase Cloud Push Operations ---
    private fun pushUserToCloud(user: User) {
        try {
            com.google.firebase.database.FirebaseDatabase.getInstance()
                .getReference("users")
                .child(user.id)
                .setValue(user)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun pushSkillsToCloud(userId: String, skills: List<Skill>) {
        try {
            com.google.firebase.database.FirebaseDatabase.getInstance()
                .getReference("skills")
                .child(userId)
                .setValue(skills)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun pushResumeHistoryToCloud(userId: String, history: ResumeHistory) {
        try {
            val dbRef = com.google.firebase.database.FirebaseDatabase.getInstance()
                .getReference("resume_history")
                .child(userId)
            val key = if (history.id != 0) history.id.toString() else dbRef.push().key ?: System.currentTimeMillis().toString()
            dbRef.child(key).setValue(history)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // --- Bidirectional Synchronization Fetch from Cloud ---
    suspend fun syncUserFromCloud(userId: String) = withContext(Dispatchers.IO) {
        try {
            val database = com.google.firebase.database.FirebaseDatabase.getInstance()
            
            // 1. Sync User Profile Info
            val userTask = database.getReference("users").child(userId).get()
            val userSnapshot = kotlin.coroutines.suspendCoroutine { continuation ->
                userTask.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resumeWith(Result.success(task.result))
                    } else {
                        continuation.resumeWith(Result.success(null))
                    }
                }
            }
            if (userSnapshot != null && userSnapshot.exists()) {
                val cloudUser = userSnapshot.getValue(User::class.java)
                if (cloudUser != null) {
                    userDao.insertUser(cloudUser)
                }
            }

            // 2. Sync Extracted/Added Skills
            val skillsTask = database.getReference("skills").child(userId).get()
            val skillsSnapshot = kotlin.coroutines.suspendCoroutine { continuation ->
                skillsTask.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resumeWith(Result.success(task.result))
                    } else {
                        continuation.resumeWith(Result.success(null))
                    }
                }
            }
            if (skillsSnapshot != null && skillsSnapshot.exists()) {
                val cloudSkillsList = mutableListOf<Skill>()
                for (child in skillsSnapshot.children) {
                    val skill = child.getValue(Skill::class.java)
                    if (skill != null) {
                        cloudSkillsList.add(skill)
                    }
                }
                if (cloudSkillsList.isNotEmpty()) {
                    skillDao.clearExtractedSkills(userId)
                    skillDao.insertSkills(cloudSkillsList)
                }
            }

            // 3. Sync Resume Scoring History
            val historyTask = database.getReference("resume_history").child(userId).get()
            val historySnapshot = kotlin.coroutines.suspendCoroutine { continuation ->
                historyTask.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resumeWith(Result.success(task.result))
                    } else {
                        continuation.resumeWith(Result.success(null))
                    }
                }
            }
            if (historySnapshot != null && historySnapshot.exists()) {
                val cloudHistoryList = mutableListOf<ResumeHistory>()
                for (child in historySnapshot.children) {
                    val history = child.getValue(ResumeHistory::class.java)
                    if (history != null) {
                        cloudHistoryList.add(history)
                    }
                }
                if (cloudHistoryList.isNotEmpty()) {
                    resumeHistoryDao.clearHistory(userId)
                    for (entry in cloudHistoryList) {
                        resumeHistoryDao.insertHistory(entry)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
