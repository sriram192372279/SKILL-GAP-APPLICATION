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
import com.sriram.skillgap.viewmodel.SkillViewModel
import com.sriram.skillgap.utils.JobData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, viewModel: SkillViewModel) {
    val user by viewModel.user.collectAsState()
    val resumeHistory by viewModel.resumeHistory.collectAsState()
    val userSkills by viewModel.userSkills.collectAsState()
    val analysis by viewModel.analysisResult.collectAsState()
    
    var showJobDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile Diagnostics", fontWeight = FontWeight.Bold, color = Color.White) },
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
                .background(Brush.verticalGradient(listOf(DeepDarkBlue, Color(0xFF101335))))
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
            }

            // User Identity Card
            item {
                PremiumGlassCard {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // User Avatar Initial Glow
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(listOf(NeonPurple, NeonBlue)))
                                .padding(3.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .background(SurfaceDark),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = (user?.name ?: "P").take(1).uppercase(),
                                    color = Color.White,
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = user?.name ?: "Anonymous Candidate",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = user?.email ?: "candidate@skillgap.ai",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Level & Streak Badges
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = NeonPurple.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, NeonPurple.copy(alpha = 0.4f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.MilitaryTech, contentDescription = null, tint = NeonPurple, modifier = Modifier.size(16.dp))
                                    Spacer(Modifier.width(4.dp))
                                    Text("LVL ${user?.level ?: 1}", color = NeonPurple, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }

                            Surface(
                                color = NeonBlue.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, NeonBlue.copy(alpha = 0.4f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = NeonBlue, modifier = Modifier.size(16.dp))
                                    Spacer(Modifier.width(4.dp))
                                    Text("${user?.learningStreak ?: 0} Day Streak", color = NeonBlue, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }

            // Career Target section
            item {
                Text("Career Target & Profile Metrics", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            item {
                PremiumGlassCard {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Dream Job Specialization", color = Color.Gray, fontSize = 11.sp)
                                Text(user?.dreamJob ?: "Not Configured", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                            IconButton(
                                onClick = { showJobDialog = true },
                                modifier = Modifier.background(NeonPurple.copy(alpha = 0.1f), CircleShape)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit Dream Job", tint = NeonPurple, modifier = Modifier.size(18.dp))
                            }
                        }

                        Divider(color = Color.White.copy(alpha = 0.05f))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("ATS Score", color = Color.Gray, fontSize = 11.sp)
                                Text("${user?.atsScore ?: 0}%", color = NeonBlue, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Technical Score", color = Color.Gray, fontSize = 11.sp)
                                Text("${user?.technicalScore ?: 0}%", color = NeonPurple, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Employability", color = Color.Gray, fontSize = 11.sp)
                                Text("${user?.employabilityScore ?: 0}%", color = HotPink, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            }
                        }
                    }
                }
            }

            // Resume Status
            item {
                Text("Resume Document Status", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            item {
                PremiumGlassCard {
                    val latestResume = resumeHistory.firstOrNull()
                    if (latestResume != null) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Description, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(28.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(latestResume.fileName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    val dateStr = remember(latestResume.timestamp) {
                                        val sdf = SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.getDefault())
                                        sdf.format(Date(latestResume.timestamp))
                                    }
                                    Text("Uploaded: $dateStr", color = Color.Gray, fontSize = 11.sp)
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { navController.navigate("resume_upload") },
                                colors = ButtonDefaults.buttonColors(containerColor = NeonPurple.copy(alpha = 0.1f)),
                                border = androidx.compose.foundation.BorderStroke(1.dp, NeonPurple),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Upload New Resume", color = NeonPurple, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.UploadFile, contentDescription = null, tint = Color.Gray.copy(0.5f), modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No resume active in your profile", color = Color.Gray, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { navController.navigate("resume_upload") },
                                colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Upload Resume (PDF)", color = DeepDarkBlue, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Extracted Skills list
            item {
                Text("Extracted Skills (${userSkills.size})", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            if (userSkills.isEmpty()) {
                item {
                    PremiumGlassCard {
                        Text(
                            text = "No skills found. Upload your resume to extract and index technical capabilities.",
                            color = Color.Gray,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                        )
                    }
                }
            } else {
                item {
                    PremiumGlassCard {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            mainAxisSpacing = 8.dp,
                            crossAxisSpacing = 8.dp
                        ) {
                            userSkills.forEach { skill ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.White.copy(0.05f))
                                        .border(1.dp, Color.White.copy(0.1f), RoundedCornerShape(8.dp))
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(skill.name, color = NeonBlue, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                }
                            }
                        }
                    }
                }
            }

            // Reset action
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { showResetDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = WarningYellow.copy(0.1f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, WarningYellow)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = WarningYellow)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Reset Resume & Diagnostics", color = WarningYellow, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }

            // Logout action
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        viewModel.logout()
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed.copy(0.15f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ErrorRed)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout", tint = ErrorRed)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Log Out Account", color = ErrorRed, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
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

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            containerColor = SurfaceDark,
            title = { Text("Reset Diagnostics?", color = Color.White, fontWeight = FontWeight.Bold) },
            text = { 
                Text(
                    "This will permanently clear your current resume history, extracted skills, and all compatibility scores so you can analyze a fresh resume.",
                    color = Color.Gray,
                    fontSize = 14.sp
                ) 
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.resetUserAnalysis()
                        showResetDialog = false
                    }
                ) {
                    Text("Reset", color = ErrorRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        )
    }
}

// Simple FlowRow helper for showing skills neatly wrapped
@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    mainAxisSpacing: androidx.compose.ui.unit.Dp = 0.dp,
    crossAxisSpacing: androidx.compose.ui.unit.Dp = 0.dp,
    content: @Composable () -> Unit
) {
    androidx.compose.ui.layout.Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints) }
        val layoutWidth = constraints.maxWidth
        var layoutHeight = 0
        
        val rows = mutableListOf<List<androidx.compose.ui.layout.Placeable>>()
        var currentRow = mutableListOf<androidx.compose.ui.layout.Placeable>()
        var currentRowWidth = 0
        
        val mainAxisSpacingPx = mainAxisSpacing.roundToPx()
        val crossAxisSpacingPx = crossAxisSpacing.roundToPx()
        
        placeables.forEach { placeable ->
            if (currentRowWidth + placeable.width + mainAxisSpacingPx > layoutWidth && currentRow.isNotEmpty()) {
                rows.add(currentRow)
                currentRow = mutableListOf()
                currentRowWidth = 0
            }
            currentRow.add(placeable)
            currentRowWidth += placeable.width + mainAxisSpacingPx
        }
        if (currentRow.isNotEmpty()) {
            rows.add(currentRow)
        }
        
        val rowHeights = rows.map { row -> row.maxOf { it.height } }
        layoutHeight = rowHeights.sum() + (rows.size - 1).coerceAtLeast(0) * crossAxisSpacingPx
        
        layout(layoutWidth, layoutHeight.coerceAtLeast(0)) {
            var y = 0
            rows.forEachIndexed { rowIndex, row ->
                var x = 0
                row.forEach { placeable ->
                    placeable.placeRelative(x, y)
                    x += placeable.width + mainAxisSpacingPx
                }
                y += rowHeights[rowIndex] + crossAxisSpacingPx
            }
        }
    }
}
