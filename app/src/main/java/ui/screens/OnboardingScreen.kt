
package ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_mobile_app.R

private val Green = Color(0xFF176B50)
private val DarkText = Color(0xFF25313B)
private val GrayText = Color(0xFF6E7890)
private val Beige = Color(0xFFF6F0E3)

@Composable
fun OnboardingScreen(
    onGetStarted: () -> Unit = {},
    onSkip: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "LocalLens",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Green
            )

            TextButton(onClick = onSkip) {
                Text(
                    text = "Skip",
                    color = GrayText
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Discover local gems,\nnear and far.",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            color = DarkText,
            lineHeight = 29.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(
                id = R.drawable.onboarding_destination
            ),
            contentDescription = "Saudi heritage destinations",
            modifier = Modifier
                .fillMaxWidth()
                .height(185.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Beige),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(22.dp))

        Text(
            text = "A closer look at Saudi Arabia",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText,
            lineHeight = 30.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Find meaningful places, make a day your own and connect with people who share your curiosity.",
            fontSize = 14.sp,
            color = GrayText,
            lineHeight = 21.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        FeatureItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Explore,
                    contentDescription = null,
                    tint = Green
                )
            },
            title = "Discover beyond the familiar",
            description = "Heritage, food, nature, arts and crafts."
        )

        Spacer(modifier = Modifier.height(18.dp))

        FeatureItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Route,
                    contentDescription = null,
                    tint = Green
                )
            },
            title = "Build a day that fits you",
            description = "Your interests, time and starting point."
        )

        Spacer(modifier = Modifier.height(18.dp))

        FeatureItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Groups,
                    contentDescription = null,
                    tint = Green
                )
            },
            title = "Explore with local communities",
            description = "Share tips and join memorable trips."
        )

        // Three onboarding dots removed

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = onGetStarted,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Green
            )
        ) {
            Text(
                text = "Get started",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun FeatureItem(
    icon: @Composable () -> Unit,
    title: String,
    description: String
) {
    Row(
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier.size(30.dp),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkText
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = description,
                fontSize = 12.sp,
                color = GrayText,
                lineHeight = 17.sp
            )
        }
    }
}
