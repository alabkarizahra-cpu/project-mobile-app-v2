package ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.project_mobile_app.R
import com.example.project_mobile_app.ui.theme.LocalLensGray
import com.example.project_mobile_app.ui.theme.LocalLensGreen
import com.example.project_mobile_app.ui.theme.LocalLensLightGreen
import com.example.project_mobile_app.ui.theme.LocalLensWhite
import com.example.project_mobile_app.ui.theme.ProjectmobileappTheme

private data class SavedPlaceItem(
    val id: String,
    val title: String,
    val category: String,
    val locationLabel: String,
    val rating: String,
    val isFeatured: Boolean,
    val imageRes: Int
)

private data class SavedItineraryItem(
    val id: String,
    val title: String,
    val stopsSummary: String,
    val durationLabel: String
)

// Navigation / Route callbacks for SavedPlacesScreen (Milestone 3_Stage 2)
@Composable
fun SavedPlacesScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToExplore: () -> Unit = {},
    onNavigateToMap: () -> Unit = {},
    onNavigateToSaved: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToBuildMyDay: () -> Unit = {},
    onNavigateToExperienceDetails: (String) -> Unit = {},
    onNavigateToItineraryResult: (String) -> Unit = {}
) {
    val tabs = listOf("Places", "Itineraries")
    val categories = listOf("All", "Heritage", "Crafts", "Nature", "Food")

    var selectedTopTab by remember { mutableStateOf("Places") }
    var selectedCategory by remember { mutableStateOf("All") }

    var savedPlaces by remember {
        mutableStateOf(
            listOf(
                SavedPlaceItem(
                    id = "qaisariyah_saved",
                    title = "Qaisariyah Souq",
                    category = "Heritage",
                    locationLabel = "Al-Ahsa • Historic Market",
                    rating = "4.8",
                    isFeatured = true,
                    imageRes = R.drawable.qaisariyah_souq
                ),
                SavedPlaceItem(
                    id = "edge_world_saved",
                    title = "Edge of the World",
                    category = "Nature",
                    locationLabel = "Riyadh • Scenic Desert Cliffs",
                    rating = "4.9",
                    isFeatured = false,
                    imageRes = R.drawable.edge_of_the_world
                ),
                SavedPlaceItem(
                    id = "palm_weaving_saved",
                    title = "Palm Weaving Craft",
                    category = "Crafts",
                    locationLabel = "Al-Ahsa • Local Artisans",
                    rating = "4.7",
                    isFeatured = false,
                    imageRes = R.drawable.palm_weaving
                )
            )
        )
    }

    val savedItineraries = listOf(
        SavedItineraryItem(
            id = "ahsa_heritage_day",
            title = "Al-Ahsa Heritage & Crafts Morning",
            stopsSummary = "3 stops • Qaisariyah Souq, Ibrahim Palace, Palm Weaving",
            durationLabel = "4 hrs"
        ),
        SavedItineraryItem(
            id = "nature_oasis_route",
            title = "Cliffs & Historic Trails Route",
            stopsSummary = "2 stops • Edge of the World, At-Turaif District",
            durationLabel = "3 hrs"
        )
    )

    val filteredPlaces = savedPlaces.filter { place ->
        selectedCategory == "All" || place.category == selectedCategory
    }
    val featuredPlace = filteredPlaces.firstOrNull { it.isFeatured }
    val otherPlaces = filteredPlaces.filter { it.id != featuredPlace?.id }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            SavedBottomNavBar(
                selectedTab = "saved",
                onHomeClick = onNavigateToHome,
                onExploreClick = onNavigateToExplore,
                onMapClick = onNavigateToMap,
                onSavedClick = onNavigateToSaved,
                onProfileClick = onNavigateToProfile
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Your saved places",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Keep track of spots and routes you want to visit.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LocalLensGray
                    )
                }
            }

            // Places / Itineraries segmented toggle
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        tabs.forEach { tab ->
                            val isSelected = selectedTopTab == tab
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) LocalLensGreen else MaterialTheme.colorScheme.surface)
                                    .clickable { selectedTopTab = tab }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = tab,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (isSelected) LocalLensWhite else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            if (selectedTopTab == "Places") {
                // Category filters
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        categories.forEach { category ->
                            val isSelected = selectedCategory == category
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedCategory = category },
                                label = {
                                    Text(
                                        text = category,
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    labelColor = MaterialTheme.colorScheme.onSurface,
                                    selectedContainerColor = LocalLensGreen,
                                    selectedLabelColor = LocalLensWhite
                                )
                            )
                        }
                    }
                }

                // Section title
                item {
                    Text(
                        text = "Explore, plenty of possibilities",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                // Featured saved card
                if (featuredPlace != null) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigateToExperienceDetails(featuredPlace.id) },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Column {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(145.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = featuredPlace.imageRes),
                                        contentDescription = featuredPlace.title,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = LocalLensLightGreen,
                                        modifier = Modifier
                                            .align(Alignment.TopStart)
                                            .padding(12.dp)
                                    ) {
                                        Text(
                                            text = featuredPlace.category,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = LocalLensGreen,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(50),
                                        color = LocalLensWhite.copy(alpha = 0.9f),
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(8.dp)
                                    ) {
                                        IconButton(
                                            onClick = {
                                                savedPlaces = savedPlaces.filterNot { it.id == featuredPlace.id }
                                            },
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Bookmark,
                                                contentDescription = "Remove saved place",
                                                tint = LocalLensGreen
                                            )
                                        }
                                    }
                                }

                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = featuredPlace.title,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "★ ${featuredPlace.rating}",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = LocalLensGreen
                                        )
                                    }
                                    Text(
                                        text = featuredPlace.locationLabel,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = LocalLensGray
                                    )
                                }
                            }
                        }
                    }
                }

                // Remaining saved places list
                items(otherPlaces, key = { it.id }) { place ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToExperienceDetails(place.id) },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = place.imageRes),
                                contentDescription = place.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(58.dp)
                                    .clip(RoundedCornerShape(10.dp))
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = place.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = place.locationLabel,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = LocalLensGray
                                )
                            }

                            IconButton(
                                onClick = {
                                    savedPlaces = savedPlaces.filterNot { it.id == place.id }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Bookmark,
                                    contentDescription = "Remove saved place",
                                    tint = LocalLensGreen
                                )
                            }
                        }
                    }
                }

                if (filteredPlaces.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Text(
                                text = "No saved places in this category yet.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = LocalLensGray,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            } else {
                // Saved Itineraries tab content
                items(savedItineraries, key = { it.id }) { itinerary ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToItineraryResult(itinerary.id) },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = itinerary.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = LocalLensLightGreen
                                ) {
                                    Text(
                                        text = itinerary.durationLabel,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = LocalLensGreen,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                            Text(
                                text = itinerary.stopsSummary,
                                style = MaterialTheme.typography.bodyMedium,
                                color = LocalLensGray
                            )
                        }
                    }
                }
            }

            // Bottom CTA Card ("Turn inspiration into a day")
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = LocalLensLightGreen
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Turn inspiration into a day",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Combine your saved spots into a smooth, personalized itinerary.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Button(
                            onClick = onNavigateToBuildMyDay,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text(
                                text = "Build My Day",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SavedBottomNavBar(
    selectedTab: String,
    onHomeClick: () -> Unit,
    onExploreClick: () -> Unit,
    onMapClick: () -> Unit,
    onSavedClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val itemColors = NavigationBarItemDefaults.colors(
        selectedIconColor = LocalLensGreen,
        selectedTextColor = LocalLensGreen,
        indicatorColor = LocalLensLightGreen,
        unselectedIconColor = LocalLensGray,
        unselectedTextColor = LocalLensGray
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        NavigationBarItem(
            selected = selectedTab == "home",
            onClick = onHomeClick,
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home", style = MaterialTheme.typography.labelSmall) },
            colors = itemColors
        )
        NavigationBarItem(
            selected = selectedTab == "explore",
            onClick = onExploreClick,
            icon = { Icon(Icons.Filled.Explore, contentDescription = "Explore") },
            label = { Text("Explore", style = MaterialTheme.typography.labelSmall) },
            colors = itemColors
        )
        NavigationBarItem(
            selected = selectedTab == "map",
            onClick = onMapClick,
            icon = { Icon(Icons.Filled.Map, contentDescription = "Map") },
            label = { Text("Map", style = MaterialTheme.typography.labelSmall) },
            colors = itemColors
        )
        NavigationBarItem(
            selected = selectedTab == "saved",
            onClick = onSavedClick,
            icon = { Icon(Icons.Filled.Bookmark, contentDescription = "Saved") },
            label = { Text("Saved", style = MaterialTheme.typography.labelSmall) },
            colors = itemColors
        )
        NavigationBarItem(
            selected = selectedTab == "profile",
            onClick = onProfileClick,
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            label = { Text("Profile", style = MaterialTheme.typography.labelSmall) },
            colors = itemColors
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SavedPlacesScreenPreview() {
    ProjectmobileappTheme {
        SavedPlacesScreen()
    }
}