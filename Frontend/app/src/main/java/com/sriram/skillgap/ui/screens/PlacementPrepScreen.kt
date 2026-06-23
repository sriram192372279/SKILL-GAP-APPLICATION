package com.sriram.skillgap.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sriram.skillgap.ui.components.BottomNavigationBar
import com.sriram.skillgap.ui.components.GlassCard
import com.sriram.skillgap.ui.components.PremiumGlassCard
import com.sriram.skillgap.ui.components.CircularScoreMeter
import com.sriram.skillgap.ui.components.RadarChart
import com.sriram.skillgap.ui.theme.*
import com.sriram.skillgap.utils.CompanyData
import com.sriram.skillgap.utils.CompanyInfo
import com.sriram.skillgap.utils.CompanyQuestion
import com.sriram.skillgap.utils.CompanyRoleInfo
import com.sriram.skillgap.viewmodel.SkillViewModel

// Local high-fidelity models for Role Preparation Intelligence System
data class LearningStage(
    val title: String,
    val timeline: String,
    val topics: List<String>,
    val projectIdea: String,
    val certifications: List<String>,
    val recommendedTools: List<String>
)

data class RolePrepQuestion(
    val question: String,
    val answer: String,
    val category: String // "Technical", "HR", "Scenario"
)

data class RoleCodingPractice(
    val id: String,
    val title: String,
    val problem: String,
    val codeAnswer: String,
    val difficulty: String
)

data class CareerRoleDetails(
    val title: String,
    val overview: String,
    val dailyResponsibilities: String,
    val salaryRange: String,
    val hiringTrends: String,
    val futureScope: String,
    val requiredSkills: List<String>,
    val technologies: List<String>,
    val dependencyMapSkills: List<String>, // Sequential sequence e.g., Java -> OOPS
    val beginnerRoadmap: LearningStage,
    val intermediateRoadmap: LearningStage,
    val advancedRoadmap: LearningStage,
    val interviewPrepQuestions: List<RolePrepQuestion>,
    val codingPractice: List<RoleCodingPractice>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacementPrepScreen(navController: NavController, viewModel: SkillViewModel) {
    val selectedCompany by viewModel.selectedCompany.collectAsState()
    val solvedAptitude by viewModel.solvedAptitude.collectAsState()
    val solvedCoding by viewModel.solvedCoding.collectAsState()
    val interviewConfidence by viewModel.interviewConfidence.collectAsState()
    val analysisResult by viewModel.analysisResult.collectAsState()

    var activeSubMode by remember { mutableStateOf("companies") } // "companies" or "roles"
    var selectedRolePath by remember { mutableStateOf<CareerRoleDetails?>(null) }

    // Dynamic readiness score calculation for selected company
    val currentCompanyInfo = remember(selectedCompany) {
        selectedCompany?.let { CompanyData.getCompanyByName(it) }
    }

    val readinessScore = remember(currentCompanyInfo, analysisResult, solvedAptitude, solvedCoding, interviewConfidence) {
        if (currentCompanyInfo == null) 0 else {
            val companyTech = currentCompanyInfo.requiredTech.map { it.lowercase() }
            val matchedUserSkills = analysisResult.matchingSkills.map { it.lowercase() }
            val matchingCount = companyTech.count { it in matchedUserSkills || matchedUserSkills.any { u -> u.contains(it) || it.contains(u) } }
            val techScoreFactor = if (companyTech.isNotEmpty()) (matchingCount.toFloat() / companyTech.size) * 40f else 20f

            val companyAptitudeQCount = currentCompanyInfo.questions.count { it.category == "Aptitude" }.coerceAtLeast(1)
            val solvedAptitudeInCompany = currentCompanyInfo.questions.filter { it.category == "Aptitude" }.count { solvedAptitude.contains(it.id) }
            val aptitudeFactor = (solvedAptitudeInCompany.toFloat() / companyAptitudeQCount) * 20f

            val companyCodingQCount = currentCompanyInfo.questions.count { it.category == "DSA" || it.category == "Java" }.coerceAtLeast(1)
            val solvedCodingInCompany = currentCompanyInfo.questions.filter { it.category == "DSA" || it.category == "Java" }.count { solvedCoding.contains(it.id) }
            val codingFactor = (solvedCodingInCompany.toFloat() / companyCodingQCount) * 20f

            val confidenceFactor = (interviewConfidence / 100f) * 20f

            (techScoreFactor + aptitudeFactor + codingFactor + confidenceFactor).coerceIn(10f, 100f).toInt()
        }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        containerColor = DeepDarkBlue,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (selectedCompany != null) "$selectedCompany Prep Hub"
                        else if (selectedRolePath != null) "${selectedRolePath!!.title} Prep"
                        else "Role Prep Intelligence",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepDarkBlue),
                navigationIcon = {
                    if (selectedCompany != null) {
                        IconButton(onClick = { viewModel.selectCompany(null) }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                    } else if (selectedRolePath != null) {
                        IconButton(onClick = { selectedRolePath = null }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                    }
                },
                actions = {
                    if (selectedCompany != null) {
                        Box(
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(NeonPurple.copy(alpha = 0.15f))
                                .border(1.dp, NeonPurple.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "$readinessScore% Ready",
                                color = NeonPurple,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else if (selectedRolePath != null) {
                        // Dynamic compatibility score for career role
                        val matchCount = selectedRolePath!!.requiredSkills.count {
                            it.lowercase() in analysisResult.matchingSkills.map { ms -> ms.lowercase() }
                        }
                        val compatibilityIndex = if (selectedRolePath!!.requiredSkills.isNotEmpty()) {
                            (matchCount * 100) / selectedRolePath!!.requiredSkills.size
                        } else 50
                        Box(
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(NeonBlue.copy(alpha = 0.15f))
                                .border(1.dp, NeonBlue.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "$compatibilityIndex% Compatible",
                                color = NeonBlue,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(DeepDarkBlue, Color(0xFF0F112D))
                    )
                )
        ) {
            if (selectedCompany != null) {
                val companyInfo = CompanyData.getCompanyByName(selectedCompany!!)
                if (companyInfo != null) {
                    CompanyPrepDashboardPhase(
                        companyInfo = companyInfo,
                        viewModel = viewModel,
                        readinessScore = readinessScore,
                        solvedAptitude = solvedAptitude,
                        solvedCoding = solvedCoding,
                        interviewConfidence = interviewConfidence,
                        userMatchingSkills = analysisResult.matchingSkills
                    )
                }
            } else if (selectedRolePath != null) {
                RolePrepDashboardPhase(
                    roleDetails = selectedRolePath!!,
                    viewModel = viewModel,
                    userMatchingSkills = analysisResult.matchingSkills,
                    solvedCoding = solvedCoding,
                    interviewConfidence = interviewConfidence,
                    analysisResultMissingSkills = analysisResult.missingSkills
                )
            } else {
                // Main split selector: Companies vs Career Roles
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceDark)
                            .padding(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (activeSubMode == "companies") NeonPurple.copy(0.2f) else Color.Transparent)
                                .border(1.dp, if (activeSubMode == "companies") NeonPurple else Color.Transparent, RoundedCornerShape(10.dp))
                                .clickable { activeSubMode = "companies" }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Dream Companies",
                                color = if (activeSubMode == "companies") Color.White else Color.Gray,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (activeSubMode == "roles") NeonBlue.copy(0.2f) else Color.Transparent)
                                .border(1.dp, if (activeSubMode == "roles") NeonBlue else Color.Transparent, RoundedCornerShape(10.dp))
                                .clickable { activeSubMode = "roles" }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Career Role Mastery",
                                color = if (activeSubMode == "roles") Color.White else Color.Gray,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }

                    if (activeSubMode == "companies") {
                        CompanySelectionPhase(
                            viewModel = viewModel,
                            analysisResultSkills = analysisResult.matchingSkills,
                            solvedAptitude = solvedAptitude,
                            solvedCoding = solvedCoding,
                            interviewConfidence = interviewConfidence
                        )
                    } else {
                        CareerRolesSelectionPhase(
                            userMatchingSkills = analysisResult.matchingSkills,
                            onSelectRole = { selectedRolePath = it }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CompanySelectionPhase(
    viewModel: SkillViewModel,
    analysisResultSkills: List<String>,
    solvedAptitude: Set<String>,
    solvedCoding: Set<String>,
    interviewConfidence: Int
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Product", "Service", "Consulting", "SaaS")

    val filteredCompanies = remember(searchQuery, selectedCategory) {
        CompanyData.companies.filter {
            val matchesSearch = it.name.contains(searchQuery, ignoreCase = true) ||
                    it.requiredTech.any { t -> t.contains(searchQuery, ignoreCase = true) }
            val matchesCategory = when (selectedCategory) {
                "All" -> true
                "Product" -> it.type.contains("Product", ignoreCase = true)
                "Service" -> it.type.contains("Service", ignoreCase = true)
                "Consulting" -> it.type.contains("Consulting", ignoreCase = true)
                "SaaS" -> it.type.contains("SaaS", ignoreCase = true)
                else -> true
            }
            matchesSearch && matchesCategory
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Target Dream Company",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Select one of the top 20 global companies to unlock specialized, completely offline mock coding pools, structured hiring timelines, custom role roadmaps, and local AI preparation bots.",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
            )

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceDark),
                placeholder = { Text("Search company or technologies...", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonPurple,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Category Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { cat ->
                    val isSelected = selectedCategory == cat
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) NeonPurple else SurfaceDark)
                            .border(1.dp, if (isSelected) Color.Transparent else Color.White.copy(0.1f), RoundedCornerShape(12.dp))
                            .clickable { selectedCategory = cat }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = cat,
                            color = if (isSelected) Color.Black else Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        if (filteredCompanies.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No companies matching filters found.", color = Color.Gray)
                }
            }
        } else {
            items(filteredCompanies) { company ->
                // Calculate dynamic company readiness score
                val compReadiness = remember(company, analysisResultSkills, solvedAptitude, solvedCoding, interviewConfidence) {
                    val companyTech = company.requiredTech.map { it.lowercase() }
                    val matchedUserSkills = analysisResultSkills.map { it.lowercase() }
                    val matchingCount = companyTech.count { it in matchedUserSkills || matchedUserSkills.any { u -> u.contains(it) || it.contains(u) } }
                    val techScoreFactor = if (companyTech.isNotEmpty()) (matchingCount.toFloat() / companyTech.size) * 40f else 20f

                    val companyAptitudeQCount = company.questions.count { it.category == "Aptitude" }.coerceAtLeast(1)
                    val solvedAptitudeInCompany = company.questions.filter { it.category == "Aptitude" }.count { solvedAptitude.contains(it.id) }
                    val aptitudeFactor = (solvedAptitudeInCompany.toFloat() / companyAptitudeQCount) * 20f

                    val companyCodingQCount = company.questions.count { it.category == "DSA" || it.category == "Java" }.coerceAtLeast(1)
                    val solvedCodingInCompany = company.questions.filter { it.category == "DSA" || it.category == "Java" }.count { solvedCoding.contains(it.id) }
                    val codingFactor = (solvedCodingInCompany.toFloat() / companyCodingQCount) * 20f

                    val confidenceFactor = (interviewConfidence / 100f) * 20f

                    (techScoreFactor + aptitudeFactor + codingFactor + confidenceFactor).coerceIn(10f, 100f).toInt()
                }

                CompanySelectionCard(
                    company = company,
                    readiness = compReadiness,
                    onClick = { viewModel.selectCompany(company.name) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            GlobalAnalyticsCard(solvedAptitude.size, solvedCoding.size, interviewConfidence)
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// TAB: CAREER ROLE SELECTION
@Composable
fun CareerRolesSelectionPhase(
    userMatchingSkills: List<String>,
    onSelectRole: (CareerRoleDetails) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val matchingSet = remember(userMatchingSkills) { userMatchingSkills.map { it.lowercase() }.toSet() }

    val filteredRoles = remember(searchQuery) {
        LocalCareerRoleData.roles.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
                    it.requiredSkills.any { s -> s.contains(searchQuery, ignoreCase = true) }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Detailed Career Role Mastery",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Master core skill maps, design custom zig-zag dependency tracks, trace 7/30/90-day timeplans, and execute scenario practice modules customized for 7 prime tech career pathways.",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
            )

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceDark),
                placeholder = { Text("Search engineering roles...", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonBlue,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true
            )
        }

        items(filteredRoles) { role ->
            // Calculate dynamic compatibility
            val matchedCount = role.requiredSkills.count {
                it.lowercase() in matchingSet || matchingSet.any { u -> u.contains(it.lowercase()) || it.lowercase().contains(u) }
            }
            val fitPercent = if (role.requiredSkills.isNotEmpty()) (matchedCount * 100) / role.requiredSkills.size else 50

            PremiumGlassCard(
                modifier = Modifier.clickable { onSelectRole(role) }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(NeonBlue.copy(0.12f))
                            .border(1.dp, NeonBlue.copy(0.4f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when {
                                role.title.contains("Java", ignoreCase = true) -> Icons.Default.Code
                                role.title.contains("Data", ignoreCase = true) -> Icons.Default.Equalizer
                                role.title.contains("AI", ignoreCase = true) -> Icons.Default.Memory
                                role.title.contains("Cloud", ignoreCase = true) -> Icons.Default.Cloud
                                role.title.contains("Security", ignoreCase = true) -> Icons.Default.Security
                                role.title.contains("DevOps", ignoreCase = true) -> Icons.Default.Build
                                else -> Icons.Default.Terminal
                            },
                            contentDescription = null,
                            tint = NeonBlue,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(role.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(
                                text = "$fitPercent% Fit",
                                color = if (fitPercent >= 75) SuccessGreen else if (fitPercent >= 45) WarningYellow else HotPink,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        Text(role.salaryRange, color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(top = 2.dp))

                        Spacer(modifier = Modifier.height(6.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(role.technologies.take(4)) { tech ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color.White.copy(0.06f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(tech, color = Color.White.copy(0.7f), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = NeonBlue)
                }
            }
        }
        
        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}

// TAB 1-6 DYNAMIC EXPLORER FOR SELECTED CAREER PATH
@Composable
fun RolePrepDashboardPhase(
    roleDetails: CareerRoleDetails,
    viewModel: SkillViewModel,
    userMatchingSkills: List<String>,
    solvedCoding: Set<String>,
    interviewConfidence: Int,
    analysisResultMissingSkills: List<String>
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Snapshot", "Skill Map", "Roadmaps", "Interview Prep", "Coding Practice", "Weakness Audit")

    // Calculate dynamic role progress scores
    val matchingSet = remember(userMatchingSkills) { userMatchingSkills.map { it.lowercase() }.toSet() }
    val successProbability = remember(roleDetails, matchingSet) {
        val matched = roleDetails.requiredSkills.count { it.lowercase() in matchingSet || matchingSet.any { u -> u.contains(it.lowercase()) } }
        if (roleDetails.requiredSkills.isNotEmpty()) (matched * 100) / roleDetails.requiredSkills.size else 50
    }

    val codingProgress = remember(roleDetails, solvedCoding) {
        val solvedInRole = roleDetails.codingPractice.count { solvedCoding.contains(it.id) }
        if (roleDetails.codingPractice.isNotEmpty()) (solvedInRole * 100) / roleDetails.codingPractice.size else 0
    }

    val interviewReadiness = remember(interviewConfidence, successProbability) {
        ((successProbability * 0.6f) + (interviewConfidence * 0.4f)).toInt().coerceIn(10, 100)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Summary Header with Dials
        PremiumGlassCard(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(roleDetails.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("Standard Package: ${roleDetails.salaryRange}", color = NeonBlue, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Text("Future Outlook: ${roleDetails.futureScope}", color = Color.Gray, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
                }

                Spacer(modifier = Modifier.width(16.dp))

                CircularScoreMeter(
                    score = successProbability,
                    label = "Success Prob",
                    modifier = Modifier.size(80.dp),
                    color = if (successProbability >= 75) SuccessGreen else NeonPurple
                )
            }
        }

        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.Transparent,
            contentColor = Color.White,
            edgePadding = 16.dp,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = NeonBlue
                )
            },
            divider = {}
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp
                        )
                    },
                    selectedContentColor = NeonBlue,
                    unselectedContentColor = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                0 -> RoleSnapshotTab(roleDetails)
                1 -> RoleSkillMapTab(roleDetails)
                2 -> RoleRoadmapsTab(roleDetails, viewModel)
                3 -> RoleInterviewPrepTab(roleDetails)
                4 -> RoleCodingPracticeTab(roleDetails, viewModel, solvedCoding)
                5 -> RoleWeaknessAuditTab(roleDetails, analysisResultMissingSkills, userMatchingSkills)
            }
        }
    }
}

// SUB-TAB 1: SNAPSHOT
@Composable
fun RoleSnapshotTab(role: CareerRoleDetails) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            GlassCard {
                Text("Role Overview", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text(role.overview, color = Color.White.copy(0.85f), fontSize = 13.sp, lineHeight = 20.sp)
            }
        }

        item {
            GlassCard {
                Text("Daily Responsibilities", color = NeonBlue, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text(role.dailyResponsibilities, color = Color.White.copy(0.85f), fontSize = 13.sp, lineHeight = 20.sp)
            }
        }

        item {
            GlassCard {
                Text("Hiring Trends & Future Outlook", color = HotPink, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(12.dp))

                SnapshotRow("Global Hiring Demands", role.hiringTrends, Icons.Default.TrendingUp)
                Divider(color = Color.White.copy(0.08f), thickness = 0.5.dp, modifier = Modifier.padding(vertical = 8.dp))
                SnapshotRow("Professional Scope", role.futureScope, Icons.Default.Star)
            }
        }

        item {
            GlassCard {
                Text("Required Tech Stack", color = WarningYellow, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(role.technologies) { tech ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White.copy(alpha = 0.08f))
                                .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(tech, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}

// SUB-TAB 2: SKILL DEPENDENCY MAP
@Composable
fun RoleSkillMapTab(role: CareerRoleDetails) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Interactive Skill Dependency Map",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "High-fidelity visualization illustrating optimal sequential path for acquiring skills offline. Follow arrow tracks to master technology loops.",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }

        item {
            // Draw visual nodes connected by Bezier curves
            PremiumGlassCard(modifier = Modifier.fillMaxWidth().height(280.dp)) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height

                        // Coordinates for 5 nodes in a zig-zag curve
                        val n1 = Offset(w * 0.18f, h * 0.18f)
                        val n2 = Offset(w * 0.78f, h * 0.35f)
                        val n3 = Offset(w * 0.22f, h * 0.55f)
                        val n4 = Offset(w * 0.75f, h * 0.72f)
                        val n5 = Offset(w * 0.45f, h * 0.88f)

                        val brush = Brush.linearGradient(
                            colors = listOf(NeonBlue, NeonPurple, HotPink)
                        )

                        drawBezierConnection(this, n1, n2, brush)
                        if (role.dependencyMapSkills.size > 2) drawBezierConnection(this, n2, n3, brush)
                        if (role.dependencyMapSkills.size > 3) drawBezierConnection(this, n3, n4, brush)
                        if (role.dependencyMapSkills.size > 4) drawBezierConnection(this, n4, n5, brush)
                    }

                    // Node Placement overlays
                    val alignments = listOf(
                        BiasAlignment(-0.65f, -0.65f),
                        BiasAlignment(0.65f, -0.35f),
                        BiasAlignment(-0.6f, 0.12f),
                        BiasAlignment(0.58f, 0.48f),
                        BiasAlignment(-0.1f, 0.82f)
                    )

                    role.dependencyMapSkills.take(5).forEachIndexed { index, skillName ->
                        val alignment = alignments.getOrElse(index) { Alignment.Center }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            contentAlignment = alignment
                        ) {
                            DependencyNodeCapsule(name = skillName, step = index + 1)
                        }
                    }
                }
            }
        }

        item {
            GlassCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.HelpOutline, contentDescription = null, tint = NeonBlue)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Reading Dependency tracks", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(
                            "Each node represents a foundational milestone. We strongly recommend mastering core systems (e.g. OOPS) completely before moving onto frameworks (e.g. Spring Boot) to ensure strong technical interview capability.",
                            color = Color.Gray,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}

// SUB-TAB 3: ROADMAPS
@Composable
fun RoleRoadmapsTab(role: CareerRoleDetails, viewModel: SkillViewModel) {
    var roadMode by remember { mutableStateOf("stages") } // "stages" (Beginner/Inter/Adv) or "days" (7/30/90 days)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceDark)
                    .padding(3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (roadMode == "stages") NeonPurple.copy(0.2f) else Color.Transparent)
                        .clickable { roadMode = "stages" }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Mastery Stages", color = if (roadMode == "stages") Color.White else Color.Gray, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (roadMode == "days") NeonBlue.copy(0.2f) else Color.Transparent)
                        .clickable { roadMode = "days" }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Time-Bound Sprints", color = if (roadMode == "days") Color.White else Color.Gray, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        if (roadMode == "stages") {
            item {
                Text("Complete 3-Stage Curriculum", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Curated technical pathways covering beginner configurations to advanced system frameworks.", color = Color.Gray, fontSize = 12.sp)
            }

            listOf(role.beginnerRoadmap, role.intermediateRoadmap, role.advancedRoadmap).forEachIndexed { idx, stage ->
                item {
                    val color = when (idx) {
                        0 -> SuccessGreen
                        1 -> NeonPurple
                        else -> NeonBlue
                    }
                    PremiumGlassCard {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(stage.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(color.copy(0.12f))
                                    .border(1.dp, color, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(stage.timeline, color = color, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Key Topics to Master:", color = NeonBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(stage.topics) { topic ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color.White.copy(0.06f))
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(topic, color = Color.White.copy(0.8f), fontSize = 10.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Recommended Project Idea:", color = NeonPurple, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text(stage.projectIdea, color = Color.White.copy(0.85f), fontSize = 12.sp, modifier = Modifier.padding(top = 2.dp))

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Suggested Tools", color = Color.Gray, fontSize = 10.sp)
                                Text(stage.recommendedTools.joinToString(", "), color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Recommended Certification", color = Color.Gray, fontSize = 10.sp)
                                Text(stage.certifications.firstOrNull() ?: "Industry standards", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        } else {
            // Days-based sprints (7-day, 30-day, 90-day plans)
            item {
                Text("Time-Bound Placement Checklists", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Select checklists to optimize daily studies, coding, and mock milestones.", color = Color.Gray, fontSize = 12.sp)
            }

            // Let's implement a neat expanding sprint list for 7, 30, and 90 Days!
            item {
                ExpandingSprintCard(
                    title = "7-Day Placement Sprint",
                    description = "High-intensity technical warmup focused on syntax and critical MCQs.",
                    tasks = listOf(
                        "Day 1: Languages Core Syntax & Arrays. Setup local workspace.",
                        "Day 2: General Aptitude math drills (Time-distance, percentages).",
                        "Day 3: Basic string processing & sorting coding algorithms.",
                        "Day 4: Core OOPs definitions, classes, and polymorph checks.",
                        "Day 5: Mock Interview warmups (STAR scenarios, introduction).",
                        "Day 6: Resume keywords check & ATS score adjustment.",
                        "Day 7: Final Revision of 10 primary question bank metrics."
                    ),
                    color = NeonPurple
                )
            }

            item {
                ExpandingSprintCard(
                    title = "30-Day Intermediate Placement Roadmap",
                    description = "Standard 4-week structured preparation covering core DSA, logical puzzles, and mock checklists.",
                    tasks = listOf(
                        "Week 1: High-fidelity DSA. Trace Binary Search, Matrix, HashMaps, stacks.",
                        "Week 2: SQL aggregates, joints, schema design, and basic Normalizations.",
                        "Week 3: Core OOPs designs + REST APIs endpoint structures.",
                        "Week 4: Real-time system mock drills and HR STAR script alignments."
                    ),
                    color = NeonBlue
                )
            }

            item {
                ExpandingSprintCard(
                    title = "90-Day Comprehensive Mastery Plan",
                    description = "Deep-dive career curriculum designed to build patent-level expertise from scratch.",
                    tasks = listOf(
                        "Month 1: Absolute foundation logic. Complete 50 coding problems, master compilers.",
                        "Month 2: High Availability System Designs. Learn load balancing, cache layers, sharding.",
                        "Month 3: Full Stack deployments. Host personal project databases and deploy secure workflows."
                    ),
                    color = SuccessGreen
                )
            }
        }
        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}

@Composable
fun ExpandingSprintCard(
    title: String,
    description: String,
    tasks: List<String>,
    color: Color
) {
    var expanded by remember { mutableStateOf(false) }
    PremiumGlassCard(
        modifier = Modifier.clickable { expanded = !expanded }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(description, color = Color.Gray, fontSize = 11.sp, modifier = Modifier.padding(top = 2.dp))
            }
            Icon(
                imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null,
                tint = color
            )
        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                tasks.forEachIndexed { index, task ->
                    Row(
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(top = 2.dp)
                                .size(14.dp)
                                .background(color, CircleShape)
                                .border(2.dp, Color.White.copy(0.8f), CircleShape)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(task, color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp, lineHeight = 16.sp)
                    }
                }
            }
        }
    }
}

// SUB-TAB 4: INTERVIEW PREP
@Composable
fun RoleInterviewPrepTab(role: CareerRoleDetails) {
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Technical", "HR", "Scenario")

    val filteredQs = remember(selectedCategory, role.interviewPrepQuestions) {
        role.interviewPrepQuestions.filter {
            selectedCategory == "All" || it.category.equals(selectedCategory, ignoreCase = true)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${role.title} Interview Bank",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Study core role-specific technical answers and situational scenario solutions prepared locally.",
                color = Color.Gray,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Chip category Row
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { cat ->
                    val isSelected = selectedCategory == cat
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) NeonBlue else SurfaceDark)
                            .border(1.dp, if (isSelected) Color.Transparent else Color.White.copy(0.1f), RoundedCornerShape(12.dp))
                            .clickable { selectedCategory = cat }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = cat,
                            color = if (isSelected) Color.Black else Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        if (filteredQs.isEmpty()) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No interview questions found.", color = Color.Gray)
                }
            }
        } else {
            items(filteredQs) { q ->
                var expanded by remember { mutableStateOf(false) }
                PremiumGlassCard(
                    modifier = Modifier.clickable { expanded = !expanded }
                ) {
                    Row(
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(NeonBlue.copy(0.12f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(q.category, color = NeonBlue, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                            Text(
                                text = q.question,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(top = 6.dp)
                            )
                        }
                        Icon(
                            imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }

                    AnimatedVisibility(
                        visible = expanded,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.Black.copy(0.3f))
                                .padding(12.dp)
                        ) {
                            Text(q.answer, color = Color.White.copy(0.9f), fontSize = 13.sp, lineHeight = 18.sp)
                        }
                    }
                }
            }
        }
        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}

// SUB-TAB 5: CODING PRACTICE
@Composable
fun RoleCodingPracticeTab(
    role: CareerRoleDetails,
    viewModel: SkillViewModel,
    solvedCoding: Set<String>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${role.title} Coding Practice Sandbox",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Practice specific code patterns and DSA milestones to build complete algorithmic execution parameters.",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }

        items(role.codingPractice) { p ->
            val isSolved = solvedCoding.contains(p.id)
            var expanded by remember { mutableStateOf(false) }

            PremiumGlassCard(
                modifier = Modifier.clickable { expanded = !expanded }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = { viewModel.toggleCodingSolved(p.id) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = if (isSolved) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = null,
                            tint = if (isSolved) SuccessGreen else Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(NeonPurple.copy(0.12f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(p.difficulty, color = NeonPurple, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Text(p.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.padding(top = 4.dp))
                    }
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }

                AnimatedVisibility(
                    visible = expanded,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column(modifier = Modifier.padding(top = 12.dp)) {
                        Text("Problem Description:", color = NeonBlue, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text(p.problem, color = Color.White.copy(0.85f), fontSize = 12.sp, modifier = Modifier.padding(top = 2.dp))

                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Recommended Solutions Code:", color = SuccessGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Black)
                                .padding(12.dp)
                        ) {
                            Text(
                                text = p.codeAnswer,
                                color = SuccessGreen,
                                fontSize = 11.sp,
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }
        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}

// SUB-TAB 6: WEAKNESS AUDIT (ATS Keyword Gaps based on Resumes)
@Composable
fun RoleWeaknessAuditTab(
    role: CareerRoleDetails,
    missingSkills: List<String>,
    matchingSkills: List<String>
) {
    val roleTechLower = remember(role) { role.technologies.map { it.lowercase() }.toSet() }

    val userMatching = remember(matchingSkills, roleTechLower) {
        matchingSkills.filter { it.lowercase() in roleTechLower || roleTechLower.any { rt -> rt.contains(it.lowercase()) } }
    }

    val userMissing = remember(role.technologies, userMatching) {
        val matchedLower = userMatching.map { it.lowercase() }.toSet()
        role.technologies.filter { it.lowercase() !in matchedLower && !matchedLower.any { m -> m.contains(it.lowercase()) || it.lowercase().contains(m) } }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${role.title} Weakness Audit",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "We parsed your resume and cross-referenced it with the required stack of the role to audit keyword gaps and weak technologies.",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }

        item {
            PremiumGlassCard {
                Text("Role ATS Keyword Audit", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Matching Keywords (${userMatching.size})", color = SuccessGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        if (userMatching.isEmpty()) {
                            Text("No keywords matched yet", color = Color.Gray, fontSize = 11.sp)
                        } else {
                            userMatching.forEach { skill ->
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(skill, color = Color.White.copy(0.9f), fontSize = 12.sp)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text("Missing ATS Gaps (${userMissing.size})", color = HotPink, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        if (userMissing.isEmpty()) {
                            Text("All keywords matched!", color = SuccessGreen, fontSize = 11.sp)
                        } else {
                            userMissing.forEach { skill ->
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                                    Icon(Icons.Default.Clear, contentDescription = null, tint = HotPink, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(skill, color = Color.White.copy(0.9f), fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        if (userMissing.isNotEmpty()) {
            item {
                GlassCard {
                    Text("Targeted Action Plans", color = WarningYellow, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "To clear your missing keyword gaps, we suggest integrating these technologies into your personal project schemas. For example, implement a custom utility utilizing ${userMissing.take(2).joinToString(" & ")} and upload it to GitHub, then compile the code and list the respective libraries explicitly on your resume.",
                        color = Color.White.copy(0.85f),
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }
        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}


// --- 1. COMMON DRAWING AND DESIGN COMPONENTS ---

data class LocalMessage(
    val text: String,
    val isUser: Boolean
)

@Composable
fun ChatBubble(message: LocalMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (message.isUser) 16.dp else 2.dp,
                        bottomEnd = if (message.isUser) 2.dp else 16.dp
                    )
                )
                .background(if (message.isUser) NeonPurple else SurfaceDark)
                .border(
                    0.5.dp,
                    if (message.isUser) Color.Transparent else Color.White.copy(0.12f),
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (message.isUser) 16.dp else 2.dp,
                        bottomEnd = if (message.isUser) 2.dp else 16.dp
                    )
                )
                .padding(12.dp)
        ) {
            Text(
                text = message.text,
                color = if (message.isUser) Color.Black else Color.White,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun SnapshotRow(title: String, description: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White.copy(0.06f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = NeonBlue,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                color = Color.White,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun GlobalAnalyticsCard(solvedAptitudeCount: Int, solvedCodingCount: Int, confidence: Int) {
    GlassCard {
        Text("Your Preparation Insights", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(0.04f))
                    .border(0.5.dp, Color.White.copy(0.08f), RoundedCornerShape(12.dp))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Quiz,
                        contentDescription = null,
                        tint = NeonBlue,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Aptitude Solved", color = Color.White.copy(0.6f), fontSize = 10.sp, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("$solvedAptitudeCount Qs", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(0.04f))
                    .border(0.5.dp, Color.White.copy(0.08f), RoundedCornerShape(12.dp))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Code,
                        contentDescription = null,
                        tint = NeonPurple,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Coding Solved", color = Color.White.copy(0.6f), fontSize = 10.sp, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("$solvedCodingCount Qs", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(0.04f))
                    .border(0.5.dp, Color.White.copy(0.08f), RoundedCornerShape(12.dp))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.EmojiEmotions,
                        contentDescription = null,
                        tint = SuccessGreen,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Prep Confidence", color = Color.White.copy(0.6f), fontSize = 10.sp, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("$confidence%", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}

@Composable
fun CompanySelectionCard(
    company: CompanyInfo,
    readiness: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(0.03f))
            .border(
                0.5.dp,
                Brush.linearGradient(
                    listOf(Color.White.copy(0.12f), Color.White.copy(0.02f))
                ),
                RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val logoColor = when(company.name.lowercase()) {
                "google" -> Color(0xFFEA4335)
                "microsoft" -> Color(0xFF00A4EF)
                "amazon" -> Color(0xFFFF9900)
                "tcs" -> Color(0xFF1B365D)
                "infosys" -> Color(0xFF007CC3)
                "zoho" -> Color(0xFFE51A24)
                "adobe" -> Color(0xFFFF0000)
                "paypal" -> Color(0xFF003087)
                "oracle" -> Color(0xFFF80000)
                "deloitte" -> Color(0xFF86BC25)
                else -> NeonPurple
            }
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(logoColor.copy(alpha = 0.15f))
                    .border(1.dp, logoColor.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = company.logoText,
                    color = logoColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = company.name,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = company.salaryRange.split("(").first().trim(),
                        color = SuccessGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = company.type,
                        color = Color.White.copy(0.6f),
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(modifier = Modifier.size(3.dp).clip(CircleShape).background(Color.White.copy(0.4f)))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = company.headquarters.split(",").last().trim(),
                        color = Color.White.copy(0.6f),
                        fontSize = 11.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Readiness: ",
                        color = Color.White.copy(0.5f),
                        fontSize = 10.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(0.08f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(readiness / 100f)
                                .clip(CircleShape)
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(NeonBlue, NeonPurple)
                                    )
                                )
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$readiness%",
                        color = NeonPurple,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun CompanyPrepDashboardPhase(
    companyInfo: CompanyInfo,
    viewModel: SkillViewModel,
    readinessScore: Int,
    solvedAptitude: Set<String>,
    solvedCoding: Set<String>,
    interviewConfidence: Int,
    userMatchingSkills: List<String>
) {
    var activeSubTab by remember { mutableStateOf("Dashboard") } // Dashboard, Timeline, Question Bank, Assistant
    val tabs = listOf("Dashboard", "Timeline", "Question Bank", "Assistant")

    var selectedQCategory by remember { mutableStateOf("Aptitude") }
    val filteredQs = remember(selectedQCategory, companyInfo.questions) {
        companyInfo.questions.filter { it.category.equals(selectedQCategory, ignoreCase = true) }
    }

    var botMessageText by remember { mutableStateOf("") }
    val localMessages = remember {
        mutableStateListOf(
            LocalMessage(
                text = "Hello! I am your local ${companyInfo.name} Recruiting Assistant. Ask me anything about our hiring processes, coding questions, required technology setups, or company culture!",
                isUser = false
            )
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // TOP BACK NAV HEADER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.selectCompany(null) },
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(0.08f))
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = companyInfo.name,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${companyInfo.type} • ${companyInfo.headquarters.split(',').last().trim()}",
                    color = Color.White.copy(0.6f),
                    fontSize = 12.sp
                )
            }
        }

        // SEGMENTED TAB SELECTOR
        ScrollableTabRow(
            selectedTabIndex = tabs.indexOf(activeSubTab),
            containerColor = Color.Transparent,
            contentColor = NeonBlue,
            edgePadding = 16.dp,
            divider = {},
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[tabs.indexOf(activeSubTab)]),
                    color = NeonBlue
                )
            }
        ) {
            tabs.forEach { tab ->
                Tab(
                    selected = activeSubTab == tab,
                    onClick = { activeSubTab = tab },
                    text = {
                        Text(
                            text = tab,
                            fontSize = 13.sp,
                            fontWeight = if (activeSubTab == tab) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // TAB VIEWS
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (activeSubTab) {
                "Dashboard" -> {
                    item {
                        // READINESS SCORE CARD
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(24.dp))
                                .background(Color.White.copy(0.03f))
                                .border(
                                    0.5.dp,
                                    Color.White.copy(0.12f),
                                    RoundedCornerShape(24.dp)
                                )
                                .padding(24.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Your Placement Readiness",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Calculated using skills check, custom solved questions, and mock interview grades.",
                                        color = Color.White.copy(0.6f),
                                        fontSize = 12.sp,
                                        lineHeight = 18.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Box(
                                    modifier = Modifier.size(80.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                                        drawCircle(
                                            color = Color.White.copy(0.08f),
                                            style = Stroke(width = 8.dp.toPx())
                                        )
                                        drawArc(
                                            color = NeonPurple,
                                            startAngle = -90f,
                                            sweepAngle = (readinessScore / 100f) * 360f,
                                            useCenter = false,
                                            style = Stroke(
                                                width = 8.dp.toPx(),
                                                cap = androidx.compose.ui.graphics.StrokeCap.Round
                                            )
                                        )
                                    }
                                    Text(
                                        text = "$readinessScore%",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Company Profile Details",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    // COMPANY QUICK FACTS GRID
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color.White.copy(0.03f))
                                        .border(0.5.dp, Color.White.copy(0.08f), RoundedCornerShape(16.dp))
                                        .padding(12.dp)
                                ) {
                                    Column {
                                        Icon(Icons.Default.Place, null, tint = NeonBlue, modifier = Modifier.size(20.dp))
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("Headquarters", color = Color.White.copy(0.5f), fontSize = 10.sp)
                                        Text(companyInfo.headquarters, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp, maxLines = 1)
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color.White.copy(0.03f))
                                        .border(0.5.dp, Color.White.copy(0.08f), RoundedCornerShape(16.dp))
                                        .padding(12.dp)
                                ) {
                                    Column {
                                        Icon(Icons.Default.Business, null, tint = NeonPurple, modifier = Modifier.size(20.dp))
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("Entity Type", color = Color.White.copy(0.5f), fontSize = 10.sp)
                                        Text(companyInfo.type, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp, maxLines = 1)
                                    }
                                }
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color.White.copy(0.03f))
                                        .border(0.5.dp, Color.White.copy(0.08f), RoundedCornerShape(16.dp))
                                        .padding(12.dp)
                                ) {
                                    Column {
                                        Icon(Icons.Default.People, null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("Employees", color = Color.White.copy(0.5f), fontSize = 10.sp)
                                        Text(companyInfo.employeeCount, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color.White.copy(0.03f))
                                        .border(0.5.dp, Color.White.copy(0.08f), RoundedCornerShape(16.dp))
                                        .padding(12.dp)
                                ) {
                                    Column {
                                        Icon(Icons.Default.AssignmentInd, null, tint = WarningYellow, modifier = Modifier.size(20.dp))
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("Eligibility", color = Color.White.copy(0.5f), fontSize = 10.sp)
                                        Text(companyInfo.eligibilityCriteria.split(",").first().trim(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp, maxLines = 1)
                                    }
                                }
                            }
                        }
                    }

                    item {
                        GlassCard {
                            Text("Work Culture Overview", color = HotPink, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(companyInfo.workCulture, color = Color.White.copy(0.8f), fontSize = 12.sp, lineHeight = 18.sp)
                        }
                    }

                    item {
                        GlassCard {
                            Text("Key Technologies Demanded", color = NeonBlue, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(companyInfo.requiredTech) { tech ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color.White.copy(0.06f))
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(tech, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                "Timeline" -> {
                    item {
                        Text(
                            text = "Placement Selection Sequence",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    items(companyInfo.hiringRounds.size) { index ->
                        val round = companyInfo.hiringRounds[index]
                        Row(modifier = Modifier.fillMaxWidth()) {
                            // Steps connecting line
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(top = 4.dp, end = 16.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(if (index == 0) NeonBlue else Color.White.copy(0.08f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${index + 1}",
                                        color = if (index == 0) DeepDarkBlue else Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                                if (index < companyInfo.hiringRounds.size - 1) {
                                    Box(
                                        modifier = Modifier
                                            .width(2.dp)
                                            .height(140.dp)
                                            .background(Color.White.copy(0.08f))
                                    )
                                }
                            }

                            // Round card content
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color.White.copy(0.03f))
                                    .border(0.5.dp, Color.White.copy(0.08f), RoundedCornerShape(16.dp))
                                    .padding(16.dp)
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = round.roundName.split(":").last().trim(),
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            modifier = Modifier.weight(1f)
                                        )
                                        val diffColor = when (round.difficulty.lowercase()) {
                                            "easy" -> SuccessGreen
                                            "medium" -> WarningYellow
                                            else -> HotPink
                                        }
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(diffColor.copy(0.15f))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(round.difficulty, color = diffColor, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Duration: ${round.duration}", color = Color.White.copy(0.5f), fontSize = 11.sp)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Commonly Asked Topics:", color = Color.White.copy(0.7f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        items(round.commonTopics) { topic ->
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(Color.White.copy(0.05f))
                                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                            ) {
                                                Text(topic, color = Color.White.copy(0.9f), fontSize = 10.sp)
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Preparation Tips:", color = Color.White.copy(0.7f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Text(round.prepTips, color = Color.White.copy(0.8f), fontSize = 11.sp, lineHeight = 16.sp)
                                }
                            }
                        }
                    }
                    item { Spacer(modifier = Modifier.height(24.dp)) }
                }

                "Question Bank" -> {
                    val qCategories = listOf("Aptitude", "DSA", "System Design", "Java", "HR")

                    item {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(qCategories) { category ->
                                val active = selectedQCategory == category
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (active) NeonBlue else Color.White.copy(0.06f))
                                        .clickable { selectedQCategory = category }
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = category,
                                        color = if (active) DeepDarkBlue else Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    if (filteredQs.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(48.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No recruiter questions available for this category.", color = Color.Gray, fontSize = 12.sp)
                            }
                        }
                    } else {
                        items(filteredQs.size) { qIndex ->
                            val question = filteredQs[qIndex]
                            var expanded by remember { mutableStateOf(false) }
                            val isSolved = if (selectedQCategory == "Aptitude") solvedAptitude.contains(question.id) else solvedCoding.contains(question.id)

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color.White.copy(0.03f))
                                    .border(
                                        0.5.dp,
                                        if (isSolved) SuccessGreen.copy(0.3f) else Color.White.copy(0.08f),
                                        RoundedCornerShape(16.dp)
                                    )
                                    .padding(16.dp)
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = question.question,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "Difficulty: ${question.difficulty}",
                                                color = Color.White.copy(0.5f),
                                                fontSize = 10.sp
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Checkbox(
                                            checked = isSolved,
                                            onCheckedChange = {
                                                if (selectedQCategory == "Aptitude") {
                                                    viewModel.toggleAptitudeSolved(question.id)
                                                } else {
                                                    viewModel.toggleCodingSolved(question.id)
                                                }
                                            },
                                            colors = CheckboxDefaults.colors(
                                                checkedColor = SuccessGreen,
                                                uncheckedColor = Color.White.copy(0.3f)
                                            )
                                        )
                                    }

                                    if (question.codeSnippet != null) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color.Black.copy(0.6f))
                                                .padding(8.dp)
                                        ) {
                                            Text(
                                                text = question.codeSnippet,
                                                color = Color(0xFFA9B7C6),
                                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                                fontSize = 10.sp
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { expanded = !expanded },
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = if (expanded) "Hide Solution" else "Reveal Recruiter Solution",
                                            color = NeonBlue,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Icon(
                                            imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                            contentDescription = null,
                                            tint = NeonBlue,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }

                                    if (expanded) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color.White.copy(0.04f))
                                                .padding(12.dp)
                                        ) {
                                            Text(
                                                text = question.answer,
                                                color = Color.White.copy(0.9f),
                                                fontSize = 11.sp,
                                                lineHeight = 16.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    item { Spacer(modifier = Modifier.height(24.dp)) }
                }

                "Assistant" -> {
                    item {
                        Text(
                            text = "Localized Recruiting Assistant",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(380.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White.copy(0.02f))
                                .border(0.5.dp, Color.White.copy(0.08f), RoundedCornerShape(16.dp))
                                .padding(12.dp)
                        ) {
                            // Message scroll history
                            LazyColumn(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(localMessages.size) { msgIdx ->
                                    val msg = localMessages[msgIdx]
                                    ChatBubble(msg)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Text entry field
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White.copy(0.04f))
                                    .padding(horizontal = 12.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                androidx.compose.foundation.text.BasicTextField(
                                    value = botMessageText,
                                    onValueChange = { botMessageText = it },
                                    modifier = Modifier.weight(1f),
                                    textStyle = androidx.compose.ui.text.TextStyle(color = Color.White, fontSize = 12.sp),
                                    cursorBrush = androidx.compose.ui.graphics.SolidColor(Color.White),
                                    decorationBox = { innerTextField ->
                                        if (botMessageText.isEmpty()) {
                                            Text("Ask ${companyInfo.name} bot...", color = Color.White.copy(0.3f), fontSize = 12.sp)
                                        }
                                        innerTextField()
                                    }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                IconButton(
                                    onClick = {
                                        if (botMessageText.isNotBlank()) {
                                            val queryText = botMessageText
                                            localMessages.add(LocalMessage(text = queryText, isUser = true))
                                            botMessageText = ""
                                            
                                            // Dynamic company bot response generator
                                            val response = when {
                                                queryText.lowercase().contains("round") || queryText.lowercase().contains("process") || queryText.lowercase().contains("hiring") -> {
                                                    "At ${companyInfo.name}, our placement process typically consists of 4 rounds: Online Technical/Aptitude Assessment, 2 Core Coding/DSA Rounds, and a final Googleyness/Managerial culture fit round. Check out the Timeline tab on this dashboard for detailed breakdowns!"
                                                }
                                                queryText.lowercase().contains("question") || queryText.lowercase().contains("dsa") || queryText.lowercase().contains("coding") -> {
                                                    "I have loaded recruiter-verified interview questions for ${companyInfo.name} inside your Question Bank workspace! You can toggle between Aptitude, DSA, System Design, and HR questions, expand to view ideal solutions, and check them off once solved to increase your Readiness score."
                                                }
                                                queryText.lowercase().contains("culture") || queryText.lowercase().contains("work") || queryText.lowercase().contains("life") -> {
                                                    "${companyInfo.workCulture}"
                                                }
                                                queryText.lowercase().contains("salary") || queryText.lowercase().contains("package") || queryText.lowercase().contains("pay") -> {
                                                    "Typical packages at ${companyInfo.name} range around ${companyInfo.salaryRange}. Solve our DSA coding questions in the Question Bank to boost your compatibility score!"
                                                }
                                                else -> {
                                                    "Hello! I am your local ${companyInfo.name} recruiting assistant. I can guide you through our specific hiring processes, commonly asked coding questions, required technologies, or help you understand our engineering culture. What aspect of preparing for ${companyInfo.name} would you like to discuss?"
                                                }
                                            }
                                            localMessages.add(LocalMessage(text = response, isUser = false))
                                        }
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Send,
                                        contentDescription = "Send",
                                        tint = NeonBlue,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                    item { Spacer(modifier = Modifier.height(24.dp)) }
                }
            }
        }
    }
}

private fun drawBezierConnection(
    drawScope: androidx.compose.ui.graphics.drawscope.DrawScope,
    start: Offset,
    end: Offset,
    brush: Brush
) {
    val controlX = (start.x + end.x) / 2
    val controlY = start.y + (end.y - start.y) * 0.15f

    val path = Path().apply {
        moveTo(start.x, start.y)
        quadraticBezierTo(controlX, controlY, end.x, end.y)
    }

    drawScope.drawPath(
        path = path,
        brush = brush,
        style = Stroke(
            width = 3f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        )
    )
}


// --- 2. LOCAL STATIC REPOSITORY FOR 7 CAREER ROLES ---

object LocalCareerRoleData {
    val roles = listOf(
        CareerRoleDetails(
            title = "Software Developer",
            overview = "Design, compile, test, and maintain enterprise software structures. Focuses heavily on algorithmic logic, database schemas, and structured components integration.",
            dailyResponsibilities = "Write clean syntax logic in languages like Java/C++, code backend APIs, review database indexing, build features components, and collaborate in Agile meetings.",
            salaryRange = "₹6 LPA - ₹25+ LPA",
            hiringTrends = "Steady high-volume hiring. Increased emphasis on clean OOPs designs and robust DSA problem solving capabilities.",
            futureScope = "Extremely high. Progression pathways lead to Senior Developer, Tech Lead, and Principal Software Architect roles.",
            requiredSkills = listOf("Java/C++", "SQL", "Data Structures", "OOPs principles", "Git"),
            technologies = listOf("Java", "C++", "MySQL", "Git", "DSA", "Design Patterns"),
            dependencyMapSkills = listOf("Syntax Basics", "Data Structures", "OOPS", "SQL Queries", "Design Patterns"),
            beginnerRoadmap = LearningStage(
                title = "Beginner Core Logic",
                timeline = "Weeks 1 - 3",
                topics = listOf("Control flows", "Array operations", "Standard String methods"),
                projectIdea = "Build a dynamic offline Contacts List CLI in Java with sorting and search capabilities.",
                certifications = listOf("Oracle Java Foundations Certification"),
                recommendedTools = listOf("IntelliJ IDEA Community", "Git CLI")
            ),
            intermediateRoadmap = LearningStage(
                title = "Intermediate DSA & SQL",
                timeline = "Weeks 4 - 8",
                topics = listOf("Recursion", "Binary Trees", "Sorting Algorithms", "SQL Joints & Subqueries"),
                projectIdea = "Create an offline Library Records Database utility with primary/foreign key connections.",
                certifications = listOf("HackerRank Problem Solving Verified"),
                recommendedTools = listOf("PostgreSQL", "DBeaver")
            ),
            advancedRoadmap = LearningStage(
                title = "Advanced Patterns & Architecture",
                timeline = "Weeks 9 - 12",
                topics = listOf("SOLID Principles", "Singleton & Factory patterns", "Multi-threading loops"),
                projectIdea = "Build an offline Multi-threaded Ticket Booking Simulator handling race conditions with synchronization.",
                certifications = listOf("Certified Software Design Professional"),
                recommendedTools = listOf("VisualVM profiling tool")
            ),
            interviewPrepQuestions = listOf(
                RolePrepQuestion("Why is method overloading called compile-time polymorphism?", "Because the compiler binds the method call to its exact signature at compile time based on parameters count and types, using overload resolution rules.", "Technical"),
                RolePrepQuestion("What is the difference between primary keys and unique keys in SQL?", "A primary key uniquely identifies a row and CANNOT contain null values. A unique key also prevents duplicates but allows one null value.", "Technical"),
                RolePrepQuestion("How do you handle a scenario where team members disagree on software architecture?", "I organize a technical review session. We list pros and cons, measure latency/complexity metrics, refer to SOLID principles, and align with the most scalable, maintainable solution.", "Scenario")
            ),
            codingPractice = listOf(
                RoleCodingPractice(
                    id = "SD-C1",
                    title = "Reverse a String in Java",
                    problem = "Write an optimized, in-place method to reverse a character array.",
                    codeAnswer = "public void reverse(char[] s) {\n    int l = 0, r = s.length - 1;\n    while(l < r) {\n        char temp = s[l];\n        s[l] = s[r];\n        s[r] = temp;\n        l++; r--;\n    }\n}",
                    difficulty = "Easy"
                ),
                RoleCodingPractice(
                    id = "SD-C2",
                    title = "Two Sum (Target Array Indices)",
                    problem = "Given an array of integers, return indices of the two numbers such that they add up to a specific target.",
                    codeAnswer = "public int[] twoSum(int[] nums, int target) {\n    Map<Integer, Integer> map = new HashMap<>();\n    for(int i = 0; i < nums.length; i++) {\n        int complement = target - nums[i];\n        if(map.containsKey(complement)) {\n            return new int[]{map.get(complement), i};\n        }\n        map.put(nums[i], i);\n    }\n    return new int[0];\n}",
                    difficulty = "Medium"
                )
            )
        ),
        CareerRoleDetails(
            title = "Java Full Stack Developer",
            overview = "Engineers capable of building complete web platforms, mastering Java backend microservices (Spring Boot) alongside responsive browser frontends (React/HTML/CSS).",
            dailyResponsibilities = "Design React user interfaces, configure state maps, deploy Spring Boot API endpoints, set JPA connections, and map REST communication secure layers.",
            salaryRange = "₹7 LPA - ₹28+ LPA",
            hiringTrends = "SaaS startups and global product hubs continuously search for developers who can orchestrate full deployment modules independently.",
            futureScope = "High flexibility. Leads to Full Stack Architect, Engineering Lead, and SaaS CTO pathways.",
            requiredSkills = listOf("Java Core", "Spring Boot", "React.js", "HTML & CSS", "REST APIs", "JPA/Hibernate"),
            technologies = listOf("Java", "Spring Boot", "React", "JavaScript", "HTML/CSS", "PostgreSQL"),
            dependencyMapSkills = listOf("Java & OOPs", "JPA & Databases", "Spring Boot", "React Components", "REST Integrations"),
            beginnerRoadmap = LearningStage(
                title = "Backend Basics & APIs",
                timeline = "Weeks 1 - 4",
                topics = listOf("Java Streams API", "Exception hierarchies", "REST verbs (GET, POST, PUT, DELETE)"),
                projectIdea = "Build a local REST API using Spring Boot to manage Student lists with fully mapped models.",
                certifications = listOf("Spring Certified Professional"),
                recommendedTools = listOf("Spring Tool Suite", "Postman")
            ),
            intermediateRoadmap = LearningStage(
                title = "Frontend & State Management",
                timeline = "Weeks 5 - 8",
                topics = listOf("HTML5 elements", "Flexbox layouts", "React state hooks", "JavaScript ES6 loops"),
                projectIdea = "Create a React-based Placement Tracking UI, integrating it with a mocked API client.",
                certifications = listOf("Meta Frontend Developer Cert"),
                recommendedTools = listOf("VS Code", "Chrome DevTools")
            ),
            advancedRoadmap = LearningStage(
                title = "Enterprise Integration & Security",
                timeline = "Weeks 9 - 12",
                topics = listOf("Spring Security JWT integration", "JPA One-To-Many relations", "CORS policy resolution"),
                projectIdea = "Construct a secure E-Commerce inventory dashboard supporting role-based logins and token validation.",
                certifications = listOf("AWS Certified Developer Associate"),
                recommendedTools = listOf("Docker Desktop", "Redis")
            ),
            interviewPrepQuestions = listOf(
                RolePrepQuestion("What is dependency injection in Spring Boot?", "It is a design pattern that removes hardcoded dependencies from classes, allowing the Spring IoC container to inject instances (Beans) at runtime via constructor or setter injection.", "Technical"),
                RolePrepQuestion("What are React custom hooks?", "They are JavaScript functions that start with 'use' and allow you to extract component logic into reusable functions, sharing stateful actions without duplicate rendering layouts.", "Technical"),
                RolePrepQuestion("Explain how you handle CORS errors.", "I configure `@CrossOrigin` annotations on Spring controllers or write a WebMvcConfigurer bean to explicitly allow the origins, headers, and methods used by the frontend.", "Technical")
            ),
            codingPractice = listOf(
                RoleCodingPractice(
                    id = "JFS-C1",
                    title = "Spring JPA Basic Entity Mapping",
                    problem = "Define a basic Kotlin/Java JPA entity for a 'User' table with an auto-incremented ID.",
                    codeAnswer = "@Entity\n@Table(name = \"users\")\npublic class User {\n    @Id\n    @GeneratedValue(strategy = GenerationType.IDENTITY)\n    private Long id;\n    private String username;\n    // Getters and Setters...\n}",
                    difficulty = "Easy"
                ),
                RoleCodingPractice(
                    id = "JFS-C2",
                    title = "Check Palindrome String",
                    problem = "Return true if a string is a palindrome, ignoring non-alphanumeric characters.",
                    codeAnswer = "public boolean isPalindrome(String s) {\n    String clean = s.replaceAll(\"[^a-zA-Z0-9]\", \"\").toLowerCase();\n    int l = 0, r = clean.length() - 1;\n    while(l < r) {\n        if(clean.charAt(l) != clean.charAt(r)) return false;\n        l++; r--;\n    }\n    return true;\n}",
                    difficulty = "Easy"
                )
            )
        ),
        CareerRoleDetails(
            title = "Data Analyst",
            overview = "Transform raw data points into actionable corporate business insights. Specializes in statistical modeling, database SQL joints, and interactive reporting charts.",
            dailyResponsibilities = "Write analytical database SQL queries, compile Excel dashboards, build Tableau charts, trace KPIs metrics, and draft recommendations reports.",
            salaryRange = "₹5 LPA - ₹18+ LPA",
            hiringTrends = "Consulting giants (Deloitte, PwC) and marketing giants constantly hire analysts to structure growth indices.",
            futureScope = "Leads directly to Data Scientist, Analytics Manager, and Business Intelligence Architect roles.",
            requiredSkills = listOf("Advanced SQL", "Excel formulas", "Tableau/Power BI", "Statistics", "Python (Pandas)"),
            technologies = listOf("SQL", "Excel", "Tableau", "Python", "Power BI", "Pandas"),
            dependencyMapSkills = listOf("Excel formulas", "SQL joins", "Tableau Charts", "Pandas analysis", "KPI metrics"),
            beginnerRoadmap = LearningStage(
                title = "Data Foundations & Excel",
                timeline = "Weeks 1 - 3",
                topics = listOf("Pivot tables", "VLOOKUP & INDEX-MATCH", "Descriptive statistics"),
                projectIdea = "Compile a Sales performance spreadsheet analyzing region-wise profits and dynamic charts.",
                certifications = listOf("Microsoft Office Specialist: Excel Expert"),
                recommendedTools = listOf("MS Excel", "Google Sheets")
            ),
            intermediateRoadmap = LearningStage(
                title = "Advanced SQL & BI",
                timeline = "Weeks 4 - 8",
                topics = listOf("SQL Group By & HAVING", "Window Functions", "Dashboard layouts in Tableau"),
                projectIdea = "Design a customer churn visualization dashboard in Power BI connected to a mock database.",
                certifications = listOf("Google Data Analytics Professional Certificate"),
                recommendedTools = listOf("Tableau Desktop Public", "MySQL WorkBench")
            ),
            advancedRoadmap = LearningStage(
                title = "Python Data Operations",
                timeline = "Weeks 9 - 12",
                topics = listOf("Pandas dataframes manipulation", "Matplotlib graphics", "Jupyter Notebook analysis"),
                projectIdea = "Write a Python script analyzing 10,000 retail records to plot seasonal sales trends.",
                certifications = listOf("IBM Data Analyst Specialist"),
                recommendedTools = listOf("Anaconda", "Jupyter Notebook")
            ),
            interviewPrepQuestions = listOf(
                RolePrepQuestion("What is the difference between WHERE and HAVING in SQL?", "WHERE filters rows *before* they are grouped; HAVING filters grouped summaries *after* the GROUP BY clause is executed.", "Technical"),
                RolePrepQuestion("Explain VLOOKUP vs INDEX-MATCH in Excel.", "VLOOKUP can only search from left to right and breaks if columns are inserted. INDEX-MATCH can search in any direction and is highly performant.", "Technical"),
                RolePrepQuestion("How do you present critical data anomalies to non-technical managers?", "I use simple visualizations (like bar charts or line trends) highlighting the anomaly in distinct colors, translate technical stats into business KPIs, and present clear action suggestions.", "Scenario")
            ),
            codingPractice = listOf(
                RoleCodingPractice(
                    id = "DA-C1",
                    title = "SQL Second Highest Salary",
                    problem = "Write an SQL query to retrieve the second highest salary from an Employee table.",
                    codeAnswer = "SELECT MAX(Salary) FROM Employee \nWHERE Salary < (SELECT MAX(Salary) FROM Employee);",
                    difficulty = "Easy"
                ),
                RoleCodingPractice(
                    id = "DA-C2",
                    title = "SQL Rank Window Function",
                    problem = "Retrieve employees ranked by their salary inside each department.",
                    codeAnswer = "SELECT id, name, dept, salary,\nDENSE_RANK() OVER (PARTITION BY dept ORDER BY salary DESC) as rank\nFROM Employee;",
                    difficulty = "Medium"
                )
            )
        ),
        CareerRoleDetails(
            title = "AI Engineer",
            overview = "Develop, evaluate, and scale machine learning and artificial intelligence systems. Focuses on neural networks, model training, NLP, and offline LLM interfaces.",
            dailyResponsibilities = "Preprocess high-volume datasets, optimize model parameters, write neural networks layers, audit ML metrics, and package models as local API integrations.",
            salaryRange = "₹12 LPA - ₹45+ LPA",
            hiringTrends = "Exponential global demand. Startups and tech leaders are hiring aggressively to implement offline LLMs and AI processors.",
            futureScope = "Unprecedented growth. Opens paths to ML Scientist, AI Architect, and Chief AI Officer.",
            requiredSkills = listOf("Python Core", "Machine Learning", "PyTorch/TensorFlow", "NLP", "Mathematics (Linear Algebra)"),
            technologies = listOf("Python", "PyTorch", "NumPy", "Pandas", "Scikit-Learn", "HuggingFace"),
            dependencyMapSkills = listOf("Python & NumPy", "Scikit-Learn", "Deep Learning", "NLP loops", "Model Scaling"),
            beginnerRoadmap = LearningStage(
                title = "Python Math & ML Foundations",
                timeline = "Weeks 1 - 4",
                topics = listOf("NumPy vector calculations", "Linear regression models", "Gradient descent mechanics"),
                projectIdea = "Create an offline housing price prediction script using simple Linear Regression and Pandas.",
                certifications = listOf("Andrew Ng Machine Learning Specialization"),
                recommendedTools = listOf("PyCharm Professional", "Jupyter")
            ),
            intermediateRoadmap = LearningStage(
                title = "Deep Learning & NLP",
                timeline = "Weeks 5 - 8",
                topics = listOf("Neural network layers", "PyTorch model pipelines", "Tokenization & embeddings"),
                projectIdea = "Build a local text classifier using PyTorch predicting the sentiment of product reviews.",
                certifications = listOf("DeepLearning.AI Deep Learning Cert"),
                recommendedTools = listOf("Google Colab", "TensorBoard")
            ),
            advancedRoadmap = LearningStage(
                title = "Model Deployment & LLMs",
                timeline = "Weeks 9 - 12",
                topics = listOf("HuggingFace pipeline optimization", "Quantization", "Model weights serving local API"),
                projectIdea = "Serve a quantized local LLM through a lightweight FastAPI wrapper for processing text offline.",
                certifications = listOf("TensorFlow Developer Certificate"),
                recommendedTools = listOf("FastAPI", "ONNX runtime")
            ),
            interviewPrepQuestions = listOf(
                RolePrepQuestion("What is overfitting in Machine Learning? How do you prevent it?", "Overfitting is when a model learns training data noise too well, causing poor validation performance. Prevent via regularization (L1/L2), dropout layers, data augmentation, or early stopping.", "Technical"),
                RolePrepQuestion("Explain the difference between Sigmoid and ReLU activation functions.", "Sigmoid scales inputs between 0 and 1 (prone to vanishing gradient). ReLU returns max(0, x), which compiles faster and resolves gradient vanishing in deep models.", "Technical"),
                RolePrepQuestion("How do you handle missing values in a dataset?", "Depending on size: 1. Delete rows (if anomalies are low). 2. Impute with mean/median/mode (for numerical/categorical). 3. Predict values using an auxiliary regression model.", "Technical")
            ),
            codingPractice = listOf(
                RoleCodingPractice(
                    id = "AI-C1",
                    title = "NumPy Matrix Multiplication",
                    problem = "Multiply two matrices using NumPy and return the shape.",
                    codeAnswer = "import numpy as np\ndef multiply_matrices(a, b):\n    return np.dot(a, b)",
                    difficulty = "Easy"
                ),
                RoleCodingPractice(
                    id = "AI-C2",
                    title = "PyTorch Custom Dataset Class",
                    problem = "Write a basic custom PyTorch Dataset structure mapping labels and inputs.",
                    codeAnswer = "import torch\nfrom torch.utils.data import Dataset\nclass CustomData(Dataset):\n    def __init__(self, x, y):\n        self.x = torch.tensor(x, dtype=torch.float32)\n        self.y = torch.tensor(y, dtype=torch.float32)\n    def __len__(self):\n        return len(self.x)\n    def __getitem__(self, idx):\n        return self.x[idx], self.y[idx]",
                    difficulty = "Medium"
                )
            )
        ),
        CareerRoleDetails(
            title = "Cloud Engineer",
            overview = "Design and manage distributed cloud infrastructure topologies. Specializes in virtual networks, secure load balancing, and scaling enterprise systems.",
            dailyResponsibilities = "Configure VPC routing tables, setup load balancers parameters, audit IAM security controls, check cloud resource utilization, and automate deployments.",
            salaryRange = "₹8 LPA - ₹30+ LPA",
            hiringTrends = "Consistently high. Every enterprise is expanding cloud infrastructure, creating massive opportunities for certified cloud professionals.",
            futureScope = "Leads to Principal Cloud Architect, Director of Infrastructure, and SaaS Ops Lead.",
            requiredSkills = listOf("AWS/Azure Core", "Virtual Networks (VPC)", "Linux System Admin", "IAM & Security", "Kubernetes basics"),
            technologies = listOf("AWS", "Azure", "Linux", "Docker", "Nginx", "Kubernetes"),
            dependencyMapSkills = listOf("Linux Admin", "VPC Networking", "Docker Containers", "IAM Security", "Load Balancers"),
            beginnerRoadmap = LearningStage(
                title = "Linux & Basic Networks",
                timeline = "Weeks 1 - 3",
                topics = listOf("Linux bash command loops", "SSH key configurations", "IP routing and subnetting"),
                projectIdea = "Setup a local secure Linux server running Nginx hosting a basic website.",
                certifications = listOf("AWS Certified Cloud Practitioner"),
                recommendedTools = listOf("Ubuntu Server", "VirtualBox")
            ),
            intermediateRoadmap = LearningStage(
                title = "Virtual Private Cloud (VPC)",
                timeline = "Weeks 4 - 8",
                topics = listOf("Private vs Public subnets", "Security Groups vs NACLs", "EC2 scaling groups"),
                projectIdea = "Design an multi-tier VPC topology on AWS console supporting private app servers and NAT gateway routing.",
                certifications = listOf("AWS Certified Solutions Architect Associate"),
                recommendedTools = listOf("AWS Console", "Azure CLI")
            ),
            advancedRoadmap = LearningStage(
                title = "Containerization & Orchestration",
                timeline = "Weeks 9 - 12",
                topics = listOf("Docker file compiles", "Kubernetes pods and deployments setups", "IAM secure policies"),
                projectIdea = "Package a Spring Boot microservice into a Docker container and deploy it to local Kubernetes minikube.",
                certifications = listOf("Certified Kubernetes Administrator (CKA)"),
                recommendedTools = listOf("Minikube", "Docker Hub")
            ),
            interviewPrepQuestions = listOf(
                RolePrepQuestion("What is the difference between a Public Subnet and a Private Subnet?", "A public subnet has an explicit route to an Internet Gateway (making resources reachable from the web), whereas a private subnet does not and requires a NAT gateway to connect to outside services.", "Technical"),
                RolePrepQuestion("Explain Security Groups vs Network Access Control Lists (NACLs).", "Security Groups are stateful (automatically allow return traffic), attached to specific EC2 instances. NACLs are stateless (require explicit inbound and outbound rules), attached to entire subnets.", "Technical"),
                RolePrepQuestion("How would you handle a sudden peak in traffic on your app server?", "I configure an Auto Scaling Group with a Target Tracking Policy based on CPU utilization, connected to an Application Load Balancer that dynamically distributes request loads.", "Scenario")
            ),
            codingPractice = listOf(
                RoleCodingPractice(
                    id = "CL-C1",
                    title = "Docker File Compile Script",
                    problem = "Write a basic Dockerfile to package a simple JAR file under openjdk-17.",
                    codeAnswer = "FROM openjdk:17-jdk-slim\nCOPY target/app.jar app.jar\nEXPOSE 8080\nENTRYPOINT [\"java\", \"-jar\", \"/app.jar\"]",
                    difficulty = "Easy"
                ),
                RoleCodingPractice(
                    id = "CL-C2",
                    title = "Linux Log Grep Command",
                    problem = "Write a Linux bash command to search for 'ERROR' in log.txt and count matches.",
                    codeAnswer = "grep -c \"ERROR\" log.txt",
                    difficulty = "Easy"
                )
            )
        ),
        CareerRoleDetails(
            title = "Cybersecurity Analyst",
            overview = "Protect enterprise computing systems and private databases from network intrusions, security breaches, and software threat factors. Specializes in cryptography, audits, and security systems.",
            dailyResponsibilities = "Audit system networks traffic, monitor logs files anomalies, run vulnerability scans, configure firewalls rules, and verify API tokenizations security protocols.",
            salaryRange = "₹7 LPA - ₹26+ LPA",
            hiringTrends = "Severe talent shortage. Critical hiring from fintechs, digital banks, and consulting giants seeking certified threat analysts.",
            futureScope = "Outstanding longevity. Leads directly to Security Architect, Pen Tester, and Chief Information Security Officer (CISO).",
            requiredSkills = listOf("Networking (TCP/IP)", "Cryptography basics", "Security Auditing", "API Threat Prevention", "Linux Security"),
            technologies = listOf("Wireshark", "Linux", "OpenSSL", "OWASP top 10", "Burp Suite", "Kali Linux"),
            dependencyMapSkills = listOf("TCP/IP Networking", "Cryptography Basics", "API vulnerability scanning", "OWASP prevention", "Audit monitoring"),
            beginnerRoadmap = LearningStage(
                title = "Network Foundations",
                timeline = "Weeks 1 - 3",
                topics = listOf("TCP/IP Handshake", "DNS & DHCP protocols", "Wireshark packet capture analysis"),
                projectIdea = "Use Wireshark to intercept local HTTP network packets and analyze plain text credentials leakage.",
                certifications = listOf("CompTIA Security+"),
                recommendedTools = listOf("Wireshark", "Packet Tracer")
            ),
            intermediateRoadmap = LearningStage(
                title = "Cryptography & Linux Security",
                timeline = "Weeks 4 - 8",
                topics = listOf("AES vs RSA encryption keys", "SSL/TLS handshake phases", "Linux user privileges"),
                projectIdea = "Generate custom local RSA keys using OpenSSL and build an offline encrypt-decrypt system.",
                certifications = listOf("EC-Council Certified Ethical Hacker"),
                recommendedTools = listOf("OpenSSL", "Kali Linux")
            ),
            advancedRoadmap = LearningStage(
                title = "API Security & OWASP Audits",
                timeline = "Weeks 9 - 12",
                topics = listOf("SQL injection prevention", "XSS threat mitigations", "OAuth2 & JWT tokenization validation"),
                projectIdea = "Build a local Java/Python API with secure prepared SQL statements to prevent SQL Injection attempts.",
                certifications = listOf("Offensive Security Certified Professional (OSCP)"),
                recommendedTools = listOf("Burp Suite", "OWASP ZAP")
            ),
            interviewPrepQuestions = listOf(
                RolePrepQuestion("Explain the difference between symmetric and asymmetric cryptography.", "Symmetric uses a single shared key for both encryption and decryption (fast, e.g. AES). Asymmetric uses a public key to encrypt and a private key to decrypt (secure, e.g. RSA).", "Technical"),
                RolePrepQuestion("What is SQL Injection? How do you prevent it in code?", "It is a vulnerability where malicious SQL commands are executed by inputting raw code in fields. Prevent by using Prepared Statements (parameterized queries) and input validation.", "Technical"),
                RolePrepQuestion("What happens during a TLS handshake?", "1. Client Hello (supported suites). 2. Server Hello (certificate + public key). 3. Client verifies certificate, sends encrypted Pre-Master Secret. 4. Both derive symmetric session keys for subsequent secure channels.", "Technical")
            ),
            codingPractice = listOf(
                RoleCodingPractice(
                    id = "CS-C1",
                    title = "Prepared Statement Prevents Injection",
                    problem = "Write a safe Java JDBC snippet querying a user record safely using prepared inputs.",
                    codeAnswer = "String sql = \"SELECT * FROM users WHERE username = ? AND password = ?\";\nPreparedStatement pstmt = conn.prepareStatement(sql);\npstmt.setString(1, user);\npstmt.setString(2, pass);\nResultSet rs = pstmt.executeQuery();",
                    difficulty = "Easy"
                ),
                RoleCodingPractice(
                    id = "CS-C2",
                    title = "Hashing Password with SHA-256",
                    problem = "Write a Java method to securely hash a password using MessageDigest.",
                    codeAnswer = "public String hashPassword(String password) throws Exception {\n    MessageDigest digest = MessageDigest.getInstance(\"SHA-256\");\n    byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));\n    StringBuilder hexString = new StringBuilder();\n    for (byte b : hash) {\n        String hex = Integer.toHexString(0xff & b);\n        if(hex.length() == 1) hexString.append('0');\n        hexString.append(hex);\n    }\n    return hexString.toString();\n}",
                    difficulty = "Medium"
                )
            )
        ),
        CareerRoleDetails(
            title = "DevOps Engineer",
            overview = "Bridges the gap between software development and IT infrastructure operations. Specializes in automating software releases, scaling CI/CD build channels, and server deployments.",
            dailyResponsibilities = "Write deployment scripts, maintain CI/CD pipelines, configure build pipelines, inspect container clusters metrics, and monitor server environments.",
            salaryRange = "₹9 LPA - ₹32+ LPA",
            hiringTrends = "Very rapid growth. Companies are migrating heavily toward automated microservices pipelines, making DevOps certified engineers highly valued.",
            futureScope = "Excellent. DevOps opens paths directly to Site Reliability Architect, DevOps Director, and Chief Infrastructure Engineer.",
            requiredSkills = listOf("Git & Version Control", "CI/CD Pipelines (Jenkins)", "Docker Containerization", "Kubernetes orchestrations", "Bash/Python Scripting"),
            technologies = listOf("Git", "Jenkins", "Docker", "Kubernetes", "Linux", "Nginx"),
            dependencyMapSkills = listOf("Git branching", "Jenkins Pipelines", "Docker Packaging", "Kubernetes Pods", "Server Orchestration"),
            beginnerRoadmap = LearningStage(
                title = "Git & Version Control",
                timeline = "Weeks 1 - 3",
                topics = listOf("Git rebasing & merging", "Branching methodologies (GitFlow)", "Resolving merge conflicts"),
                projectIdea = "Setup a local git repository simulating overlapping commits and practice manual rebases.",
                certifications = listOf("GitLab Certified Git Associate"),
                recommendedTools = listOf("Git", "GitHub Desktop")
            ),
            intermediateRoadmap = LearningStage(
                title = "CI/CD Automation",
                timeline = "Weeks 4 - 8",
                topics = listOf("Jenkins file pipeline stages", "Automated unit tests execution", "Artifact management"),
                projectIdea = "Create an automated local Jenkins pipeline that compiles a Java application whenever a new commit occurs.",
                certifications = listOf("Certified Jenkins Engineer"),
                recommendedTools = listOf("Jenkins Server local", "Maven")
            ),
            advancedRoadmap = LearningStage(
                title = "Cluster Orchestrations",
                timeline = "Weeks 9 - 12",
                topics = listOf("Docker container builds", "Kubernetes deployments", "Nginx reverse proxy setups"),
                projectIdea = "Build a local Dockerized microservices stack managed inside an offline Kubernetes deployment.",
                certifications = listOf("Certified Kubernetes Application Developer (CKAD)"),
                recommendedTools = listOf("Kubernetes minikube", "Docker")
            ),
            interviewPrepQuestions = listOf(
                RolePrepQuestion("What is Continuous Integration (CI)?", "It is the DevOps practice of regularly merging code changes into a central repository, where automated builds and tests are run to verify code health immediately.", "Technical"),
                RolePrepQuestion("What is a container? How does it differ from a Virtual Machine?", "A container packages an application and its dependencies, sharing the host OS kernel (making it fast and lightweight). A VM includes a full guest OS, running on top of a hypervisor (making it heavier and slower).", "Technical"),
                RolePrepQuestion("How do you resolve a merge conflict in Git?", "1. Identify conflicting files via `git status`. 2. Open files and manually resolve conflict markers (<<<<<<<, =======, >>>>>>>). 3. Save, stage with `git add`, and finalize commit with `git commit`.", "Technical")
            ),
            codingPractice = listOf(
                RoleCodingPractice(
                    id = "DO-C1",
                    title = "Basic Jenkinsfile Pipeline Syntax",
                    problem = "Draft a basic Jenkinsfile containing 'Build' and 'Test' stages.",
                    codeAnswer = "pipeline {\n    agent any\n    stages {\n        stage('Build') {\n            steps { sh 'mvn clean compile' }\n        }\n        stage('Test') {\n            steps { sh 'mvn test' }\n        }\n    }\n}",
                    difficulty = "Easy"
                ),
                RoleCodingPractice(
                    id = "DO-C2",
                    title = "Kubernetes Deployment YAML",
                    problem = "Draft a minimal Kubernetes Deployment YAML for an Nginx pod.",
                    codeAnswer = "apiVersion: apps/v1\nkind: Deployment\nmetadata:\n  name: nginx-deploy\nspec:\n  replicas: 2\n  selector:\n    matchLabels:\n      app: web\n  template:\n    metadata:\n      labels:\n        app: web\n    spec:\n      containers:\n      - name: nginx\n        image: nginx:1.14.2",
                    difficulty = "Medium"
                )
            )
        )
    )
}
