package com.sriram.skillgap.ui.components

import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sriram.skillgap.ui.theme.NeonPurple
import com.sriram.skillgap.ui.theme.SurfaceDark
import com.sriram.skillgap.viewmodel.SkillViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun BottomNavigationBar(
    navController: NavController,
    viewModel: SkillViewModel = viewModel()
) {
    val context = LocalContext.current
    val resumeHistory by viewModel.resumeHistory.collectAsState()
    val hasUploadedResume = resumeHistory.isNotEmpty()

    val items = listOf(
        NavigationItem("Home", "dashboard", Icons.Default.Home),
        NavigationItem("Insights", "analysis", Icons.Default.AutoAwesome),
        NavigationItem("Prep", "prep", Icons.Default.School),
        NavigationItem("Assistant", "chat", Icons.Default.ChatBubble),
        NavigationItem("Rank", "analytics", Icons.Default.MilitaryTech)
    )

    NavigationBar(
        containerColor = SurfaceDark,
        tonalElevation = 8.dp
    ) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route

        items.forEach { item ->
            val isLocked = !hasUploadedResume && item.route != "dashboard"
            NavigationBarItem(
                icon = { 
                    Icon(
                        imageVector = if (isLocked) Icons.Default.Lock else item.icon, 
                        contentDescription = item.title
                    ) 
                },
                label = { Text(item.title, fontSize = 10.sp) },
                selected = currentRoute == item.route,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = NeonPurple,
                    selectedTextColor = NeonPurple,
                    unselectedIconColor = if (isLocked) Color.Gray.copy(alpha = 0.4f) else Color.Gray,
                    unselectedTextColor = if (isLocked) Color.Gray.copy(alpha = 0.4f) else Color.Gray,
                    indicatorColor = NeonPurple.copy(alpha = 0.1f)
                ),
                onClick = {
                    if (currentRoute != item.route) {
                        if (isLocked) {
                            Toast.makeText(
                                context,
                                "Upload your resume first to unlock the ${item.title} tab!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            navController.navigate(item.route) {
                                popUpTo("dashboard") { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                }
            )
        }
    }
}

data class NavigationItem(val title: String, val route: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)
