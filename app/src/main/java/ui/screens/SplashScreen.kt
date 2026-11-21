
package ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_mobile_app.R

private val SplashGreen = Color(0xFF176B52)
private val SplashGray = Color(0xFF718096)

@Composable
fun SplashScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier.weight(0.75f)
        )

        // LocalLens logo
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(SplashGreen),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = "LocalLens logo",
                tint = Color.White,
                modifier = Modifier.size(34.dp)
            )
        }

        Spacer(Modifier.height(18.dp))

        Text(
            text = "LocalLens",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            color = SplashGreen,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Discover Saudi Arabia",
            fontSize = 17.sp,
            color = SplashGray,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(38.dp))

        // Saudi heritage image
        Image(
            painter = painterResource(
                id = R.drawable.splash_destination
            ),
            contentDescription = "Saudi Arabian heritage",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .weight(3.1f)
                .clip(RoundedCornerShape(16.dp))
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = "أهلًا بك في السعودية",
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
            color = SplashGreen,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Every place has a story.",
            fontSize = 14.sp,
            color = SplashGray,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(34.dp))

        CircularProgressIndicator(
            modifier = Modifier.size(23.dp),
            color = SplashGreen,
            strokeWidth = 2.dp
        )

        Spacer(
            modifier = Modifier.weight(0.25f)
        )

        Spacer(Modifier.height(16.dp))
    }
}
