package com.elitecode.taskplan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.elitecode.taskplan.components.createNotificationChannel
import com.elitecode.taskplan.navigation.NavigationApp
import com.elitecode.taskplan.ui.theme.TaskPlanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel(this) // Crear el canal de notificaciones
        setContent {
            NavigationApp()
        }
    }
}


