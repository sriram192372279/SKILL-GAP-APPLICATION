package com.sriram.skillgap.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sriram.skillgap.data.database.AppDatabase
import com.sriram.skillgap.data.model.Skill
import com.sriram.skillgap.data.model.User
import com.sriram.skillgap.data.model.ResumeHistory
import com.google.firebase.auth.FirebaseAuth
import com.sriram.skillgap.data.repository.AppRepository
import com.sriram.skillgap.utils.JobData
import com.sriram.skillgap.utils.PdfParser
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import com.sriram.skillgap.data.model.*
import com.sriram.skillgap.utils.JobMatcherData
import com.google.firebase.firestore.FirebaseFirestore

data class ResumeIssue(val type: String, val description: String, val severity: String)
data class Recommendation(val title: String, val provider: String, val type: String)
data class RoadmapPhase(val title: String, val items: List<String>, val difficulty: String = "Medium", val isCompleted: Boolean = false)
data class CareerRiskItem(val outdatedSkill: String, val severity: String, val status: String, val suggestedReplacement: String)

enum class AnalysisStepStatus { PENDING, IN_PROGRESS, DONE, ERROR }
data class AnalysisStep(
    val id: String,
    val label: String,
    val detail: String,
    val status: AnalysisStepStatus = AnalysisStepStatus.PENDING
)

data class AnalysisResult(
    val matchPercentage: Int = 0,
    val matchingSkills: List<String> = emptyList(),
    val missingSkills: List<String> = emptyList(),
    val atsScore: Int = 0,
    val readabilityScore: Int = 0,
    val projectStrengthScore: Int = 0,
    val keywordDensityScore: Int = 0,
    val resumeIssues: List<ResumeIssue> = emptyList(),
    val recommendations: String = "",
    val smartRecommendations: List<Recommendation> = emptyList(),
    val suggestedProjects: List<String> = emptyList(),
    val prepQuestions: List<String> = emptyList(),
    val roadmapPhases: List<RoadmapPhase> = emptyList(),
    val futureSkills: List<String> = emptyList(),
    val careerRiskItems: List<CareerRiskItem> = emptyList()
)

class SkillViewModel(application: Application) : AndroidViewModel(application) {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db = AppDatabase.getDatabase(application)
    private val repository = AppRepository(db.userDao(), db.skillDao(), db.resumeHistoryDao())

    val user = repository.activeUser.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)



    private val prefs = application.getSharedPreferences("skillgap_prep_prefs", Context.MODE_PRIVATE)

    private val _selectedCompany = MutableStateFlow<String?>(prefs.getString("selected_company", null))
    val selectedCompany: StateFlow<String?> = _selectedCompany

    private val _selectedCompanyRole = MutableStateFlow<String?>(prefs.getString("selected_company_role", null))
    val selectedCompanyRole: StateFlow<String?> = _selectedCompanyRole

    private val _solvedAptitude = MutableStateFlow<Set<String>>(
        prefs.getStringSet("solved_aptitude", emptySet()) ?: emptySet()
    )
    val solvedAptitude: StateFlow<Set<String>> = _solvedAptitude

    private val _solvedCoding = MutableStateFlow<Set<String>>(
        prefs.getStringSet("solved_coding", emptySet()) ?: emptySet()
    )
    val solvedCoding: StateFlow<Set<String>> = _solvedCoding

    private val _interviewConfidence = MutableStateFlow(prefs.getInt("interview_confidence", 65))
    val interviewConfidence: StateFlow<Int> = _interviewConfidence

    private val _checkedChecklistItems = MutableStateFlow<Set<String>>(
        prefs.getStringSet("checked_checklist_items", emptySet()) ?: emptySet()
    )
    val checkedChecklistItems: StateFlow<Set<String>> = _checkedChecklistItems

    fun selectCompany(company: String?) {
        _selectedCompany.value = company
        prefs.edit().putString("selected_company", company).apply()
        if (company != null) {
            val companyInfo = com.sriram.skillgap.utils.CompanyData.getCompanyByName(company)
            val currentRole = _selectedCompanyRole.value
            if (currentRole != null && companyInfo?.roles?.any { it.roleTitle == currentRole } == false) {
                selectCompanyRole(companyInfo.roles.firstOrNull()?.roleTitle)
            } else if (currentRole == null) {
                selectCompanyRole(companyInfo?.roles?.firstOrNull()?.roleTitle)
            }
        } else {
            selectCompanyRole(null)
        }
    }

    fun selectCompanyRole(role: String?) {
        _selectedCompanyRole.value = role
        prefs.edit().putString("selected_company_role", role).apply()
    }

    fun toggleAptitudeSolved(questionId: String) {
        val current = _solvedAptitude.value.toMutableSet()
        if (current.contains(questionId)) {
            current.remove(questionId)
        } else {
            current.add(questionId)
        }
        _solvedAptitude.value = current
        prefs.edit().putStringSet("solved_aptitude", current).apply()
    }

    fun toggleCodingSolved(problemId: String) {
        val current = _solvedCoding.value.toMutableSet()
        if (current.contains(problemId)) {
            current.remove(problemId)
        } else {
            current.add(problemId)
        }
        _solvedCoding.value = current
        prefs.edit().putStringSet("solved_coding", current).apply()
    }

    fun setInterviewConfidence(score: Int) {
        val clamped = score.coerceIn(0, 100)
        _interviewConfidence.value = clamped
        prefs.edit().putInt("interview_confidence", clamped).apply()
    }

    fun toggleChecklistItem(itemId: String) {
        val current = _checkedChecklistItems.value.toMutableSet()
        if (current.contains(itemId)) {
            current.remove(itemId)
        } else {
            current.add(itemId)
        }
        _checkedChecklistItems.value = current
        prefs.edit().putStringSet("checked_checklist_items", current).apply()
    }

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val resumeHistory = user.flatMapLatest { currentUser ->
        if (currentUser == null) flowOf(emptyList())
        else repository.getResumeHistory(currentUser.id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val userSkills: StateFlow<List<Skill>> = user.flatMapLatest { currentUser ->
        if (currentUser == null) flowOf(emptyList())
        else repository.getSkills(currentUser.id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _onlineJobs = MutableStateFlow<List<Job>>(emptyList())
    val onlineJobs: StateFlow<List<Job>> = _onlineJobs

    private val _onlineCompanies = MutableStateFlow<List<Company>>(emptyList())
    val onlineCompanies: StateFlow<List<Company>> = _onlineCompanies

    private val _onlineInterviewQuestions = MutableStateFlow<List<InterviewQuestion>>(emptyList())
    val onlineInterviewQuestions: StateFlow<List<InterviewQuestion>> = _onlineInterviewQuestions

    private val _onlineJobRoles = MutableStateFlow<List<JobRoleModel>>(emptyList())
    val onlineJobRoles: StateFlow<List<JobRoleModel>> = _onlineJobRoles

    private val _savedJobsList = MutableStateFlow<List<UserSavedJob>>(emptyList())
    val savedJobsList: StateFlow<List<UserSavedJob>> = _savedJobsList

    // Expose saved job IDs as a Set for fast lookup in UI
    val savedJobIds: StateFlow<Set<String>> = _savedJobsList.map { list ->
        list.map { it.jobId }.toSet()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    private val _bookmarkedCompanyNames = MutableStateFlow<Set<String>>(
        prefs.getStringSet("bookmarked_companies", emptySet()) ?: emptySet()
    )
    val bookmarkedCompanyNames: StateFlow<Set<String>> = _bookmarkedCompanyNames

    private val _userApplications = MutableStateFlow<List<UserApplication>>(emptyList())
    val userApplications: StateFlow<List<UserApplication>> = _userApplications

    private val _isLoadingJobs = MutableStateFlow(false)
    val isLoadingJobs: StateFlow<Boolean> = _isLoadingJobs

    private val _syncMessage = MutableStateFlow<String?>(null)
    val syncMessage: StateFlow<String?> = _syncMessage

    private var savedJobsListener: com.google.firebase.firestore.ListenerRegistration? = null
    private var applicationsListener: com.google.firebase.firestore.ListenerRegistration? = null

    init {
        firebaseAuth.currentUser?.let { firebaseUser ->
            viewModelScope.launch {
                repository.syncUserFromCloud(firebaseUser.uid)
            }
        }
        loadJobsAndCompanies()
        viewModelScope.launch {
            user.collect { currentUser ->
                if (currentUser != null) {
                    loadSavedJobs(currentUser.id)
                    loadUserApplications(currentUser.id)
                }
            }
        }
    }

    fun loadSavedJobs(userId: String) {
        savedJobsListener?.remove()
        val firestore = FirebaseFirestore.getInstance()
        savedJobsListener = firestore.collection("user_saved_jobs")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, e ->
                if (snapshot != null) {
                    val list = snapshot.toObjects(UserSavedJob::class.java)
                    _savedJobsList.value = list
                }
            }
    }

    fun loadUserApplications(userId: String) {
        applicationsListener?.remove()
        val firestore = FirebaseFirestore.getInstance()
        applicationsListener = firestore.collection("user_applications")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, e ->
                if (snapshot != null) {
                    val list = snapshot.toObjects(UserApplication::class.java)
                    _userApplications.value = list
                }
            }
    }

    fun loadJobsAndCompanies() {
        viewModelScope.launch {
            _isLoadingJobs.value = true
            try {
                val db = FirebaseFirestore.getInstance()
                
                // Fetch companies
                db.collection("companies").get()
                    .addOnSuccessListener { result ->
                        val list = result.toObjects(Company::class.java)
                        if (list.isNotEmpty()) {
                            _onlineCompanies.value = list
                        } else {
                            _onlineCompanies.value = JobMatcherData.getOfflineCompanies()
                        }
                    }
                    .addOnFailureListener {
                        _onlineCompanies.value = JobMatcherData.getOfflineCompanies()
                    }

                // Fetch jobs
                db.collection("jobs").get()
                    .addOnSuccessListener { result ->
                        val list = result.toObjects(Job::class.java)
                        if (list.isNotEmpty()) {
                            _onlineJobs.value = list
                        } else {
                            _onlineJobs.value = JobMatcherData.getOfflineJobs()
                        }
                        _isLoadingJobs.value = false
                    }
                    .addOnFailureListener {
                        _onlineJobs.value = JobMatcherData.getOfflineJobs()
                        _isLoadingJobs.value = false
                    }

                // Fetch interview questions
                db.collection("interview_questions").get()
                    .addOnSuccessListener { result ->
                        val list = result.toObjects(InterviewQuestion::class.java)
                        if (list.isNotEmpty()) {
                            _onlineInterviewQuestions.value = list
                        } else {
                            _onlineInterviewQuestions.value = JobMatcherData.getOfflineQuestions()
                        }
                    }
                    .addOnFailureListener {
                        _onlineInterviewQuestions.value = JobMatcherData.getOfflineQuestions()
                    }

                // Fetch job roles
                db.collection("job_roles").get()
                    .addOnSuccessListener { result ->
                        val list = result.toObjects(JobRoleModel::class.java)
                        if (list.isNotEmpty()) {
                            _onlineJobRoles.value = list
                        } else {
                            _onlineJobRoles.value = JobMatcherData.getOfflineRoles()
                        }
                    }
                    .addOnFailureListener {
                        _onlineJobRoles.value = JobMatcherData.getOfflineRoles()
                    }

            } catch (e: Exception) {
                _onlineCompanies.value = JobMatcherData.getOfflineCompanies()
                _onlineJobs.value = JobMatcherData.getOfflineJobs()
                _onlineInterviewQuestions.value = JobMatcherData.getOfflineQuestions()
                _onlineJobRoles.value = JobMatcherData.getOfflineRoles()
                _isLoadingJobs.value = false
            }
        }
    }

    fun syncAndSeedDatabase() {
        viewModelScope.launch {
            _syncMessage.value = "Seeding Firestore database online..."
            JobMatcherData.seedFirestore { success, error ->
                if (success) {
                    _syncMessage.value = "Database seeded successfully!"
                    loadJobsAndCompanies()
                } else {
                    _syncMessage.value = "Sync failed: $error. Running in offline hybrid mode."
                    loadJobsAndCompanies()
                }
            }
        }
    }

    fun clearSyncMessage() {
        _syncMessage.value = null
    }

    fun toggleSaveJob(job: Job) {
        val currentUser = user.value ?: return
        val firestore = FirebaseFirestore.getInstance()
        val docId = "${currentUser.id}_${job.jobId}"
        val docRef = firestore.collection("user_saved_jobs").document(docId)

        if (savedJobIds.value.contains(job.jobId)) {
            docRef.delete()
        } else {
            val savedJob = UserSavedJob(
                userId = currentUser.id,
                jobId = job.jobId,
                companyName = job.companyName,
                jobTitle = job.jobTitle,
                savedDate = System.currentTimeMillis()
            )
            docRef.set(savedJob)
        }
    }

    fun toggleSaveJob(jobId: String) {
        val job = onlineJobs.value.find { it.jobId == jobId }
        if (job != null) {
            toggleSaveJob(job)
        } else {
            // Fallback for offline jobs whose IDs might match
            val offlineJob = JobMatcherData.getOfflineJobs().find { it.jobId == jobId } ?: return
            toggleSaveJob(offlineJob)
        }
    }

    fun toggleBookmarkCompany(companyName: String) {
        val current = _bookmarkedCompanyNames.value.toMutableSet()
        if (current.contains(companyName)) {
            current.remove(companyName)
        } else {
            current.add(companyName)
        }
        _bookmarkedCompanyNames.value = current
        prefs.edit().putStringSet("bookmarked_companies", current).apply()
    }

    fun updateApplicationStatus(jobId: String, companyName: String, jobTitle: String, status: String, notes: String = "") {
        val currentUser = user.value ?: return
        val firestore = FirebaseFirestore.getInstance()
        val docId = "${currentUser.id}_$jobId"
        val docRef = firestore.collection("user_applications").document(docId)

        val application = UserApplication(
            userId = currentUser.id,
            jobId = jobId,
            companyName = companyName,
            jobTitle = jobTitle,
            applicationStatus = status,
            appliedDate = System.currentTimeMillis(),
            notes = notes
        )
        docRef.set(application)
    }

    fun adminAddCompany(company: Company, onResult: (Boolean, String?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val id = company.companyId.ifEmpty { company.companyName.lowercase().replace(" ", "_") }
        val updated = company.copy(companyId = id, lastUpdated = System.currentTimeMillis())
        db.collection("companies").document(id).set(updated)
            .addOnSuccessListener {
                loadJobsAndCompanies()
                onResult(true, null)
            }
            .addOnFailureListener { e ->
                onResult(false, e.localizedMessage)
            }
    }

    fun adminAddJob(job: Job, onResult: (Boolean, String?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val id = job.jobId.ifEmpty { "job_" + UUID.randomUUID().toString().take(6) }
        val updated = job.copy(jobId = id, createdAt = System.currentTimeMillis())
        db.collection("jobs").document(id).set(updated)
            .addOnSuccessListener {
                loadJobsAndCompanies()
                onResult(true, null)
            }
            .addOnFailureListener { e ->
                onResult(false, e.localizedMessage)
            }
    }

    fun adminAddInterviewQuestion(question: InterviewQuestion, onResult: (Boolean, String?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val id = question.questionId.ifEmpty { "q_" + UUID.randomUUID().toString().take(6) }
        val updated = question.copy(questionId = id)
        db.collection("interview_questions").document(id).set(updated)
            .addOnSuccessListener {
                loadJobsAndCompanies()
                onResult(true, null)
            }
            .addOnFailureListener { e ->
                onResult(false, e.localizedMessage)
            }
    }

    fun adminAddJobRole(role: JobRoleModel, onResult: (Boolean, String?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val id = role.roleId.ifEmpty { role.roleName.lowercase().replace(" ", "_") }
        val updated = role.copy(roleId = id)
        db.collection("job_roles").document(id).set(updated)
            .addOnSuccessListener {
                loadJobsAndCompanies()
                onResult(true, null)
            }
            .addOnFailureListener { e ->
                onResult(false, e.localizedMessage)
            }
    }

    override fun onCleared() {
        super.onCleared()
        savedJobsListener?.remove()
        applicationsListener?.remove()
    }

    fun getMatchPercentageForJob(job: Job, userSkills: List<String>): Int {
        if (resumeHistory.value.isEmpty()) return 0
        if (job.requiredSkills.isEmpty()) return 100
        val skillsLower = userSkills.map { it.lowercase() }
        val matching = job.requiredSkills.filter { it.lowercase() in skillsLower }
        return (matching.size * 100) / job.requiredSkills.size
    }

    fun getMissingSkillsForJob(job: Job, userSkills: List<String>): List<String> {
        if (resumeHistory.value.isEmpty()) return job.requiredSkills
        val skillsLower = userSkills.map { it.lowercase() }
        return job.requiredSkills.filter { it.lowercase() !in skillsLower }
    }

    fun getPlacementReadiness(companyName: String?, jobTitle: String?, userSkillsList: List<String>): PlacementReadiness {
        if (resumeHistory.value.isEmpty()) {
            return PlacementReadiness(0, 0, 0, 0)
        }
        val skills = userSkillsList.map { it.lowercase() }
        
        val targetRole = JobMatcherData.getOfflineRoles().find { it.title.equals(jobTitle, ignoreCase = true) }
        val jobReadiness = if (targetRole != null && targetRole.requiredSkills.isNotEmpty()) {
            val matching = targetRole.requiredSkills.filter { it.lowercase() in skills }
            (matching.size * 100) / targetRole.requiredSkills.size
        } else {
            65
        }

        val targetComp = JobMatcherData.getOfflineCompanies().find { it.name.equals(companyName ?: "", ignoreCase = true) }
        val companyReadiness = if (targetComp != null) {
            val matchingTech = targetComp.technologies.filter { it.lowercase() in skills }
            val techMatch = if (targetComp.technologies.isNotEmpty()) (matchingTech.size * 100) / targetComp.technologies.size else 70
            val solvedCountForCompany = _solvedAptitude.value.size + _solvedCoding.value.size
            val solvedBonus = (solvedCountForCompany * 5).coerceAtMost(20)
            (techMatch * 0.8 + solvedBonus).toInt().coerceIn(0, 100)
        } else {
            60
        }

        val solvedAptSize = _solvedAptitude.value.size
        val solvedCodeSize = _solvedCoding.value.size
        val aptProgress = (solvedAptSize * 100 / 15).coerceAtMost(100)
        val codeProgress = (solvedCodeSize * 100 / 15).coerceAtMost(100)
        val confidence = _interviewConfidence.value
        val interviewReadiness = (aptProgress * 0.3 + codeProgress * 0.4 + confidence * 0.3).toInt().coerceIn(0, 100)

        val selectionProbability = (companyReadiness * 0.3 + jobReadiness * 0.4 + interviewReadiness * 0.3).toInt().coerceIn(0, 100)

        return PlacementReadiness(companyReadiness, jobReadiness, interviewReadiness, selectionProbability)
    }

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing

    private val _analysisComplete = MutableStateFlow(false)
    val analysisComplete: StateFlow<Boolean> = _analysisComplete

    private val _analysisResult = MutableStateFlow(AnalysisResult())
    val analysisResult: StateFlow<AnalysisResult> = _analysisResult

    private val _analysisSteps = MutableStateFlow<List<AnalysisStep>>(
        listOf(
            AnalysisStep("parse",    "Reading PDF Document",          "Extracting raw text content from your resume file"),
            AnalysisStep("skills",   "Detecting Your Skills",         "Matching 200+ technical skills with synonym intelligence"),
            AnalysisStep("gap",      "Computing Skill Gaps",          "Comparing your profile against your target job role"),
            AnalysisStep("ats",      "Calculating ATS Score",         "Running multi-factor keyword density & structure analysis"),
            AnalysisStep("roadmap",  "Building Your Learning Roadmap","Generating a personalized phase-based study plan"),
            AnalysisStep("risk",     "Analysing Career Risks",        "Identifying outdated skills & suggesting modern replacements"),
            AnalysisStep("save",     "Saving Results",                "Persisting your history and updating your career scores")
        )
    )
    val analysisSteps: StateFlow<List<AnalysisStep>> = _analysisSteps

    private fun setStep(id: String, status: AnalysisStepStatus) {
        _analysisSteps.value = _analysisSteps.value.map {
            if (it.id == id) it.copy(status = status) else it
        }
    }

    private fun resetSteps() {
        _analysisSteps.value = _analysisSteps.value.map { it.copy(status = AnalysisStepStatus.PENDING) }
        _analysisComplete.value = false
    }

    fun login(name: String, email: String) {
        viewModelScope.launch {
            val newUser = User(
                id = UUID.randomUUID().toString(), 
                name = name, 
                email = email, 
                level = 1, 
                expPoints = 100,
                learningStreak = 1,
                lastActiveDate = System.currentTimeMillis()
            )
            repository.saveUser(newUser)
        }
    }

    fun signUpWithFirebase(name: String, email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val firebaseUser = task.result?.user
                            if (firebaseUser != null) {
                                viewModelScope.launch {
                                    val newUser = User(
                                        id = firebaseUser.uid,
                                        name = name,
                                        email = email,
                                        level = 1,
                                        expPoints = 100,
                                        learningStreak = 1,
                                        lastActiveDate = System.currentTimeMillis()
                                    )
                                    repository.saveUser(newUser)
                                    onResult(true, null)
                                }
                            } else {
                                onResult(false, "User session empty after creation")
                            }
                        } else {
                            onResult(false, task.exception?.localizedMessage ?: "Registration failed")
                        }
                    }
            } catch (e: Exception) {
                onResult(false, e.localizedMessage ?: "Unknown error occurred")
            }
        }
    }

    fun signInWithFirebase(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val firebaseUser = task.result?.user
                            if (firebaseUser != null) {
                                viewModelScope.launch {
                                    val resolvedName = firebaseUser.displayName ?: email.substringBefore("@").replaceFirstChar { it.uppercase() }
                                    val newUser = User(
                                        id = firebaseUser.uid,
                                        name = resolvedName,
                                        email = email,
                                        level = 1,
                                        expPoints = 100,
                                        learningStreak = 1,
                                        lastActiveDate = System.currentTimeMillis()
                                    )
                                    repository.saveUser(newUser)
                                    repository.syncUserFromCloud(firebaseUser.uid)
                                    onResult(true, null)
                                }
                            } else {
                                onResult(false, "User session empty after verification")
                            }
                        } else {
                            onResult(false, task.exception?.localizedMessage ?: "Verification failed")
                        }
                    }
            } catch (e: Exception) {
                onResult(false, e.localizedMessage ?: "Unknown error occurred")
            }
        }
    }

    fun signInWithGoogle(idToken: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val credential = com.google.firebase.auth.GoogleAuthProvider.getCredential(idToken, null)
                firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val firebaseUser = task.result?.user
                            if (firebaseUser != null) {
                                viewModelScope.launch {
                                    val resolvedName = firebaseUser.displayName ?: firebaseUser.email?.substringBefore("@")?.replaceFirstChar { it.uppercase() } ?: "Google User"
                                    val newUser = User(
                                        id = firebaseUser.uid,
                                        name = resolvedName,
                                        email = firebaseUser.email ?: "",
                                        level = 1,
                                        expPoints = 100,
                                        learningStreak = 1,
                                        lastActiveDate = System.currentTimeMillis()
                                    )
                                    repository.saveUser(newUser)
                                    repository.syncUserFromCloud(firebaseUser.uid)
                                    onResult(true, null)
                                }
                            } else {
                                onResult(false, "User session empty after verification")
                            }
                        } else {
                            onResult(false, task.exception?.localizedMessage ?: "Verification failed")
                        }
                    }
            } catch (e: Exception) {
                onResult(false, e.localizedMessage ?: "Unknown error occurred")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            firebaseAuth.signOut()
            repository.logout()
        }
    }

    fun resetUserAnalysis() {
        viewModelScope.launch {
            val currentUser = user.value ?: return@launch
            repository.clearResumeHistory(currentUser.id)
            repository.clearExtractedSkills(currentUser.id)
            val updatedUser = currentUser.copy(
                technicalScore = 0,
                atsScore = 0,
                experienceScore = 0,
                employabilityScore = 0
            )
            repository.updateUser(updatedUser)
            _analysisResult.value = AnalysisResult()
        }
    }


    fun setDreamJob(jobTitle: String) {
        viewModelScope.launch {
            val currentUser = user.value ?: return@launch
            repository.updateUser(currentUser.copy(dreamJob = jobTitle))
            performAnalysis(customJobTitle = jobTitle)
        }
    }

    fun uploadResume(context: Context, uri: Uri) {
        viewModelScope.launch {
            _isAnalyzing.value = true
            resetSteps()
            val currentUser = user.value ?: run {
                _isAnalyzing.value = false
                return@launch
            }

            // Step 1: Parse PDF
            setStep("parse", AnalysisStepStatus.IN_PROGRESS)
            delay(400)
            val text = PdfParser.extractText(context, uri)
            setStep("parse", if (text.isNotBlank()) AnalysisStepStatus.DONE else AnalysisStepStatus.ERROR)
            delay(200)

            // Step 2: Detect skills
            setStep("skills", AnalysisStepStatus.IN_PROGRESS)
            delay(500)
            val extractedNames = PdfParser.parseSkills(text)
            repository.clearExtractedSkills(currentUser.id)
            val skills = extractedNames.map { Skill(userId = currentUser.id, name = it, level = "Intermediate", isExtracted = true) }
            repository.addSkills(skills)
            setStep("skills", AnalysisStepStatus.DONE)
            delay(200)

            // Steps 3-7 are handled inside performAnalysis with step callbacks
            performAnalysis(text, stepCallback = { id, status -> setStep(id, status) })

            _isAnalyzing.value = false
            _analysisComplete.value = true
        }
    }

    fun resetAnalysisComplete() {
        _analysisComplete.value = false
    }

    fun performAnalysis(resumeText: String = "", customJobTitle: String? = null, stepCallback: ((String, AnalysisStepStatus) -> Unit)? = null) {
        viewModelScope.launch {
            val currentUser = user.value ?: return@launch
            val jobTitle = customJobTitle ?: currentUser.dreamJob ?: return@launch
            val role = JobData.roles.find { it.title == jobTitle } ?: return@launch
            
            // Step 3: Gap analysis
            stepCallback?.invoke("gap", AnalysisStepStatus.IN_PROGRESS)
            delay(300)

            val userSkillsEntities = repository.getSkills(currentUser.id).first()
            val userSkills = userSkillsEntities.map { it.name.lowercase() }
            
            val matching = role.requiredSkills.filter { it.lowercase() in userSkills }
            val missing = role.requiredSkills.filter { it.lowercase() !in userSkills }
            val matchPercent = if (role.requiredSkills.isNotEmpty()) (matching.size * 100) / role.requiredSkills.size else 0

            stepCallback?.invoke("gap", AnalysisStepStatus.DONE)
            delay(150)

            // Step 4: ATS Score
            stepCallback?.invoke("ats", AnalysisStepStatus.IN_PROGRESS)
            delay(300)
            
            val atsKeywordsFound = role.atsKeywords.filter { it.lowercase() in userSkills }
            var atsScore = if (role.atsKeywords.isNotEmpty()) (atsKeywordsFound.size * 100) / role.atsKeywords.size else 75

            // --- MULTI-FACTOR RESUME SCORING ---
            var readability = 72
            var projectStrength = 65
            var keywordDensity = 70
            val issues = mutableListOf<ResumeIssue>()

            if (resumeText.isNotEmpty()) {
                // 1. Readability Score calculation
                var readScore = 40
                val words = resumeText.split(Regex("\\s+")).filter { it.isNotBlank() }.size
                if (words in 200..750) readScore += 20
                else if (words > 750) readScore += 10 // Stuffed or overly long

                val markers = listOf("education", "experience", "skills", "projects", "certifications", "contact")
                val foundMarkers = markers.count { resumeText.contains(it, ignoreCase = true) }
                readScore += (foundMarkers * 5).coerceAtMost(20)

                val hasEmail = resumeText.contains(Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"))
                if (hasEmail) readScore += 10

                val hasPhone = resumeText.contains(Regex("\\+?\\d{1,4}?[-.\\s]?\\(?\\d{1,3}?\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,9}"))
                if (hasPhone) readScore += 10

                val hasLink = resumeText.contains("github.com", ignoreCase = true) || resumeText.contains("linkedin.com", ignoreCase = true)
                if (hasLink) readScore += 10

                readability = readScore.coerceIn(40, 100)

                // 2. Project Strength Score calculation
                var projScore = 30
                if (resumeText.contains("project", ignoreCase = true)) projScore += 20

                val actionVerbs = listOf("built", "implemented", "deployed", "designed", "optimized", "integrated", "developed", "architected", "migrated", "automated")
                val foundVerbs = actionVerbs.count { resumeText.contains(it, ignoreCase = true) }
                projScore += (foundVerbs * 4).coerceAtMost(20)

                val metrics = listOf("%", "users", "scale", "performance", "reduced", "increased", "dollar", "optimized", "ms")
                val foundMetrics = metrics.count { resumeText.contains(it, ignoreCase = true) }
                projScore += (foundMetrics * 4).coerceAtMost(20)

                if (resumeText.contains("github.com", ignoreCase = true) || resumeText.contains("gitlab.com", ignoreCase = true)) projScore += 10

                projectStrength = projScore.coerceIn(30, 100)

                // 3. Keyword Density Score calculation
                val allKeywords = role.requiredSkills + role.atsKeywords
                var matchCount = 0
                val textLower = resumeText.lowercase()
                allKeywords.forEach { kw ->
                    val occurrences = textLower.split(kw.lowercase()).size - 1
                    if (occurrences > 0) {
                        matchCount += occurrences
                    }
                }
                val density = if (words > 0) (matchCount * 100.0) / words else 0.0
                keywordDensity = when {
                    density in 2.2..5.5 -> 96
                    density in 1.5..2.2 -> 85
                    density in 5.5..7.5 -> 82
                    density in 7.5..10.0 -> 60
                    density < 1.5 -> 55
                    else -> 40
                }

                // Populate resume issues based on analytics
                if (words < 200) issues.add(ResumeIssue("Word Count", "Profile description is too brief. Ideal length is 250-600 words.", "High"))
                if (words > 800) issues.add(ResumeIssue("Verbosity", "Resume is highly wordy. Recruiters prefer concise, bulleted resumes.", "Medium"))
                if (!resumeText.contains("project", ignoreCase = true)) issues.add(ResumeIssue("Structure", "No Projects section detected. Highlight practical works.", "High"))
                if (foundVerbs < 3) issues.add(ResumeIssue("Impact", "Add strong action verbs like 'Architected' or 'Deployed' to state outcomes.", "Medium"))
                if (foundMetrics < 2) issues.add(ResumeIssue("Quantifiable Results", "Include metrics (e.g., 'improved latency by 30%', '500+ active users') to show value.", "High"))
                if (keywordDensity < 60) issues.add(ResumeIssue("Optimization", "Keyword density is very low (${String.format("%.1f", density)}%). Missing key industry terms.", "Medium"))
                if (keywordDensity > 85) issues.add(ResumeIssue("Stuffing", "Keyword density is too high (${String.format("%.1f", density)}%). Avoid keyword stuffing.", "Medium"))

                // --- PROFESSIONAL MULTI-FACTOR ATS SCORING ---
                // 1. Keyword Match Rate (60% weight)
                val allTargetKeywords = (role.requiredSkills + role.atsKeywords).distinct()
                var keywordsMatched = 0
                allTargetKeywords.forEach { kw ->
                    val kwLower = kw.lowercase()
                    val listToCheck = when (kwLower) {
                        "restful api", "restful apis" -> listOf("rest api", "restful api", "restful apis", "rest apis")
                        "mern stack" -> listOf("mern stack", "mern")
                        "infrastructure as code" -> listOf("infrastructure as code", "iac")
                        "native mobile" -> listOf("native mobile", "android", "ios")
                        "deep learning models" -> listOf("deep learning", "neural network")
                        "nlp processing", "nlp" -> listOf("nlp", "natural language processing")
                        else -> listOf(kwLower)
                    }
                    val isMatched = listToCheck.any { term ->
                        val escapedTerm = Regex.escape(term)
                        val isPureAlpha = term.all { it.isLetter() || it.isWhitespace() }
                        if (isPureAlpha) {
                            textLower.contains(Regex("(?i)\\b$escapedTerm\\b"))
                        } else {
                            textLower.contains(Regex("(?i)(?:^|[^a-zA-Z0-9])($escapedTerm)(?:$|[^a-zA-Z0-9])"))
                        }
                    }
                    if (isMatched) {
                        keywordsMatched++
                    }
                }
                
                val keywordMatchRate = if (allTargetKeywords.isNotEmpty()) (keywordsMatched * 100) / allTargetKeywords.size else 80
                
                // 2. Formatting & Structure (15% weight)
                val formattingComponent = readability
                
                // 3. Impact & Action-Oriented (15% weight)
                val impactComponent = projectStrength
                
                // 4. Keyword Density (10% weight)
                val densityComponent = keywordDensity
                
                // Weighted average: 60% Keyword Match + 15% Readability + 15% Project/Impact Strength + 10% Keyword Density
                val computedAts = (keywordMatchRate * 0.60 + formattingComponent * 0.15 + impactComponent * 0.15 + densityComponent * 0.10).toInt()
                atsScore = computedAts.coerceIn(30, 100)
            } else {
                // Baseline score when no resume is uploaded yet (estimating from database manual/extracted skills):
                val allTargetKeywords = (role.requiredSkills + role.atsKeywords).distinct()
                val matchingInSkills = allTargetKeywords.filter { it.lowercase() in userSkills }
                val matchRate = if (allTargetKeywords.isNotEmpty()) (matchingInSkills.size * 100) / allTargetKeywords.size else 0
                atsScore = (50 + (matchRate * 0.5)).toInt().coerceIn(30, 100)
            }

            // --- CAREER RISK ANALYSIS (Low vs High demand replacement suggestions) ---
            val riskItems = mutableListOf<CareerRiskItem>()
            role.outdatedSkillsReplacements.forEach { (outdated, replacement) ->
                val isUserOutdated = userSkills.contains(outdated.lowercase()) || resumeText.lowercase().contains(outdated.lowercase())
                if (isUserOutdated) {
                    riskItems.add(CareerRiskItem(outdated, "High", "REPLACE IMMEDIATELY", replacement))
                } else {
                    riskItems.add(CareerRiskItem(outdated, "Medium", "UPGRADE AVAILABLE", replacement))
                }
            }
            if (riskItems.isEmpty()) {
                riskItems.add(CareerRiskItem("Legacy Scripting / Manual Workflows", "Medium", "UPGRADE AVAILABLE", "Modern Container APIs"))
                riskItems.add(CareerRiskItem("Monolithic local setups", "Medium", "UPGRADE AVAILABLE", "Cloud Serverless Architectures"))
            }

            // --- FUTURE SKILLS DEMAND PREDICTION ---
            val future = role.futureTrendingSkills.ifEmpty {
                when {
                    jobTitle.contains("DevOps", true) -> listOf("GitOps Git-Action flow", "Infrastructure Drift Auditing", "Zero Trust Secrets manager")
                    jobTitle.contains("Cyber", true) -> listOf("Zero Trust Access Policy", "Automated Threat response", "AI-based vulnerability scans")
                    jobTitle.contains("Cloud", true) -> listOf("Multi-cloud orchestrator", "FinOps Budget alerts", "Cloud Serverless Containers")
                    jobTitle.contains("AI", true) -> listOf("RAG Vector knowledgebases", "AI Agent orchestrator frameworks", "Reinforcement learning (RLHF)")
                    else -> listOf("Advanced Cloud integrations", "AI-assisted coding architectures", "Serverless Edge deployments")
                }
            }

            stepCallback?.invoke("ats", AnalysisStepStatus.DONE)
            delay(150)

            // Step 5: Roadmap generation
            stepCallback?.invoke("roadmap", AnalysisStepStatus.IN_PROGRESS)
            delay(400)

            // --- ADAPTIVE ROADMAP SCHEDULER ---
            val difficulty = when {
                matchPercent < 45 -> "Beginner - Foundation Acceleration Stage"
                matchPercent in 45..75 -> "Intermediate - Growth & Application Stage"
                else -> "Advanced - Industry Specialization Stage"
            }

            val p1 = role.roadmapPhase1.filter { it.lowercase() !in userSkills }
            val p2 = role.roadmapPhase2.filter { it.lowercase() !in userSkills }
            val p3 = role.roadmapPhase3.filter { it.lowercase() !in userSkills }

            val phases = listOf(
                RoadmapPhase("Phase 1: Foundation Core", p1.ifEmpty { listOf("Advanced Foundation Principles", "Scalable System Mechanics") }, difficulty, p1.isEmpty()),
                RoadmapPhase("Phase 2: Technical Application", p2.ifEmpty { listOf("High-Throughput Integrations", "Optimized Component Flow") }, difficulty, p2.isEmpty()),
                RoadmapPhase("Phase 3: Production Deployment", p3.ifEmpty { listOf("Enterprise cloud deployment scaling", "Production audit review checks") }, difficulty, p3.isEmpty())
            )

            stepCallback?.invoke("roadmap", AnalysisStepStatus.DONE)
            delay(150)

            // Step 6: Career risk
            stepCallback?.invoke("risk", AnalysisStepStatus.IN_PROGRESS)
            delay(300)

            val smartRecs = missing.map { skill ->
                Recommendation("Master $skill", "YouTube / Coursera", "Course")
            }.take(3).ifEmpty { 
                listOf(Recommendation("Advanced Portfolio Building", "Self-Paced", "Project"))
            }

            _analysisResult.value = AnalysisResult(
                matchPercentage = matchPercent,
                matchingSkills = matching,
                missingSkills = missing,
                atsScore = atsScore,
                readabilityScore = readability,
                projectStrengthScore = projectStrength,
                keywordDensityScore = keywordDensity,
                resumeIssues = issues,
                roadmapPhases = phases,
                prepQuestions = role.interviewQuestions,
                smartRecommendations = smartRecs,
                futureSkills = future,
                careerRiskItems = riskItems
            )
            
            // Update current user statistics
            val updatedExp = (matchPercent * 12) + (atsScore * 6)
            val baseUser = if (customJobTitle != null) currentUser.copy(dreamJob = customJobTitle) else currentUser
            repository.updateUser(baseUser.copy(
                technicalScore = matchPercent,
                atsScore = atsScore,
                employabilityScore = (matchPercent + atsScore + readability + projectStrength) / 4,
                expPoints = updatedExp,
                level = (updatedExp / 500).coerceAtLeast(1)
            ))

            stepCallback?.invoke("risk", AnalysisStepStatus.DONE)
            delay(150)

            // Step 7: Save results
            stepCallback?.invoke("save", AnalysisStepStatus.IN_PROGRESS)
            delay(200)

            // --- RESUME EVOLUTION HISTORY SAVING ---
            if (resumeText.isNotEmpty()) {
                val historyEntry = ResumeHistory(
                    userId = currentUser.id,
                    timestamp = System.currentTimeMillis(),
                    compatibilityScore = matchPercent,
                    atsScore = atsScore,
                    readabilityScore = readability,
                    projectStrengthScore = projectStrength,
                    keywordDensityScore = keywordDensity,
                    fileName = "Resume_${System.currentTimeMillis() % 100000}.pdf"
                )
                repository.saveResumeHistory(historyEntry)
            }

            stepCallback?.invoke("save", AnalysisStepStatus.DONE)
        }
    }

    fun getChatResponse(query: String): String {
        val q = query.lowercase()
        val currentJob = user.value?.dreamJob ?: "Future Leader"
        val analysis = _analysisResult.value
        val missing = analysis.missingSkills
        val matching = analysis.matchingSkills

        return when {
            q.contains("project") || q.contains("build") -> {
                val gapText = if (missing.isNotEmpty()) {
                    "focusing on your missing skills like **${missing.take(2).joinToString()}**"
                } else {
                    "focusing on advanced systems design and enterprise deployments"
                }
                "🚀 **Custom Portfolio Project Recommendation**\n\nFor your target role as an **$currentJob**, I highly recommend building an **'End-to-End Enterprise Operations Control Hub'**.\n\n* **Features:** Fully responsive, localized database persistence, asynchronous caching, and automated testing integration.\n* **Goal:** Showcase your skills in a practical environment, $gapText.\n* **Implementation Tip:** Code this completely offline, write clean unit tests, and host it as a GitHub project."
            }
            
            q.contains("roadmap") || q.contains("plan") || q.contains("step") -> {
                val nextUp = if (missing.isNotEmpty()) {
                    "Next immediate skills to master: **${missing.take(3).joinToString(", ")}**"
                } else {
                    "You've cleared all basic gaps! Start studying future trends: **${analysis.futureSkills.take(2).joinToString(", ")}**"
                }
                "📅 **Your Personalized Adaptive Roadmap**\n\nBased on your profile matching score of **${analysis.matchPercentage}%**:\n\n1. **Active Phase:** Focus on bridging specific technical gaps.\n2. **Action Item:** $nextUp.\n3. **Methodology:** We recommend 70% practical coding sprints and 30% local textbook/video study. You can check the 'Roadmap' tab inside Placement Prep for a day-by-day sprint plan."
            }

            q.contains("cert") || q.contains("certification") -> {
                val roleData = JobData.roles.find { it.title == currentJob }
                val certs = roleData?.recommendedCerts?.joinToString("\n* ") ?: "Google Career Certificates, Meta Developer Certs"
                "🎓 **Recommended Industry Certifications**\n\nTo boost your resume verification factor for a **$currentJob** role, prepare locally for:\n\n* $certs\n\n*Tip: Focus on building a strong GitHub repository as primary proof, which often outweighs formal certificates in advanced engineering rounds!*"
            }

            q.contains("resume") || q.contains("score") || q.contains("audit") || q.contains("ats") -> {
                "📝 **Resume Analysis Audit Report**\n\n* **ATS Score:** `${analysis.atsScore}/100` (Industry compliance status)\n* **Readability:** `${analysis.readabilityScore}/100` (Clarity and structure index)\n* **Project Strength:** `${analysis.projectStrengthScore}/100` (Quantifiable action impacts)\n* **Keyword Density:** `${analysis.keywordDensityScore}/100` (Term frequency distribution)\n\n💡 **Improvement Tips:** Add strong action verbs (built, deployed, scaled), include metrics (e.g. 'improved performance by 25%'), and clear out unused legacy technologies."
            }

            q.contains("outdated") || q.contains("risk") || q.contains("replace") -> {
                val replacementsList = analysis.careerRiskItems.joinToString("\n") { 
                    "* Legacy: **${it.outdatedSkill}** $\\rightarrow$ High-Demand replacement: **${it.suggestedReplacement}** (${it.status})" 
                }
                "⚠️ **Career Risk & Outdated Skills Audit**\n\nIndustry trends move rapidly. Here are the low-demand legacy skills vs high-demand replacements identified for a **$currentJob**:\n\n$replacementsList\n\n*Recommendation: Phase out legacy tech from your project profiles and rewrite descriptions emphasizing modern standards!*"
            }

            q.contains("interview") || q.contains("question") || q.contains("drill") -> {
                val topic = missing.firstOrNull() ?: matching.firstOrNull() ?: "Software Engineering"
                "❓ **Mock Interview Technical Drill**\n\nLet's test your understanding on **$topic** (a key area for a **$currentJob**):\n\n* **Question:** Explain the core principles of **$topic**, how you avoid common pitfalls when deploying it in production, and how you design it for maximum performance.\n\n*How to answer: Think about structural trade-offs, error handling, and thread safety. Try detailing your answer in the simulator below!*"
            }

            else -> "Hello! I am your offline career consultant. I can guide you through career preparation for **$currentJob**.\n\nAsk me about:\n* 💡 **'Suggest custom projects'** to bridge your specific missing skills.\n* 📅 **'Show my learning roadmap'** or day-by-day plan.\n* 🎓 **'What certifications do I need?'**\n* 📝 **'Audit my resume score'** (ATS, readability, etc.)\n* ⚠️ **'Analyze career risk'** or outdated skills.\n* ❓ **'Give me an interview question'** to drill on technical terms."
        }
    }

    fun getCompanyChatResponse(query: String): String {
        val q = query.lowercase().trim()
        
        // Find if a company name is mentioned
        val mentionedCompany = com.sriram.skillgap.utils.CompanyData.companies.find { 
            q.contains(it.name.lowercase()) || q.contains(it.logoText.lowercase())
        }
        
        val company = mentionedCompany ?: com.sriram.skillgap.utils.CompanyData.getCompanyByName(selectedCompany.value ?: "")
        
        if (company == null) {
            return "🤖 **Dream Company Preparation Assistant**\n\nI can help you prepare for any of the top 20 target companies (Google, TCS, Infosys, Zoho, Microsoft, etc.) completely offline!\n\n**Ask me things like:**\n* 🚀 *\"How to crack TCS interview?\"*\n* ☕ *\"Most asked Java questions in Infosys?\"*\n* 🗺️ *\"Google DSA preparation roadmap\"*\n* 💼 *\"What is Microsoft's hiring process?\"*\n* 💰 *\"What is Zoho's average package?\"*\n\nSelect a company from the dashboard above to focus our preparation hub!"
        }
        
        val name = company.name
        
        return when {
            // How to crack / general roadmap
            q.contains("crack") || q.contains("prepare") || q.contains("how to join") -> {
                val round1 = company.hiringRounds.firstOrNull()
                val round2 = company.hiringRounds.getOrNull(1)
                "🚀 **How to Crack $name Interviews**\n\nTo clear the placement rounds at **$name**, follow this structured execution plan:\n\n1. **Analyze Eligibility & Tech:** Ensure you meet the criteria: *${company.eligibilityCriteria}*. Focus heavily on *${company.requiredTech.joinToString(", ")}*.\n2. **Master the High-Weight Rounds:**\n   * **${round1?.roundName}** (${round1?.difficulty} difficulty): ${round1?.prepTips}\n   * **${round2?.roundName}** (${round2?.difficulty} difficulty): ${round2?.prepTips}\n3. **Solve Company-Specific Questions:** Go to the *Interview Question Bank* tab inside our Prep Hub and complete all questions categorized for $name.\n4. **Work Culture Alignment:** During the final rounds, align your answers with their core culture: *${company.workCulture}*."
            }
            
            // Java questions
            q.contains("java") -> {
                val javaQuestions = company.questions.filter { it.category.equals("Java", ignoreCase = true) || it.category.equals("OOPS", ignoreCase = true) }
                val builder = StringBuilder("☕ **Most Asked Java & OOPS Questions in $name**\n\nHere are the top questions frequently asked during $name's technical screening:\n\n")
                if (javaQuestions.isNotEmpty()) {
                    javaQuestions.forEachIndexed { idx, qn ->
                        builder.append("${idx + 1}. **Q: ${qn.question}** *(Difficulty: ${qn.difficulty})*\n")
                        builder.append("   * **A:** ${qn.answer}\n\n")
                    }
                } else {
                    builder.append("1. **Q: What are the main features of Java?**\n")
                    builder.append("   * **A:** Platform independence (bytecode), Garbage Collection, robust memory management, and multi-threading.\n\n")
                    builder.append("2. **Q: How does OOPS help in large systems?**\n")
                    builder.append("   * **A:** It provides modifiability, extensibility, and reusability through inheritance, encapsulation, polymorphism, and abstraction.\n")
                }
                builder.toString()
            }
            
            // DSA roadmaps or questions
            q.contains("dsa") || q.contains("coding") || q.contains("algorithms") -> {
                val dsaQuestions = company.questions.filter { it.category.equals("DSA", ignoreCase = true) }
                val roundInfo = company.hiringRounds.find { it.roundName.contains("Coding", ignoreCase = true) || it.roundName.contains("Technical", ignoreCase = true) }
                val builder = StringBuilder("🗺️ **$name DSA & Coding Interview Roadmap**\n\n")
                builder.append("💼 **Hiring Standard:** $name's coding assessments are rated **${roundInfo?.difficulty ?: "Advanced"}**. The average assessment lasts *${roundInfo?.duration ?: "60-90 minutes"}*.\n\n")
                builder.append("💡 **Most Asked DSA Topics:** *${roundInfo?.commonTopics?.joinToString(", ") ?: "Arrays, Strings, Dynamic Programming, Trees"}*\n\n")
                if (dsaQuestions.isNotEmpty()) {
                    builder.append("❓ **Key Practice Problem Example:**\n\n")
                    dsaQuestions.take(1).forEach { qn ->
                        builder.append("**Problem: ${qn.question}**\n")
                        builder.append("**Solution Guide:** ${qn.answer}\n\n")
                    }
                }
                builder.append("🎯 **Practice Task:** You can solve standard dynamic programming and graph algorithms using our local coding simulator under the *Practice Questions* tab.")
                builder.toString()
            }
            
            // Hiring process
            q.contains("hiring") || q.contains("process") || q.contains("round") || q.contains("selection") -> {
                val builder = StringBuilder("💼 **$name Complete Placement Hiring Process Flow**\n\n")
                builder.append("The hiring pipeline at **$name** consists of ${company.hiringRounds.size} primary stages:\n\n")
                company.hiringRounds.forEachIndexed { idx, round ->
                    builder.append("🔹 **Round ${idx + 1}: ${round.roundName}**\n")
                    builder.append("   * **Difficulty:** `${round.difficulty}` | **Duration:** `${round.duration}`\n")
                    builder.append("   * **Key Topics:** *${round.commonTopics.joinToString(", ")}*\n")
                    builder.append("   * **Preparation Tip:** *${round.prepTips}*\n\n")
                }
                builder.toString()
            }
            
            // Salary / package / explorer
            q.contains("salary") || q.contains("package") || q.contains("pay") || q.contains("lpa") || q.contains("role") -> {
                val builder = StringBuilder("💰 **Available Job Roles & Expected Packages at $name**\n\n")
                builder.append("Here is the list of active entry-level engineering roles and average packages at $name:\n\n")
                company.roles.forEach { role ->
                    builder.append("🏢 **${role.roleTitle}**\n")
                    builder.append("   * **Expected Package:** `${role.expectedPackage}`\n")
                    builder.append("   * **Required Core Skills:** *${role.requiredSkills.joinToString(", ")}*\n")
                    builder.append("   * **Crack Roadmap:** *${role.roadmapSteps.joinToString(" ➔ ")}*\n\n")
                }
                builder.append("💡 *Note: Higher packages are frequently offered for outstanding off-campus coding performance and hackathon achievements.*")
                builder.toString()
            }
            
            // Default company overview
            else -> {
                "🏢 **$name Company Information Dashboard**\n\n" +
                "* **Headquarters:** ${company.headquarters}\n" +
                "* **Company Type:** `${company.type}`\n" +
                "* **Employee Strength:** ${company.employeeCount}\n" +
                "* **Salary Range:** `${company.salaryRange}`\n" +
                "* **Eligibility Criteria:** *${company.eligibilityCriteria}*\n\n" +
                "🌟 **Work Culture:**\n${company.workCulture}\n\n" +
                "🛠️ **Required Technologies:**\n${company.requiredTech.joinToString(", ")}\n\n" +
                "💡 *Tip: Ask me specifically about \"$name hiring process\", \"$name DSA questions\", or \"how to crack $name\" to get deeper guidance!*"
            }
        }
    }
}
