package com.example.expansetracker

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.expansetracker.ui.theme.ExpanseTrackerTheme
import com.example.expansetracker.ui.theme.Zinca
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExpanseTrackerTheme {
                rememberSystemUiController().setStatusBarColor(color = Zinca, darkIcons = false)
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                      NavHostScreen()
                }
            }
        }
    }
}

