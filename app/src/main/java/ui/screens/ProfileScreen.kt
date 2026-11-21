package ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(
    onSavedPlacesClick: () -> Unit = {},
    onMyTripsClick: () -> Unit = {},
    onJournalClick: () -> Unit = {},
    onReviewsClick: () -> Unit = {},
    onAccountClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onEditInterestsClick: () -> Unit = {},
    onManageExperiencesClick: () -> Unit = {}
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Your LocalLens",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = colors.onBackground,
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Settings",
                    tint = colors.primary
                )
            }
        }


        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        color = colors.secondaryContainer,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "NA",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = colors.onSecondaryContainer
                )
            }

            Text(
                text = "Noura Al Saleh",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = colors.onBackground
            )

            Text(
                text = "Local explorer · Al Ahsa",
                style = MaterialTheme.typography.bodySmall,
                color = colors.onSurfaceVariant
            )
        }


        ProfileCard {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ProfileStatistic("12", "Places visited")
                ProfileStatistic("3", "Saved places")
                ProfileStatistic("2", "Communities")
            }
        }


        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfileSectionTitle("Your interests", Modifier.weight(1f))

                TextButton(onClick = onEditInterestsClick) {
                    Text("Edit")
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Heritage", "Food", "Crafts").forEach { interest ->
                    Surface(
                        color = colors.primary,
                        contentColor = colors.onPrimary,
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = interest,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(
                                horizontal = 14.dp,
                                vertical = 9.dp
                            )
                        )
                    }
                }
            }
        }


        ProfileCard {
            ProfileMenuItem(
                icon = Icons.Outlined.BookmarkBorder,
                title = "Saved places",
                subtitle = "3 places · 2 collections",
                onClick = onSavedPlacesClick
            )

            ProfileMenuItem(
                icon = Icons.Outlined.Groups,
                title = "My trips",
                subtitle = "Al Ahsa Heritage Morning · Upcoming",
                onClick = onMyTripsClick
            )

            ProfileMenuItem(
                icon = Icons.Outlined.MenuBook,
                title = "Travel journal",
                subtitle = "Personal notes and memories",
                onClick = onJournalClick
            )

            ProfileMenuItem(
                icon = Icons.Outlined.CameraAlt,
                title = "My reviews & photos",
                onClick = onReviewsClick
            )
        }

        ProfileSectionTitle("Account & preferences")

        ProfileCard {
            ProfileMenuItem(
                icon = Icons.Outlined.Person,
                title = "Account details",
                onClick = onAccountClick
            )

            ProfileMenuItem(
                icon = Icons.Outlined.Settings,
                title = "Language & notifications",
                subtitle = "English · Notifications on",
                onClick = onSettingsClick
            )
        }


        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            color = colors.primaryContainer
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = colors.secondaryContainer
                ) {
                    Text(
                        text = "Authorized local contributor",
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.onSecondaryContainer,
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 7.dp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                ProfileMenuItem(
                    icon = Icons.Outlined.VerifiedUser,
                    title = "Manage experiences",
                    subtitle = "Available only with contributor access",
                    onClick = onManageExperiencesClick
                )
            }
        }

        Text(
            text = "LocalLens · Discover Saudi Arabia",
            style = MaterialTheme.typography.labelSmall,
            color = colors.onSurfaceVariant,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun ProfileCard(
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            content = content
        )
    }
}

@Composable
private fun ProfileStatistic(
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ProfileSectionTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String = "",
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(22.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (subtitle.isNotEmpty()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
    }
}

