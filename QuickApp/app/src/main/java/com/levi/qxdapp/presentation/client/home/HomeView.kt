package com.levi.qxdapp.presentation.client.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.levi.qxdapp.data.local.StoreRepository
import com.levi.qxdapp.domain.model.WaterGasStore
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.levi.qxdapp.presentation.client.map.QuixadaMapView

// cores para o tema
private val BluePrimary = Color(0xFF1964C3)
private val BackgroundGray = Color(0xFFF5F6FA)
private val TextDark = Color(0xFF1A1A1A)
private val GreenOpen = Color(0xFF34A853)
private val RedClosed = Color(0xFFEA4335)

@Composable
fun HomeView(onSearchClick: (String) -> Unit = {}) {
    val listState = rememberLazyListState()
    val stores = remember { StoreRepository.getStores() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            item { HeaderSection(onSearchClick = onSearchClick) }
            item { QuixadaMapView() }
            item { StoreListHeader(storeCount = stores.size) }
            items(stores) { store ->
                val distance = when (store.id) {
                    1 -> "1.0km"
                    2 -> "1.2km"
                    3 -> "1.5km"
                    4 -> "3.0km"
                    5 -> "2.0km"
                    6 -> "1.8km"
                    7 -> "1.4km"
                    8 -> "2.5km"
                    else -> "1.2km"
                }
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    StoreCard(
                        store = store,
                        distance = distance
                    )
                }
            }
        }

        // Scrollbar vertical à direita
        VerticalScrollbar(
            listState = listState,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .padding(end = 4.dp, top = 4.dp, bottom = 4.dp)
        )
    }
}

@Composable
fun HeaderSection(onSearchClick: (String) -> Unit = {}) {
    val corFundoTransparente = Color.White.copy(alpha = 0.15f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = BluePrimary,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(corFundoTransparente, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "JS", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }


            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = "Localização atual",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = "Ícone Localização",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Centro, Quixadá - CE",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }


            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(corFundoTransparente, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Schedule,
                    contentDescription = "Histórico",
                    tint = Color.White
                )

                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(Color(0xFFFF3B30), CircleShape)
                        .align(Alignment.TopEnd)
                        .offset(x = (-2).dp, y = 2.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier.fillMaxWidth().clickable { onSearchClick("Todos") }) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar fornecedor ou produto...", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color.Gray) },
                shape = RoundedCornerShape(50),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    disabledContainerColor = Color.White,
                    disabledBorderColor = Color.Transparent,
                    disabledTextColor = Color.Black
                ),
                singleLine = true,
                enabled = false
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val filtros = listOf("Todos", "Água", "Gás", "Abertos agora")
            items(filtros) { filter ->
                FilterChipCustom(
                    text = filter,
                    isSelected = filter == "Todos",
                    colorPrimary = BluePrimary,
                    onClick = {
                        onSearchClick(filter)
                    }
                )
            }
        }
    }
}

@Composable
fun FilterChipCustom(text: String, isSelected: Boolean, colorPrimary: Color, onClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .clickable { onClick() }
            .background(
                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.15f),
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = if (isSelected) colorPrimary else Color.White,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}


@Composable
fun StoreListHeader(storeCount: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Locais de Venda", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDark)
        Surface(
            color = BluePrimary.copy(alpha = 0.1f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                "$storeCount locais",
                color = BluePrimary,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun StoreCard(store: WaterGasStore, distance: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color.Gray)
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)).background(Color.White))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(store.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }

                    Surface(
                        color = if (store.isOpen) GreenOpen else RedClosed,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (store.isOpen) "• ABERTO" else "• FECHADO",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }


            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(store.deliveryTime, fontSize = 12.sp, color = Color.Gray)
                    Text(distance, fontSize = 12.sp, color = Color.Gray)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color(0xFFFFC107), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(String.format("%.1f", store.rating), fontSize = 12.sp, color = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))


                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (store.waterPrice.isNotEmpty()) {
                        ProductChip("Água", store.waterPrice)
                    }
                    if (store.gasPrice.isNotEmpty()) {
                        ProductChip("Gás", store.gasPrice)
                    }
                }
            }
        }
    }
}

@Composable
fun ProductChip(type: String, price: String) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
        color = Color.White
    ) {
        Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
            Text(type, fontSize = 12.sp, color = Color.DarkGray)
            Spacer(modifier = Modifier.width(8.dp))
            Text(price, fontSize = 12.sp, color = BluePrimary, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun VerticalScrollbar(
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    val layoutInfo = listState.layoutInfo
    val totalItemsCount = layoutInfo.totalItemsCount
    val visibleItemsInfo = layoutInfo.visibleItemsInfo

    if (totalItemsCount == 0 || visibleItemsInfo.isEmpty()) return

    val viewportHeight = layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset
    val avgItemHeight = visibleItemsInfo.sumOf { it.size } / visibleItemsInfo.size
    val estimatedTotalHeight = avgItemHeight * totalItemsCount

    if (estimatedTotalHeight <= viewportHeight) return

    val thumbFraction = (viewportHeight.toFloat() / estimatedTotalHeight).coerceIn(0.08f, 1f)

    val firstItem = visibleItemsInfo.first()
    val scrolledPx = firstItem.index * avgItemHeight - firstItem.offset
    val scrollFraction = (scrolledPx.toFloat() / (estimatedTotalHeight - viewportHeight)).coerceIn(0f, 1f)

    Canvas(modifier = modifier.width(6.dp)) {
        val trackHeight = size.height
        val thumbHeight = (trackHeight * thumbFraction).coerceAtLeast(40.dp.toPx())
        val availableTrack = trackHeight - thumbHeight
        val thumbTop = availableTrack * scrollFraction

        drawRoundRect(
            color = Color.Gray.copy(alpha = 0.15f),
            size = Size(size.width, trackHeight),
            cornerRadius = CornerRadius(size.width / 2)
        )
        drawRoundRect(
            color = BluePrimary.copy(alpha = 0.6f),
            topLeft = Offset(0f, thumbTop),
            size = Size(size.width, thumbHeight),
            cornerRadius = CornerRadius(size.width / 2)
        )
    }
}
