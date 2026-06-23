package com.sriram.skillgap.data.model

import com.google.firebase.firestore.DocumentId

data class Company(
    val companyId: String = "",
    val companyName: String = "",
    val logoUrl: String = "",
    val location: String = "",
    val industry: String = "",
    val website: String = "",
    val workCulture: String = "",
    val salaryRange: String = "",
    val technologiesUsed: List<String> = emptyList(),
    val eligibilityCriteria: String = "",
    val hiringProcess: List<String> = emptyList(),
    val interviewDifficulty: String = "", // Easy, Medium, Hard
    val lastUpdated: Long = System.currentTimeMillis()
) {
    // Backwards compatibility properties for UI
    val name: String get() = companyName
    val logo: String get() = logoUrl
    val technologies: List<String> get() = technologiesUsed
    val interviewProcess: List<String> get() = hiringProcess
}

data class Job(
    val jobId: String = "",
    val companyId: String = "",
    val companyName: String = "",
    val jobTitle: String = "",
    val jobRole: String = "",
    val requiredSkills: List<String> = emptyList(),
    val preferredSkills: List<String> = emptyList(),
    val experienceRequired: String = "",
    val salaryRange: String = "",
    val jobDescription: String = "",
    val applicationLink: String = "",
    val location: String = "",
    val hiringProcess: List<String> = emptyList(),
    val interviewDifficulty: String = "", // Easy, Medium, Hard
    val lastDateToApply: String = "",
    val createdAt: Long = System.currentTimeMillis()
) {
    // Backwards compatibility properties for UI
    val id: String get() = jobId
}

data class InterviewQuestion(
    val questionId: String = "",
    val companyId: String = "",
    val companyName: String = "",
    val role: String = "",
    val questionType: String = "", // Aptitude, Coding, Technical, HR
    val question: String = "",
    val difficulty: String = "", // Easy, Medium, Hard
    val answerHint: String = ""
) {
    // Backwards compatibility properties for UI
    val id: String get() = questionId
    val category: String get() = questionType
    val answer: String get() = answerHint
}

data class JobRoleModel(
    val roleId: String = "",
    val roleName: String = "",
    val requiredSkills: List<String> = emptyList(),
    val certifications: List<String> = emptyList(),
    val projects: List<String> = emptyList(),
    val roadmap: List<String> = emptyList(),
    val salaryRange: String = "",
    val futureScope: String = ""
) {
    // Backwards compatibility properties for UI
    val title: String get() = roleName
    val atsKeywords: List<String> get() = requiredSkills
}

data class UserSavedJob(
    val userId: String = "",
    val jobId: String = "",
    val companyName: String = "",
    val jobTitle: String = "",
    val savedDate: Long = System.currentTimeMillis()
)

data class UserApplication(
    val userId: String = "",
    val jobId: String = "",
    val companyName: String = "",
    val jobTitle: String = "",
    val applicationStatus: String = "", // Interested, Applied, Interview Scheduled, Selected, Rejected, Offer Received
    val appliedDate: Long = System.currentTimeMillis(),
    val notes: String = ""
)

data class PlacementReadiness(
    val companyReadiness: Int = 0,
    val jobReadiness: Int = 0,
    val interviewReadiness: Int = 0,
    val selectionProbability: Int = 0
)
