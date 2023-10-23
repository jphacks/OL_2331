package com.example.test

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun DebugScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Button(onClick = { navController.navigate(Screen.InputCondition.route) }) {
            Text(text = "Navigate to InputCondition")
        }
        Button(onClick = { navController.navigate(Screen.WhenShouldYouWakeUpResult.route) }) {
            Text(text = "Navigate to WhenShouldYouWakeUpResult")
        }
        Button(onClick = { navController.navigate(Screen.SelectDestinationMap.route) }) {
            Text(text = "Navigate to SelectDestinationMap")
        }
        Button(onClick = { navController.navigate(Screen.Settings.route) }) {
            Text(text = "Navigate to Settings")
        }
    }
}