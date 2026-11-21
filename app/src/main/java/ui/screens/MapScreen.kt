package ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val ScreenBg = Color(0xFFF7F5EE)
val PrimaryGreen = Color(0xFF1E6648)
val LightPillBg = Color(0xFFEFE6D8)
val SelectedChipBg = Color(0xFFE1ECE6)
val DarkText = Color(0xFF1F292E)
val MutedText = Color(0xFF6B7278)

@Composable
fun MapScreen(
    onViewPlaceClick: (String) -> Unit,
    onBackClick: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToExplore: () -> Unit = {},
    onNavigateToCommunity: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var isMapMode by remember { mutableStateOf(true) }
    var selectedCategory by remember { mutableStateOf("All places") }
    var selectedPlace by remember { mutableStateOf(RiyadhDemoData.places.first()) }
    var savedIds by remember { mutableStateOf(setOf("boulevard_city", "diriyah_turaif")) }

    val categories = listOf("All places", "Heritage", "Food", "Entertainment", "Crafts")

    val filteredPlaces = remember(selectedCategory, searchQuery) {
        RiyadhDemoData.places.filter { place ->
            val matchesCategory = selectedCategory == "All places" ||
                    place.filterCategory.equals(selectedCategory, ignoreCase = true) ||
                    place.categoryBadge.contains(selectedCategory, ignoreCase = true)
            val matchesSearch = searchQuery.isBlank() ||
                    place.title.contains(searchQuery, ignoreCase = true) ||
                    place.locationText.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    LaunchedEffect(filteredPlaces) {
        if (filteredPlaces.isNotEmpty() && filteredPlaces.none { it.id == selectedPlace.id }) {
            selectedPlace = filteredPlaces.first()
        }
    }

    Scaffold(
        containerColor = ScreenBg,
        bottomBar = { LocalLensBottomBar(
            selectedTab = "Map",
            onNavigateToHome = onNavigateToHome,
            onNavigateToExplore = onNavigateToExplore,
            onNavigateToCommunity = onNavigateToCommunity,
            onNavigateToProfile = onNavigateToProfile
        ) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // العنوان العلوي مع سهم الرجوع للصفحة السابقة (واجهات البنات)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = PrimaryGreen
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "RIYADH",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryGreen,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = "Explore Riyadh",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                    }
                }
                IconButton(onClick = { selectedCategory = "All places"; searchQuery = "" }) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Reset Filter",
                        tint = MutedText
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // شريط البحث
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFFE6E2D8)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 14.dp)
                ) {
                    Icon(Icons.Default.Search, contentDescription = null, tint = MutedText, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        if (searchQuery.isEmpty()) {
                            Text("Search Boulevard, Diriyah...", color = MutedText, fontSize = 14.sp)
                        }
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    if (searchQuery.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            tint = MutedText,
                            modifier = Modifier
                                .size(18.dp)
                                .clickable { searchQuery = "" }
                        )
                    } else {
                        Icon(Icons.Default.Tune, contentDescription = null, tint = PrimaryGreen, modifier = Modifier.size(20.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // زر التبديل List / Map
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(LightPillBg)
                    .padding(4.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (!isMapMode) Color.White else Color.Transparent)
                        .clickable { isMapMode = false }
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "List",
                        fontWeight = if (!isMapMode) FontWeight.Bold else FontWeight.Medium,
                        color = if (!isMapMode) PrimaryGreen else MutedText,
                        fontSize = 14.sp
                    )
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isMapMode) Color.White else Color.Transparent)
                        .clickable { isMapMode = true }
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "Map",
                        fontWeight = if (isMapMode) FontWeight.Bold else FontWeight.Medium,
                        color = if (isMapMode) PrimaryGreen else MutedText,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // أزرار الفئات (Chips)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    val isSelected = selectedCategory == category
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) PrimaryGreen else LightPillBg)
                            .clickable { selectedCategory = category }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = category,
                            color = if (isSelected) Color.White else DarkText,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (isMapMode) {
                // === الخريطة التفاعلية ===
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(275.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFFECE7D8))
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height

                        drawRoundRect(
                            color = Color(0xFFD5DFCA),
                            topLeft = Offset(w * 0.72f, h * 0.05f),
                            size = Size(w * 0.28f, h * 0.36f),
                            cornerRadius = CornerRadius(32f, 32f)
                        )
                        drawRoundRect(
                            color = Color(0xFFD5DFCA),
                            topLeft = Offset(w * 0.03f, h * 0.70f),
                            size = Size(w * 0.16f, h * 0.24f),
                            cornerRadius = CornerRadius(28f, 28f)
                        )
                        drawRoundRect(
                            color = Color(0xFFDFD3BC),
                            topLeft = Offset(w * 0.28f, h * 0.30f),
                            size = Size(w * 0.20f, h * 0.12f),
                            cornerRadius = CornerRadius(18f, 18f)
                        )

                        val streetColor = Color.White.copy(alpha = 0.85f)
                        val strokeW = 14f
                        drawLine(streetColor, Offset(w * 0.20f, 0.22f * h), Offset(w * 0.82f, 0.22f * h), strokeW)
                        drawLine(streetColor, Offset(w * 0.20f, 0.45f * h), Offset(w * 0.82f, 0.45f * h), strokeW)
                        drawLine(streetColor, Offset(w * 0.20f, 0.65f * h), Offset(w * 0.82f, 0.65f * h), strokeW)
                        drawLine(streetColor, Offset(w * 0.20f, 0.88f * h), Offset(w * 0.82f, 0.88f * h), strokeW)

                        drawLine(streetColor, Offset(w * 0.24f, 0.18f * h), Offset(w * 0.24f, 0.90f * h), strokeW)
                        drawLine(streetColor, Offset(w * 0.44f, 0.18f * h), Offset(w * 0.44f, 0.90f * h), strokeW)
                        drawLine(streetColor, Offset(w * 0.64f, 0.18f * h), Offset(w * 0.64f, 0.90f * h), strokeW)
                        drawLine(streetColor, Offset(w * 0.80f, 0.18f * h), Offset(w * 0.80f, 0.90f * h), strokeW)

                        val mainRoad = Color(0xFFC5AC83)
                        drawLine(mainRoad, Offset(0f, h * 0.65f), Offset(w, h * 0.65f), 3f)
                        drawLine(mainRoad, Offset(w * 0.58f, 0f), Offset(w * 0.58f, h), 3f)
                    }

                    Text("RIYADH", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MutedText, modifier = Modifier.padding(14.dp))
                    Text("Wadi Hanifah", fontSize = 10.sp, color = PrimaryGreen, modifier = Modifier.align(Alignment.CenterEnd).padding(end = 10.dp, bottom = 50.dp))
                    Text("King Fahd Road", fontSize = 10.sp, color = MutedText, modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 72.dp, end = 25.dp))
                    Text("Illustrative map · 2 km", fontSize = 9.sp, color = MutedText, modifier = Modifier.align(Alignment.BottomStart).padding(10.dp))

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color.White,
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .size(34.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.MyLocation, contentDescription = "My location", tint = PrimaryGreen, modifier = Modifier.size(18.dp))
                        }
                    }

                    // دبابيس الأماكن: الضغط على أي دبوس يفتح صفحة الديتيلز مباشرة
                    filteredPlaces.forEach { place ->
                        val isSelected = selectedPlace.id == place.id
                        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                            val xOffset = (maxWidth * place.mapXFraction - 42.dp).coerceIn(6.dp, maxWidth - 105.dp)
                            val yOffset = (maxHeight * place.mapYFraction - 16.dp).coerceIn(26.dp, maxHeight - 44.dp)
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .offset(x = xOffset, y = yOffset)
                                    .clickable {
                                        selectedPlace = place
                                        onViewPlaceClick(place.id)
                                    }
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(if (isSelected) 22.dp else 18.dp)
                                        .clip(CircleShape)
                                        .background(PrimaryGreen)
                                        .border(2.dp, Color.White, CircleShape)
                                ) {
                                    Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(Color.White))
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (isSelected) PrimaryGreen else Color.White,
                                    shadowElevation = 2.dp
                                ) {
                                    Text(
                                        text = place.title,
                                        fontSize = 8.5.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Color.White else DarkText,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // بطاقة المكان المختار + أزرار View place و Directions
                if (filteredPlaces.isNotEmpty()) {
                    val isBookmarked = savedIds.contains(selectedPlace.id)
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White,
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onViewPlaceClick(selectedPlace.id) }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Image(
                                painter = painterResource(id = selectedPlace.imageRes),
                                contentDescription = selectedPlace.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(selectedPlace.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkText)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("Selected · ${selectedPlace.distanceText}", fontSize = 12.sp, color = MutedText)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${selectedPlace.rating} ★ · ${selectedPlace.durationText.removePrefix("Allow ")}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = PrimaryGreen
                                )
                            }
                            IconButton(onClick = {
                                savedIds = if (isBookmarked) savedIds - selectedPlace.id else savedIds + selectedPlace.id
                            }) {
                                Icon(
                                    imageVector = if (isBookmarked) Icons.Default.BookmarkAdded else Icons.Outlined.BookmarkBorder,
                                    contentDescription = "Save",
                                    tint = PrimaryGreen
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { onViewPlaceClick(selectedPlace.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                        ) {
                            Text("View place", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.White)
                        }

                        OutlinedButton(
                            onClick = { },
                            border = BorderStroke(1.dp, PrimaryGreen),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                        ) {
                            Text("Directions", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = PrimaryGreen)
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // قسم عرض جميع الأماكن أسفل الخريطة عشان تنزلين وتشوفينها كلها
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (selectedCategory == "All places") "All places in Riyadh (${filteredPlaces.size})" else "$selectedCategory (${filteredPlaces.size})",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        Text(
                            text = "Tap to view",
                            fontSize = 12.sp,
                            color = PrimaryGreen,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        filteredPlaces.forEach { place ->
                            PlaceListItemCard(
                                place = place,
                                onClick = {
                                    selectedPlace = place
                                    onViewPlaceClick(place.id)
                                }
                            )
                        }
                    }
                }
            } else {
                // === وضع القائمة الكاملة (LIST VIEW) ===
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    filteredPlaces.forEach { place ->
                        PlaceListItemCard(
                            place = place,
                            onClick = { onViewPlaceClick(place.id) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun PlaceListItemCard(
    place: PlaceExperience,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Image(
                painter = painterResource(id = place.imageRes),
                contentDescription = place.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(place.categoryBadge, fontSize = 11.sp, color = PrimaryGreen, fontWeight = FontWeight.SemiBold)
                Text(place.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkText)
                Text(place.locationText, fontSize = 12.sp, color = MutedText)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${place.rating} ★ (${place.reviewCount}) · ${place.durationText}",
                    fontSize = 12.sp,
                    color = PrimaryGreen,
                    fontWeight = FontWeight.Medium
                )
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MutedText)
        }
    }
}

@Composable
fun LocalLensBottomBar(
    selectedTab: String,
    onNavigateToHome: () -> Unit = {},
    onNavigateToExplore: () -> Unit = {},
    onNavigateToCommunity: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 4.dp
    ) {
        val items = listOf(
            Pair("Home", Icons.Outlined.Home),
            Pair("Explore", Icons.Outlined.Explore),
            Pair("Map", Icons.Outlined.Map),
            Pair("Community", Icons.Outlined.Groups),
            Pair("Profile", Icons.Outlined.PersonOutline)
        )

        items.forEach { (label, icon) ->
            val isSelected = selectedTab == label
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    when (label) {
                        "Home" -> onNavigateToHome()
                        "Explore" -> onNavigateToExplore()
                        "Community" -> onNavigateToCommunity()
                        "Profile" -> onNavigateToProfile()
                    }
                },
                icon = {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) SelectedChipBg else Color.Transparent)
                            .padding(horizontal = 14.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            tint = if (isSelected) PrimaryGreen else MutedText
                        )
                    }
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) PrimaryGreen else MutedText
                    )
                },
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
            )
        }
    }
}