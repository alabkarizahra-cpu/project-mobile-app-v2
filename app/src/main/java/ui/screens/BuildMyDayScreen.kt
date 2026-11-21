package ui.screens

import android.app.TimePickerDialog
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_mobile_app.ui.theme.ProjectmobileappTheme
import kotlinx.coroutines.launch
import java.util.Locale

// Screen colors sampled approximately from the reference image.
private val DayGreen = Color(0xFF196F56)
private val DayBackground = Color(0xFFFAF8F4)
private val DaySelected = Color(0xFFE7F1EB)
private val DayText = Color(0xFF222D37)
private val DayMuted = Color(0xFF6D7890)
private val DayBorder = Color(0xFFE5E5DE)

@Composable
fun BuildMyDayScreen(
    onBack: () -> Unit = {},
    onUseCurrentLocation: (() -> Unit)? = null,
    onGenerateItinerary: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }

    var selectedInterests by rememberSaveable {
        mutableStateOf(listOf("Heritage", "Food", "Crafts"))
    }
    var selectedHours by rememberSaveable { mutableStateOf(4) }
    var startHour by rememberSaveable { mutableStateOf(9) }
    var startMinute by rememberSaveable { mutableStateOf(0) }
    var startingPoint by rememberSaveable {
        mutableStateOf("Al Hofuf, Al Ahsa")
    }
    var searchText by rememberSaveable { mutableStateOf("") }
    var showResult by rememberSaveable { mutableStateOf(false) }

    BackHandler(enabled = showResult) {
        showResult = false
    }

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Ltr
    ) {
        if (showResult) {
            ItineraryResultScreen(
                interests = selectedInterests,
                durationHours = selectedHours,
                startMinutes = startHour * 60 + startMinute,
                startingPoint = startingPoint,
                onBack = { showResult = false },
                onEditPlan = { showResult = false }
            )
        } else {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding(),
                containerColor = DayBackground,
                snackbarHost = { SnackbarHost(snackbar) }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp)
                        .padding(top = 12.dp, bottom = 24.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier.size(32.dp)
                        ) {
                            DayIcon("back", Modifier.size(24.dp))
                        }

                        Spacer(Modifier.width(8.dp))

                        Text(
                            text = "Build My Day",
                            fontSize = 22.sp,
                            lineHeight = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = DayText
                        )
                    }

                    Spacer(Modifier.height(40.dp))

                    Text(
                        text = "Your time. Your kind of adventure.",
                        fontSize = 28.sp,
                        lineHeight = 38.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DayText
                    )

                    Spacer(Modifier.height(24.dp))

                    Text(
                        text = "Bring together the places you’ll love into one easy-to-follow day.",
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        color = DayMuted
                    )

                    Spacer(Modifier.height(28.dp))

                    DaySection("Interests", completedSegments = 1)

                    Spacer(Modifier.height(20.dp))

                    val interests = listOf(
                        Triple(
                            "Heritage",
                            "Historic places and local stories",
                            "heritage"
                        ),
                        Triple(
                            "Food",
                            "Traditional tastes and coffee stops",
                            "food"
                        ),
                        Triple(
                            "Nature",
                            "Green oases and outdoor escapes",
                            "nature"
                        ),
                        Triple(
                            "Arts",
                            "Creative spaces and exhibitions",
                            "arts"
                        ),
                        Triple(
                            "Crafts",
                            "Hands-on workshops and artisans",
                            "crafts"
                        )
                    )

                    interests.forEachIndexed { index, item ->
                        DayOptionCard(
                            title = item.first,
                            description = item.second,
                            icon = item.third,
                            selected = item.first in selectedInterests,
                            onClick = {
                                selectedInterests =
                                    if (item.first in selectedInterests) {
                                        selectedInterests - item.first
                                    } else {
                                        selectedInterests + item.first
                                    }
                            }
                        )

                        if (index < interests.lastIndex) {
                            Spacer(Modifier.height(20.dp))
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    DaySection("Available time", completedSegments = 2)

                    Spacer(Modifier.height(20.dp))

                    val durations = listOf(
                        Triple(
                            2,
                            "A short escape · 1–2 experiences",
                            "clock"
                        ),
                        Triple(
                            4,
                            "A relaxed morning · 3 experiences",
                            "clock"
                        ),
                        Triple(
                            6,
                            "A fuller day · Time to explore more",
                            "sun"
                        )
                    )

                    durations.forEachIndexed { index, item ->
                        DayOptionCard(
                            title = "${item.first} hours",
                            description = item.second,
                            icon = item.third,
                            selected = selectedHours == item.first,
                            onClick = { selectedHours = item.first }
                        )

                        if (index < durations.lastIndex) {
                            Spacer(Modifier.height(20.dp))
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    Text(
                        text = "Preferred start time",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = DayText
                    )

                    Spacer(Modifier.height(10.dp))

                    Surface(
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
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, DayBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 56.dp)
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = String.format(
                                    Locale.US,
                                    "%02d:%02d",
                                    startHour,
                                    startMinute
                                ),
                                fontSize = 16.sp,
                                color = DayText
                            )

                            DayIcon(
                                name = "clock",
                                modifier = Modifier.size(22.dp),
                                tint = DayMuted
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    DaySection("Starting point", completedSegments = 3)

                    Spacer(Modifier.height(20.dp))

                    DayOptionCard(
                        title = startingPoint,
                        description = if (
                            startingPoint == "Al Hofuf, Al Ahsa"
                        ) {
                            "Selected starting area · Saudi Arabia"
                        } else {
                            "Selected starting area"
                        },
                        icon = "pin",
                        selected = true,
                        onClick = {}
                    )

                    Spacer(Modifier.height(20.dp))

                    OutlinedButton(
                        onClick = {
                            if (onUseCurrentLocation != null) {
                                onUseCurrentLocation()
                            } else {
                                scope.launch {
                                    snackbar.showSnackbar(
                                        "Location is not connected yet. " +
                                                "You can enter your starting area below."
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 52.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, DayGreen),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = DayGreen
                        )
                    ) {
                        DayIcon("target", Modifier.size(20.dp))

                        Spacer(Modifier.width(10.dp))

                        Text(
                            text = "Use current location",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(Modifier.height(20.dp))

                    OutlinedTextField(
                        value = searchText,
                        onValueChange = {
                            searchText = it
                            startingPoint = it.trim().ifBlank {
                                "Al Hofuf, Al Ahsa"
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                text = "Search a city or neighborhood",
                                fontSize = 14.sp,
                                color = DayMuted
                            )
                        },
                        leadingIcon = {
                            DayIcon(
                                "search",
                                Modifier.size(22.dp),
                                DayMuted
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 14.sp,
                            color = DayText
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = DayGreen,
                            unfocusedBorderColor = DayBorder,
                            cursorColor = DayGreen
                        )
                    )

                    Spacer(Modifier.height(20.dp))

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        color = DaySelected
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            DayIcon("shield", Modifier.size(20.dp))

                            Text(
                                text = "Allow location access to suggest nearby places. " +
                                        "Your location is used for planning only; " +
                                        "manual selection works too.",
                                modifier = Modifier.weight(1f),
                                fontSize = 12.sp,
                                lineHeight = 18.sp,
                                color = DayMuted
                            )
                        }
                    }

                    Spacer(Modifier.height(22.dp))

                    Text(
                        text = "This sample plan is a demonstration, not a booking.",
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = DayMuted
                    )

                    Spacer(Modifier.height(30.dp))

                    if (selectedInterests.isEmpty()) {
                        Text(
                            text = "Select at least one interest.",
                            color = DayGreen,
                            fontSize = 12.sp
                        )

                        Spacer(Modifier.height(8.dp))
                    }

                    Button(
                        onClick = { showResult = true },
                        enabled = selectedInterests.isNotEmpty(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DayGreen,
                            contentColor = Color.White
                        )
                    ) {
                        DayIcon(
                            "arrow",
                            Modifier.size(20.dp),
                            Color.White
                        )

                        Spacer(Modifier.width(10.dp))

                        Text(
                            text = "Generate itinerary",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun DaySection(
    title: String,
    completedSegments: Int
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = title,
            fontSize = 12.sp,
            lineHeight = 18.sp,
            color = DayMuted
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            repeat(3) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(5.dp)
                        .background(
                            color = if (index < completedSegments) {
                                DayGreen
                            } else {
                                DayBorder
                            },
                            shape = RoundedCornerShape(10.dp)
                        )
                )
            }
        }
    }
}

@Composable
private fun DayOptionCard(
    title: String,
    description: String,
    icon: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = if (selected) DaySelected else Color.White,
        border = BorderStroke(
            width = if (selected) 1.5.dp else 1.dp,
            color = if (selected) DayGreen else DayBorder
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 84.dp)
                .padding(horizontal = 16.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            DayIcon(icon, Modifier.size(26.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DayText
                )

                Text(
                    text = description,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = DayMuted
                )
            }

            DayIcon(
                name = if (selected) "checked" else "unchecked",
                modifier = Modifier.size(22.dp),
                tint = if (selected) DayGreen else DayBorder
            )
        }
    }
}

// Drawn locally so no additional icon dependency is required.
@Composable
private fun DayIcon(
    name: String,
    modifier: Modifier = Modifier,
    tint: Color = DayGreen
) {
    Canvas(modifier = modifier) {
        val unit = size.minDimension / 24f
        val lineWidth = 1.6f * unit

        fun point(x: Float, y: Float) = Offset(x * unit, y * unit)

        fun line(x1: Float, y1: Float, x2: Float, y2: Float) {
            drawLine(
                color = tint,
                start = point(x1, y1),
                end = point(x2, y2),
                strokeWidth = lineWidth,
                cap = StrokeCap.Round
            )
        }

        fun circle(x: Float, y: Float, radius: Float) {
            drawCircle(
                color = tint,
                radius = radius * unit,
                center = point(x, y),
                style = Stroke(lineWidth)
            )
        }

        fun path(
            close: Boolean = false,
            block: Path.() -> Unit
        ) {
            val result = Path().apply(block)
            if (close) result.close()

            // Path coordinates are scaled to the actual icon size.
            val matrix = androidx.compose.ui.graphics.Matrix()
            matrix.scale(unit, unit)
            result.transform(matrix)

            drawPath(
                path = result,
                color = tint,
                style = Stroke(
                    width = lineWidth,
                    cap = StrokeCap.Round
                )
            )
        }

        when (name) {
            "back" -> {
                line(20f, 12f, 4f, 12f)
                line(4f, 12f, 11f, 5f)
                line(4f, 12f, 11f, 19f)
            }

            "arrow" -> {
                line(4f, 12f, 20f, 12f)
                line(20f, 12f, 13f, 5f)
                line(20f, 12f, 13f, 19f)
            }

            "checked", "unchecked" -> {
                circle(12f, 12f, 9f)

                if (name == "checked") {
                    line(8.5f, 12f, 11f, 14.5f)
                    line(11f, 14.5f, 15.5f, 9.5f)
                }
            }

            "clock" -> {
                circle(12f, 12f, 10f)
                line(12f, 6f, 12f, 12f)
                line(12f, 12f, 16.5f, 14.5f)
            }

            "sun" -> {
                circle(12f, 12f, 4.5f)

                for (index in 0 until 8) {
                    val angle = Math.PI * index / 4
                    val cosine = kotlin.math.cos(angle).toFloat()
                    val sine = kotlin.math.sin(angle).toFloat()

                    line(
                        12f + cosine * 8f,
                        12f + sine * 8f,
                        12f + cosine * 10.5f,
                        12f + sine * 10.5f
                    )
                }
            }

            "heritage" -> {
                path(close = true) {
                    moveTo(2f, 7f)
                    lineTo(12f, 2f)
                    lineTo(22f, 7f)
                }

                line(2f, 9f, 22f, 9f)

                listOf(5f, 10f, 14f, 19f).forEach {
                    line(it, 11f, it, 19f)
                }

                line(3f, 22f, 21f, 22f)
            }

            "food" -> {
                line(3f, 2f, 3f, 9f)
                line(7f, 2f, 7f, 9f)
                line(11f, 2f, 11f, 9f)

                path {
                    moveTo(3f, 9f)
                    quadraticTo(7f, 14f, 11f, 9f)
                }

                line(7f, 12f, 7f, 22f)

                path {
                    moveTo(21f, 2f)
                    cubicTo(14f, 3f, 14f, 15f, 21f, 15f)
                    lineTo(21f, 2f)
                    lineTo(21f, 22f)
                }
            }

            "nature" -> {
                path(close = true) {
                    moveTo(20f, 2f)
                    cubicTo(16f, 8f, 5f, 3f, 4f, 13f)
                    cubicTo(3f, 23f, 24f, 22f, 20f, 2f)
                }

                path {
                    moveTo(2f, 22f)
                    quadraticTo(5f, 13f, 15f, 11f)
                }
            }

            "arts" -> {
                path(close = true) {
                    moveTo(12f, 2f)
                    cubicTo(0f, 2f, -1f, 21f, 11f, 22f)
                    cubicTo(18f, 23f, 10f, 16f, 17f, 16f)
                    cubicTo(26f, 17f, 23f, 2f, 12f, 2f)
                }

                listOf(
                    Offset(7f, 8f),
                    Offset(12f, 6f),
                    Offset(17f, 9f),
                    Offset(6f, 14f)
                ).forEach {
                    drawCircle(
                        color = tint,
                        radius = unit,
                        center = point(it.x, it.y)
                    )
                }
            }

            "crafts" -> {
                path(close = true) {
                    moveTo(7f, 12f)
                    lineTo(7f, 5f)
                    cubicTo(7f, 2f, 10f, 2f, 10f, 5f)
                    lineTo(10f, 11f)
                    lineTo(10f, 3f)
                    cubicTo(10f, 0f, 13f, 0f, 13f, 3f)
                    lineTo(13f, 11f)
                    lineTo(13f, 5f)
                    cubicTo(13f, 2f, 16f, 2f, 16f, 5f)
                    lineTo(16f, 12f)
                    lineTo(16f, 8f)
                    cubicTo(16f, 5f, 19f, 5f, 19f, 8f)
                    lineTo(19f, 16f)
                    cubicTo(19f, 25f, 10f, 24f, 6f, 20f)
                    lineTo(2f, 15f)
                    cubicTo(0f, 12f, 3f, 10f, 7f, 15f)
                }
            }

            "pin" -> {
                path(close = true) {
                    moveTo(12f, 23f)
                    cubicTo(-4f, 9f, 5f, 1f, 12f, 1f)
                    cubicTo(19f, 1f, 28f, 9f, 12f, 23f)
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

            "search" -> {
                circle(10f, 10f, 7f)
                line(15f, 15f, 22f, 22f)
            }

            "shield" -> {
                path(close = true) {
                    moveTo(12f, 2f)
                    lineTo(21f, 6f)
                    lineTo(21f, 13f)
                    quadraticTo(20f, 19f, 12f, 23f)
                    quadraticTo(4f, 19f, 3f, 13f)
                    lineTo(3f, 6f)
                }

                line(8f, 12f, 11f, 15f)
                line(11f, 15f, 16f, 9f)
            }
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
fun BuildMyDayScreenPreview() {
    ProjectmobileappTheme {
        BuildMyDayScreen()
    }
}