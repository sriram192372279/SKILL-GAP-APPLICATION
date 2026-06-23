package com.sriram.skillgap.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sriram.skillgap.ui.components.PremiumGlassCard
import com.sriram.skillgap.ui.theme.*
import com.sriram.skillgap.utils.AdvancedEngineData
import com.sriram.skillgap.utils.CompanyReadiness
import com.sriram.skillgap.viewmodel.SkillViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacementPredictorScreen(navController: NavController, viewModel: SkillViewModel) {
    val user by viewModel.user.collectAsState()
    val analysis by viewModel.analysisResult.collectAsState()
    val solvedAptitude by viewModel.solvedAptitude.collectAsState()
    val solvedCoding by viewModel.solvedCoding.collectAsState() // mapping coding prep progress

    var selectedCompany by remember { mutableStateOf("Google") }
    var interviewConfidence by remember { mutableStateOf(75) } // dynamic confidence controller

    // Recalculate probabilities on user update
    val readinessList = remember(user, analysis, solvedAptitude, solvedCoding, interviewConfidence) {
        val u = user ?: com.sriram.skillgap.data.model.User(id = "default_user", name = "Candidate", email = "candidate@skillgap.ai", dreamJob = "Software Engineer", technicalScore = 60, atsScore = 65, employabilityScore = 55)
        AdvancedEngineData.calculatePlacementProbability(
            user = u,
            matchingSkills = analysis.matchingSkills,
            missingSkills = analysis.missingSkills,
            solvedAptitude = solvedAptitude,
            solvedCoding = solvedCoding,
            interviewConfidence = interviewConfidence
        )
    }

    val activeReadiness = readinessList.find { it.companyName.equals(selectedCompany, ignoreCase = true) }
        ?: readinessList.first()

    var animationPlayed by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = activeReadiness) {
        animationPlayed = false
        // small delay to reset/retrigger animation
        kotlinx.coroutines.delay(10)
        animationPlayed = true
    }
    
    val animatedReadinessProgress by animateFloatAsState(
        targetValue = if (animationPlayed) activeReadiness.readinessPercentage / 100f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "readinessProg"
    )
    val animatedReadinessScore by animateFloatAsState(
        targetValue = if (animationPlayed) activeReadiness.readinessPercentage.toFloat() else 0f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "readinessText"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Placement Probability Predictor", fontWeight = FontWeight.Bold, color = Color.White) },
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
                .background(Brush.verticalGradient(listOf(DeepDarkBlue, Color(0xFF101435))))
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Select Company Profile",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "Compare your readiness matrix against recruiters' benchmarks.",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            // Company horizontal scroller
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceDark)
                            .padding(8.dp)
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            item {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    AdvancedEngineData.companiesList.forEach { comp ->
                                        val isSel = comp == selectedCompany
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(if (isSel) NeonPurple else GlassWhite)
                                                .clickable { selectedCompany = comp }
                                                .padding(horizontal = 14.dp, vertical = 6.dp)
                                        ) {
                                            Text(
                                                text = comp,
                                                color = if (isSel) DeepDarkBlue else Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Probability gauge ring
            item {
                PremiumGlassCard {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${activeReadiness.companyName} Selection Target",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(140.dp)
                        ) {
                            CircularProgressIndicator(
                                progress = animatedReadinessProgress,
                                modifier = Modifier.fillMaxSize(),
                                strokeWidth = 10.dp,
                                color = if (activeReadiness.readinessPercentage >= 75) SuccessGreen else if (activeReadiness.readinessPercentage >= 50) WarningYellow else HotPink
                            )
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${animatedReadinessScore.toInt()}%",
                                    color = Color.White,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Probability",
                                    color = Color.Gray,
                                    fontSize = 11.sp
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (activeReadiness.readinessPercentage >= 75) "Highly Favorable Candidate" else if (activeReadiness.readinessPercentage >= 50) "Competitive Match" else "Unsatisfied Requirements",
                            color = if (activeReadiness.readinessPercentage >= 75) SuccessGreen else if (activeReadiness.readinessPercentage >= 50) WarningYellow else HotPink,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // dynamic confidence controller slider
            item {
                PremiumGlassCard {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Interview Confidence Factor", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("$interviewConfidence%", color = NeonPurple, fontWeight = FontWeight.Bold)
                        }
                        Text("Simulate how communication and interview posture affects probability.", color = Color.Gray, fontSize = 11.sp)
                        Slider(
                            value = interviewConfidence.toFloat(),
                            onValueChange = { interviewConfidence = it.toInt() },
                            valueRange = 30f..100f,
                            colors = SliderDefaults.colors(
                                thumbColor = NeonPurple,
                                activeTrackColor = NeonPurple,
                                inactiveTrackColor = Color.Gray.copy(0.3f)
                            )
                        )
                    }
                }
            }

            // Readiness metrics grid
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ScoreBarCard("Technical", activeReadiness.technicalScore, NeonPurple, Modifier.weight(1f))
                    ScoreBarCard("Coding", activeReadiness.codingScore, NeonBlue, Modifier.weight(1f))
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ScoreBarCard("Interview Prep", activeReadiness.interviewScore, HotPink, Modifier.weight(1f))
                    ScoreBarCard("Communications", activeReadiness.communicationScore, WarningYellow, Modifier.weight(1f))
                }
            }

            // Missing Requirements
            item {
                Text("Missing Requirements", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            if (activeReadiness.missingRequirements.isEmpty()) {
                item {
                    PremiumGlassCard {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Verified, contentDescription = null, tint = SuccessGreen)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("No missing structural requirements! You meet this company's baseline tech specs.", color = Color.White, fontSize = 13.sp)
                        }
                    }
                }
            } else {
                items(activeReadiness.missingRequirements) { req ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = Color.Red.copy(0.08f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, HotPink.copy(0.2f))
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Cancel, contentDescription = null, tint = HotPink, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Missing technology: $req", color = Color.White.copy(0.9f), fontSize = 13.sp)
                        }
                    }
                }
            }

            // Improvement Recommendations
            item {
                Text("Strategic AI Recommendations", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            items(activeReadiness.improvementRecommendations) { rec ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceDark,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(0.05f))
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
                        Icon(Icons.Default.Lightbulb, contentDescription = null, tint = WarningYellow, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(rec, color = Color.White.copy(0.85f), fontSize = 13.sp)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ScoreBarCard(title: String, score: Int, color: Color, modifier: Modifier = Modifier) {
    var animationPlayed by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(
        targetValue = if (animationPlayed) score / 100f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "scoreBarProg"
    )
    LaunchedEffect(key1 = score) {
        animationPlayed = true
    }

    PremiumGlassCard(modifier = modifier) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text("$score/100", color = color, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
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
