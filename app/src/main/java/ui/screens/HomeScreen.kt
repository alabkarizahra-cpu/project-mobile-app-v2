package ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.project_mobile_app.ui.theme.LocalLensBeige
import com.example.project_mobile_app.ui.theme.LocalLensGray
import com.example.project_mobile_app.ui.theme.LocalLensGreen
import com.example.project_mobile_app.ui.theme.LocalLensLightGreen
import com.example.project_mobile_app.ui.theme.LocalLensWhite
import com.example.project_mobile_app.ui.theme.ProjectmobileappTheme

private data class HomePlaceItem(
    val id: String,
    val title: String,
    val category: String,
    val locationLabel: String,
    val rating: String
)

// Navigation / Route callbacks for HomeScreen (Milestone 3_Stage 2)
@Composable
fun HomeScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToExplore: () -> Unit = {},
    onNavigateToMap: () -> Unit = {},
    onNavigateToSaved: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToBuildMyDay: () -> Unit = {},
    onNavigateToExperienceDetails: (String) -> Unit = {}
) {
    val categories = listOf("All", "Heritage", "Crafts", "Nature", "Food")
    var selectedCategory by remember { mutableStateOf("All") }
    var bookmarkedIds by remember { mutableStateOf(setOf("qaisariyah")) }

    val featuredPlace = HomePlaceItem(
        id = "qaisariyah",
        title = "Qaisariyah Souq",
        category = "Heritage",
        locationLabel = "Al-Ahsa • Historic Market",
        rating = "4.8"
    )

    val nearbyPlaces = listOf(
        HomePlaceItem(
            id = "al_qarah",
            title = "Al-Qarah Mountain",
            category = "Nature",
            locationLabel = "Al-Hofuf • 12 min away",
            rating = "4.9"
        ),
        HomePlaceItem(
            id = "pottery_studio",
            title = "Traditional Pottery Workshop",
            category = "Crafts",
            locationLabel = "Al-Ahsa • 8 min away",
            rating = "4.7"
        )
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            HomeBottomNavBar(
                selectedTab = "home",
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
            // Greeting & subheading
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Good morning, Noura",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "A new story, nearby.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = LocalLensGray
                    )
                }
            }

            // Quick category filter chips
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

            // "A day made for you" card
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
                            text = "A day made for you",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Plan a curated route tailored to your pace, interests, and nearby spots.",
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

            // "Picked for your curiosity" section
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Picked for your curiosity",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "See all",
                            style = MaterialTheme.typography.labelLarge,
                            color = LocalLensGreen,
                            modifier = Modifier.clickable { onNavigateToExplore() }
                        )
                    }

                    val isSaved = featuredPlace.id in bookmarkedIds
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
                                    .height(150.dp)
                                    .background(LocalLensBeige)
                                    .padding(12.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = LocalLensLightGreen,
                                    modifier = Modifier.align(Alignment.TopStart)
                                ) {
                                    Text(
                                        text = featuredPlace.category,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = LocalLensGreen,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        bookmarkedIds = if (isSaved) {
                                            bookmarkedIds - featuredPlace.id
                                        } else {
                                            bookmarkedIds + featuredPlace.id
                                        }
                                    },
                                    modifier = Modifier.align(Alignment.TopEnd)
                                ) {
                                    Icon(
                                        imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                                        contentDescription = "Save place",
                                        tint = LocalLensGreen
                                    )
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

            // "A little closer" section
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "A little closer",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Map",
                            style = MaterialTheme.typography.labelLarge,
                            color = LocalLensGreen,
                            modifier = Modifier.clickable { onNavigateToMap() }
                        )
                    }

                    nearbyPlaces.forEach { place ->
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
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(LocalLensBeige),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = place.category.take(1),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = LocalLensGreen
                                    )
                                }

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

                                Text(
                                    text = "★ ${place.rating}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = LocalLensGreen
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
private fun HomeBottomNavBar(
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
private fun HomeScreenPreview() {
    ProjectmobileappTheme {
        HomeScreen()
    }
}