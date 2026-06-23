package com.sriram.skillgap.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sriram.skillgap.ui.components.PremiumGlassCard
import com.sriram.skillgap.ui.theme.*
import com.sriram.skillgap.utils.AdvancedEngineData
import com.sriram.skillgap.utils.RecruiterInsights
import com.sriram.skillgap.viewmodel.SkillViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecruiterViewScreen(navController: NavController, viewModel: SkillViewModel) {
    val user by viewModel.user.collectAsState()
    val analysis by viewModel.analysisResult.collectAsState()

    val recruiterInsights = remember(user, analysis) {
        val u = user ?: com.sriram.skillgap.data.model.User(id = "default_user", name = "Candidate", email = "candidate@skillgap.ai", dreamJob = "Software Engineer", technicalScore = 60, atsScore = 65, employabilityScore = 55)
        AdvancedEngineData.generateRecruiterDashboard(u, analysis.matchingSkills, analysis.missingSkills)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("👁️ Recruiter View Mode", fontWeight = FontWeight.Bold, color = Color.White) },
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
                .background(Brush.verticalGradient(listOf(DeepDarkBlue, Color(0xFF10133A))))
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Recruiter Evaluation Portal",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = "Observe how global recruiters view your resume parameters, technical alignment, and ATS integrity.",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            // Summary card (Recruiter recommendation)
            item {
                PremiumGlassCard {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Hiring Recommendation",
                            color = Color.Gray,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = recruiterInsights.hiringRecommendation.uppercase(),
                            color = when (recruiterInsights.hiringRecommendation) {
                                "Strong Hire" -> SuccessGreen
                                "Hire" -> NeonBlue
                                "Consider" -> WarningYellow
                                else -> HotPink
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = recruiterInsights.employabilityRating,
                            color = WarningYellow,
                            fontSize = 18.sp
                        )
                    }
                }
            }

            // Strength & ATS dials
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    PremiumGlassCard(modifier = Modifier.weight(1f)) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                            Text("Resume Strength", color = Color.Gray, fontSize = 11.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(75.dp)) {
                                CircularProgressIndicator(
                                    progress = recruiterInsights.resumeStrength / 100f,
                                    modifier = Modifier.fillMaxSize(),
                                    strokeWidth = 6.dp,
                                    color = NeonPurple
                                )
                                Text("${recruiterInsights.resumeStrength}%", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                    }

                    PremiumGlassCard(modifier = Modifier.weight(1f)) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                            Text("ATS Score", color = Color.Gray, fontSize = 11.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(75.dp)) {
                                CircularProgressIndicator(
                                    progress = recruiterInsights.atsScore / 100f,
                                    modifier = Modifier.fillMaxSize(),
                                    strokeWidth = 6.dp,
                                    color = NeonBlue
                                )
                                Text("${recruiterInsights.atsScore}%", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }

            // Recruiter insights block
            item {
                Text("Recruiter Insights", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            item {
                PremiumGlassCard {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.Insights, contentDescription = null, tint = NeonBlue)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = recruiterInsights.recruiterInsightsText,
                                color = Color.White.copy(0.9f),
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            // Resume Weaknesses list
            item {
                Text("Resume Audit Weaknesses", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            items(recruiterInsights.resumeWeakness) { weak ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.Red.copy(0.06f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, HotPink.copy(0.15f))
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = HotPink, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(weak, color = Color.White.copy(0.85f), fontSize = 12.sp)
                    }
                }
            }

            // Recruiter indicators
            item {
                PremiumGlassCard {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Employability Index Breakdown", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Interview Readiness", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.width(120.dp))
                            LinearProgressIndicator(
                                progress = recruiterInsights.interviewReadiness / 100f,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = NeonPurple
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("${recruiterInsights.interviewReadiness}%", color = NeonPurple, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Skills Compatibility", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.width(120.dp))
                            LinearProgressIndicator(
                                progress = recruiterInsights.atsScore / 100f,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = NeonBlue
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("${recruiterInsights.atsScore}%", color = NeonBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}
