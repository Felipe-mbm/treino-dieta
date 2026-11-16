package com.felipe.treinodieta

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.felipe.treinodieta.ui.TreinoApp
import com.felipe.treinodieta.ui.theme.TreinoDietaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TreinoDietaTheme {
                TreinoApp()
            }
        }
    }
}
