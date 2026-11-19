package ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExperienceDetailScreen(
    placeId: String,
    onBackClick: () -> Unit
) {
    val place = remember(placeId) {
        RiyadhDemoData.places.find { it.id == placeId } ?: RiyadhDemoData.places.first()
    }
    var isSaved by remember { mutableStateOf(true) }
    var checkedIn by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = ScreenBg,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ScreenBg),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = PrimaryGreen
                        )
                    }
                },
                title = {
                    Text(
                        text = "Experience",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                },
                actions = {
                    IconButton(onClick = { isSaved = !isSaved }) {
                        Icon(
                            imageVector = if (isSaved) Icons.Default.BookmarkAdded else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Save",
                            tint = if (isSaved) PrimaryGreen else MutedText
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Image(
                painter = painterResource(id = place.imageRes),
                contentDescription = place.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .clip(RoundedCornerShape(18.dp))
            )

            Spacer(modifier = Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(LightPillBg)
                    .padding(horizontal = 12.dp, vertical = 5.dp)
            ) {
                Text(
                    text = place.categoryBadge,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF5A5246)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = place.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.StarOutline, contentDescription = null, tint = Color(0xFFC89B3C), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("${place.rating} · ${place.reviewCount} reviews", fontSize = 13.sp, color = DarkText)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.LocationOn, contentDescription = null, tint = PrimaryGreen, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(place.locationText, fontSize = 12.sp, color = MutedText)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.Schedule, contentDescription = null, tint = PrimaryGreen, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(place.durationText, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = DarkText)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(LightPillBg)
                                .padding(horizontal = 12.dp, vertical = 5.dp)
                        ) {
                            Text(place.entryType, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF5A5246))
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(place.hoursText, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = DarkText)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Illustrative only. Confirm opening times and entry details before visiting.",
                        fontSize = 11.sp,
                        color = MutedText,
                        lineHeight = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(place.storyTitle, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkText)
            Spacer(modifier = Modifier.height(6.dp))
            Text(place.storyDescription, fontSize = 13.sp, color = MutedText, lineHeight = 19.sp)

            Spacer(modifier = Modifier.height(18.dp))

            Text("What to expect", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkText)
            Spacer(modifier = Modifier.height(6.dp))
            Text(place.expectDescription, fontSize = 13.sp, color = MutedText, lineHeight = 19.sp)

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(LightPillBg.copy(alpha = 0.7f))
                    .padding(12.dp)
            ) {
                Icon(Icons.Outlined.WbSunny, contentDescription = null, tint = PrimaryGreen, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text(place.tipText, fontSize = 12.sp, color = Color(0xFF5A5246), lineHeight = 17.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Traveler notes", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkText)
                Text("Write review", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = PrimaryGreen)
            }

            Spacer(modifier = Modifier.height(10.dp))

            val review = place.review
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(LightPillBg)
                        ) {
                            Text(review.initials, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryGreen)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(review.authorName, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DarkText)
                            Text("${review.rating} ★ · ${review.date}", fontSize = 11.sp, color = MutedText)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(review.comment, fontSize = 13.sp, color = DarkText, lineHeight = 18.sp)
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            OutlinedButton(
                onClick = { isSaved = !isSaved },
                border = BorderStroke(1.dp, PrimaryGreen),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
            ) {
                Icon(
                    imageVector = if (isSaved) Icons.Default.BookmarkAdded else Icons.Outlined.BookmarkBorder,
                    contentDescription = null,
                    tint = PrimaryGreen,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isSaved) place.savedPlanName else "Save to Riyadh weekend",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = PrimaryGreen
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                ) {
                    Icon(Icons.Outlined.NearMe, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Directions", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.White)
                }

                OutlinedButton(
                    onClick = { checkedIn = !checkedIn },
                    border = BorderStroke(1.dp, PrimaryGreen),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                ) {
                    Icon(
                        imageVector = if (checkedIn) Icons.Default.CheckCircle else Icons.Outlined.LocationOn,
                        contentDescription = null,
                        tint = PrimaryGreen,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (checkedIn) "Checked in" else "Check in",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = PrimaryGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}