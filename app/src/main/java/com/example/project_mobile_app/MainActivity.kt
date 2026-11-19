package com.example.project_mobile_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.project_mobile_app.ui.theme.ProjectmobileappTheme
import ui.screens.ExperienceDetailScreen
import ui.screens.MapScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjectmobileappTheme {
                var selectedPlaceIdForDetail by remember { mutableStateOf<String?>(null) }

                if (selectedPlaceIdForDetail != null) {
                    BackHandler {
                        selectedPlaceIdForDetail = null
                    }
                    ExperienceDetailScreen(
                        placeId = selectedPlaceIdForDetail!!,
                        onBackClick = { selectedPlaceIdForDetail = null }
                    )
                } else {
                    MapScreen(
                        onViewPlaceClick = { placeId ->
                            selectedPlaceIdForDetail = placeId
                        }
                    )
                }
            }
        }
    }
}