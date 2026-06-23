package com.sriram.skillgap.ui.screens

import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sriram.skillgap.ui.components.BottomNavigationBar
import com.sriram.skillgap.ui.components.CircularScoreMeter
import com.sriram.skillgap.ui.components.PremiumGlassCard
import com.sriram.skillgap.ui.theme.*
import com.sriram.skillgap.utils.JobData
import com.sriram.skillgap.viewmodel.SkillViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController, viewModel: SkillViewModel) {
    val user by viewModel.user.collectAsState()
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
    val analysisComplete by viewModel.analysisComplete.collectAsState()
    val resumeHistory by viewModel.resumeHistory.collectAsState()
    val hasUploadedResume = resumeHistory.isNotEmpty()
    val context = LocalContext.current
    
    var showJobDialog by remember { mutableStateOf(false) }

    // If analysis finishes while we're on dashboard (e.g. from setDreamJob), navigate to results
    LaunchedEffect(analysisComplete) {
        if (analysisComplete) {
            viewModel.resetAnalysisComplete()
            navController.navigate("resume_results")
        }
    }

    if (isAnalyzing) {
        // Navigate to the dedicated upload progress screen
        LaunchedEffect(Unit) { navController.navigate("resume_upload") }
    } else {
        Scaffold(
            bottomBar = { BottomNavigationBar(navController) },
            containerColor = DeepDarkBlue
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(DeepDarkBlue, Color(0xFF12153B))
                        )
                    )
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            UserHeader(user?.name ?: "Professional", user?.level ?: 1)
                        }
                        IconButton(
                            onClick = {
                                navController.navigate("profile")
                            },
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .background(Color.White.copy(alpha = 0.05f), CircleShape)
                                .border(1.dp, Color.White.copy(alpha = 0.1f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile",
                                tint = NeonPurple
                            )
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        CircularScoreMeter(user?.technicalScore ?: 0, "Technical", color = NeonPurple)
                        CircularScoreMeter(user?.atsScore ?: 0, "ATS Score", color = NeonBlue)
                        CircularScoreMeter(user?.employabilityScore ?: 0, "Readiness", color = HotPink)
                    }
                }

                item {
                    Text("AI Command Center", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }

                item {
                    PremiumGlassCard {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { showJobDialog = true }.padding(16.dp)
                        ) {
                            Box(
                                modifier = Modifier.size(48.dp).background(NeonPurple.copy(alpha = 0.1f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Radar, contentDescription = null, tint = NeonPurple)
                            }
                            Spacer(Modifier.width(16.dp))
                            Column {
                                Text("Career Target", color = Color.White, fontWeight = FontWeight.Bold)
                                Text(user?.dreamJob ?: "Set Dream Job", color = NeonPurple, fontSize = 14.sp)
                            }
                            Spacer(Modifier.weight(1f))
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
                        }
                    }
                }

                item {
                    PremiumGlassCard {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { 
                                if (user?.dreamJob.isNullOrBlank()) {
                                    showJobDialog = true
                                } else {
                                    navController.navigate("resume_upload")
                                }
                            }.padding(16.dp)
                        ) {
                            Box(
                                modifier = Modifier.size(48.dp).background(NeonBlue.copy(alpha = 0.1f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Description, contentDescription = null, tint = NeonBlue)
                            }
                            Spacer(Modifier.width(16.dp))
                            Column {
                                Text("AI Resume Audit", color = Color.White, fontWeight = FontWeight.Bold)
                                Text(
                                    text = if (user?.dreamJob.isNullOrBlank()) "Set target career first" else "Upload PDF → instant skill gap report",
                                    color = if (user?.dreamJob.isNullOrBlank()) NeonPurple else Color.Gray,
                                    fontSize = 12.sp,
                                    fontWeight = if (user?.dreamJob.isNullOrBlank()) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                            Spacer(Modifier.weight(1f))
                            Icon(Icons.Default.ArrowForward, null, tint = NeonBlue)
                        }
                    }
                }

                item {
                    IntelligenceReportCard(
                        score = user?.employabilityScore ?: 0,
                        hasUploadedResume = hasUploadedResume,
                        onClick = { navController.navigate("analysis") }
                    )
                }

                item {
                    PlacementPrepQuickLink(
                        hasUploadedResume = hasUploadedResume,
                        onClick = { navController.navigate("prep") }
                    )
                }
                
                item {
                    Text("Advanced Career Engines (PATENTED)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }

                item {
                    AdvancedCareerEnginesGrid(navController, hasUploadedResume)
                }
                
                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }
    }

    if (showJobDialog) {
        JobSelectionDialog(
            onDismiss = { showJobDialog = false },
            onSelect = { 
                viewModel.setDreamJob(it)
                showJobDialog = false
            }
        )
    }
}

@Composable
fun UserHeader(name: String, level: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
            Text("Welcome back,", color = Color.Gray, fontSize = 14.sp)
            Text(name, color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        }
        Surface(
            color = NeonPurple.copy(alpha = 0.2f),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, NeonPurple)
        ) {
            Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.MilitaryTech, contentDescription = null, tint = NeonPurple, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("LVL $level", color = NeonPurple, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun IntelligenceReportCard(score: Int, hasUploadedResume: Boolean, onClick: () -> Unit) {
    val context = LocalContext.current
    val effectiveOnClick = {
        if (!hasUploadedResume) {
            Toast.makeText(context, "Please upload your resume first to unlock Placement Insights!", Toast.LENGTH_SHORT).show()
        } else {
            onClick()
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                if (hasUploadedResume) {
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF6200EE), Color(0xFFBB86FC))
                    )
                } else {
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF2C2D42), Color(0xFF3F4160))
                    )
                }
            )
            .clickable { effectiveOnClick() }
            .padding(24.dp)
    ) {
        Column {
            Text("Placement Insights", color = if (hasUploadedResume) Color.White else Color.White.copy(alpha = 0.6f), fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(
                if (hasUploadedResume) "Your employability is currently at $score%" else "Lock State: Upload resume to unlock analysis insights", 
                color = Color.White.copy(alpha = 0.6f), 
                fontSize = 13.sp
            )
            Spacer(Modifier.weight(1f))
            Button(
                onClick = effectiveOnClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (hasUploadedResume) Color.White else Color.White.copy(alpha = 0.1f),
                    contentColor = if (hasUploadedResume) Color.Black else Color.Gray
                ),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 24.dp)
            ) {
                if (hasUploadedResume) {
                    Text("View Full Report", color = Color.Black, fontWeight = FontWeight.Bold)
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                        Spacer(Modifier.width(6.dp))
                        Text("Locked", color = Color.Gray, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        Icon(
            if (hasUploadedResume) Icons.Default.AutoAwesome else Icons.Default.Lock, 
            contentDescription = null, 
            tint = Color.White.copy(alpha = 0.1f), 
            modifier = Modifier.size(100.dp).align(Alignment.BottomEnd).offset(x = 20.dp, y = 20.dp)
        )
    }
}

@Composable
fun PlacementPrepQuickLink(hasUploadedResume: Boolean, onClick: () -> Unit) {
    val context = LocalContext.current
    val effectiveOnClick = {
        if (!hasUploadedResume) {
            Toast.makeText(context, "Please upload your resume first to unlock Interview Preparation!", Toast.LENGTH_SHORT).show()
        } else {
            onClick()
        }
    }
    PremiumGlassCard(
        modifier = Modifier
            .clickable { effectiveOnClick() }
            .graphicsLayer { alpha = if (hasUploadedResume) 1f else 0.6f }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
            Icon(
                if (hasUploadedResume) Icons.Default.School else Icons.Default.Lock, 
                contentDescription = null, 
                tint = if (hasUploadedResume) NeonBlue else Color.Gray
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text("Interview Preparation", color = if (hasUploadedResume) Color.White else Color.White.copy(alpha = 0.6f), fontWeight = FontWeight.Bold)
                Text(
                    if (hasUploadedResume) "Aptitude, Tech Questions & HR Tips" else "Locked - Complete your resume upload", 
                    color = Color.Gray, 
                    fontSize = 12.sp
                )
            }
            Spacer(Modifier.weight(1f))
            Icon(
                if (hasUploadedResume) Icons.Default.PlayArrow else Icons.Default.Lock, 
                contentDescription = null, 
                tint = if (hasUploadedResume) NeonBlue else Color.Gray
            )
        }
    }
}

@Composable
fun LoadingAnalysisScreen() {
    val infiniteTransition = rememberInfiniteTransition(label = "loading")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Box(
        modifier = Modifier.fillMaxSize().background(DeepDarkBlue),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .border(4.dp, Brush.sweepGradient(listOf(NeonPurple, NeonBlue, NeonPurple)), CircleShape)
                    .graphicsLayer { rotationZ = rotation }
            )
            Spacer(Modifier.height(32.dp))
            Text(
                "AI Scanning Resume...",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.graphicsLayer { alpha = alphaAnim }
            )
            Text("Extracting Skills • Correlating Roles", color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@Composable
fun JobSelectionDialog(onDismiss: () -> Unit, onSelect: (String) -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceDark,
        title = { Text("Select Target Career", color = Color.White) },
        text = {
            LazyColumn {
                items(JobData.roles.size) { index ->
                    val role = JobData.roles[index]
                    ListItem(
                        headlineContent = { Text(role.title, color = Color.White) },
                        modifier = Modifier.clickable { onSelect(role.title) },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = NeonPurple) }
        }
    )
}

@Composable
fun AdvancedCareerEnginesGrid(navController: NavController, hasUploadedResume: Boolean) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            EngineCard(
                title = "Career Twin",
                desc = "Digital career simulation & forecasts",
                icon = Icons.Default.Psychology,
                color = NeonPurple,
                hasUploadedResume = hasUploadedResume,
                onClick = { navController.navigate("career_twin") },
                modifier = Modifier.weight(1f)
            )
            EngineCard(
                title = "Placement Odds",
                desc = "Company probability predictor",
                icon = Icons.Default.Radar,
                color = NeonBlue,
                hasUploadedResume = hasUploadedResume,
                onClick = { navController.navigate("placement_predictor") },
                modifier = Modifier.weight(1f)
            )
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            EngineCard(
                title = "Mock Interview",
                desc = "Simulate Tech, HR, Coding drills",
                icon = Icons.Default.Mic,
                color = HotPink,
                hasUploadedResume = hasUploadedResume,
                onClick = { navController.navigate("mock_interview") },
                modifier = Modifier.weight(1f)
            )
            EngineCard(
                title = "Success Simulator",
                desc = "Study hours & learning path roadmap",
                icon = Icons.Default.Timeline,
                color = WarningYellow,
                hasUploadedResume = hasUploadedResume,
                onClick = { navController.navigate("career_success_simulator") },
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            EngineCard(
                title = "Recruiter View",
                desc = "Strengths, weaknesses & ATS audit",
                icon = Icons.Default.AssignmentInd,
                color = GlowBlue,
                hasUploadedResume = hasUploadedResume,
                onClick = { navController.navigate("recruiter_view") },
                modifier = Modifier.weight(1f)
            )
            EngineCard(
                title = "Coding Prep",
                desc = "Java, SQL & DSA challenges",
                icon = Icons.Default.Code,
                color = NeonPurple,
                hasUploadedResume = hasUploadedResume,
                onClick = { navController.navigate("coding_prep") },
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            EngineCard(
                title = "Company Intel",
                desc = "Hiring process & checklist for 20+ firms",
                icon = Icons.Default.Business,
                color = NeonBlue,
                hasUploadedResume = hasUploadedResume,
                onClick = { navController.navigate("company_intelligence") },
                modifier = Modifier.weight(1f)
            )
            EngineCard(
                title = "Patent Center",
                desc = "5 patent-grade diagnostic scores",
                icon = Icons.Default.MilitaryTech,
                color = HotPink,
                hasUploadedResume = hasUploadedResume,
                onClick = { navController.navigate("patent_innovation") },
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            EngineCard(
                title = "Job Matcher",
                desc = "AI job matching & Opportunity Hub",
                icon = Icons.Default.Work,
                color = NeonPurple,
                hasUploadedResume = hasUploadedResume,
                onClick = { navController.navigate("job_matcher") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun EngineCard(
    title: String,
    desc: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    hasUploadedResume: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val effectiveOnClick = {
        if (!hasUploadedResume) {
            Toast.makeText(context, "Please upload your resume first to unlock $title!", Toast.LENGTH_SHORT).show()
        } else {
            onClick()
        }
    }
    PremiumGlassCard(
        modifier = modifier
            .clickable { effectiveOnClick() }
            .graphicsLayer { alpha = if (hasUploadedResume) 1f else 0.5f }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            if (hasUploadedResume) color.copy(alpha = 0.1f) else Color.Gray.copy(alpha = 0.1f), 
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icon, 
                        contentDescription = title, 
                        tint = if (hasUploadedResume) color else Color.Gray, 
                        modifier = Modifier.size(18.dp)
                    )
                }
                if (!hasUploadedResume) {
                    Icon(
                        Icons.Default.Lock, 
                        contentDescription = "Locked", 
                        tint = Color.Gray, 
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
            Text(
                title, 
                color = if (hasUploadedResume) Color.White else Color.White.copy(alpha = 0.6f), 
                fontWeight = FontWeight.Bold, 
                fontSize = 14.sp
            )
            Spacer(Modifier.height(2.dp))
            Text(
                if (hasUploadedResume) desc else "Locked: Upload resume", 
                color = Color.Gray, 
                fontSize = 10.sp, 
                lineHeight = 13.sp
            )
        }
    }
}
