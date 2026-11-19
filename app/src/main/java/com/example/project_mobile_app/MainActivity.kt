
package com.example.project_mobile_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.project_mobile_app.navigation.LocalLensNavHost
import com.example.project_mobile_app.ui.theme.ProjectmobileappTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            ProjectmobileappTheme {
                LocalLensNavHost()
            }
        }
    }
}
