package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.project_mobile_app.ui.theme.*

// Community table
data class Community(
    val communityId: String,
    val name: String,
    val description: String,
    val imageUrl: String? = null,
    val createdAt: Long
)

// Trip table
data class GroupTrip(
    val tripId: String,
    val communityId: String,
    val title: String,
    val description: String,
    val destination: String,
    val startDate: String,
    val endDate: String,
    val meetingTime: String,
    val meetingPoint: String
)

@Composable
fun CommunityTripsScreen(
    onCommunityClick: (String) -> Unit = {},
    onTripClick: (String) -> Unit = {}
) {
    var searchText by remember { mutableStateOf("") }

    // Temporary sample data
    // These records will be replaced by Firebase data.

    val communities = listOf(
        Community(
            communityId = "community_001",
            name = "AlUla Explorers",
            description = "Discover the beauty of AlUla together",
            imageUrl = "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?w=900",
            createdAt = 1794000000000L
        ),
        Community(
            communityId = "community_002",
            name = "Saudi Food Lovers",
            description = "Explore traditional Saudi food",
            imageUrl = "https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=900",
            createdAt = 1794000000000L
        ),
        Community(
            communityId = "community_003",
            name = "Heritage Discoverers",
            description = "Explore Saudi history and culture",
            imageUrl = "https://images.unsplash.com/photo-1518005020951-eccb494ad742?w=900",
            createdAt = 1794000000000L
        )
    )

    val trips = listOf(
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

    val filteredCommunities = communities.filter {
        it.name.contains(searchText, ignoreCase = true) ||
                it.description.contains(searchText, ignoreCase = true)
    }

    val filteredTrips = trips.filter {
        it.title.contains(searchText, ignoreCase = true) ||
                it.destination.contains(searchText, ignoreCase = true)
    }

    Scaffold(
        containerColor = LocalLensBeige
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 20.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Column {
                    Text(
                        text = "LocalLens",
                        style = MaterialTheme.typography.headlineMedium,
                        color = LocalLensGreen
                    )

                    Text(
                        text = "Explore. Connect. Travel.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LocalLensGray
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Find Your Travel Community",
                        style = MaterialTheme.typography.headlineMedium,
                        color = LocalLensBlack
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text("Search communities or trips...")
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LocalLensGreen,
                            unfocusedBorderColor = LocalLensGray
                        )
                    )
                }
            }

            item {
                Text(
                    text = "Popular Communities",
                    style = MaterialTheme.typography.titleLarge,
                    color = LocalLensBlack
                )
            }

            if (filteredCommunities.isEmpty()) {
                item {
                    Text(
                        text = "No communities found.",
                        color = LocalLensGray
                    )
                }
            }

            items(filteredCommunities) { community ->
                CommunityCard(
                    community = community,
                    onClick = {
                        onCommunityClick(community.communityId)
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Upcoming Group Trips",
                    style = MaterialTheme.typography.titleLarge,
                    color = LocalLensBlack
                )
            }

            if (filteredTrips.isEmpty()) {
                item {
                    Text(
                        text = "No trips found.",
                        color = LocalLensGray
                    )
                }
            }

            items(filteredTrips) { trip ->
                TripCard(
                    trip = trip,
                    onClick = {
                        onTripClick(trip.tripId)
                    }
                )
            }
        }
    }
}

@Composable
fun CommunityCard(
    community: Community,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = LocalLensWhite
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            if (!community.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = community.imageUrl,
                    contentDescription = community.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = community.name,
                style = MaterialTheme.typography.titleMedium,
                color = LocalLensGreen
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = community.description,
                style = MaterialTheme.typography.bodyMedium,
                color = LocalLensGray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onClick,
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LocalLensGreen
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("View Community")
            }
        }
    }
}

@Composable
fun TripCard(
    trip: GroupTrip,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
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
                text = "📍 ${trip.destination}",
                style = MaterialTheme.typography.bodyMedium,
                color = LocalLensBlack
            )

            Text(
                text = "📅 ${trip.startDate} - ${trip.endDate}",
                style = MaterialTheme.typography.bodyMedium,
                color = LocalLensGray
            )

            Text(
                text = "Meeting time: ${trip.meetingTime}",
                style = MaterialTheme.typography.bodyMedium,
                color = LocalLensGray
            )

            Text(
                text = "Meeting point: ${trip.meetingPoint}",
                style = MaterialTheme.typography.bodyMedium,
                color = LocalLensGray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onClick,
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LocalLensGreen
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("View Trip")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommunityTripsScreenPreview() {
    ProjectmobileappTheme {
        CommunityTripsScreen()
    }
}
