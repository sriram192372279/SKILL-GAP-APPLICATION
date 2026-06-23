package com.sriram.skillgap.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sriram.skillgap.ui.components.PremiumGlassCard
import com.sriram.skillgap.ui.components.ThreeDBarChart
import com.sriram.skillgap.ui.theme.*
import com.sriram.skillgap.viewmodel.SkillViewModel
import com.sriram.skillgap.viewmodel.CareerRiskItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisScreen(navController: NavController, viewModel: SkillViewModel) {
    val analysis by viewModel.analysisResult.collectAsState()
    val user by viewModel.user.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Intelligence Report", fontWeight = FontWeight.Bold, color = Color.White) },
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
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                MatchHeader(
                    percentage = analysis.matchPercentage,
                    atsScore = analysis.atsScore,
                    role = user?.dreamJob ?: "Target Role"
                )
            }

            item {
                Text("Multi-Factor Resume Analysis", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Deep local NLP parsing metrics tracking technical, design, and structural parameters.", color = Color.Gray, fontSize = 12.sp)
            }

            item {
                MultiFactorScoresGrid(
                    atsScore = analysis.atsScore,
                    readability = analysis.readabilityScore,
                    projects = analysis.projectStrengthScore,
                    density = analysis.keywordDensityScore
                )
            }

            if (analysis.careerRiskItems.isNotEmpty()) {
                item {
                    Text("Career Risk Analysis", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Outdated legacy tools found in industry vs modern replacements you should adopt.", color = Color.Gray, fontSize = 12.sp)
                }
                items(analysis.careerRiskItems) { risk ->
                    CareerRiskCard(risk)
                }
            }

            if (analysis.futureSkills.isNotEmpty()) {
                item {
                    Text("Future Skill Demand (Next 6-12 Months)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Pre-learn these predicted trending technologies to futureproof your application.", color = Color.Gray, fontSize = 12.sp)
                }
                item {
                    FutureSkillsCard(analysis.futureSkills)
                }
            }

            if (analysis.resumeIssues.isNotEmpty()) {
                item {
                    Text("Resume Audit Details", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                items(analysis.resumeIssues) { issue ->
                    IssueCard(issue.type, issue.description, issue.severity)
                }
            }

            item {
                Text("AI Recommendations", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            items(analysis.smartRecommendations) { rec ->
                RecommendationCard(rec.title, rec.provider, rec.type)
            }

            item {
                Text("Skills Gap Breakdown", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            item {
                PremiumGlassCard {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Interactive 3D Skill Balance",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            "Visual comparison of skills detected in your resume vs skills needed for the role.",
                            color = Color.Gray,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
                        )
                        
                        // 3D Isometric Bar Graph
                        ThreeDBarChart(
                            matchingCount = analysis.matchingSkills.size,
                            missingCount = analysis.missingSkills.size,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .padding(vertical = 8.dp)
                        )
                        
                        Spacer(Modifier.height(12.dp))
                        
                        // Legend Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .background(Color(0xFF00E5FF), RoundedCornerShape(2.dp))
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    "Skills You Have: ${analysis.matchingSkills.size}",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .background(Color(0xFFFF4081), RoundedCornerShape(2.dp))
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    "Skills To Prepare: ${analysis.missingSkills.size}",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                        
                        Spacer(Modifier.height(24.dp))
                        Divider(color = Color.White.copy(alpha = 0.1f))
                        Spacer(Modifier.height(16.dp))

                        Text("Skills You Have (Matching)", color = NeonBlue, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(Modifier.height(8.dp))
                        if (analysis.matchingSkills.isEmpty()) {
                            Text("No matching skills detected yet. Upload an updated resume or add skills in your target role profile.", color = Color.Gray, fontSize = 12.sp)
                        } else {
                            analysis.matchingSkills.forEach { skill ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NeonBlue, modifier = Modifier.size(16.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text(skill, color = Color.White.copy(0.85f), fontSize = 13.sp)
                                }
                            }
                        }

                        Spacer(Modifier.height(20.dp))

                        Text("Skills You Must Prepare (Missing)", color = HotPink, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(Modifier.height(8.dp))
                        if (analysis.missingSkills.isEmpty()) {
                            Text("✓ Congratulations! You have covered all required skills for this role.", color = Color(0xFF4CAF50), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        } else {
                            analysis.missingSkills.forEach { skill ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                ) {
                                    Icon(Icons.Default.Cancel, contentDescription = null, tint = HotPink, modifier = Modifier.size(16.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text(skill, color = Color.White.copy(0.85f), fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
            }

            item {
                PremiumGlassCard {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Schedule, contentDescription = null, tint = NeonPurple, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Dynamic Roadmap Scheduled", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                        Text(
                            text = "We have dynamically generated and scheduled a step-by-step roadmap to cover your missing skills. Click below to begin your prep schedule.",
                            color = Color.Gray,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            item {
                Button(
                    onClick = { navController.navigate("roadmap") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(bottom = 24.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonPurple)
                ) {
                    Text("Schedule & View Roadmap", color = DeepDarkBlue, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun MatchHeader(percentage: Int, atsScore: Int, role: String) {
    PremiumGlassCard {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(NeonPurple.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Verified, contentDescription = null, tint = NeonPurple, modifier = Modifier.size(20.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(role, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("Target Job Role Selected", color = Color.Gray, fontSize = 11.sp)
                }
            }
            
            Spacer(Modifier.height(20.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Compatibility Circle
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(72.dp)) {
                        CircularProgressIndicator(
                            progress = percentage / 100f,
                            modifier = Modifier.fillMaxSize(),
                            strokeWidth = 6.dp,
                            color = NeonPurple
                        )
                        Text("$percentage%", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("Compatibility", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Skills correlation", color = Color.Gray, fontSize = 10.sp)
                }
                
                // Vertical divider
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(80.dp)
                        .background(Color.White.copy(alpha = 0.1f))
                )
                
                // ATS Score Circle
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(72.dp)) {
                        CircularProgressIndicator(
                            progress = (atsScore.coerceIn(0, 100)) / 100f,
                            modifier = Modifier.fillMaxSize(),
                            strokeWidth = 6.dp,
                            color = NeonBlue
                        )
                        Text("$atsScore/100", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("ATS Resume Score", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Industry compliance", color = Color.Gray, fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
fun MultiFactorScoresGrid(atsScore: Int, readability: Int, projects: Int, density: Int) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ScoreIndicatorCard(
                title = "ATS Compliance",
                score = atsScore,
                color = NeonPurple,
                icon = Icons.Default.CheckCircle,
                desc = "Structure audit",
                modifier = Modifier.weight(1f)
            )
            ScoreIndicatorCard(
                title = "Readability Index",
                score = readability,
                color = NeonBlue,
                icon = Icons.Default.Subject,
                desc = "Formatting flow",
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ScoreIndicatorCard(
                title = "Project Strength",
                score = projects,
                color = HotPink,
                icon = Icons.Default.Construction,
                desc = "Action metrics",
                modifier = Modifier.weight(1f)
            )
            ScoreIndicatorCard(
                title = "Keyword Density",
                score = density,
                color = WarningYellow,
                icon = Icons.Default.Tag,
                desc = "Term optimization",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun ScoreIndicatorCard(
    title: String,
    score: Int,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    desc: String,
    modifier: Modifier = Modifier
) {
    PremiumGlassCard(modifier = modifier) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
                Text(
                    text = "$score/100",
                    color = color,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text(desc, color = Color.Gray, fontSize = 10.sp)
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = score / 100f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(2.dp)),
                color = color
            )
        }
    }
}

@Composable
fun CareerRiskCard(risk: CareerRiskItem) {
    val isCritical = risk.status.contains("REPLACE")
    val borderColor = if (isCritical) HotPink else NeonBlue
    val statusColor = if (isCritical) HotPink else NeonBlue
    
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = SurfaceDark.copy(alpha = 0.5f),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.Red.copy(0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = risk.outdatedSkill,
                            color = Color(0xFFFF5252),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                    
                    Spacer(Modifier.width(10.dp))
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = "Upgrade Pathway",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.Green.copy(0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = risk.suggestedReplacement,
                            color = Color(0xFF69F0AE),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "High-demand replacement pathway selected offline by local analyzer model.",
                    color = Color.Gray,
                    fontSize = 10.sp
                )
            }
            
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(statusColor.copy(alpha = 0.1f))
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                Text(
                    text = risk.status,
                    color = statusColor,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun FutureSkillsCard(skills: List<String>) {
    PremiumGlassCard {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = NeonPurple, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("Predicted Trending Skills", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
            Spacer(Modifier.height(12.dp))
            skills.forEach { skill ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Box(modifier = Modifier.size(6.dp).background(NeonBlue, CircleShape))
                    Spacer(Modifier.width(12.dp))
                    Text(skill, color = Color.White.copy(alpha = 0.9f), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
fun IssueCard(type: String, desc: String, severity: String) {
    val color = when(severity) {
        "High" -> HotPink
        "Medium" -> WarningYellow
        else -> NeonBlue
    }
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = 0.1f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.3f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Info, contentDescription = null, tint = color)
            Spacer(Modifier.width(12.dp))
            Column {
                Text(type, color = color, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(desc, color = Color.White.copy(0.8f), fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun RecommendationCard(title: String, provider: String, type: String) {
    val icon = when(type) {
        "Certification" -> Icons.Default.Verified
        "Course" -> Icons.Default.PlayCircle
        else -> Icons.Default.Lightbulb
    }
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = SurfaceDark,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(40.dp).background(NeonBlue.copy(0.1f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = NeonBlue)
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("$provider • $type", color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}
