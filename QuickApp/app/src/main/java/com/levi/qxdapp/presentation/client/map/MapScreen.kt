package com.levi.qxdapp.presentation.client.map

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.LocalDrink
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*
import com.levi.qxdapp.data.local.StoreRepository
import com.levi.qxdapp.domain.model.StoreType
import com.levi.qxdapp.domain.model.WaterGasStore

// Cores do tema
private val BluePrimary = Color(0xFF1964C3)
private val GreenOpen = Color(0xFF34A853)
private val RedClosed = Color(0xFFEA4335)
private val OrangeGas = Color(0xFFFF6D00)
private val BlueWater = Color(0xFF039BE5)


@Composable
fun QuixadaMapView(
    modifier: Modifier = Modifier,
    heightDp: Int = 280
) {
    val stores = remember { StoreRepository.getStores() }
    var selectedStore by remember { mutableStateOf<WaterGasStore?>(null) }

    val quixadaCenter = LatLng(StoreRepository.QUIXADA_LAT, StoreRepository.QUIXADA_LNG)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(quixadaCenter, StoreRepository.DEFAULT_ZOOM)
    }


    val quixadaBounds = remember {
        LatLngBounds(
            LatLng(-4.9900, -39.0400), // sudoeste
            LatLng(-4.9500, -39.0000)  // nordeste
        )
    }

    Box(modifier = modifier) {
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(heightDp.dp),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                mapType = MapType.NORMAL,
                isMyLocationEnabled = false,
                latLngBoundsForCameraTarget = quixadaBounds,
                minZoomPreference = 13f,
                maxZoomPreference = 18f
            ),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                mapToolbarEnabled = false,
                myLocationButtonEnabled = false,
                compassEnabled = true
            ),
            onMapClick = {
                selectedStore = null
            }
        ) {
            // Marcadores para cada loja
            stores.forEach { store ->
                val position = LatLng(store.latitude, store.longitude)
                val markerColor = when (store.type) {
                    StoreType.WATER -> BitmapDescriptorFactory.HUE_AZURE
                    StoreType.GAS -> BitmapDescriptorFactory.HUE_ORANGE
                    StoreType.BOTH -> BitmapDescriptorFactory.HUE_GREEN
                }

                MarkerInfoWindowContent(
                    state = MarkerState(position = position),
                    icon = BitmapDescriptorFactory.defaultMarker(markerColor),
                    title = store.name,
                    snippet = store.type.label,
                    onInfoWindowClick = {
                        selectedStore = store
                    }
                ) { marker ->
                    // InfoWindow customizada
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        shadowElevation = 4.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = store.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF1A1A1A)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                val typeIcon = when (store.type) {
                                    StoreType.WATER -> Icons.Outlined.LocalDrink
                                    StoreType.GAS -> Icons.Outlined.LocalFireDepartment
                                    StoreType.BOTH -> Icons.Outlined.LocalDrink
                                }
                                Icon(
                                    typeIcon,
                                    contentDescription = null,
                                    tint = BluePrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = store.type.label,
                                    fontSize = 12.sp,
                                    color = BluePrimary,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Surface(
                                color = if (store.isOpen) GreenOpen.copy(alpha = 0.15f) else RedClosed.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = if (store.isOpen) "● ABERTO" else "● FECHADO",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (store.isOpen) GreenOpen else RedClosed,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Toque para detalhes",
                                fontSize = 10.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }


        Surface(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color.White.copy(alpha = 0.95f),
            shadowElevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    "Quixadá-CE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A)
                )
                Spacer(modifier = Modifier.height(4.dp))
                LegendItem(color = BlueWater, text = "Água")
                LegendItem(color = OrangeGas, text = "Gás")
                LegendItem(color = GreenOpen, text = "Água e Gás")
            }
        }

        // Contador de locais no canto superior direito
        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp),
            shape = RoundedCornerShape(12.dp),
            color = BluePrimary,
            shadowElevation = 4.dp
        ) {
            Text(
                "${stores.size} locais",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
            )
        }


        AnimatedVisibility(
            visible = selectedStore != null,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            selectedStore?.let { store ->
                StoreDetailCard(
                    store = store,
                    onDismiss = { selectedStore = null }
                )
            }
        }
    }
}

@Composable
private fun LegendItem(color: Color, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 1.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text, fontSize = 10.sp, color = Color.DarkGray)
    }
}


@Composable
private fun StoreDetailCard(
    store: WaterGasStore,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = store.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF1A1A1A)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val typeColor = when (store.type) {
                            StoreType.WATER -> BlueWater
                            StoreType.GAS -> OrangeGas
                            StoreType.BOTH -> GreenOpen
                        }
                        Surface(
                            color = typeColor.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = store.type.label,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = typeColor,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = if (store.isOpen) GreenOpen.copy(alpha = 0.15f) else RedClosed.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = if (store.isOpen) "● ABERTO" else "● FECHADO",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (store.isOpen) GreenOpen else RedClosed,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Fechar",
                        tint = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Informações da loja
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Outlined.LocationOn,
                    contentDescription = null,
                    tint = BluePrimary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = store.address,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Outlined.Phone,
                    contentDescription = null,
                    tint = BluePrimary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = store.phone,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Rating
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = String.format("%.1f", store.rating),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1A1A1A)
                    )
                }

                // Tempo de entrega
                if (store.deliveryTime.isNotEmpty()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.Schedule,
                            contentDescription = null,
                            tint = BluePrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = store.deliveryTime,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            // Preços
            if (store.waterPrice.isNotEmpty() || store.gasPrice.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (store.waterPrice.isNotEmpty()) {
                        PriceChip(
                            icon = Icons.Outlined.LocalDrink,
                            label = "Água",
                            price = store.waterPrice,
                            color = BlueWater
                        )
                    }
                    if (store.gasPrice.isNotEmpty()) {
                        PriceChip(
                            icon = Icons.Outlined.LocalFireDepartment,
                            label = "Gás",
                            price = store.gasPrice,
                            color = OrangeGas
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PriceChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    price: String,
    color: Color
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.08f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(label, fontSize = 12.sp, color = Color.DarkGray)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                price,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}
