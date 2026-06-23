package com.sriram.skillgap.ui.screens

import androidx.compose.animation.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sriram.skillgap.ui.components.PremiumGlassCard
import com.sriram.skillgap.ui.theme.*
import com.sriram.skillgap.utils.AdvancedEngineData
import com.sriram.skillgap.viewmodel.SkillViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyHubScreen(navController: NavController, viewModel: SkillViewModel) {
    var selectedCompany by remember { mutableStateOf("Google") }
    val checkedItems by viewModel.checkedChecklistItems.collectAsState()

    val activeHub = remember(selectedCompany) {
        AdvancedEngineData.companyHubData[selectedCompany] 
            ?: AdvancedEngineData.companyHubData["Google"]!!
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🏢 Company Intelligence Hub", fontWeight = FontWeight.Bold, color = Color.White) },
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
                    text = "Select Enterprise Intelligence",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            // Company scroller
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val companiesList = listOf("Google", "Microsoft", "Amazon", "TCS")
                    companiesList.forEach { comp ->
                        val isSel = comp == selectedCompany
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSel) NeonPurple else SurfaceDark)
                                .clickable { selectedCompany = comp }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(comp, color = if (isSel) DeepDarkBlue else Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Overview card
            item {
                PremiumGlassCard {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("${selectedCompany} Overview", color = NeonPurple, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(activeHub["Overview"] as String, color = Color.White.copy(0.85f), fontSize = 13.sp)
                    }
                }
            }

            // Hiring specs card
            item {
                PremiumGlassCard {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Recruiting Criteria Profile", color = NeonBlue, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Interview Difficulty:", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.width(130.dp))
                            Text(activeHub["Difficulty"] as String, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Salary Package Offer:", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.width(130.dp))
                            Text(activeHub["Salary"] as String, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }

                        Row(verticalAlignment = Alignment.Top) {
                            Text("Eligibility Standards:", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.width(130.dp))
                            Text(activeHub["Eligibility"] as String, color = Color.White, fontSize = 12.sp, modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            // Culture & Stack
            item {
                PremiumGlassCard {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Workplace Culture & Tech Stack", color = WarningYellow, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        
                        Column {
                            Text("Work Culture", color = Color.Gray, fontSize = 11.sp)
                            Text(activeHub["Culture"] as String, color = Color.White.copy(0.85f), fontSize = 12.sp)
                        }

                        Column {
                            Text("Core Technologies Utilized", color = Color.Gray, fontSize = 11.sp)
                            Text(activeHub["Tech"] as String, color = NeonBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Hiring process steps
            item {
                Text("Structured Recruitment Stages", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            item {
                PremiumGlassCard {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(activeHub["HiringProcess"] as String, color = Color.White.copy(0.85f), fontSize = 13.sp)
                    }
                }
            }

            // Most Asked Questions
            item {
                Text("Most Frequently Asked Interview Questions", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            val questions = activeHub["Questions"] as List<String>
            items(questions) { q ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceDark,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(0.05f))
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
                        Icon(Icons.Default.HelpOutline, contentDescription = null, tint = NeonPurple, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(q, color = Color.White.copy(0.85f), fontSize = 13.sp)
                    }
                }
            }

            // Preparation checklist
            item {
                Text("AI Strategic Readiness Checklist", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            val checklist = activeHub["Checklist"] as List<String>
            items(checklist) { item ->
                val itemKey = "${selectedCompany}_$item"
                val isChecked = checkedItems.contains(itemKey)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.toggleChecklistItem(itemKey) }
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isChecked) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                        contentDescription = null,
                        tint = if (isChecked) NeonPurple else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = item,
                        color = if (isChecked) Color.Gray else Color.White.copy(0.8f),
                        fontSize = 13.sp,
                        textDecoration = if (isChecked) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}
