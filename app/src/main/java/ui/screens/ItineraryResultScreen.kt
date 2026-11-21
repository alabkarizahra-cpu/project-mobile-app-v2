package ui.screens

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_mobile_app.ui.theme.ProjectmobileappTheme
import kotlinx.coroutines.launch
import java.util.Locale

private val ResultGreen = Color(0xFF196F56)
private val ResultBackground = Color(0xFFFAF8F4)
private val ResultLightGreen = Color(0xFFE7F1EB)
private val ResultBeige = Color(0xFFF3E8D6)
private val ResultText = Color(0xFF1F2933)
private val ResultMuted = Color(0xFF6D7890)

private data class ResultPlace(
    val name: String,
    val category: String,
    val description: String,
    val imageName: String,
    val mapLabel: String,
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

    var selectedTab by rememberSaveable { mutableStateOf(0) }
    var generation by rememberSaveable { mutableStateOf(0) }

    val catalog = listOf(
        ResultPlace(
            name = "Qaisariyah Souq",
            category = "Food",
            description = "Taste local bites and wander heritage lanes.",
            imageName = "qaisariyah_souq",
            mapLabel = "Qaisariyah Souq",
            mapsQuery = "Qaisariyah Souq, Al Hofuf, Saudi Arabia"
        ),
        ResultPlace(
            name = "Ibrahim Palace",
            category = "Heritage",
            description = "Discover Al Ahsa’s architecture and stories.",
            imageName = "ibrahim_palace",
            mapLabel = "Ibrahim Palace",
            mapsQuery = "Ibrahim Palace, Al Hofuf, Saudi Arabia"
        ),
        ResultPlace(
            name = "Palm-weaving workshop",
            category = "Crafts",
            description = "Try a traditional craft with a local artisan.",
            imageName = "palm_workshop",
            mapLabel = "Palm workshop"
        ),
        ResultPlace(
            name = "Nature stop",
            category = "Nature",
            description = "Enjoy green spaces and a quiet outdoor escape.",
            imageName = "nature_stop",
            mapLabel = "Nature stop"
        ),
        ResultPlace(
            name = "Art exhibition",
            category = "Arts",
            description = "Explore creative spaces and local exhibitions.",
            imageName = "art_exhibition",
            mapLabel = "Art exhibition"
        )
    )

    val candidates = catalog.filter { it.category in interests }

    val orderedPlaces = if (candidates.isEmpty()) {
        emptyList()
    } else {
        val shift = generation % candidates.size
        candidates.drop(shift) + candidates.take(shift)
    }

    val safeHours = durationHours.coerceIn(2, 6)
    val maximumStops = when (safeHours) {
        2 -> 1
        3, 4 -> 3
        else -> 5
    }

    val stops = orderedPlaces.take(maximumStops)
    val totalMinutes = safeHours * 60
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
    val mappedStops = stops.filter { it.mapsQuery != null }

    val editAction = onEditPlan ?: onBack

    val dayPart = when ((startMinutes / 60) % 24) {
        in 5..11 -> "morning"
        in 12..16 -> "afternoon"
        else -> "evening"
    }

    fun showMessage(message: String) {
        scope.launch {
            snackbar.showSnackbar(message)
        }
    }

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Ltr
    ) {
        Scaffold(
            containerColor = ResultBackground,
            snackbarHost = { SnackbarHost(snackbar) }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
                    .padding(top = 12.dp, bottom = 28.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            onBack?.invoke()
                                ?: showMessage(
                                    "Open this screen from Build My Day to go back."
                                )
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        ResultIcon("back", Modifier.size(24.dp))
                    }

                    Spacer(Modifier.width(8.dp))

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            text = "BUILD MY DAY",
                            fontSize = 11.sp,
                            lineHeight = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = ResultGreen
                        )

                        Text(
                            text = "Your Al Ahsa $dayPart",
                            fontSize = 22.sp,
                            lineHeight = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = ResultText
                        )
                    }

                    IconButton(
                        onClick = {
                            editAction?.invoke()
                                ?: showMessage(
                                    "Open this screen from Build My Day to edit."
                                )
                        },
                        modifier = Modifier.size(40.dp)
                    ) {
                        ResultIcon(
                            "edit",
                            Modifier.size(23.dp),
                            ResultMuted
                        )
                    }
                }

                Spacer(Modifier.height(28.dp))

                // Plan summary
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    color = ResultLightGreen
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "$safeHours hours of local discovery",
                            fontSize = 22.sp,
                            lineHeight = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = ResultText
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ResultIcon("pin", Modifier.size(22.dp))

                            Text(
                                text = "$startingPoint · " +
                                        "${itineraryTime(startMinutes)}–" +
                                        itineraryTime(endMinutes),
                                modifier = Modifier.weight(1f),
                                fontSize = 14.sp,
                                lineHeight = 21.sp,
                                color = ResultText
                            )
                        }

                        // Wrap chips if the user chooses many interests.
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            interests.chunked(3).forEach { rowInterests ->
                                Row(
                                    horizontalArrangement =
                                        Arrangement.spacedBy(8.dp)
                                ) {
                                    rowInterests.forEach { interest ->
                                        Surface(
                                            shape = RoundedCornerShape(30.dp),
                                            color = ResultBeige
                                        ) {
                                            Text(
                                                text = interest,
                                                modifier = Modifier.padding(
                                                    horizontal = 13.dp,
                                                    vertical = 10.dp
                                                ),
                                                fontSize = 14.sp,
                                                color = ResultText
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                // Itinerary / Map tabs
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = ResultBeige
                ) {
                    Row(
                        modifier = Modifier.padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        listOf("Itinerary", "Map").forEachIndexed { index, title ->
                            TextButton(
                                onClick = { selectedTab = index },
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(min = 44.dp),
                                shape = RoundedCornerShape(9.dp),
                                colors = ButtonDefaults.textButtonColors(
                                    containerColor = if (selectedTab == index) {
                                        Color.White
                                    } else {
                                        Color.Transparent
                                    },
                                    contentColor = if (selectedTab == index) {
                                        ResultGreen
                                    } else {
                                        ResultMuted
                                    }
                                )
                            ) {
                                Text(
                                    text = title,
                                    fontSize = 14.sp,
                                    fontWeight = if (selectedTab == index) {
                                        FontWeight.SemiBold
                                    } else {
                                        FontWeight.Normal
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                if (selectedTab == 0) {
                    if (stops.isEmpty()) {
                        Text(
                            text = "No places match the selected interests.",
                            color = ResultMuted,
                            fontSize = 14.sp
                        )

                        Spacer(Modifier.height(20.dp))
                    }

                    stops.forEachIndexed { index, place ->
                        val stopStart =
                            startMinutes + index * (visitMinutes + 15)
                        val stopEnd = stopStart + visitMinutes

                        ItineraryPlaceCard(
                            place = place,
                            number = index + 1,
                            time = "${itineraryTime(stopStart)}–" +
                                    itineraryTime(stopEnd),
                            minutes = visitMinutes
                        )

                        if (index < stops.lastIndex) {
                            Row(
                                modifier = Modifier.padding(vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement =
                                    Arrangement.spacedBy(10.dp)
                            ) {
                                ResultIcon("walk", Modifier.size(22.dp))

                                Text(
                                    text = "${itineraryTime(stopEnd)}–" +
                                            "${itineraryTime(stopEnd + 15)} · " +
                                            "15 min transfer (demo)",
                                    modifier = Modifier.weight(1f),
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp,
                                    color = ResultMuted
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = ResultBeige
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ResultIcon("coffee", Modifier.size(22.dp))

                            Text(
                                text = "${itineraryTime(breakStart)}–" +
                                        "${itineraryTime(endMinutes)} · " +
                                        "Flexible break. Pause for coffee or " +
                                        "give yourself a little extra time.",
                                modifier = Modifier.weight(1f),
                                fontSize = 12.sp,
                                lineHeight = 18.sp,
                                color = ResultMuted
                            )
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        color = ResultBeige
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "A balanced $totalMinutes-minute day",
                                fontSize = 16.sp,
                                lineHeight = 23.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ResultText
                            )

                            Text(
                                text = "$visitsTotal min visits + " +
                                        "$travelMinutes min travel + " +
                                        "$flexibleMinutes min flexible time. " +
                                        "Finish at ${itineraryTime(endMinutes)}.",
                                fontSize = 14.sp,
                                lineHeight = 22.sp,
                                color = ResultMuted
                            )
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Your route, at a glance",
                            modifier = Modifier.weight(1f),
                            fontSize = 17.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ResultText
                        )

                        TextButton(
                            onClick = { selectedTab = 1 },
                            contentPadding = PaddingValues(
                                start = 8.dp,
                                top = 8.dp,
                                bottom = 8.dp
                            )
                        ) {
                            Text(
                                text = "View map",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ResultGreen
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))
                }

                ItineraryRouteMap(stops)

                Spacer(Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ResultAction(
                        title = "Edit plan",
                        icon = "edit",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            editAction?.invoke()
                                ?: showMessage(
                                    "Open this screen from Build My Day to edit."
                                )
                        }
                    )

                    ResultAction(
                        title = "Regenerate",
                        icon = "refresh",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            if (candidates.size > 1) {
                                generation =
                                    (generation + 1) % candidates.size
                                showMessage("Demo itinerary order updated.")
                            } else {
                                showMessage(
                                    "Choose more interests for another demo order."
                                )
                            }
                        }
                    )
                }

                Spacer(Modifier.height(20.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = ResultLightGreen
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ResultIcon("info", Modifier.size(20.dp))

                        Text(
                            text = "Demo itinerary. Travel times, workshop " +
                                    "participation and opening hours are " +
                                    "illustrative; confirm details before going.",
                            modifier = Modifier.weight(1f),
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = ResultMuted
                        )
                    }
                }

                Spacer(Modifier.height(30.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ResultAction(
                        title = "Save day",
                        icon = "save",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            onSaveDay?.invoke()
                                ?: showMessage(
                                    "Saving is not connected yet."
                                )
                        }
                    )

                    ResultAction(
                        title = "Start route",
                        icon = "send",
                        filled = true,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            val first = mappedStops.firstOrNull()
                            val last = mappedStops.lastOrNull()

                            if (first == null) {
                                showMessage(
                                    "No named landmarks are available to open."
                                )
                            } else {
                                val uri = if (
                                    mappedStops.size > 1 && last != null
                                ) {
                                    Uri.parse(
                                        "https://www.google.com/maps/dir/"
                                    )
                                        .buildUpon()
                                        .appendQueryParameter("api", "1")
                                        .appendQueryParameter(
                                            "origin",
                                            first.mapsQuery
                                        )
                                        .appendQueryParameter(
                                            "destination",
                                            last.mapsQuery
                                        )
                                        .build()
                                } else {
                                    Uri.parse(
                                        "https://www.google.com/maps/search/"
                                    )
                                        .buildUpon()
                                        .appendQueryParameter("api", "1")
                                        .appendQueryParameter(
                                            "query",
                                            first.mapsQuery
                                        )
                                        .build()
                                }

                                try {
                                    context.startActivity(
                                        Intent(Intent.ACTION_VIEW, uri)
                                    )

                                    if (mappedStops.size < stops.size) {
                                        showMessage(
                                            "Google Maps includes named " +
                                                    "landmarks only. Sample " +
                                                    "activities are excluded."
                                        )
                                    }
                                } catch (_: ActivityNotFoundException) {
                                    showMessage(
                                        "No app is available to open Google Maps."
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ItineraryPlaceCard(
    place: ResultPlace,
    number: Int,
    time: String,
    minutes: Int
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(ResultGreen, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = number.toString(),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Text(
                    text = time,
                    modifier = Modifier.weight(1f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ResultGreen
                )

                Text(
                    text = "$minutes min",
                    fontSize = 12.sp,
                    color = ResultMuted
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ItineraryPlaceImage(place.imageName)

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = place.name,
                        fontSize = 16.sp,
                        lineHeight = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ResultText
                    )

                    Text(
                        text = place.description,
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = ResultMuted
                    )
                }
            }
        }
    }
}

@Composable
private fun ItineraryPlaceImage(imageName: String) {
    val context = LocalContext.current

    // Optional images: the screen also works before they are added.
    val imageId = remember(context, imageName) {
        val id = context.resources.getIdentifier(
            imageName,
            "drawable",
            context.packageName
        )
        if (id != 0) {
            try {
                context.resources.getDrawable(id, null)
                id
            } catch (e: Exception) {
                0
            }
        } else {
            0
        }
    }

    val imageModifier = Modifier
        .size(width = 72.dp, height = 78.dp)
        .clip(RoundedCornerShape(15.dp))

    if (imageId != 0) {
        Image(
            painter = painterResource(imageId),
            contentDescription = null,
            modifier = imageModifier,
            contentScale = ContentScale.Crop
        )
    } else {
        Box(
            modifier = imageModifier.background(ResultBeige),
            contentAlignment = Alignment.Center
        ) {
            ResultIcon("pin", Modifier.size(28.dp))
        }
    }
}

@Composable
private fun ResultAction(
    title: String,
    icon: String,
    modifier: Modifier = Modifier,
    filled: Boolean = false,
    onClick: () -> Unit
) {
    val foreground = if (filled) Color.White else ResultGreen

    Surface(
        onClick = onClick,
        modifier = modifier.heightIn(min = 52.dp),
        shape = RoundedCornerShape(12.dp),
        color = if (filled) ResultGreen else Color.White,
        border = if (filled) null else BorderStroke(1.dp, ResultGreen)
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 14.dp
            ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ResultIcon(
                name = icon,
                modifier = Modifier.size(21.dp),
                tint = foreground
            )

            Spacer(Modifier.width(8.dp))

            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = foreground
            )
        }
    }
}

@Composable
private fun ItineraryRouteMap(stops: List<ResultPlace>) {
    val markerPositions = listOf(
        Offset(0.395f, 0.515f),
        Offset(0.605f, 0.270f),
        Offset(0.780f, 0.800f),
        Offset(0.570f, 0.680f),
        Offset(0.300f, 0.320f)
    ).take(stops.size)

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.01f)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFEDECDD))
    ) {
        val mapWidth = maxWidth
        val mapHeight = maxHeight

        Canvas(Modifier.matchParentSize()) {
            val w = size.width
            val h = size.height

            fun position(x: Float, y: Float) = Offset(w * x, h * y)

            // Green areas.
            drawRoundRect(
                color = Color(0xFFD6E0C5),
                topLeft = position(0.735f, 0.04f),
                size = Size(w * 0.31f, h * 0.34f),
                cornerRadius =
                    androidx.compose.ui.geometry.CornerRadius(w * 0.08f)
            )

            drawRoundRect(
                color = Color(0xFFD6E0C5),
                topLeft = position(0.03f, 0.76f),
                size = Size(w * 0.14f, h * 0.23f),
                cornerRadius =
                    androidx.compose.ui.geometry.CornerRadius(w * 0.06f)
            )

            drawRoundRect(
                color = Color(0xFFE5D8BA),
                topLeft = position(0.305f, 0.36f),
                size = Size(w * 0.18f, h * 0.10f),
                cornerRadius =
                    androidx.compose.ui.geometry.CornerRadius(w * 0.03f)
            )

            // Main roads.
            drawLine(
                Color(0xFFDDB67B),
                position(0f, 0.675f),
                position(1f, 0.675f),
                strokeWidth = 2.dp.toPx()
            )

            drawLine(
                Color(0xFFDDB67B),
                position(0.78f, 0f),
                position(0.78f, 1f),
                strokeWidth = 2.dp.toPx()
            )

            // Local streets.
            listOf(0.24f, 0.395f, 0.605f, 0.77f).forEach { x ->
                drawLine(
                    Color.White,
                    position(x, 0.22f),
                    position(x, 0.86f),
                    strokeWidth = 7.dp.toPx()
                )
            }

            listOf(0.22f, 0.515f, 0.675f, 0.86f).forEach { y ->
                drawLine(
                    Color.White,
                    position(0.24f, y),
                    position(0.77f, y),
                    strokeWidth = 7.dp.toPx()
                )
            }

            if (markerPositions.isNotEmpty()) {
                val first = markerPositions.first()

                val route = Path().apply {
                    moveTo(w * 0.18f, h * 0.88f)
                    lineTo(w * 0.235f, h * 0.88f)
                    lineTo(w * 0.235f, h * first.y)
                    lineTo(w * first.x, h * first.y)

                    markerPositions.drop(1).forEachIndexed { index, point ->
                        val previous = markerPositions[index]

                        if (index == 0) {
                            lineTo(w * point.x, h * previous.y)
                            lineTo(w * point.x, h * point.y)
                        } else {
                            lineTo(w * point.x, h * previous.y)
                            lineTo(w * point.x, h * point.y)
                        }
                    }
                }

                drawPath(
                    route,
                    Color.White,
                    style = Stroke(width = 7.dp.toPx())
                )

                drawPath(
                    route,
                    ResultGreen,
                    style = Stroke(width = 4.dp.toPx())
                )

                drawCircle(
                    ResultGreen.copy(alpha = 0.18f),
                    radius = 12.dp.toPx(),
                    center = position(0.18f, 0.88f)
                )

                drawCircle(
                    Color.White,
                    radius = 6.dp.toPx(),
                    center = position(0.18f, 0.88f)
                )

                drawCircle(
                    ResultGreen,
                    radius = 3.dp.toPx(),
                    center = position(0.18f, 0.88f)
                )
            }
        }

        Text(
            text = "AL HOFUF",
            modifier = Modifier.padding(start = 18.dp, top = 22.dp),
            color = ResultMuted,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Al Koot",
            modifier = Modifier.offset(
                x = mapWidth * 0.05f,
                y = mapHeight * 0.36f
            ),
            color = ResultMuted,
            fontSize = 10.sp
        )

        Text(
            text = "Palm gardens",
            modifier = Modifier.offset(
                x = mapWidth * 0.80f,
                y = mapHeight * 0.30f
            ),
            color = ResultGreen,
            fontSize = 9.sp
        )

        Text(
            text = "King Abdulaziz Road",
            modifier = Modifier.offset(
                x = mapWidth * 0.26f,
                y = mapHeight * 0.70f
            ),
            color = ResultMuted,
            fontSize = 10.sp
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(40.dp)
                .background(Color.White, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            ResultIcon("target", Modifier.size(23.dp))
        }

        markerPositions.forEachIndexed { index, position ->
            Box(
                modifier = Modifier
                    .offset(
                        x = mapWidth * position.x - 15.dp,
                        y = mapHeight * position.y - 15.dp
                    )
                    .size(30.dp)
                    .background(Color.White, CircleShape)
                    .padding(2.dp)
                    .background(ResultGreen, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${index + 1}",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Surface(
                modifier = Modifier.offset(
                    x = (mapWidth * position.x - 46.dp)
                        .coerceIn(4.dp, mapWidth - 108.dp),
                    y = mapHeight * position.y + 18.dp
                ),
                shape = RoundedCornerShape(4.dp),
                color = Color.White
            ) {
                Text(
                    text = stops[index].mapLabel,
                    modifier = Modifier.padding(
                        horizontal = 5.dp,
                        vertical = 3.dp
                    ),
                    fontSize = 9.sp,
                    color = ResultText
                )
            }
        }

        Text(
            text = "Illustrative map · 500 m",
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 14.dp, bottom = 8.dp),
            fontSize = 9.sp,
            color = ResultMuted
        )
    }
}

@Composable
private fun ResultIcon(
    name: String,
    modifier: Modifier = Modifier,
    tint: Color = ResultGreen
) {
    Canvas(modifier) {
        val scale = size.minDimension / 24f
        val stroke = 1.7f * scale

        fun point(x: Float, y: Float) = Offset(x * scale, y * scale)

        fun line(x1: Float, y1: Float, x2: Float, y2: Float) {
            drawLine(
                color = tint,
                start = point(x1, y1),
                end = point(x2, y2),
                strokeWidth = stroke,
                cap = StrokeCap.Round
            )
        }

        fun circle(x: Float, y: Float, radius: Float) {
            drawCircle(
                color = tint,
                radius = radius * scale,
                center = point(x, y),
                style = Stroke(stroke)
            )
        }

        fun path(block: Path.() -> Unit) {
            val result = Path().apply(block)
            val matrix = androidx.compose.ui.graphics.Matrix()
            matrix.scale(scale, scale)
            result.transform(matrix)

            drawPath(
                path = result,
                color = tint,
                style = Stroke(
                    width = stroke,
                    cap = StrokeCap.Round
                )
            )
        }

        when (name) {
            "back" -> {
                line(21f, 12f, 3f, 12f)
                line(3f, 12f, 10f, 5f)
                line(3f, 12f, 10f, 19f)
            }

            "edit" -> {
                path {
                    moveTo(3f, 21f)
                    lineTo(5f, 14f)
                    lineTo(17f, 2f)
                    quadraticTo(19f, 0f, 22f, 4f)
                    lineTo(9f, 18f)
                    close()
                }
                line(15f, 4f, 20f, 9f)
            }

            "refresh" -> {
                path {
                    moveTo(20f, 9f)
                    cubicTo(18f, 0f, 4f, 1f, 3f, 10f)
                    moveTo(4f, 15f)
                    cubicTo(7f, 24f, 21f, 22f, 21f, 13f)
                }
                line(20f, 3f, 20f, 9f)
                line(20f, 9f, 14f, 9f)
                line(4f, 21f, 4f, 15f)
                line(4f, 15f, 10f, 15f)
            }

            "save" -> {
                path {
                    moveTo(5f, 22f)
                    lineTo(5f, 4f)
                    quadraticTo(5f, 2f, 7f, 2f)
                    lineTo(17f, 2f)
                    quadraticTo(19f, 2f, 19f, 4f)
                    lineTo(19f, 22f)
                    lineTo(12f, 17f)
                    close()
                }
            }

            "send" -> {
                path {
                    moveTo(2f, 10f)
                    lineTo(23f, 1f)
                    lineTo(14f, 23f)
                    lineTo(11f, 13f)
                    close()
                    moveTo(11f, 13f)
                    lineTo(23f, 1f)
                }
            }

            "pin" -> {
                path {
                    moveTo(12f, 23f)
                    cubicTo(-4f, 9f, 5f, 1f, 12f, 1f)
                    cubicTo(19f, 1f, 28f, 9f, 12f, 23f)
                    close()
                }
                circle(12f, 9f, 3f)
            }

            "target" -> {
                circle(12f, 12f, 8f)
                circle(12f, 12f, 3f)
                line(12f, 1f, 12f, 5f)
                line(12f, 19f, 12f, 23f)
                line(1f, 12f, 5f, 12f)
                line(19f, 12f, 23f, 12f)
            }

            "info" -> {
                circle(12f, 12f, 10f)
                line(12f, 11f, 12f, 17f)
                drawCircle(tint, scale, point(12f, 7f))
            }

            "coffee" -> {
                path {
                    moveTo(4f, 7f)
                    lineTo(18f, 7f)
                    lineTo(18f, 16f)
                    quadraticTo(18f, 21f, 13f, 21f)
                    lineTo(9f, 21f)
                    quadraticTo(4f, 21f, 4f, 16f)
                    close()
                    moveTo(18f, 9f)
                    cubicTo(25f, 7f, 25f, 17f, 18f, 15f)
                }
                line(6f, 2f, 6f, 4f)
                line(10f, 2f, 10f, 4f)
                line(14f, 2f, 14f, 4f)
            }

            "walk" -> {
                drawOval(
                    color = tint,
                    topLeft = point(3f, 2f),
                    size = Size(5f * scale, 10f * scale),
                    style = Stroke(stroke)
                )
                drawOval(
                    color = tint,
                    topLeft = point(14f, 7f),
                    size = Size(5f * scale, 10f * scale),
                    style = Stroke(stroke)
                )
                line(5f, 15f, 5f, 19f)
                line(16f, 20f, 16f, 23f)
            }
        }
    }
}

private fun itineraryTime(totalMinutes: Int): String {
    val normalized = ((totalMinutes % 1440) + 1440) % 1440

    val time = String.format(
        Locale.US,
        "%02d:%02d",
        normalized / 60,
        normalized % 60
    )

    val days = Math.floorDiv(totalMinutes, 1440)

    return if (days > 0) "$time (+$days day)" else time
}

@Preview(
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
fun ItineraryResultScreenPreview() {
    ProjectmobileappTheme {
        ItineraryResultScreen(
            onBack = {},
            onEditPlan = {}
        )
    }
}