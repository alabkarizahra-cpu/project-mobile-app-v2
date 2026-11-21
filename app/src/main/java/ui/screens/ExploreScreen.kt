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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.material.icons.filled.Groups
private data class ExploreExperienceItem(
    val id: String,
    val title: String,
    val category: String,
    val areaTag: String,
    val subtitle: String,
    val rating: String,
    val isFeatured: Boolean
)

// Navigation / Route callbacks for ExploreScreen (Milestone 3_Stage 2)
@Composable
fun ExploreScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToExplore: () -> Unit = {},
    onNavigateToMap: () -> Unit = {},
    onNavigateToSaved: () -> Unit = {},
    onNavigateToCommunity: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToExperienceDetails: (String) -> Unit = {}
) {
    val categories = listOf("All", "Heritage", "Crafts", "Nature", "Food")
    val quickFilters = listOf("Near me", "Top rated", "Half-day")

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedQuickFilter by remember { mutableStateOf<String?>(null) }
    var bookmarkedIds by remember { mutableStateOf(setOf("qaisariyah_exp")) }

    val allExperiences = listOf(
        ExploreExperienceItem(
            id = "qaisariyah_exp",
            title = "Qaisariyah Souq",
            category = "Heritage",
            areaTag = "Al-Ahsa",
            subtitle = "Historic covered market with spices, textiles, and local stories",
            rating = "4.8",
            isFeatured = true
        ),
        ExploreExperienceItem(
            id = "al_qarah_exp",
            title = "Al-Qarah Mountain",
            category = "Nature",
            areaTag = "Al-Hofuf",
            subtitle = "Limestone caves and cool walkways surrounded by palm groves",
            rating = "4.9",
            isFeatured = true
        ),
        ExploreExperienceItem(
            id = "bisht_weaving_exp",
            title = "Al-Ahsa Bisht Craft",
            category = "Crafts",
            areaTag = "Al-Hofuf",
            subtitle = "Traditional hand-woven bisht tailoring and heritage artisans",
            rating = "4.7",
            isFeatured = false
        ),
        ExploreExperienceItem(
            id = "oasis_trail_exp",
            title = "Palm Oasis Walking Trail",
            category = "Nature",
            areaTag = "Al-Ahsa",
            subtitle = "Quiet morning walk through historic irrigation channels",
            rating = "4.6",
            isFeatured = false
        )
    )

    val filteredExperiences = allExperiences.filter { item ->
        val matchesCategory = selectedCategory == "All" || item.category == selectedCategory
        val matchesSearch = searchQuery.isBlank() ||
                item.title.contains(searchQuery, ignoreCase = true) ||
                item.category.contains(searchQuery, ignoreCase = true) ||
                item.areaTag.contains(searchQuery, ignoreCase = true)
        val matchesQuick = when (selectedQuickFilter) {
            "Top rated" -> (item.rating.toDoubleOrNull() ?: 0.0) >= 4.8
            else -> true
        }
        matchesCategory && matchesSearch && matchesQuick
    }

    val featuredItems = filteredExperiences.filter { it.isFeatured }
    val moreWaysItems = filteredExperiences.filter { !it.isFeatured }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            ExploreBottomNavBar(
                selectedTab = "explore",
                onHomeClick = onNavigateToHome,
                onExploreClick = onNavigateToExplore,
                onMapClick = onNavigateToMap,
                onCommunityClick = onNavigateToCommunity,
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
            // Header & Search field
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Explore experiences",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Search experiences",
                                tint = LocalLensGray
                            )
                        },
                        placeholder = {
                            Text(
                                text = "Search places, crafts, or areas...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = LocalLensGray
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedBorderColor = LocalLensGreen,
                            unfocusedBorderColor = LocalLensBeige
                        )
                    )
                }
            }

            // Category & secondary filter chips
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        quickFilters.forEach { filter ->
                            val isSelected = selectedQuickFilter == filter
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    selectedQuickFilter = if (isSelected) null else filter
                                },
                                label = {
                                    Text(
                                        text = filter,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = LocalLensLightGreen,
                                    labelColor = LocalLensGreen,
                                    selectedContainerColor = LocalLensGreen,
                                    selectedLabelColor = LocalLensWhite
                                )
                            )
                        }
                    }
                }
            }

            // "Heritage, near and far" section
            if (featuredItems.isNotEmpty()) {
                item {
                    Text(
                        text = "Heritage, near and far",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                items(featuredItems, key = { it.id }) { experience ->
                    val isSaved = experience.id in bookmarkedIds
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToExperienceDetails(experience.id) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp)
                                    .background(LocalLensBeige)
                                    .padding(12.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = LocalLensLightGreen,
                                    modifier = Modifier.align(Alignment.TopStart)
                                ) {
                                    Text(
                                        text = "${experience.category} • ${experience.areaTag}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = LocalLensGreen,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        bookmarkedIds = if (isSaved) {
                                            bookmarkedIds - experience.id
                                        } else {
                                            bookmarkedIds + experience.id
                                        }
                                    },
                                    modifier = Modifier.align(Alignment.TopEnd)
                                ) {
                                    Icon(
                                        imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                                        contentDescription = "Save experience",
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
                                        text = experience.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "★ ${experience.rating}",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = LocalLensGreen
                                    )
                                }
                                Text(
                                    text = experience.subtitle,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = LocalLensGray
                                )
                            }
                        }
                    }
                }
            }

            // "More ways to explore" section
            if (moreWaysItems.isNotEmpty()) {
                item {
                    Text(
                        text = "More ways to explore",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                items(moreWaysItems, key = { it.id }) { experience ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToExperienceDetails(experience.id) },
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
                                    .size(58.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(LocalLensBeige),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = experience.category.take(1),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = LocalLensGreen
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = experience.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${experience.areaTag} • ${experience.subtitle}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = LocalLensGray,
                                    maxLines = 1
                                )
                            }

                            Text(
                                text = "★ ${experience.rating}",
                                style = MaterialTheme.typography.labelLarge,
                                color = LocalLensGreen
                            )
                        }
                    }
                }
            }

            if (filteredExperiences.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Text(
                            text = "No matching experiences found. Try another filter or keyword.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = LocalLensGray,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ExploreBottomNavBar(
    selectedTab: String,
    onHomeClick: () -> Unit,
    onExploreClick: () -> Unit,
    onMapClick: () -> Unit,
    onCommunityClick: () -> Unit,
    onProfileClick: () -> Unit,
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
            selected = selectedTab == "community",
            onClick = onCommunityClick,
            icon = {
                Icon(
                    Icons.Filled.Groups,
                    contentDescription = "Community"
                )
            },
            label = {
                Text(
                    "Community",
                    style = MaterialTheme.typography.labelSmall
                )
            },
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
private fun ExploreScreenPreview() {
    ProjectmobileappTheme {
        ExploreScreen()
    }
}