package com.sriram.skillgap.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sriram.skillgap.ui.components.PremiumGlassCard
import com.sriram.skillgap.ui.theme.*
import com.sriram.skillgap.utils.AdvancedEngineData
import com.sriram.skillgap.viewmodel.SkillViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatentInnovationScreen(navController: NavController, viewModel: SkillViewModel) {
    val user by viewModel.user.collectAsState()
    val analysis by viewModel.analysisResult.collectAsState()
    val solvedAptitude by viewModel.solvedAptitude.collectAsState()
    val solvedCoding by viewModel.solvedCoding.collectAsState()

    val patentScores = remember(user, analysis, solvedAptitude, solvedCoding) {
        val u = user ?: com.sriram.skillgap.data.model.User(id = "default_user", name = "Candidate", email = "candidate@skillgap.ai", dreamJob = "Software Engineer", technicalScore = 60, atsScore = 65, employabilityScore = 55)
        AdvancedEngineData.calculatePatentMetrics(
            user = u,
            matchingSkills = analysis.matchingSkills,
            missingSkills = analysis.missingSkills,
            solvedAptitude = solvedAptitude,
            solvedCoding = solvedCoding
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🎖️ Patent Innovation Center", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepDarkBlue)
            )
        },
        containerColor = DeepDarkBlue
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Brush.verticalGradient(listOf(DeepDarkBlue, Color(0xFF0F153B))))
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Career Intelligence Diagnostics",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = "Five core patent-grade metrics auditing career risks, learning speeds, and recruiter acceptance algorithms.",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            // 1. Career Risk
            item {
                PatentMetricCard(
                    title = "Career Risk Index",
                    score = patentScores.careerRiskScore,
                    description = "Measures standard placement vulnerabilities based on active technology mismatch. Lower is better.",
                    color = if (patentScores.careerRiskScore > 60) HotPink else SuccessGreen,
                    icon = Icons.Default.Warning
                )
            }

            // 2. Future Skill Demand
            item {
                PatentMetricCard(
                    title = "Future Skill Demand Score",
                    score = patentScores.futureSkillDemandScore,
                    description = "Measures correlations between resume technologies and predicted market trends over the next 12 months.",
                    color = NeonBlue,
                    icon = Icons.Default.TrendingUp
                )
            }

            // 3. Adaptive Learning
            item {
                PatentMetricCard(
                    title = "Adaptive Learning Quotient",
                    score = patentScores.adaptiveLearningScore,
                    description = "Calculates active student knowledge acquisition pace based on coding problems solved and aptitude metrics completed.",
                    color = WarningYellow,
                    icon = Icons.Default.Speed
                )
            }

            // 4. Placement Probability
            item {
                PatentMetricCard(
                    title = "Placement Probability Factor",
                    score = patentScores.placementProbabilityScore,
                    description = "Composite score assessing correlation across technical levels, DSA expertise, and company-specific requirements.",
                    color = NeonPurple,
                    icon = Icons.Default.Radar
                )
            }

            // 5. Recruiter Acceptance
            item {
                PatentMetricCard(
                    title = "Recruiter Acceptance Rating",
                    score = patentScores.recruiterAcceptanceScore,
                    description = "Simulates resume structural soundness, formatting integrity, and keywords parsing index under recruiter scanners.",
                    color = GlowBlue,
                    icon = Icons.Default.AssignmentInd
                )
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun PatentMetricCard(
    title: String,
    score: Int,
    description: String,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    var animationPlayed by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(
        targetValue = if (animationPlayed) score / 100f else 0f,
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        label = "patentMetricProg"
    )
    val animatedScoreText by animateFloatAsState(
        targetValue = if (animationPlayed) score.toFloat() else 0f,
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        label = "patentMetricText"
    )
    LaunchedEffect(key1 = score) {
        animationPlayed = true
    }

    PremiumGlassCard {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(color.copy(0.1f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
                Text("${animatedScoreText.toInt()}%", color = color, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(description, color = Color.Gray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = animatedProgress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = color,
                trackColor = Color.White.copy(0.05f)
            )
        }
    }
}
