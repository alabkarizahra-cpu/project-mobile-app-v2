package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.project_mobile_app.ui.theme.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.LocationOn
@Composable
fun CommunityDetailsScreen(
    communityId: String,
    onBackClick: () -> Unit = {}
) {

    var isJoined by remember(communityId) {
        mutableStateOf(false)
    }

    // Temporary sample data matching the project models

    val communities = listOf(
        Community(
            communityId = "community_001",
            name = "AlUla Explorers",
            description = "Discover the beauty of AlUla together",
            imageUrl = "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?w=1000",
            createdAt = 1794000000000L
        ),
        Community(
            communityId = "community_002",
            name = "Saudi Food Lovers",
            description = "Explore traditional Saudi food",
            imageUrl = "https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=1000",
            createdAt = 1794000000000L
        ),
        Community(
            communityId = "community_003",
            name = "Heritage Discoverers",
            description = "Explore Saudi history and culture",
            imageUrl = "https://images.unsplash.com/photo-1518005020951-eccb494ad742?w=1000",
            createdAt = 1794000000000L
        )
    )

    val allTrips = listOf(
        GroupTrip(
            tripId = "trip_001",
            communityId = "community_001",
            title = "Explore AlUla",
            description = "Discover AlUla's beautiful landscapes",
            destination = "AlUla",
            startDate = "2026-12-05",
            endDate = "2026-12-07",
            meetingTime = "08:00",
            meetingPoint = "AlUla Visitor Center"
        ),
        GroupTrip(
            tripId = "trip_002",
            communityId = "community_002",
            title = "Jeddah Food Tour",
            description = "Explore traditional Saudi food",
            destination = "Jeddah",
            startDate = "2026-12-12",
            endDate = "2026-12-12",
            meetingTime = "17:00",
            meetingPoint = "Historic Jeddah"
        ),
        GroupTrip(
            tripId = "trip_003",
            communityId = "community_003",
            title = "Heritage Weekend",
            description = "Discover Saudi heritage",
            destination = "Diriyah",
            startDate = "2026-12-19",
            endDate = "2026-12-20",
            meetingTime = "09:00",
            meetingPoint = "Diriyah Gate"
        )
    )

    val community = communities.find {
        it.communityId == communityId
    }

    val communityTrips = allTrips.filter {
        it.communityId == communityId
    }

    Scaffold(
        containerColor = LocalLensBeige
    ) { innerPadding ->

        if (community == null) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Community not found")

                    Button(onClick = onBackClick) {
                        Text("Go Back")
                    }
                }
            }

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {

                // Community cover

                item {
                    Box {

                        AsyncImage(
                            model = community.imageUrl,
                            contentDescription = community.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp),
                            contentScale = ContentScale.Crop
                        )

                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier
                                .padding(16.dp)
                                .background(
                                    LocalLensWhite,
                                    CircleShape
                                )
                                .align(Alignment.TopStart)
                        ) {
                            Text(
                                text = "←",
                                color = LocalLensGreen,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                }

                // Community information

                item {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {

                        Text(
                            text = community.name,
                            style = MaterialTheme.typography.headlineMedium,
                            color = LocalLensGreen
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = community.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = LocalLensGray
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        CommunityInfo(
                            number = communityTrips.size.toString(),
                            label = "Upcoming Trips"
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                isJoined = !isJoined
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isJoined)
                                    LocalLensGray
                                else
                                    LocalLensGreen
                            )
                        ) {
                            Text(
                                text = if (isJoined)
                                    "Joined ✓"
                                else
                                    "Join Community",
                                modifier = Modifier.padding(6.dp),
                                color = LocalLensWhite
                            )
                        }
                    }
                }

                item {
                    HorizontalDivider(
                        color = LocalLensLightGreen,
                        thickness = 1.dp
                    )
                }

                // Community members

                item {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {

                        Text(
                            text = "Community Members",
                            style = MaterialTheme.typography.titleLarge,
                            color = LocalLensBlack
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            listOf("Sarah", "Ahmed", "Lina").forEach { name ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(64.dp)
                                            .clip(CircleShape)
                                            .background(LocalLensLightGreen),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = "Member",
                                            tint = LocalLensGreen,
                                            modifier = Modifier.size(36.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = name,
                                        color = LocalLensBlack
                                    )
                                }
                            }
                        }

                    }
                }

                item {
                    HorizontalDivider(
                        color = LocalLensLightGreen,
                        thickness = 1.dp
                    )
                }
                // Community Posts

                item {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Community Posts",
                            style = MaterialTheme.typography.titleLarge,
                            color = LocalLensBlack
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        val samplePosts = listOf(
                            "Sarah" to "Excited to explore AlUla with everyone! 🌄",
                            "Ahmed" to "Don't forget to bring your camera for the trip!",
                            "Lina" to "Looking forward to meeting all community members!"
                        )

                        samplePosts.forEach { post ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = LocalLensWhite
                                ),
                                elevation = CardDefaults.cardElevation(2.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        text = post.first,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = LocalLensGreen
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = "Community Member",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = LocalLensGray
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = post.second,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = LocalLensBlack
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    HorizontalDivider(
                        color = LocalLensLightGreen,
                        thickness = 1.dp
                    )
                }
// Community Guidelines

                item {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Community Guidelines",
                            style = MaterialTheme.typography.titleLarge,
                            color = LocalLensBlack
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = LocalLensWhite
                            ),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                listOf(
                                    "Be respectful and kind to all members.",
                                    "Keep discussions friendly and relevant.",
                                    "Respect local culture and traditions.",
                                    "Follow trip instructions and meeting times."
                                ).forEach { guideline ->
                                    Row(
                                        modifier = Modifier.padding(vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "•",
                                            color = LocalLensGreen
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))

                                        Text(
                                            text = guideline,
                                            color = LocalLensBlack,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    HorizontalDivider(
                        color = LocalLensLightGreen,
                        thickness = 1.dp
                    )
                }
                // Community trips

                item {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Upcoming Community Trips",
                            style = MaterialTheme.typography.titleLarge,
                            color = LocalLensBlack
                        )
                    }
                }

                if (communityTrips.isEmpty()) {
                    item {
                        Text(
                            text = "No trips available yet.",
                            modifier = Modifier.padding(horizontal = 20.dp),
                            color = LocalLensGray
                        )
                    }
                }

                items(communityTrips) { trip ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 20.dp,
                                vertical = 6.dp
                            ),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = LocalLensWhite
                        ),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = trip.title,
                                style = MaterialTheme.typography.titleMedium,
                                color = LocalLensGreen
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = trip.description,
                                color = LocalLensGray
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Place,
                                    contentDescription = null,
                                    tint = LocalLensGreen
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = trip.destination,
                                    color = LocalLensBlack
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = null,
                                    tint = LocalLensGreen
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${trip.startDate} - ${trip.endDate}",
                                    color = LocalLensGray
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccessTime,
                                    contentDescription = null,
                                    tint = LocalLensGreen
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = trip.meetingTime,
                                    color = LocalLensGray
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = LocalLensGreen
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = trip.meetingPoint,
                                    color = LocalLensGray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CommunityInfo(
    number: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = number,
            style = MaterialTheme.typography.titleLarge,
            color = LocalLensGreen
        )

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = LocalLensGray
        )
    }
}
@Preview(showBackground = true)
@Composable
fun CommunityDetailsScreenPreview() {
    CommunityDetailsScreen(
        communityId = "community_001"
    )
}