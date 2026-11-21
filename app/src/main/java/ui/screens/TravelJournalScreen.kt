package ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.project_mobile_app.R

private data class JournalMemory(
    val id: Int,
    val title: String,
    val date: String,
    val location: String,
    val description: String,
    val image: Int,
    val isThisMonth: Boolean
)

@Composable
fun TravelJournalScreen(
    onBackClick: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    onAddEntryClick: () -> Unit = {},
    onMemoryClick: (Int) -> Unit = {}
) {
    val colors = MaterialTheme.colorScheme

    var search by rememberSaveable { mutableStateOf("") }
    var selectedFilter by rememberSaveable {
        mutableStateOf("All entries")
    }

    val memories = remember {
        listOf(
            JournalMemory(
                id = 1,
                title = "Qaisariyah Souq",
                date = "3 October 2026",
                location = "Al Hofuf, Al Ahsa",
                description = "A peaceful morning among the market arches. " +
                        "I brought home a little palm basket and a lovely memory.",
                image = R.drawable.journal_souq,
                isThisMonth = true
            ),
            JournalMemory(
                id = 2,
                title = "Ibrahim Palace",
                date = "3 October 2026",
                location = "Al Hofuf, Al Ahsa",
                description = "Quiet courtyards and beautiful details. " +
                        "I want to come back with more time to look around.",
                image = R.drawable.journal_palace,
                isThisMonth = true
            ),
            JournalMemory(
                id = 3,
                title = "Al Qarah Mountain",
                date = "26 September 2026",
                location = "Al Ahsa",
                description = "The cool cave passages were a welcome surprise. " +
                        "Loved the view across the oasis.",
                image = R.drawable.journal_mountain,
                isThisMonth = false
            )
        )
    }

    val visibleMemories = memories.filter { memory ->
        val matchesSearch = search.isBlank() ||
                memory.title.contains(search, ignoreCase = true) ||
                memory.location.contains(search, ignoreCase = true) ||
                memory.description.contains(search, ignoreCase = true)

        val matchesFilter = when (selectedFilter) {
            "Al Ahsa" -> memory.location.contains("Al Ahsa")
            "This month" -> memory.isThisMonth
            else -> true
        }

        matchesSearch && matchesFilter
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background),
        contentPadding = PaddingValues(
            horizontal = 20.dp,
            vertical = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = "Back",
                        tint = colors.primary
                    )
                }

                Text(
                    text = "Little moments, kept",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = colors.onBackground,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = onFilterClick) {
                    Icon(
                        imageVector = Icons.Outlined.Tune,
                        contentDescription = "Filter memories",
                        tint = colors.primary
                    )
                }
            }
        }

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    color = colors.secondaryContainer,
                    shape = RoundedCornerShape(50)
                ) {
                    Row(
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 8.dp
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = null,
                            tint = colors.onSecondaryContainer,
                            modifier = Modifier.size(15.dp)
                        )

                        Text(
                            text = "Private journal",
                            style = MaterialTheme.typography.labelMedium,
                            color = colors.onSecondaryContainer
                        )
                    }
                }

                Text(
                    text = "${memories.size} memories",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onSurfaceVariant
                )
            }
        }

        item {
            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Search your memories",
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = colors.surface,
                    unfocusedContainerColor = colors.surface,
                    unfocusedBorderColor = colors.outlineVariant
                )
            )
        }

        item {
            Row(
                modifier = Modifier.horizontalScroll(
                    rememberScrollState()
                ),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    "All entries",
                    "Al Ahsa",
                    "This month"
                ).forEach { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = {
                            Text(
                                text = filter,
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        shape = RoundedCornerShape(50),
                        border = null,
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = colors.secondaryContainer,
                            labelColor = colors.onSecondaryContainer,
                            selectedContainerColor = colors.primary,
                            selectedLabelColor = colors.onPrimary
                        )
                    )
                }
            }
        }

        item {
            Button(
                onClick = onAddEntryClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Add a journal entry")
            }
        }

        item {
            Text(
                text = "Your latest discoveries",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = colors.onBackground
            )
        }

        items(
            items = visibleMemories,
            key = { it.id }
        ) { memory ->
            JournalMemoryCard(
                memory = memory,
                onClick = { onMemoryClick(memory.id) }
            )
        }

        if (visibleMemories.isEmpty()) {
            item {
                Text(
                    text = "No memories found.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 24.dp)
                )
            }
        }
    }
}

@Composable
private fun JournalMemoryCard(
    memory: JournalMemory,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Image(
                painter = painterResource(memory.image),
                contentDescription = memory.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = memory.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colors.onSurface,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Outlined.ChevronRight,
                    contentDescription = null,
                    tint = colors.primary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Text(
                text = "${memory.date} · ${memory.location}",
                style = MaterialTheme.typography.labelSmall,
                color = colors.primary
            )

            Text(
                text = memory.description,
                style = MaterialTheme.typography.bodySmall,
                color = colors.onSurfaceVariant
            )
        }
    }
}