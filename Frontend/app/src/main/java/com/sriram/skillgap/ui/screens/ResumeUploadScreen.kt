package com.sriram.skillgap.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sriram.skillgap.ui.theme.*
import com.sriram.skillgap.viewmodel.AnalysisStep
import com.sriram.skillgap.viewmodel.AnalysisStepStatus
import com.sriram.skillgap.viewmodel.SkillViewModel

@Composable
fun ResumeUploadScreen(navController: NavController, viewModel: SkillViewModel) {
    val context = LocalContext.current
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
    val analysisComplete by viewModel.analysisComplete.collectAsState()
    val steps by viewModel.analysisSteps.collectAsState()

    var fileSelected by remember { mutableStateOf(false) }
    var pickerOpened by remember { mutableStateOf(false) }

    val resumeLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            fileSelected = true
            viewModel.uploadResume(context, uri)
        } else {
            // User cancelled — go back
            navController.popBackStack()
        }
    }

    // Open picker once on first composition
    LaunchedEffect(Unit) {
        if (!pickerOpened) {
            pickerOpened = true
            resumeLauncher.launch("application/pdf")
        }
    }

    LaunchedEffect(analysisComplete) {
        if (analysisComplete) {
            viewModel.resetAnalysisComplete()
            navController.navigate("resume_results") {
                popUpTo("resume_upload") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(Color(0xFF0A0D1F), DeepDarkBlue, Color(0xFF12153B)))
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            // Animated scanner ring
            ScannerRing(isAnalyzing)
            Spacer(Modifier.height(32.dp))

            Text(
                text = when {
                    !fileSelected -> "Select Your Resume PDF"
                    isAnalyzing   -> "Analysing Your Resume…"
                    else          -> "Analysis Complete!"
                },
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = when {
                    !fileSelected -> "Opening file picker…"
                    isAnalyzing   -> "Real-time AI skill gap detection in progress"
                    else          -> "Redirecting to your results…"
                },
                color = Color.Gray,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            // Show steps only while analysing or done
            if (fileSelected) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.04f))
                        .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(20.dp))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    steps.forEach { step -> StepRow(step) }
                }
            }
        }
    }
}

@Composable
private fun ScannerRing(spinning: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "ring")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(1800, easing = LinearEasing)),
        label = "rotation"
    )
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.92f, targetValue = 1.08f,
        animationSpec = infiniteRepeatable(tween(900, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulse"
    )

    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(110.dp)) {
        // Outer rotating ring
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { if (spinning) rotationZ = rotation }
                .border(
                    3.dp,
                    Brush.sweepGradient(listOf(NeonPurple, Color.Transparent, NeonBlue, Color.Transparent, NeonPurple)),
                    CircleShape
                )
        )
        // Inner pulsing icon
        Box(
            modifier = Modifier
                .size(70.dp)
                .scale(if (spinning) pulse else 1f)
                .background(NeonPurple.copy(alpha = 0.12f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                if (spinning) Icons.Default.DocumentScanner else Icons.Default.CheckCircle,
                contentDescription = null,
                tint = NeonPurple,
                modifier = Modifier.size(34.dp)
            )
        }
    }
}

@Composable
private fun StepRow(step: AnalysisStep) {
    val isActive = step.status == AnalysisStepStatus.IN_PROGRESS
    val isDone   = step.status == AnalysisStepStatus.DONE
    val isError  = step.status == AnalysisStepStatus.ERROR

    val infiniteTransition = rememberInfiniteTransition(label = "step_${step.id}")
    val shimmer by infiniteTransition.animateFloat(
        initialValue = 0.5f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(600, easing = LinearEasing), RepeatMode.Reverse),
        label = "shimmer"
    )

    val rowAlpha = if (step.status == AnalysisStepStatus.PENDING) 0.35f else 1f
    val accentColor = when {
        isError -> Color(0xFFFF5252)
        isDone  -> Color(0xFF69F0AE)
        isActive-> NeonBlue
        else    -> Color.Gray
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { alpha = rowAlpha }
    ) {
        // Status icon
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(accentColor.copy(alpha = 0.12f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            when {
                isActive -> CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp,
                    color = NeonBlue,
                    trackColor = Color.Transparent
                )
                isDone -> Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF69F0AE), modifier = Modifier.size(20.dp))
                isError -> Icon(Icons.Default.Cancel, null, tint = Color(0xFFFF5252), modifier = Modifier.size(20.dp))
                else   -> Icon(Icons.Default.Circle, null, tint = Color.Gray.copy(0.3f), modifier = Modifier.size(12.dp))
            }
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                step.label,
                color = if (isActive) Color.White else if (isDone) Color(0xFF69F0AE) else Color.Gray,
                fontSize = 13.sp,
                fontWeight = if (isActive || isDone) FontWeight.SemiBold else FontWeight.Normal,
                modifier = if (isActive) Modifier.graphicsLayer { alpha = shimmer } else Modifier
            )
            Text(step.detail, color = Color.Gray, fontSize = 10.sp, lineHeight = 13.sp)
        }

        if (isDone) {
            Text("✓", color = Color(0xFF69F0AE), fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// RESULTS SCREEN — shown right after analysis completes
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumeResultsScreen(navController: NavController, viewModel: SkillViewModel) {
    val result by viewModel.analysisResult.collectAsState()
    val user by viewModel.user.collectAsState()

    Scaffold(
        containerColor = DeepDarkBlue,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Skill Gap Report", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 18.sp)
                        Text(user?.dreamJob ?: "", color = NeonPurple, fontSize = 12.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("dashboard") {
                        popUpTo("dashboard") { inclusive = false }
                    }}) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("analysis") }) {
                        Icon(Icons.Default.OpenInFull, null, tint = NeonPurple)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepDarkBlue)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(Modifier.height(4.dp)) }

            // ── Score Banner ──────────────────────────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.linearGradient(listOf(Color(0xFF1A0533), Color(0xFF0D1A3A), Color(0xFF001A33)))
                        )
                        .border(1.dp, NeonPurple.copy(alpha = 0.25f), RoundedCornerShape(20.dp))
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ScorePill("Match", result.matchPercentage, NeonPurple, "%")
                    VertDivider()
                    ScorePill("ATS", result.atsScore, NeonBlue, "/100")
                    VertDivider()
                    ScorePill("Readability", result.readabilityScore, HotPink, "/100")
                }
            }

            // ── Missing Skills (THE KEY FEATURE) ─────────────────────────────
            item {
                SectionHeader(
                    icon = Icons.Default.Warning,
                    title = "Skills You Are Missing",
                    subtitle = "${result.missingSkills.size} gaps found for ${user?.dreamJob ?: "your role"}",
                    color = HotPink
                )
            }

            if (result.missingSkills.isEmpty()) {
                item {
                    StatusCard(
                        message = "🎉 You have ALL required skills for this role!",
                        color = Color(0xFF4CAF50)
                    )
                }
            } else {
                items(result.missingSkills) { skill ->
                    MissingSkillCard(skill)
                }
            }

            // ── Skills You Have ────────────────────────────────────────────────
            item {
                SectionHeader(
                    icon = Icons.Default.CheckCircle,
                    title = "Skills You Already Have",
                    subtitle = "${result.matchingSkills.size} skills detected in your resume",
                    color = Color(0xFF69F0AE)
                )
            }

            if (result.matchingSkills.isEmpty()) {
                item { StatusCard("No matching skills detected. Set your target job or upload a stronger resume.", Color.Gray) }
            } else {
                item {
                    SkillChipsWrap(result.matchingSkills, Color(0xFF69F0AE))
                }
            }

            // ── What To Learn ──────────────────────────────────────────────────
            if (result.missingSkills.isNotEmpty()) {
                item {
                    SectionHeader(
                        icon = Icons.Default.School,
                        title = "What You Need To Learn",
                        subtitle = "Personalised study plan for your ${result.missingSkills.size} missing skills",
                        color = NeonBlue
                    )
                }
                items(result.smartRecommendations) { rec ->
                    LearnCard(rec.title, rec.provider)
                }
                // Roadmap phases
                items(result.roadmapPhases) { phase ->
                    RoadmapPhaseCard(phase.title, phase.items, phase.isCompleted, phase.difficulty)
                }
            }

            // ── Future Skills ──────────────────────────────────────────────────
            if (result.futureSkills.isNotEmpty()) {
                item {
                    SectionHeader(
                        icon = Icons.Default.AutoAwesome,
                        title = "Future Skills To Pre-Learn",
                        subtitle = "Trending tech in the next 6–12 months",
                        color = WarningYellow
                    )
                }
                item { SkillChipsWrap(result.futureSkills, WarningYellow) }
            }

            // ── Resume Issues ──────────────────────────────────────────────────
            if (result.resumeIssues.isNotEmpty()) {
                item {
                    SectionHeader(
                        icon = Icons.Default.RateReview,
                        title = "Resume Issues Found",
                        subtitle = "${result.resumeIssues.size} improvements recommended",
                        color = WarningYellow
                    )
                }
                items(result.resumeIssues) { issue ->
                    IssueCard(issue.type, issue.description, issue.severity)
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = { navController.navigate("job_matcher") },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)
                    ) {
                        Icon(Icons.Default.Work, null, tint = DeepDarkBlue, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Find Matching Jobs", fontWeight = FontWeight.Bold, color = DeepDarkBlue)
                    }
                    Button(
                        onClick = { navController.navigate("analysis") },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonPurple)
                    ) {
                        Icon(Icons.Default.BarChart, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("View Full Intelligence Report", fontWeight = FontWeight.Bold, color = DeepDarkBlue)
                    }
                    OutlinedButton(
                        onClick = { navController.navigate("roadmap") },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NeonBlue)
                    ) {
                        Icon(Icons.Default.Map, null, tint = NeonBlue, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Open Learning Roadmap", fontWeight = FontWeight.Bold, color = NeonBlue)
                    }
                }
            }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

// ── Sub-composables ──────────────────────────────────────────────────────────

@Composable
private fun ScorePill(label: String, value: Int, color: Color, suffix: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("$value$suffix", color = color, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
        Text(label, color = Color.Gray, fontSize = 11.sp)
        Spacer(Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = value / 100f,
            modifier = Modifier.width(60.dp).height(3.dp).clip(RoundedCornerShape(2.dp)),
            color = color,
            trackColor = color.copy(alpha = 0.15f)
        )
    }
}

@Composable
private fun VertDivider() {
    Box(modifier = Modifier.width(1.dp).height(56.dp).background(Color.White.copy(alpha = 0.08f)))
}

@Composable
private fun SectionHeader(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    color: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
        Box(
            modifier = Modifier.size(34.dp).background(color.copy(alpha = 0.12f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = color, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(10.dp))
        Column {
            Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(subtitle, color = Color.Gray, fontSize = 11.sp)
        }
    }
}

@Composable
private fun StatusCard(message: String, color: Color) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = color.copy(alpha = 0.08f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.25f))
    ) {
        Text(message, color = color, modifier = Modifier.padding(16.dp), fontWeight = FontWeight.Medium, fontSize = 13.sp)
    }
}

@Composable
private fun MissingSkillCard(skill: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFF1A0A0A),
        border = androidx.compose.foundation.BorderStroke(1.dp, HotPink.copy(alpha = 0.25f))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Cancel, null, tint = HotPink, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(skill, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Text("Missing from resume • Must learn", color = Color.Gray, fontSize = 10.sp)
                }
            }
            Surface(
                color = HotPink.copy(alpha = 0.15f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "LEARN",
                    color = HotPink,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun SkillChipsWrap(skills: List<String>, color: Color) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = 0.05f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.15f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            val rows = skills.chunked(3)
            rows.forEach { row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    row.forEach { skill ->
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = color.copy(alpha = 0.12f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.3f))
                        ) {
                            Text(
                                skill,
                                color = color,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LearnCard(title: String, provider: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFF0A1020),
        border = androidx.compose.foundation.BorderStroke(1.dp, NeonBlue.copy(alpha = 0.2f))
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(38.dp).background(NeonBlue.copy(alpha = 0.12f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.PlayCircle, null, tint = NeonBlue, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(title, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                Text("via $provider", color = NeonBlue, fontSize = 11.sp)
            }
        }
    }
}

@Composable
private fun RoadmapPhaseCard(title: String, items: List<String>, isCompleted: Boolean, difficulty: String) {
    val accent = if (isCompleted) Color(0xFF69F0AE) else NeonPurple
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = if (isCompleted) Color(0xFF0A1A10) else Color(0xFF0F0A1A),
        border = androidx.compose.foundation.BorderStroke(1.dp, accent.copy(alpha = 0.25f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(title, color = accent, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                if (isCompleted) {
                    Text("✓ COMPLETE", color = Color(0xFF69F0AE), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }
            Text(difficulty, color = Color.Gray, fontSize = 10.sp, modifier = Modifier.padding(bottom = 10.dp, top = 2.dp))
            items.forEach { item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 3.dp)
                ) {
                    Box(modifier = Modifier.size(6.dp).background(accent.copy(alpha = 0.6f), CircleShape))
                    Spacer(Modifier.width(10.dp))
                    Text(item, color = if (isCompleted) Color.Gray else Color.White.copy(0.85f), fontSize = 12.sp)
                }
            }
        }
    }
}
