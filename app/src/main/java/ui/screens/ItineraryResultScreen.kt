package ui.screens

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.project_mobile_app.ui.theme.ProjectmobileappTheme
import kotlinx.coroutines.launch
import java.util.Locale

private data class DemoStop(
    val name: String,
    val category: String,
    val description: String,
    val mapsQuery: String? = null
)

@Composable
fun ItineraryResultScreen(
    interests: List<String> = listOf("Heritage", "Food", "Crafts"),
    durationHours: Int = 4,
    startMinutes: Int = 9 * 60,
    startingPoint: String = "Al Hofuf, Al Ahsa",
    onBack: (() -> Unit)? = null,
    onEditPlan: (() -> Unit)? = null,
    onSaveDay: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }

    var selectedTab by rememberSaveable {
        mutableStateOf(0)
    }

    var generation by rememberSaveable {
        mutableStateOf(0)
    }

    val catalog = listOf(
        DemoStop(
            name = "Qaisariyah Souq",
            category = "Food",
            description = "Explore the souq and plan a local food stop.",
            mapsQuery = "Qaisariyah Souq, Al Hofuf, Saudi Arabia"
        ),
        DemoStop(
            name = "Ibrahim Palace",
            category = "Heritage",
            description = "Discover Al Ahsa’s architecture and stories.",
            mapsQuery = "Ibrahim Palace, Al Hofuf, Saudi Arabia"
        ),
        DemoStop(
            name = "Palm-weaving workshop",
            category = "Crafts",
            description =
                "Sample activity. An actual workshop must be selected."
        ),
        DemoStop(
            name = "Nature stop (demo)",
            category = "Nature",
            description =
                "Sample outdoor activity. An actual place must be selected."
        ),
        DemoStop(
            name = "Art exhibition (demo)",
            category = "Arts",
            description =
                "Sample creative activity. An actual venue must be selected."
        )
    )

    val candidates = catalog.filter {
        it.category in interests
    }

    val orderedCandidates = if (candidates.isEmpty()) {
        emptyList()
    } else {
        val shift = generation % candidates.size
        candidates.drop(shift) + candidates.take(shift)
    }

    val maxStops = when (durationHours) {
        2 -> 1
        4 -> 3
        else -> 5
    }

    val stops = orderedCandidates.take(maxStops)
    val totalMinutes = durationHours * 60
    val travelMinutes = (stops.size - 1).coerceAtLeast(0) * 15

    val visitMinutes = if (stops.isEmpty()) {
        0
    } else {
        minOf(
            60,
            (totalMinutes - travelMinutes - 30)
                .coerceAtLeast(0) / stops.size
        )
    }

    val visitsTotal = stops.size * visitMinutes
    val flexibleMinutes = totalMinutes - visitsTotal - travelMinutes
    val endMinutes = startMinutes + totalMinutes
    val breakStart = startMinutes + visitsTotal + travelMinutes

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = {
            SnackbarHost(snackbar)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // Header: navigation and label on one line,
            // followed by the full-width title.
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (onBack != null) {
                        TextButton(onClick = onBack) {
                            Text("Back")
                        }
                    }

                    Text(
                        text = "BUILD MY DAY",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Text(
                    text = "Your Al Ahsa demo plan",
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor =
                        MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "$durationHours hours of local discovery",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text = "${resultTime(startMinutes)}–" +
                                resultTime(endMinutes),
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = "Entered starting area: $startingPoint",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = interests.joinToString(" · "),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ResultTabButton(
                    text = "Itinerary",
                    selected = selectedTab == 0,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        selectedTab = 0
                    }
                )

                ResultTabButton(
                    text = "Map",
                    selected = selectedTab == 1,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        selectedTab = 1
                    }
                )
            }

            if (selectedTab == 0) {
                if (stops.isEmpty()) {
                    ResultNotice(
                        text = "No demo places match these interests."
                    )
                }

                stops.forEachIndexed { index, stop ->
                    val stopStart =
                        startMinutes + index * (visitMinutes + 15)

                    val stopEnd = stopStart + visitMinutes

                    ResultStopCard(
                        number = index + 1,
                        time = "${resultTime(stopStart)}–" +
                                resultTime(stopEnd),
                        minutes = visitMinutes,
                        title = stop.name,
                        description = stop.description
                    )

                    if (index < stops.lastIndex) {
                        Text(
                            text = "${resultTime(stopEnd)}–" +
                                    "${resultTime(stopEnd + 15)} · " +
                                    "15 min transfer (demo)",
                            style = MaterialTheme.typography.bodyMedium,
                            color =
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                ResultNotice(
                    text = "${resultTime(breakStart)}–" +
                            "${resultTime(endMinutes)} · " +
                            "$flexibleMinutes min flexible time."
                )

                ResultNotice(
                    text = "A balanced $totalMinutes-minute day\n\n" +
                            "$visitsTotal min visits + " +
                            "$travelMinutes min travel + " +
                            "$flexibleMinutes min flexible time."
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Your route, at a glance",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.titleMedium
                    )

                    TextButton(
                        onClick = {
                            selectedTab = 1
                        }
                    ) {
                        Text("View map")
                    }
                }
            }

            ResultIllustrativeMap(stops)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        onEditPlan?.invoke()
                    },
                    enabled = onEditPlan != null,
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 52.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Edit plan")
                }

                OutlinedButton(
                    onClick = {
                        generation = (generation + 1) %
                                candidates.size.coerceAtLeast(1)

                        scope.launch {
                            snackbar.showSnackbar(
                                "Demo order changed. Travel times are illustrative."
                            )
                        }
                    },
                    enabled = candidates.size > 1,
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 52.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Regenerate")
                }
            }

            ResultNotice(
                text = "Demo only. Places are sample Al Ahsa suggestions. " +
                        "Your entered area has not been geocoded. " +
                        "Travel times, availability and opening hours are " +
                        "illustrative; confirm details before going.",
                green = true
            )

            val mappedStops = stops.filter {
                it.mapsQuery != null
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        onSaveDay?.invoke()
                    },
                    enabled = onSaveDay != null,
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 52.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Save day")
                }

                Button(
                    onClick = {
                        val firstStop = mappedStops.firstOrNull()

                        if (firstStop != null) {
                            val uri = if (mappedStops.size >= 2) {
                                Uri.parse(
                                    "https://www.google.com/maps/dir/"
                                ).buildUpon()
                                    .appendQueryParameter("api", "1")
                                    .appendQueryParameter(
                                        "origin",
                                        firstStop.mapsQuery
                                    )
                                    .appendQueryParameter(
                                        "destination",
                                        mappedStops.last().mapsQuery
                                    )
                                    .build()
                            } else {
                                Uri.parse(
                                    "https://www.google.com/maps/search/"
                                ).buildUpon()
                                    .appendQueryParameter("api", "1")
                                    .appendQueryParameter(
                                        "query",
                                        firstStop.mapsQuery
                                    )
                                    .build()
                            }

                            try {
                                context.startActivity(
                                    Intent(Intent.ACTION_VIEW, uri)
                                )
                            } catch (_: ActivityNotFoundException) {
                                scope.launch {
                                    snackbar.showSnackbar(
                                        "No app is available to open Google Maps."
                                    )
                                }
                            }
                        }
                    },
                    enabled = mappedStops.isNotEmpty(),
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 52.dp),
                    shape = RoundedCornerShape(14.dp)
               ) {
                    Text("Start route")
                }
            }

            Text(
                text = "Google Maps includes named landmarks only. " +
                        "Sample workshops and activities are excluded.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ResultTabButton(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.textButtonColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.surface
            } else {
                MaterialTheme.colorScheme.secondary
            },
            contentColor = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSecondary
            }
        )
    ) {
        Text(text)
    }
}

@Composable
private fun ResultStopCard(
    number: Int,
    time: String,
    minutes: Int,
    title: String,
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = number.toString(),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Text(
                    text = time,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "$minutes min",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

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
    }
}

@Composable
private fun ResultNotice(
    text: String,
    green: Boolean = false
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (green) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.secondary
            }
        )
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(18.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ResultIllustrativeMap(stops: List<DemoStop>) {
    val roadColor = MaterialTheme.colorScheme.surface
    val routeColor = MaterialTheme.colorScheme.primary

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "AL HOFUF · DEMO",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                val w = size.width
                val h = size.height

                listOf(0.25f, 0.5f, 0.75f).forEach { position ->
                    drawLine(
                        color = roadColor,
                        start = Offset(w * position, 0f),
                        end = Offset(w * position, h),
                        strokeWidth = 8.dp.toPx()
                    )

                    drawLine(
                        color = roadColor,
                        start = Offset(0f, h * position),
                        end = Offset(w, h * position),
                        strokeWidth = 8.dp.toPx()
                    )
                }

                val points = stops.indices.map { index ->
                    val x = (index + 1f) / (stops.size + 1f)
                    val y = if (index % 2 == 0) 0.7f else 0.3f

                    Offset(w * x, h * y)
                }

                if (points.size > 1) {
                    val path = Path().apply {
                        moveTo(
                            points.first().x,
                            points.first().y
                        )

                        points.drop(1).forEachIndexed { index, point ->
                            lineTo(point.x, points[index].y)
                            lineTo(point.x, point.y)
                        }
                    }

                    drawPath(
                        path = path,
                        color = routeColor,
                        style = Stroke(width = 4.dp.toPx())
                    )
                }

                points.forEach { point ->
                    drawCircle(
                        color = roadColor,
                        radius = 10.dp.toPx(),
                        center = point
                    )

                    drawCircle(
                        color = routeColor,
                        radius = 7.dp.toPx(),
                        center = point
                    )
                }
            }

            stops.forEachIndexed { index, stop ->
                Text(
                    text = "${index + 1}. ${stop.name}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Text(
                text = "Illustrative route · Not a geographic map",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun resultTime(totalMinutes: Int): String {
    val minutesInDay = 24 * 60

    val normalized =
        ((totalMinutes % minutesInDay) + minutesInDay) % minutesInDay

    val time = String.format(
        Locale.US,
        "%02d:%02d",
        normalized / 60,
        normalized % 60
    )

    return if (totalMinutes >= minutesInDay) {
        "$time (+1 day)"
    } else {
        time
    }
}

@Preview(showBackground = true)
@Composable
fun ItineraryResultScreenPreview() {
    ProjectmobileappTheme {
        ItineraryResultScreen(
            onBack = {}
        )
    }
    }