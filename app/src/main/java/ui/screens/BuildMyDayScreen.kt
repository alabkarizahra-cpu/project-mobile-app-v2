package ui.screens

import android.app.TimePickerDialog
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.project_mobile_app.ui.theme.ProjectmobileappTheme
import java.util.Locale

@Composable
fun BuildMyDayScreen() {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var selectedInterests by rememberSaveable {
        mutableStateOf(listOf<String>())
    }
    var selectedHours by rememberSaveable {
        mutableStateOf(4)
    }
    var startHour by rememberSaveable {
        mutableStateOf(9)
    }
    var startMinute by rememberSaveable {
        mutableStateOf(0)
    }
    var startingPoint by rememberSaveable {
        mutableStateOf("Al Hofuf, Al Ahsa")
    }
    var showResult by rememberSaveable {
        mutableStateOf(false)
    }

    val interests = listOf(
        "Heritage" to "Historic places and local stories",
        "Food" to "Traditional tastes and coffee stops",
        "Nature" to "Green oases and outdoor escapes",
        "Arts" to "Creative spaces and exhibitions",
        "Crafts" to "Hands-on workshops and artisans"
    )

    val durations = listOf(
        2 to "A short escape",
        4 to "A relaxed morning",
        6 to "A fuller day"
    )

    BackHandler(enabled = showResult) {
        showResult = false
    }

    if (showResult) {
        ItineraryResultScreen(
            interests = selectedInterests,
            durationHours = selectedHours,
            startMinutes = startHour * 60 + startMinute,
            startingPoint = startingPoint.trim(),
            onBack = { showResult = false },
            onEditPlan = { showResult = false }
        )
    } else {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            containerColor = MaterialTheme.colorScheme.background,

            // Fixed button: outside the scrolling content.
            bottomBar = {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    shadowElevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(
                                horizontal = 20.dp,
                                vertical = 12.dp
                            ),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (selectedInterests.isEmpty()) {
                            Text(
                                text = "Select at least one interest.",
                                style = MaterialTheme.typography.labelSmall,
                                color =
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else if (startingPoint.isBlank()) {
                            Text(
                                text = "Enter a starting area.",
                                style = MaterialTheme.typography.labelSmall,
                                color =
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Button(
                            onClick = { showResult = true },
                            enabled = selectedInterests.isNotEmpty() &&
                                    startingPoint.isNotBlank(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 56.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = "Generate itinerary",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(scrollState)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = "Build My Day",
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "Your time. Your kind of adventure.",
                    style = MaterialTheme.typography.headlineMedium
                )

                Text(
                    text = "Bring together the places you’ll love into one easy-to-follow day.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                SectionLabel("Interests")

                interests.forEach { (title, description) ->
                    PlanOptionCard(
                        title = title,
                        description = description,
                        selected = title in selectedInterests,
                        multipleChoice = true,
                        onClick = {
                            selectedInterests =
                                if (title in selectedInterests) {
                                    selectedInterests - title
                                } else {
                                    selectedInterests + title
                                }
                        }
                    )
                }

                SectionLabel("Available time")

                durations.forEach { (hours, description) ->
                    PlanOptionCard(
                        title = "$hours hours",
                        description = description,
                        selected = selectedHours == hours,
                        multipleChoice = false,
                        onClick = { selectedHours = hours }
                    )
                }

                Text(
                    text = "Preferred start time",
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedButton(
                    onClick = {
                        TimePickerDialog(
                            context,
                            { _, hour, minute ->
                                startHour = hour
                                startMinute = minute
                            },
                            startHour,
                            startMinute,
                            true
                        ).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = String.format(
                                Locale.US,
                                "%02d:%02d",
                                startHour,
                                startMinute
                            ),
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Text(
                            text = "Change",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                SectionLabel("Starting point")

                OutlinedTextField(
                    value = startingPoint,
                    onValueChange = { startingPoint = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("City or neighborhood") },
                    placeholder = { Text("Enter your starting area") },
                    supportingText = {
                        Text(
                            "Manual entry. The current demo uses Al Ahsa places."
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor =
                            MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor =
                            MaterialTheme.colorScheme.surface
                    )
                )

                if (startingPoint.isNotBlank()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor =
                                MaterialTheme.colorScheme.primaryContainer,
                            contentColor =
                                MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = startingPoint.trim(),
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = "Your entered starting area",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                Text(
                    text = "This sample plan is a demonstration, not a booking.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun PlanOptionCard(
    title: String,
    description: String,
    selected: Boolean,
    multipleChoice: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            },
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        border = BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outlineVariant
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (multipleChoice) {
                Checkbox(
                    checked = selected,
                    onCheckedChange = null
                )
            } else {
                RadioButton(
                    selected = selected,
                    onClick = null
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BuildMyDayScreenPreview() {
    ProjectmobileappTheme {
        BuildMyDayScreen()
    }
}