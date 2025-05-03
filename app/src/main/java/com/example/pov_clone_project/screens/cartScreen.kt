package com.example.pov_clone_project.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.modifier.modifierLocalOf


@Composable
fun OSMMapPicker(
    modifier: Modifier = Modifier,
    onLocationSelected: (GeoPoint) -> Unit
) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    LaunchedEffect(Unit) {
        Configuration.getInstance().load(
            context,
            context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE)
        )
    }

    AndroidView(
        factory = {
            mapView.apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(15.0)
                controller.setCenter(GeoPoint(28.6139, 77.2090)) // Delhi

                // Create and add MapEventsOverlay
                val receiver = object : MapEventsReceiver {
                    override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                        // Clear old markers
                        overlays.removeAll { it is Marker }

                        val marker = Marker(this@apply).apply {
                            position = p
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            title = "Selected Location"
                        }
                        overlays.add(marker)

                        onLocationSelected(p)
                        return true
                    }

                    override fun longPressHelper(p: GeoPoint): Boolean = false
                }

                val eventsOverlay = MapEventsOverlay(receiver)
                overlays.add(eventsOverlay)
            }
        },
        modifier = modifier.fillMaxSize()
    )
}

@Composable
fun MapScreen(navController: NavController) {
    val context = LocalContext.current

    val mapView = remember { MapView(context) }

    AndroidView(
        factory = {
            mapView.apply {
                Configuration.getInstance().load(
                    context,
                    context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE)
                )

                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(15.0)
                controller.setCenter(GeoPoint(28.6139, 77.2090)) // Delhi

                val receiver = object : MapEventsReceiver {
                    override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                        // Pass back lat/lng to CartScreen
                        navController.previousBackStackEntry?.savedStateHandle?.set(
                            "selected_location",
                            listOf(p.latitude, p.longitude)
                        )
                        navController.popBackStack() // Go back to CartScreen
                        return true
                    }

                    override fun longPressHelper(p: GeoPoint): Boolean = false
                }

                overlays.add(MapEventsOverlay(receiver))
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun CartScreen(navController: NavController, viewModel: MedicineViewModel) {
    val cartItems by viewModel.cartItems.collectAsState()
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val locationData = savedStateHandle?.getStateFlow<List<Double>?>("selected_location", null)
        ?.collectAsState()

    val selectedLocation = locationData?.value?.let {
        GeoPoint(it[0], it[1])
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Cart", style = MaterialTheme.typography.headlineSmall)
        }

        items(cartItems) { item ->
            MedicineCard(medicine = item, navController = navController, viewModel = viewModel,showAddToCartButton = false)
        }

        if (cartItems.isEmpty()) {
            item {
                Text("Cart is empty")
            }
        }

        item {
            Button(
                onClick = { navController.navigate("map") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Select Delivery Location")
            }
        }

        selectedLocation?.let { loc ->
            item {
                Text("Selected Location: Lat ${loc.latitude}, Lon ${loc.longitude}")
            }
        }

        item {
            Button(
                onClick = {
                    if (selectedLocation != null && cartItems.isNotEmpty()) {
                        println("Buying with location $selectedLocation")
                    } else {
                        println("Select location or add items")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Buy Now")
            }
        }
    }
}
