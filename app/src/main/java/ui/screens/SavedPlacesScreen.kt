package ui.screens

import androidx.annotation.DrawableRes
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Tune
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
import com.example.project_mobile_app.ui.theme.LocalLensBeige
import com.example.project_mobile_app.ui.theme.LocalLensGray
import com.example.project_mobile_app.ui.theme.LocalLensGreen
import com.example.project_mobile_app.ui.theme.LocalLensLightGreen
import com.example.project_mobile_app.ui.theme.LocalLensWhite
import com.example.project_mobile_app.ui.theme.ProjectmobileappTheme

private data class SavedPlaceEntry(
    val id: String,
    val title: String,
    val subtitle: String,
    val rating: String,
    val metaLabel: String,
    val tag: String,
    @DrawableRes val imageRes: Int,
    val isFeaturedCard: Boolean = false
)

@Composable
fun SavedPlacesScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToExplore: () -> Unit = {},
    onNavigateToMap: () -> Unit = {},
    onNavigateToSaved: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToBuildMyDay: () -> Unit = {},
    onNavigateToExperienceDetails: (String) -> Unit = {}
) {
    var selectedSegment by remember { mutableStateOf("Places") }
    val filters = listOf("All saved", "Al Ahsa weekend", "Heritage")
    var selectedFilter by remember { mutableStateOf("All saved") }

    val allSavedPlaces = remember {
        listOf(
            SavedPlaceEntry(
                id = "qaisariyah",
                title = "Qaisariyah Souq",
                subtitle = "Al Hofuf, Al Ahsa • Heritage • Food",
                rating = "4.8",
                metaLabel = "1 hour • 1.2 km",
                tag = "Heritage",
                imageRes = R.drawable.qaisariyah_souq,
                isFeaturedCard = true
            ),
            SavedPlaceEntry(
                id = "ibrahim_palace",
                title = "Ibrahim Palace",
                subtitle = "Al Ahsa weekend",
                rating = "4.7",
                metaLabel = "1 hour",
                tag = "Al Ahsa weekend",
                imageRes = R.drawable.ibrahim_palace,
                isFeaturedCard = false
            ),
            SavedPlaceEntry(
                id = "at_turaif",
                title = "At-Turaif District",
                subtitle = "Heritage collection • Diriyah",
                rating = "4.9",
                metaLabel = "2 hours",
                tag = "Heritage",
                imageRes = R.drawable.at_turaif_district,
                isFeaturedCard = false
            )
        )
    }

    var savedIds by remember { mutableStateOf(allSavedPlaces.map { it.id }.toSet()) }

    val visiblePlaces = allSavedPlaces.filter { place ->
        val isStillSaved = place.id in savedIds
        val matchesFilter = when (selectedFilter) {
            "All saved" -> true
            "Al Ahsa weekend" -> place.subtitle.contains("Al Ahsa", ignoreCase = true) || place.tag == "Al Ahsa weekend"
            "Heritage" -> place.subtitle.contains("Heritage", ignoreCase = true) || place.tag == "Heritage"
            else -> true
        }
        isStillSaved && matchesFilter
    }

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
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "YOUR CURATED LIST",
                        style = MaterialTheme.typography.labelSmall,
                        color = LocalLensGray
                    )
                    Text(
                        text = "Saved places",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Keep the places you want to come back to.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LocalLensGray
                    )
                }
            }

            // Places / Collections segmented switcher
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(LocalLensBeige)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("Places", "Collections").forEach { segment ->
                        val isSelected = selectedSegment == segment
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedSegment = segment },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.surface else LocalLensBeige
                        ) {
                            Box(
                                modifier = Modifier.padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = segment,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (isSelected) MaterialTheme.colorScheme.onSurface else LocalLensGray
                                )
                            }
                        }
                    }
                }
            }

            // Filter chips
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filters.forEach { filter ->
                        val isSelected = selectedFilter == filter
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedFilter = filter },
                            label = {
                                Text(
                                    text = filter,
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

            // Count subtitle
            item {
                val countText = if (visiblePlaces.size == 1) {
                    "1 place, plenty of possibility"
                } else {
                    "${visiblePlaces.size} places, plenty of possibility"
                }
                Text(
                    text = countText,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            // Saved items
            visiblePlaces.forEach { place ->
                item(key = place.id) {
                    val isBookmarked = place.id in savedIds
                    if (place.isFeaturedCard) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigateToExperienceDetails(place.id) },
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
                                ) {
                                    Image(
                                        painter = painterResource(id = place.imageRes),
                                        contentDescription = place.title,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )

                                    Surface(
                                        shape = RoundedCornerShape(50),
                                        color = LocalLensWhite.copy(alpha = 0.92f),
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(10.dp)
                                            .size(34.dp)
                                    ) {
                                        IconButton(
                                            onClick = {
                                                savedIds = if (isBookmarked) {
                                                    savedIds - place.id
                                                } else {
                                                    savedIds + place.id
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                                                contentDescription = "Toggle saved",
                                                tint = LocalLensGreen,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }

                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = place.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = place.subtitle,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = LocalLensGray
                                    )
                                    Text(
                                        text = "☆ ${place.rating}  •  ${place.metaLabel}",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = LocalLensGray
                                    )
                                }
                            }
                        }
                    } else {
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
                                        .size(64.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Text(
                                        text = place.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = place.subtitle,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = LocalLensGray
                                    )
                                    Text(
                                        text = "${place.rating} ★  •  ${place.metaLabel}",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = LocalLensGreen
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        savedIds = if (isBookmarked) {
                                            savedIds - place.id
                                        } else {
                                            savedIds + place.id
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                                        contentDescription = "Toggle saved",
                                        tint = LocalLensGreen
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Bottom "Turn inspiration into a day." callout card
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
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Turn inspiration into a day.",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Start with your interests, time and location.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = LocalLensGray
                        )
                        Button(
                            onClick = onNavigateToBuildMyDay,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LocalLensGreen,
                                contentColor = LocalLensWhite
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Tune,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
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