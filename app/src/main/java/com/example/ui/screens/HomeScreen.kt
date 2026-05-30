package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.models.MockData
import com.example.models.Restaurant
import com.example.ui.theme.GlassOutline

data class Coupon(
    val title: String, 
    val subtitle: String, 
    val code: String, 
    val rules: String, 
    val eligibility: String, 
    val expiration: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onRestaurantClick: (String) -> Unit,
    onProfileClick: () -> Unit
) {
    var selectedCoupon by remember { mutableStateOf<Coupon?>(null) }
    var showLocationDialog by remember { mutableStateOf(false) }
    var currentLocation by remember { mutableStateOf("123 Main St, New York, NY") }
    var localTempLocation by remember { mutableStateOf(currentLocation) }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { 
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { 
                                localTempLocation = currentLocation
                                showLocationDialog = true 
                            }
                            .padding(vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = "Location", tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Home", fontWeight = FontWeight.ExtraBold, style = MaterialTheme.typography.titleMedium)
                                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, modifier = Modifier.size(20.dp))
                            }
                            Text(currentLocation, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .clickable { onProfileClick() }
                    ) {
                        AsyncImage(
                            model = "https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?w=200&q=80", 
                            contentDescription = "Profile", 
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth().height(56.dp).padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, GlassOutline)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp).fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.width(12.dp))
                        Text("Search \"biryani\" or \"pizza\"", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val banners = listOf(
                        Coupon("50% OFF", "UPTO ₹100", "WELCOME50", "Apply at checkout to get 50% max ₹100.", "Valid for new users only.", "Expires in 2 days"),
                        Coupon("FREE DELIVERY", "ON ORDERS ABOVE ₹199", "TRYNEW", "Get free delivery on orders above ₹199.", "Valid for all users.", "Expires today"),
                        Coupon("FLAT 20% OFF", "UPTO ₹50", "CRAZY20", "Flat 20% off on your order.", "Valid on select restaurants.", "Expires in 1 week")
                    )
                    items(banners.size) { index ->
                        val banner = banners[index]
                        Surface(
                            modifier = Modifier
                                .width(300.dp)
                                .height(160.dp)
                                .clickable { selectedCoupon = banner }
                                .testTag("coupon_banner_${index}"),
                            shape = RoundedCornerShape(24.dp),
                            color = if (index % 2 == 0) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
                            border = BorderStroke(1.dp, GlassOutline)
                        ) {
                            Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(banner.title, fontWeight = FontWeight.ExtraBold, style = MaterialTheme.typography.headlineMedium, color = if (index % 2 == 0) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer)
                                    Spacer(Modifier.height(8.dp))
                                    Text(banner.subtitle, fontWeight = FontWeight.Bold, color = if (index % 2 == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary)
                                    Spacer(Modifier.height(8.dp))
                                    Text("Use code ${banner.code}", style = MaterialTheme.typography.labelMedium)
                                }
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "What's on your mind?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                )
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    val categories = listOf("Burgers" to "🍔", "Pizza" to "🍕", "Sushi" to "🍣", "Healthy" to "🥗", "Desserts" to "🍰", "Biryani" to "🥘", "Drinks" to "🥤")
                    items(categories, key = { "cat_${it.first}" }) { category ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.surface,
                                border = BorderStroke(1.dp, GlassOutline),
                                modifier = Modifier.size(72.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(category.second, style = MaterialTheme.typography.headlineMedium)
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                            Text(category.first, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Top brands for you",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                )
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val topBrands = MockData.restaurants.take(3)
                    items(topBrands, key = { "brand_${it.id}" }) { brand ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onRestaurantClick(brand.id) }) {
                            Surface(
                                shape = CircleShape,
                                border = BorderStroke(1.dp, GlassOutline),
                                modifier = Modifier.size(80.dp)
                            ) {
                                AsyncImage(model = brand.imageUrl, contentDescription = brand.name, contentScale = ContentScale.Crop)
                            }
                            Spacer(Modifier.height(8.dp))
                            Text(brand.name, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                            Text(brand.deliveryTimeInfo, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            item {
                Text(
                    text = "${MockData.restaurants.size} restaurants to explore",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                )
            }
            
            items(MockData.restaurants, key = { "rest_${it.id}" }) { restaurant ->
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    RestaurantCard(
                        restaurant = restaurant,
                        onClick = { onRestaurantClick(restaurant.id) }
                    )
                }
            }
            
            item {
                Spacer(Modifier.height(32.dp))
            }
        }
    }

    selectedCoupon?.let { coupon ->
        AlertDialog(
            onDismissRequest = { selectedCoupon = null },
            title = { Text(text = coupon.title, fontWeight = FontWeight.Bold) },
            text = {
                Column(modifier = Modifier.verticalScroll(androidx.compose.foundation.rememberScrollState())) {
                    Text(text = "Code: ${coupon.code}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Details", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Text(text = coupon.subtitle, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Rules", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Text(text = coupon.rules, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Eligibility", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Text(text = coupon.eligibility, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Expiration", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Text(text = coupon.expiration, style = MaterialTheme.typography.bodyMedium)
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { selectedCoupon = null },
                    modifier = Modifier.testTag("coupon_close_button")
                ) {
                    Text("Close")
                }
            }
        )
    }

    if (showLocationDialog) {
        AlertDialog(
            onDismissRequest = { showLocationDialog = false },
            title = { Text("Set Location") },
            text = {
                OutlinedTextField(
                    value = localTempLocation,
                    onValueChange = { localTempLocation = it },
                    label = { Text("Enter your address") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(onClick = { 
                    currentLocation = localTempLocation
                    showLocationDialog = false 
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLocationDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun RestaurantCard(restaurant: Restaurant, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("restaurant_card_${restaurant.id}"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, GlassOutline),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column {
            Box {
                AsyncImage(
                    model = restaurant.imageUrl,
                    contentDescription = restaurant.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                if (restaurant.offers.isNotEmpty()) {
                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.align(Alignment.BottomStart).padding(16.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = restaurant.offers.first(),
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = restaurant.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Surface(
                        color = Color(0xFF1B5E20),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(text = restaurant.rating.toString(), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium, color = Color.White)
                            Spacer(modifier = Modifier.width(2.dp))
                            Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color.White, modifier = Modifier.size(12.dp))
                       }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = restaurant.cuisines.joinToString(" • "),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "₹200 for one",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = GlassOutline)
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = restaurant.deliveryTimeInfo,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

