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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
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

private data class ExploreExperienceItem(
    val id: String,
    val title: String,
    val category: String,
    val subtitle: String,
    val rating: String,
    val metaLabel: String,
    @DrawableRes val imageRes: Int,
    val isFeaturedCard: Boolean = true,
    val section: String = "heritage"
)

@Composable
fun ExploreScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToExplore: () -> Unit = {},
    onNavigateToMap: () -> Unit = {},
    onNavigateToSaved: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToExperienceDetails: (String) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    val categories = listOf("Heritage", "Food", "Nature", "Arts", "Crafts")
    var selectedCategory by remember { mutableStateOf("Heritage") }
    var selectedMode by remember { mutableStateOf("List") }
    var bookmarkedIds by remember { mutableStateOf(setOf("qaisariyah")) }

    val experiences = listOf(
        ExploreExperienceItem(
            id = "qaisariyah",
            title = "Qaisariyah Souq",
            category = "Heritage",
            subtitle = "Al Hofuf, Al Ahsa • Heritage • Food",
            rating = "4.8",
            metaLabel = "1 hour • 1.2 km",
            imageRes = R.drawable.qaisariyah_souq,
            isFeaturedCard = true,
            section = "heritage"
        ),
        ExploreExperienceItem(
            id = "at_turaif",
            title = "At-Turaif District",
            category = "Heritage",
            subtitle = "Diriyah, Riyadh • Heritage",
            rating = "4.9",
            metaLabel = "2 hours • 330 km",
            imageRes = R.drawable.at_turaif_district,
            isFeaturedCard = true,
            section = "heritage"
        ),
        ExploreExperienceItem(
            id = "historic_al_balad",
            title = "Historic Al Balad",
            category = "Arts",
            subtitle = "Jeddah • Heritage & Arts",
            rating = "4.8",
            metaLabel = "2 hours",
            imageRes = R.drawable.historic_al_balad,
            isFeaturedCard = false,
            section = "heritage"
        ),
        ExploreExperienceItem(
            id = "edge_of_the_world",
            title = "Edge of the World",
            category = "Nature",
            subtitle = "Riyadh • Nature • 3 hours",
            rating = "4.8",
            metaLabel = "3 hours",
            imageRes = R.drawable.edge_of_the_world,
            isFeaturedCard = false,
            section = "more"
        )
    )

    val filteredExperiences = experiences.filter { item ->
        val matchesSearch = searchQuery.isBlank() ||
                item.title.contains(searchQuery, ignoreCase = true) ||
                item.subtitle.contains(searchQuery, ignoreCase = true)
        val matchesCategory = selectedCategory.isBlank() ||
                item.category.equals(selectedCategory, ignoreCase = true) ||
                item.subtitle.contains(selectedCategory, ignoreCase = true) ||
                selectedCategory == "Heritage"
        matchesSearch && matchesCategory
    }

    val heritageItems = filteredExperiences.filter { it.section == "heritage" }
    val moreItems = filteredExperiences.filter { it.section == "more" }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            ExploreBottomNavBar(
                selectedTab = "explore",
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "DISCOVER SAUDI ARABIA",
                        style = MaterialTheme.typography.labelSmall,
                        color = LocalLensGray
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Explore experiences",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        IconButton(onClick = onNavigateToSaved) {
                            Icon(
                                imageVector = Icons.Filled.BookmarkBorder,
                                contentDescription = "Saved places",
                                tint = LocalLensGreen
                            )
                        }
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    placeholder = {
                        Text(
                            text = "Search places, food, experiences",
                            style = MaterialTheme.typography.bodyMedium,
                            color = LocalLensGray
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = LocalLensGray
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Tune,
                            contentDescription = "Filter options",
                            tint = LocalLensGreen
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

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(LocalLensBeige)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("List", "Map").forEach { mode ->
                        val isSelected = selectedMode == mode
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    selectedMode = mode
                                    if (mode == "Map") {
                                        onNavigateToMap()
                                    }
                                },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.surface else LocalLensBeige
                        ) {
                            Box(
                                modifier = Modifier.padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = mode,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (isSelected) MaterialTheme.colorScheme.onSurface else LocalLensGray
                                )
                            }
                        }
                    }
                }
            }

            if (heritageItems.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Heritage, near and far",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Filters",
                            style = MaterialTheme.typography.labelLarge,
                            color = LocalLensGreen
                        )
                    }
                }

                heritageItems.forEach { item ->
                    item(key = item.id) {
                        val isSaved = item.id in bookmarkedIds
                        if (item.isFeaturedCard) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onNavigateToExperienceDetails(item.id) },
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
                                            painter = painterResource(id = item.imageRes),
                                            contentDescription = item.title,
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
                                                    bookmarkedIds = if (isSaved) {
                                                        bookmarkedIds - item.id
                                                    } else {
                                                        bookmarkedIds + item.id
                                                    }
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                                                    contentDescription = "Save experience",
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
                                            text = item.title,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = item.subtitle,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = LocalLensGray
                                        )
                                        Text(
                                            text = "☆ ${item.rating}  •  ${item.metaLabel}",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = LocalLensGray
                                        )
                                    }
                                }
                            }
                        } else {
                            ExploreCompactCard(
                                item = item,
                                onClick = { onNavigateToExperienceDetails(item.id) }
                            )
                        }
                    }
                }
            }

            if (moreItems.isNotEmpty()) {
                item {
                    Text(
                        text = "More ways to explore",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                moreItems.forEach { item ->
                    item(key = item.id) {
                        ExploreCompactCard(
                            item = item,
                            onClick = { onNavigateToExperienceDetails(item.id) }
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Ratings and visit lengths are illustrative demo content.",
                    style = MaterialTheme.typography.bodySmall,
                    color = LocalLensGray
                )
            }
        }
    }
}

@Composable
private fun ExploreCompactCard(
    item: ExploreExperienceItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
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
                painter = painterResource(id = item.imageRes),
                contentDescription = item.title,
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
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = LocalLensGray
                )
                Text(
                    text = "${item.rating} ★  •  ${item.metaLabel}",
                    style = MaterialTheme.typography.labelMedium,
                    color = LocalLensGreen
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Open details",
                tint = LocalLensGray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun ExploreBottomNavBar(
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
private fun ExploreScreenPreview() {
    ProjectmobileappTheme {
        ExploreScreen()
    }
}