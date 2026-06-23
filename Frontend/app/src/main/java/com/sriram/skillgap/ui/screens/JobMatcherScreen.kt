package com.sriram.skillgap.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sriram.skillgap.data.model.*
import com.google.firebase.firestore.FirebaseFirestore
import com.sriram.skillgap.ui.components.BottomNavigationBar
import com.sriram.skillgap.ui.components.PremiumGlassCard
import com.sriram.skillgap.ui.theme.*
import com.sriram.skillgap.viewmodel.SkillViewModel
import com.sriram.skillgap.utils.JobMatcherData
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobMatcherScreen(navController: NavController, viewModel: SkillViewModel) {
    val context = LocalContext.current
    val user by viewModel.user.collectAsState()
    val userSkills by viewModel.userSkills.collectAsState()
    val onlineJobs by viewModel.onlineJobs.collectAsState()
    val onlineCompanies by viewModel.onlineCompanies.collectAsState()
    val savedJobIds by viewModel.savedJobIds.collectAsState()
    val bookmarkedCompanyNames by viewModel.bookmarkedCompanyNames.collectAsState()
    val isLoadingJobs by viewModel.isLoadingJobs.collectAsState()
    val syncMessage by viewModel.syncMessage.collectAsState()

    var selectedTab by remember { mutableStateOf(0) }
    val tabTitles = listOf("Matched Jobs", "Dream Finder", "Readiness", "Applications", "Analytics")

    val userSkillsNames = remember(userSkills) { userSkills.map { it.name } }
    val dreamJob = user?.dreamJob ?: "Software Developer"

    // Toast notifications for database syncing
    LaunchedEffect(syncMessage) {
        syncMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearSyncMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("AI Job Matcher", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("Career Opportunities Hub", color = Color.Gray, fontSize = 12.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.syncAndSeedDatabase() }) {
                        Icon(Icons.Default.CloudSync, contentDescription = "Sync Cloud", tint = NeonBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepDarkBlue)
            )
        },
        bottomBar = { BottomNavigationBar(navController) },
        containerColor = DeepDarkBlue
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(DeepDarkBlue, Color(0xFF12153B))
                    )
                )
        ) {
            // Tab Selector
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = Color.White,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = NeonBlue
                    )
                },
                divider = { Divider(color = Color.White.copy(alpha = 0.1f)) }
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }

            if (isLoadingJobs) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = NeonBlue)
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    when (selectedTab) {
                        0 -> MatchedJobsTab(
                            jobs = onlineJobs,
                            userSkills = userSkillsNames,
                            savedJobIds = savedJobIds,
                            viewModel = viewModel
                        )
                        1 -> DreamFinderTab(
                            companies = onlineCompanies,
                            jobs = onlineJobs,
                            bookmarkedCompanies = bookmarkedCompanyNames,
                            viewModel = viewModel
                        )
                        2 -> ReadinessTab(
                            companyName = viewModel.selectedCompany.collectAsState().value,
                            dreamJob = dreamJob,
                            userSkills = userSkillsNames,
                            viewModel = viewModel
                        )
                        3 -> ApplicationsTab(
                            viewModel = viewModel
                        )
                        4 -> AnalyticsTab(
                            jobs = onlineJobs,
                            userSkills = userSkillsNames,
                            savedJobIds = savedJobIds,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchedJobsTab(
    jobs: List<Job>,
    userSkills: List<String>,
    savedJobIds: Set<String>,
    viewModel: SkillViewModel
) {
    var searchQuery by remember { mutableStateOf("") }
    var minMatchFilter by remember { mutableStateOf(0) } // 0%, 50%, 75%
    var showOnlySaved by remember { mutableStateOf(false) }
    var selectedJobForDetails by remember { mutableStateOf<Job?>(null) }

    val processedJobs = remember(jobs, userSkills, searchQuery, minMatchFilter, showOnlySaved, savedJobIds) {
        jobs.map { job ->
            val matchPct = viewModel.getMatchPercentageForJob(job, userSkills)
            val missing = viewModel.getMissingSkillsForJob(job, userSkills)
            Triple(job, matchPct, missing)
        }.filter { (job, matchPct, _) ->
            val matchesSearch = job.jobTitle.contains(searchQuery, ignoreCase = true) ||
                    job.companyName.contains(searchQuery, ignoreCase = true)
            val matchesPct = matchPct >= minMatchFilter
            val matchesSaved = !showOnlySaved || savedJobIds.contains(job.id)
            matchesSearch && matchesPct && matchesSaved
        }.sortedByDescending { it.second }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(12.dp))

        // Search and Filters
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search title/company...", color = Color.Gray, fontSize = 14.sp) },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = NeonBlue,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                    containerColor = SurfaceDark
                ),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) }
            )

            // Saved filter toggle
            IconButton(
                onClick = { showOnlySaved = !showOnlySaved },
                modifier = Modifier
                    .size(52.dp)
                    .background(
                        if (showOnlySaved) NeonPurple.copy(alpha = 0.2f) else SurfaceDark,
                        RoundedCornerShape(12.dp)
                    )
                    .border(
                        1.dp,
                        if (showOnlySaved) NeonPurple else Color.White.copy(alpha = 0.1f),
                        RoundedCornerShape(12.dp)
                    )
            ) {
                Icon(
                    imageVector = if (showOnlySaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = "Show Bookmarked",
                    tint = if (showOnlySaved) NeonPurple else Color.LightGray
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Match range chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(0 to "All Match", 50 to "50%+ Match", 75 to "75%+ Match").forEach { (pct, label) ->
                val selected = minMatchFilter == pct
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (selected) NeonBlue else SurfaceDark)
                        .clickable { minMatchFilter = pct }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        label,
                        color = if (selected) Color.Black else Color.LightGray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (processedJobs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.WorkOff, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(60.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("No matching jobs found.", color = Color.Gray, fontSize = 16.sp)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                items(processedJobs) { (job, matchPct, missing) ->
                    LinkedInJobCard(
                        job = job,
                        matchPercentage = matchPct,
                        missingSkills = missing,
                        isSaved = savedJobIds.contains(job.id),
                        onSaveToggle = { viewModel.toggleSaveJob(job.id) },
                        onClick = { selectedJobForDetails = job }
                    )
                }
            }
        }
    }

    if (selectedJobForDetails != null) {
        JobDetailsDialog(
            job = selectedJobForDetails!!,
            matchPct = viewModel.getMatchPercentageForJob(selectedJobForDetails!!, userSkills),
            missing = viewModel.getMissingSkillsForJob(selectedJobForDetails!!, userSkills),
            onDismiss = { selectedJobForDetails = null },
            isSaved = savedJobIds.contains(selectedJobForDetails!!.id),
            onSaveToggle = { viewModel.toggleSaveJob(selectedJobForDetails!!.id) },
            viewModel = viewModel
        )
    }
}

@Composable
fun LinkedInJobCard(
    job: Job,
    matchPercentage: Int,
    missingSkills: List<String>,
    isSaved: Boolean,
    onSaveToggle: () -> Unit,
    onClick: () -> Unit
) {
    val matchColor = when {
        matchPercentage >= 90 -> NeonGreen
        matchPercentage >= 70 -> NeonBlue
        else -> WarningYellow
    }

    PremiumGlassCard(
        modifier = Modifier.clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Mock Logo Badge
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(NeonBlue.copy(alpha = 0.3f), NeonPurple.copy(alpha = 0.3f))
                            )
                        )
                        .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = job.companyName.take(2).uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = job.jobTitle,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = job.companyName,
                        color = Color.LightGray,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Saved button
                IconButton(onClick = onSaveToggle, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Save Job",
                        tint = if (isSaved) NeonPurple else Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Stats and Badge Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Match percentage badge
                Surface(
                    color = matchColor.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, matchColor),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "$matchPercentage% Match",
                        color = matchColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                // Salary Range
                Text(
                    text = job.salaryRange.ifEmpty { "Competitive Pay" },
                    color = Color.LightGray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )

                // Divider dot
                Text("•", color = Color.Gray, fontSize = 12.sp)

                // Exp Required
                Text(
                    text = job.experienceRequired,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            // Missing skills section
            if (missingSkills.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = "Missing",
                        tint = WarningYellow,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Gaps: ${missingSkills.take(3).joinToString(", ")}${if (missingSkills.size > 3) " +${missingSkills.size - 3} more" else ""}",
                        color = WarningYellow,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Full Match",
                        tint = NeonGreen,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "100% Skill Alignment! Ready to apply.",
                        color = NeonGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DreamFinderTab(
    companies: List<Company>,
    jobs: List<Job>,
    bookmarkedCompanies: Set<String>,
    viewModel: SkillViewModel
) {
    var selectedCompanyForDetails by remember { mutableStateOf<Company?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredCompanies = remember(companies, searchQuery) {
        companies.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.industry.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(12.dp))

        // Search Box
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search dream companies...", color = Color.Gray, fontSize = 14.sp) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = NeonBlue,
                unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                containerColor = SurfaceDark
            ),
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) }
        )

        Spacer(modifier = Modifier.height(14.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            items(filteredCompanies.size) { index ->
                val company = filteredCompanies[index]
                val isBookmarked = bookmarkedCompanies.contains(company.name)
                val companyJobsCount = jobs.count { it.companyName.equals(company.name, ignoreCase = true) }

                CompanyCard(
                    company = company,
                    isBookmarked = isBookmarked,
                    jobsCount = companyJobsCount,
                    onBookmarkToggle = { viewModel.toggleBookmarkCompany(company.name) },
                    onClick = { selectedCompanyForDetails = company }
                )
            }
        }
    }

    if (selectedCompanyForDetails != null) {
        val companyJobs = jobs.filter { it.companyName.equals(selectedCompanyForDetails!!.name, ignoreCase = true) }
        CompanyDetailsDialog(
            company = selectedCompanyForDetails!!,
            availableJobs = companyJobs,
            onDismiss = { selectedCompanyForDetails = null },
            isBookmarked = bookmarkedCompanies.contains(selectedCompanyForDetails!!.name),
            onBookmarkToggle = { viewModel.toggleBookmarkCompany(selectedCompanyForDetails!!.name) },
            viewModel = viewModel
        )
    }
}

@Composable
fun CompanyCard(
    company: Company,
    isBookmarked: Boolean,
    jobsCount: Int,
    onBookmarkToggle: () -> Unit,
    onClick: () -> Unit
) {
    PremiumGlassCard(
        modifier = Modifier.clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Badge for job counts
                Surface(
                    color = NeonBlue.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        "$jobsCount roles",
                        color = NeonBlue,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                IconButton(
                    onClick = onBookmarkToggle,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Bookmark Company",
                        tint = if (isBookmarked) HotPink else Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Logo circle
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(NeonPurple.copy(alpha = 0.3f), Color.Transparent)
                        )
                    )
                    .border(1.dp, NeonPurple, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = company.logo.ifEmpty { company.name.take(1) },
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                company.name,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
            Text(
                company.industry,
                color = Color.Gray,
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                company.salaryRange,
                color = NeonGreen,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ReadinessTab(
    companyName: String?,
    dreamJob: String,
    userSkills: List<String>,
    viewModel: SkillViewModel
) {
    val readiness = remember(companyName, dreamJob, userSkills) {
        viewModel.getPlacementReadiness(companyName, dreamJob, userSkills)
    }

    val targetRole = remember(dreamJob) {
        JobMatcherData.getOfflineRoles().find { it.title.equals(dreamJob, ignoreCase = true) }
    }

    val userSkillsLower = remember(userSkills) { userSkills.map { it.lowercase() } }
    val missingSkills: List<String> = targetRole?.requiredSkills?.filter { it.lowercase() !in userSkillsLower } ?: emptyList()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
    ) {
        // Targets Card
        item {
            PremiumGlassCard {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Target Career Blueprint", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text("DREAM ROLE", color = Color.Gray, fontSize = 10.sp)
                            Text(dreamJob, color = NeonPurple, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("TARGET COMPANY", color = Color.Gray, fontSize = 10.sp)
                            Text(companyName ?: "Not Selected", color = NeonBlue, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
            }
        }

        // Readiness score indicators
        item {
            Text("Placement Readiness Predictor", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        item {
            PremiumGlassCard {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Main selection probability Dial
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(HotPink.copy(alpha = 0.1f))
                                .border(2.dp, HotPink, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${readiness.selectionProbability}%",
                                    color = HotPink,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text("PROBABILITY", color = Color.Gray, fontSize = 8.sp)
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text("Selection Probability Index", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(
                                "Weighted evaluation across technical skills matches, interview simulation answers, and company alignment.",
                                color = Color.Gray,
                                fontSize = 11.sp,
                                lineHeight = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Readiness metrics split
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ReadinessSubGauge(readiness.jobReadiness, "Job", NeonBlue, Modifier.weight(1f))
                        ReadinessSubGauge(readiness.companyReadiness, "Company", NeonPurple, Modifier.weight(1f))
                        ReadinessSubGauge(readiness.interviewReadiness, "Interview", NeonGreen, Modifier.weight(1f))
                    }
                }
            }
        }

        // Skill Gap Mapping & Timeline
        item {
            Text("Skill Gap To Job Mapping", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        item {
            PremiumGlassCard {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Current Match", color = Color.Gray, fontSize = 12.sp)
                            Text("${readiness.jobReadiness}%", color = NeonBlue, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                        }
                        Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.Gray)
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Future Match", color = Color.Gray, fontSize = 12.sp)
                            Text("92%", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Prep time banner
                    Surface(
                        color = WarningYellow.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, WarningYellow),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Schedule, contentDescription = null, tint = WarningYellow)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Estimated Preparation Time", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text("30 Days sprint of targeted practice required.", color = WarningYellow, fontSize = 11.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Identified Gaps (Remaining Skills):", color = Color.LightGray, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    if (missingSkills.isEmpty()) {
                        Text("No gaps detected! You are fully aligned.", color = NeonGreen, fontSize = 12.sp)
                    } else {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            for (skill in missingSkills) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(SurfaceDark)
                                        .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(skill, color = Color.White, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReadinessSubGauge(score: Int, label: String, color: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(SurfaceDark, RoundedCornerShape(12.dp))
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.1f))
                .border(1.dp, color.copy(alpha = 0.3f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("$score%", color = color, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
fun AnalyticsTab(
    jobs: List<Job>,
    userSkills: List<String>,
    savedJobIds: Set<String>,
    viewModel: SkillViewModel
) {
    val stats = remember(jobs, userSkills, savedJobIds) {
        val totalJobs = jobs.size
        val savedCount = savedJobIds.size
        
        // Compute match percentage for all jobs
        val matchPcts = jobs.map { job ->
            viewModel.getMatchPercentageForJob(job, userSkills) to job
        }
        val topMatches = matchPcts.sortedByDescending { it.first }.take(3)

        // Find missing skills frequency
        val missingSkillsFreq = mutableMapOf<String, Int>()
        jobs.forEach { job ->
            viewModel.getMissingSkillsForJob(job, userSkills).forEach { skill ->
                missingSkillsFreq[skill] = (missingSkillsFreq[skill] ?: 0) + 1
            }
        }
        val topMissingSkills = missingSkillsFreq.toList().sortedByDescending { it.second }.take(4)

        // Parse packages to sort highest salary (crude parser for mock LPA ranges)
        val highestSalaries = jobs.sortedByDescending { job ->
            val numStr = job.salaryRange.filter { it.isDigit() }
            if (numStr.isNotEmpty()) numStr.takeLast(2).toInt() else 0
        }.take(3)

        QuadStats(totalJobs, savedCount, topMatches, topMissingSkills, highestSalaries)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
    ) {
        // Summary row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatSumCard("Available Opportunities", "${stats.totalJobs} Active", NeonBlue, Modifier.weight(1f))
                StatSumCard("Bookmarked Opportunities", "${stats.savedCount} Saved", NeonPurple, Modifier.weight(1f))
            }
        }

        // Top Matching Jobs
        item {
            Text("Top Matching Careers", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        items(stats.topMatches) { (matchPct, job) ->
            PremiumGlassCard {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(NeonBlue.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.TrendingUp, contentDescription = null, tint = NeonBlue)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(job.jobTitle, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(job.companyName, color = Color.Gray, fontSize = 12.sp)
                    }
                    Text("$matchPct% Match", color = NeonGreen, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                }
            }
        }

        // High Salary opportunities
        item {
            Text("Highest Package Roles", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        items(stats.highestSalaries) { job ->
            PremiumGlassCard {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(NeonPurple.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Payments, contentDescription = null, tint = NeonPurple)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(job.jobTitle, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(job.companyName, color = Color.Gray, fontSize = 12.sp)
                    }
                    Text(job.salaryRange, color = NeonGreen, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                }
            }
        }

        // Hot Skill Gaps
        item {
            Text("Most Demanded Missing Skills", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        item {
            PremiumGlassCard {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Acquiring these key skills unlocks the highest matching percentages:", color = Color.Gray, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(14.dp))
                    stats.topMissingSkills.forEach { (skill, count) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Text(skill, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                            Text("Found in $count jobs", color = HotPink, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Admin Panel Button/Expansion
        item {
            var showAdminPanel by remember { mutableStateOf(false) }
            Column(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { showAdminPanel = !showAdminPanel },
                    colors = ButtonDefaults.buttonColors(containerColor = if (showAdminPanel) HotPink else NeonPurple),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = if (showAdminPanel) Icons.Default.Close else Icons.Default.Build,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (showAdminPanel) "Close Administrator Dashboard" else "Open Administrator Dashboard",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (showAdminPanel) {
                    Spacer(modifier = Modifier.height(12.dp))
                    AdminDashboardPanel(viewModel = viewModel)
                }
            }
        }
    }
}

data class QuadStats(
    val totalJobs: Int,
    val savedCount: Int,
    val topMatches: List<Pair<Int, Job>>,
    val topMissingSkills: List<Pair<String, Int>>,
    val highestSalaries: List<Job>
)

@Composable
fun StatSumCard(title: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(SurfaceDark, RoundedCornerShape(16.dp))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(title, color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))
        Text(value, color = color, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
    }
}

@Composable
fun JobDetailsDialog(
    job: Job,
    matchPct: Int,
    missing: List<String>,
    isSaved: Boolean,
    onSaveToggle: () -> Unit,
    onDismiss: () -> Unit,
    viewModel: SkillViewModel
) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceDark,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(job.jobTitle, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(job.companyName, color = NeonBlue, fontSize = 14.sp)
                }
                IconButton(onClick = onSaveToggle) {
                    Icon(
                        imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Save",
                        tint = if (isSaved) NeonPurple else Color.LightGray
                    )
                }
            }
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Salary: ${job.salaryRange}", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("|", color = Color.Gray, fontSize = 12.sp)
                        Text("Exp: ${job.experienceRequired}", color = Color.LightGray, fontSize = 12.sp)
                        Text("|", color = Color.Gray, fontSize = 12.sp)
                        Text("Difficulty: ${job.interviewDifficulty}", color = HotPink, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }

                item {
                    Divider(color = Color.White.copy(alpha = 0.1f))
                }

                item {
                    Text("Job Description", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(job.jobDescription, color = Color.LightGray, fontSize = 13.sp, lineHeight = 18.sp)
                }

                item {
                    Text("Hiring Process Stages", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    for ((idx, stage) in job.hiringProcess.withIndex()) {
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
                            Text("🔹 Round ${idx + 1}: ", color = NeonBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text(stage, color = Color.LightGray, fontSize = 12.sp)
                        }
                    }
                }

                item {
                    val company = remember(job) { viewModel.onlineCompanies.value.find { it.companyId == job.companyId || it.companyName.equals(job.companyName, ignoreCase = true) } }
                    val eligibility = company?.eligibilityCriteria ?: "Basic eligibility criteria based on skills match."
                    Text("Eligibility Criteria", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(eligibility, color = Color.LightGray, fontSize = 13.sp)
                }

                item {
                    Text("Match Report & Skill Gaps", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "Your resume aligns at $matchPct% with this role.",
                        color = if (matchPct >= 70) NeonGreen else WarningYellow,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (missing.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Missing Skills to Learn:", color = Color.Gray, fontSize = 12.sp)
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            for (s in missing) {
                                Surface(
                                    color = WarningYellow.copy(alpha = 0.1f),
                                    border = BorderStroke(1.dp, WarningYellow.copy(alpha = 0.5f)),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        s,
                                        color = WarningYellow,
                                        fontSize = 10.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Divider(color = Color.White.copy(alpha = 0.1f))
                }

                item {
                    val applications by viewModel.userApplications.collectAsState(emptyList())
                    val currentApp = remember(applications, job.jobId) { applications.find { it.jobId == job.jobId } }
                    val currentStatus = currentApp?.applicationStatus ?: "Not Tracked"
                    
                    var dropdownExpanded by remember { mutableStateOf(false) }
                    val statuses = listOf("Not Tracked", "Interested", "Applied", "Interview Scheduled", "Selected", "Rejected", "Offer Received")
                    
                    Column {
                        Text("Application Tracker Status", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White.copy(alpha = 0.03f), RoundedCornerShape(8.dp))
                                .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                                .clickable { dropdownExpanded = true }
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val statusColor = when (currentStatus) {
                                    "Interested" -> Color.Cyan
                                    "Applied" -> NeonBlue
                                    "Interview Scheduled" -> WarningYellow
                                    "Selected", "Offer Received" -> NeonGreen
                                    "Rejected" -> ErrorRed
                                    else -> Color.Gray
                                }
                                Text(currentStatus, color = statusColor, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Gray)
                            }
                            
                            DropdownMenu(
                                expanded = dropdownExpanded,
                                onDismissRequest = { dropdownExpanded = false },
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
                                    .background(SurfaceDark)
                                    .border(1.dp, Color.White.copy(alpha = 0.1f))
                            ) {
                                statuses.forEach { status ->
                                    DropdownMenuItem(
                                        text = { Text(status, color = Color.White) },
                                        onClick = {
                                            if (status == "Not Tracked") {
                                                val currentUser = viewModel.user.value
                                                if (currentUser != null) {
                                                    FirebaseFirestore.getInstance()
                                                        .collection("user_applications")
                                                        .document("${currentUser.id}_${job.jobId}")
                                                        .delete()
                                                }
                                            } else {
                                                viewModel.updateApplicationStatus(
                                                    jobId = job.jobId,
                                                    companyName = job.companyName,
                                                    jobTitle = job.jobTitle,
                                                    status = status,
                                                    notes = currentApp?.notes ?: ""
                                                )
                                            }
                                            dropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (job.applicationLink.isNotEmpty()) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(job.applicationLink))
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(context, "No apply link available.", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = NeonBlue),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Apply Now", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = Color.LightGray)
            }
        }
    )
}

@Composable
fun CompanyDetailsDialog(
    company: Company,
    availableJobs: List<Job>,
    isBookmarked: Boolean,
    onBookmarkToggle: () -> Unit,
    onDismiss: () -> Unit,
    viewModel: SkillViewModel
) {
    var selectedJobForDetails by remember { mutableStateOf<Job?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceDark,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(company.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(company.industry, color = NeonPurple, fontSize = 13.sp)
                }
                IconButton(onClick = onBookmarkToggle) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Bookmark",
                        tint = if (isBookmarked) HotPink else Color.LightGray
                    )
                }
            }
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("HQ: ${company.location}", color = Color.LightGray, fontSize = 12.sp)
                        Text("|", color = Color.Gray, fontSize = 12.sp)
                        Text("Package: ${company.salaryRange}", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }

                item { Divider(color = Color.White.copy(alpha = 0.1f)) }

                item {
                    Text("Work Culture", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(company.workCulture, color = Color.LightGray, fontSize = 13.sp, lineHeight = 18.sp)
                }

                item {
                    Text("Technologies Used", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        for (tech in company.technologies) {
                            Surface(
                                color = NeonBlue.copy(alpha = 0.1f),
                                border = BorderStroke(1.dp, NeonBlue.copy(alpha = 0.4f)),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    tech,
                                    color = NeonBlue,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                item {
                    Text("Hiring Process Overview", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    for ((index, process) in company.interviewProcess.withIndex()) {
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
                            Text("🔹 Round ${index + 1}: ", color = NeonPurple, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text(process, color = Color.LightGray, fontSize = 12.sp)
                        }
                    }
                }

                item { Divider(color = Color.White.copy(alpha = 0.1f)) }

                item {
                    Text("Available Roles (${availableJobs.size})", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }

                if (availableJobs.isEmpty()) {
                    item {
                        Text("No open positions found in database right now.", color = Color.Gray, fontSize = 12.sp)
                    }
                } else {
                    items(availableJobs) { job ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White.copy(alpha = 0.03f), RoundedCornerShape(10.dp))
                                .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(10.dp))
                                .clickable { selectedJobForDetails = job }
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(job.jobTitle, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(job.salaryRange, color = NeonGreen, fontSize = 11.sp)
                                }
                                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            val context = LocalContext.current
            Button(
                onClick = {
                    if (company.website.isNotEmpty()) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(company.website))
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(context, "No website link available.", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Visit Careers Page", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = Color.LightGray)
            }
        }
    )

    if (selectedJobForDetails != null) {
        val userSkillsState by viewModel.userSkills.collectAsState()
        val userSkills = remember(userSkillsState) { userSkillsState.map { it.name } }
        JobDetailsDialog(
            job = selectedJobForDetails!!,
            matchPct = viewModel.getMatchPercentageForJob(selectedJobForDetails!!, userSkills),
            missing = viewModel.getMissingSkillsForJob(selectedJobForDetails!!, userSkills),
            onDismiss = { selectedJobForDetails = null },
            isSaved = viewModel.savedJobIds.collectAsState().value.contains(selectedJobForDetails!!.id),
            onSaveToggle = { viewModel.toggleSaveJob(selectedJobForDetails!!.id) },
            viewModel = viewModel
        )
    }
}

@Composable
fun ApplicationsTab(
    viewModel: SkillViewModel
) {
    val applications by viewModel.userApplications.collectAsState(emptyList())
    var selectedAppForEdit by remember { mutableStateOf<UserApplication?>(null) }

    val totalApplied = remember(applications) { applications.count { it.applicationStatus == "Applied" } }
    val underInterview = remember(applications) { applications.count { it.applicationStatus == "Interview Scheduled" } }
    val offersReceived = remember(applications) { applications.count { it.applicationStatus == "Offer Received" || it.applicationStatus == "Selected" } }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
    ) {
        // Stats Summary
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Applied",
                    value = totalApplied.toString(),
                    color = NeonBlue,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Interviews",
                    value = underInterview.toString(),
                    color = WarningYellow,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Offers",
                    value = offersReceived.toString(),
                    color = NeonGreen,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Section Title
        item {
            Text(
                text = "Active Application Pipeline",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        if (applications.isEmpty()) {
            item {
                PremiumGlassCard {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Assignment,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No applications tracked yet.",
                            color = Color.LightGray,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Find a matching job and update its application status to start tracking!",
                            color = Color.Gray,
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(applications) { app ->
                ApplicationRow(
                    application = app,
                    onClick = { selectedAppForEdit = app }
                )
            }
        }
    }

    if (selectedAppForEdit != null) {
        ApplicationStatusEditDialog(
            application = selectedAppForEdit!!,
            onDismiss = { selectedAppForEdit = null },
            onUpdate = { status, notes ->
                viewModel.updateApplicationStatus(
                    jobId = selectedAppForEdit!!.jobId,
                    companyName = selectedAppForEdit!!.companyName,
                    jobTitle = selectedAppForEdit!!.jobTitle,
                    status = status,
                    notes = notes
                )
                selectedAppForEdit = null
            }
        )
    }
}

@Composable
fun StatCard(title: String, value: String, color: Color, modifier: Modifier = Modifier) {
    PremiumGlassCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            Text(value, color = color, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ApplicationRow(application: UserApplication, onClick: () -> Unit) {
    val statusColor = when (application.applicationStatus) {
        "Interested" -> Color.Cyan
        "Applied" -> NeonBlue
        "Interview Scheduled" -> WarningYellow
        "Selected", "Offer Received" -> NeonGreen
        "Rejected" -> ErrorRed
        else -> Color.Gray
    }

    PremiumGlassCard(
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = application.jobTitle,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Text(
                    text = application.companyName,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                if (application.notes.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Notes: ${application.notes}",
                        color = Color.LightGray,
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Surface(
                    color = statusColor.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, statusColor.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = application.applicationStatus,
                        color = statusColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                val dateStr = remember(application.appliedDate) {
                    val sdf = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())
                    sdf.format(java.util.Date(application.appliedDate))
                }
                Text(
                    text = dateStr,
                    color = Color.Gray,
                    fontSize = 9.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationStatusEditDialog(
    application: UserApplication,
    onDismiss: () -> Unit,
    onUpdate: (String, String) -> Unit
) {
    var selectedStatus by remember { mutableStateOf(application.applicationStatus) }
    var notes by remember { mutableStateOf(application.notes) }
    var dropdownExpanded by remember { mutableStateOf(false) }

    val statuses = listOf("Interested", "Applied", "Interview Scheduled", "Selected", "Rejected", "Offer Received")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text("Update Application Status", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("${application.jobTitle} at ${application.companyName}", color = Color.Gray, fontSize = 12.sp)
            }
        },
        containerColor = Color(0xFF0F1123),
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Status Dropdown
                Column {
                    Text("Current Status", color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SurfaceDark, RoundedCornerShape(8.dp))
                            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                            .clickable { dropdownExpanded = true }
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(selectedStatus, color = Color.White, fontSize = 14.sp)
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Gray)
                        }

                        DropdownMenu(
                            expanded = dropdownExpanded,
                            onDismissRequest = { dropdownExpanded = false },
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .background(SurfaceDark)
                                .border(1.dp, Color.White.copy(alpha = 0.1f))
                        ) {
                            statuses.forEach { status ->
                                DropdownMenuItem(
                                    text = { Text(status, color = Color.White) },
                                    onClick = {
                                        selectedStatus = status
                                        dropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Notes Text Field
                Column {
                    Text("Preparation / Follow-up Notes", color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        placeholder = { Text("Add interview rounds feedback, salary package negotiations...", color = Color.Gray, fontSize = 12.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = NeonBlue,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.15f),
                            containerColor = SurfaceDark
                        ),
                        maxLines = 4
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onUpdate(selectedStatus, notes) },
                colors = ButtonDefaults.buttonColors(containerColor = NeonBlue)
            ) {
                Text("Save Changes", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Gray)
            }
        }
    )
}

@Composable
fun AdminDashboardPanel(viewModel: SkillViewModel) {
    var formType by remember { mutableStateOf(0) } // 0: Company, 1: Job, 2: Question, 3: Role
    val formNames = listOf("Company", "Job", "Question", "Role")

    PremiumGlassCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Online Firestore Editor",
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Sub-tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                formNames.forEachIndexed { index, name ->
                    val selected = formType == index
                    Surface(
                        color = if (selected) NeonBlue.copy(alpha = 0.2f) else Color.Transparent,
                        border = BorderStroke(1.dp, if (selected) NeonBlue else Color.White.copy(alpha = 0.1f)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { formType = index }
                    ) {
                        Text(
                            text = name,
                            color = if (selected) NeonBlue else Color.Gray,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (formType) {
                0 -> AddCompanyForm(viewModel = viewModel)
                1 -> AddJobForm(viewModel = viewModel)
                2 -> AddQuestionForm(viewModel = viewModel)
                3 -> AddRoleForm(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun AddCompanyForm(viewModel: SkillViewModel) {
    val context = LocalContext.current
    var companyName by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var industry by remember { mutableStateOf("") }
    var website by remember { mutableStateOf("") }
    var salaryRange by remember { mutableStateOf("") }
    var technologies by remember { mutableStateOf("") }
    var hiringProcess by remember { mutableStateOf("") }
    var difficulty by remember { mutableStateOf("Medium") }
    var workCulture by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        AdminTextField(value = companyName, onValueChange = { companyName = it }, label = "Company Name (e.g. Google)")
        AdminTextField(value = location, onValueChange = { location = it }, label = "Location (e.g. Bangalore)")
        AdminTextField(value = industry, onValueChange = { industry = it }, label = "Industry (e.g. SaaS)")
        AdminTextField(value = website, onValueChange = { website = it }, label = "Website URL")
        AdminTextField(value = salaryRange, onValueChange = { salaryRange = it }, label = "Salary Range (e.g. ₹10 - ₹25 LPA)")
        AdminTextField(value = technologies, onValueChange = { technologies = it }, label = "Technologies Used (comma separated)")
        AdminTextField(value = hiringProcess, onValueChange = { hiringProcess = it }, label = "Hiring Process Rounds (comma separated)")
        AdminTextField(value = workCulture, onValueChange = { workCulture = it }, label = "Work Culture Description")
        
        Button(
            onClick = {
                if (companyName.isBlank()) {
                    Toast.makeText(context, "Company name is required", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                isSubmitting = true
                val newComp = Company(
                    companyName = companyName.trim(),
                    location = location.trim(),
                    industry = industry.trim(),
                    website = website.trim(),
                    salaryRange = salaryRange.trim(),
                    technologiesUsed = technologies.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                    hiringProcess = hiringProcess.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                    workCulture = workCulture.trim(),
                    interviewDifficulty = difficulty,
                    companyId = companyName.trim().lowercase().replace(" ", "_")
                )
                viewModel.adminAddCompany(newComp) { success, err ->
                    isSubmitting = false
                    if (success) {
                        Toast.makeText(context, "Company added successfully!", Toast.LENGTH_SHORT).show()
                        companyName = ""; location = ""; industry = ""; website = ""; salaryRange = ""; technologies = ""; hiringProcess = ""; workCulture = ""
                    } else {
                        Toast.makeText(context, "Error: $err", Toast.LENGTH_LONG).show()
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = NeonBlue),
            modifier = Modifier.fillMaxWidth(),
            enabled = !isSubmitting
        ) {
            Text(if (isSubmitting) "Saving..." else "Add Company to Firestore", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AddJobForm(viewModel: SkillViewModel) {
    val context = LocalContext.current
    var companyName by remember { mutableStateOf("") }
    var jobTitle by remember { mutableStateOf("") }
    var jobRole by remember { mutableStateOf("") }
    var requiredSkills by remember { mutableStateOf("") }
    var experienceRequired by remember { mutableStateOf("") }
    var salaryRange by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var jobDescription by remember { mutableStateOf("") }
    var applicationLink by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        AdminTextField(value = companyName, onValueChange = { companyName = it }, label = "Company Name")
        AdminTextField(value = jobTitle, onValueChange = { jobTitle = it }, label = "Job Title (e.g. AI Engineer)")
        AdminTextField(value = jobRole, onValueChange = { jobRole = it }, label = "Job Role Category (e.g. AI Engineer)")
        AdminTextField(value = requiredSkills, onValueChange = { requiredSkills = it }, label = "Required Skills (comma separated)")
        AdminTextField(value = experienceRequired, onValueChange = { experienceRequired = it }, label = "Experience Required (e.g. 0-2 Years)")
        AdminTextField(value = salaryRange, onValueChange = { salaryRange = it }, label = "Salary Range")
        AdminTextField(value = location, onValueChange = { location = it }, label = "Job Location")
        AdminTextField(value = jobDescription, onValueChange = { jobDescription = it }, label = "Job Description")
        AdminTextField(value = applicationLink, onValueChange = { applicationLink = it }, label = "Application Link URL")

        Button(
            onClick = {
                if (companyName.isBlank() || jobTitle.isBlank()) {
                    Toast.makeText(context, "Company and Title are required", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                isSubmitting = true
                val newJob = Job(
                    companyName = companyName.trim(),
                    jobTitle = jobTitle.trim(),
                    jobRole = jobRole.trim().ifEmpty { jobTitle.trim() },
                    requiredSkills = requiredSkills.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                    experienceRequired = experienceRequired.trim(),
                    salaryRange = salaryRange.trim(),
                    location = location.trim(),
                    jobDescription = jobDescription.trim(),
                    applicationLink = applicationLink.trim(),
                    companyId = companyName.trim().lowercase().replace(" ", "_"),
                    jobId = "job_" + java.util.UUID.randomUUID().toString().take(6)
                )
                viewModel.adminAddJob(newJob) { success, err ->
                    isSubmitting = false
                    if (success) {
                        Toast.makeText(context, "Job added successfully!", Toast.LENGTH_SHORT).show()
                        companyName = ""; jobTitle = ""; jobRole = ""; requiredSkills = ""; experienceRequired = ""; salaryRange = ""; location = ""; jobDescription = ""; applicationLink = ""
                    } else {
                        Toast.makeText(context, "Error: $err", Toast.LENGTH_LONG).show()
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = NeonBlue),
            modifier = Modifier.fillMaxWidth(),
            enabled = !isSubmitting
        ) {
            Text(if (isSubmitting) "Saving..." else "Add Job to Firestore", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AddQuestionForm(viewModel: SkillViewModel) {
    val context = LocalContext.current
    var companyName by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var questionType by remember { mutableStateOf("Technical") }
    var question by remember { mutableStateOf("") }
    var difficulty by remember { mutableStateOf("Medium") }
    var answerHint by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        AdminTextField(value = companyName, onValueChange = { companyName = it }, label = "Company Name")
        AdminTextField(value = role, onValueChange = { role = it }, label = "Role Target (e.g. Software Developer)")
        AdminTextField(value = questionType, onValueChange = { questionType = it }, label = "Question Type (Aptitude, Coding, Technical, HR)")
        AdminTextField(value = question, onValueChange = { question = it }, label = "Question Text")
        AdminTextField(value = difficulty, onValueChange = { difficulty = it }, label = "Difficulty (Easy, Medium, Hard)")
        AdminTextField(value = answerHint, onValueChange = { answerHint = it }, label = "Answer Hint / Explanation")

        Button(
            onClick = {
                if (question.isBlank()) {
                    Toast.makeText(context, "Question text is required", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                isSubmitting = true
                val newQ = InterviewQuestion(
                    companyName = companyName.trim(),
                    role = role.trim(),
                    questionType = questionType.trim(),
                    question = question.trim(),
                    difficulty = difficulty,
                    answerHint = answerHint.trim(),
                    companyId = companyName.trim().lowercase().replace(" ", "_"),
                    questionId = "q_" + java.util.UUID.randomUUID().toString().take(6)
                )
                viewModel.adminAddInterviewQuestion(newQ) { success, err ->
                    isSubmitting = false
                    if (success) {
                        Toast.makeText(context, "Question added successfully!", Toast.LENGTH_SHORT).show()
                        companyName = ""; role = ""; question = ""; answerHint = ""
                    } else {
                        Toast.makeText(context, "Error: $err", Toast.LENGTH_LONG).show()
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = NeonBlue),
            modifier = Modifier.fillMaxWidth(),
            enabled = !isSubmitting
        ) {
            Text(if (isSubmitting) "Saving..." else "Add Question to Firestore", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AddRoleForm(viewModel: SkillViewModel) {
    val context = LocalContext.current
    var roleName by remember { mutableStateOf("") }
    var requiredSkills by remember { mutableStateOf("") }
    var certifications by remember { mutableStateOf("") }
    var projects by remember { mutableStateOf("") }
    var roadmap by remember { mutableStateOf("") }
    var salaryRange by remember { mutableStateOf("") }
    var futureScope by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        AdminTextField(value = roleName, onValueChange = { roleName = it }, label = "Role Name (e.g. AI Engineer)")
        AdminTextField(value = requiredSkills, onValueChange = { requiredSkills = it }, label = "Required Skills (comma separated)")
        AdminTextField(value = certifications, onValueChange = { certifications = it }, label = "Recommended Certifications (comma separated)")
        AdminTextField(value = projects, onValueChange = { projects = it }, label = "Recommended Projects (comma separated)")
        AdminTextField(value = roadmap, onValueChange = { roadmap = it }, label = "Learning Roadmap Stages (comma separated)")
        AdminTextField(value = salaryRange, onValueChange = { salaryRange = it }, label = "Role Salary Range")
        AdminTextField(value = futureScope, onValueChange = { futureScope = it }, label = "Future Demand Outlook Scope")

        Button(
            onClick = {
                if (roleName.isBlank()) {
                    Toast.makeText(context, "Role name is required", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                isSubmitting = true
                val newRole = JobRoleModel(
                    roleName = roleName.trim(),
                    requiredSkills = requiredSkills.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                    certifications = certifications.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                    projects = projects.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                    roadmap = roadmap.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                    salaryRange = salaryRange.trim(),
                    futureScope = futureScope.trim(),
                    roleId = roleName.trim().lowercase().replace(" ", "_")
                )
                viewModel.adminAddJobRole(newRole) { success, err ->
                    isSubmitting = false
                    if (success) {
                        Toast.makeText(context, "Job Role added successfully!", Toast.LENGTH_SHORT).show()
                        roleName = ""; requiredSkills = ""; certifications = ""; projects = ""; roadmap = ""; salaryRange = ""; futureScope = ""
                    } else {
                        Toast.makeText(context, "Error: $err", Toast.LENGTH_LONG).show()
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = NeonBlue),
            modifier = Modifier.fillMaxWidth(),
            enabled = !isSubmitting
        ) {
            Text(if (isSubmitting) "Saving..." else "Add Job Role to Firestore", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(label, color = Color.Gray, fontSize = 12.sp) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth().height(48.dp),
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = NeonBlue,
            unfocusedBorderColor = Color.White.copy(alpha = 0.12f),
            containerColor = SurfaceDark
        )
    )
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement
    ) {
        content()
    }
}
