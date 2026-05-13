package com.levi.qxdapp.presentation.client.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// cores para o tema
val BluePrimary = Color(0xFF1964C3)
val BackgroundGray = Color(0xFFF5F6FA)
val TextDark = Color(0xFF1A1A1A)
val GreenOpen = Color(0xFF34A853)
val RedClosed = Color(0xFFEA4335)

@Composable
fun HomeView() {
    val listState = rememberLazyListState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            item { HeaderSection() }
            item { MapPlaceholder() }
            item { StoreListHeader() }
            items(3) { index ->
                val isOpen = index != 2
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    StoreCard(
                        name = if (index == 0) "JP Águas e Gás" else "Gás Express Quixadá",
                        isOpen = isOpen,
                        deliveryTime = if (index == 0) "~15 min" else "~25 min",
                        distance = if (index == 0) "1.2km" else "2.5km",
                        rating = if (index == 0) "4.8" else "4.5"
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
fun HeaderSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BluePrimary)
            .padding(16.dp)
    ) {
        // Top Bar: Usuário e Localização
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text("JS", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Localização atual", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        "Centro, Quixadá - Ce",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Barra de Pesquisa
        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(24.dp)),
            placeholder = { Text("Buscar fornecedor ou produto...", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))


        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(listOf("Todos", "Água", "Gás", "Abertos agora")) { filter ->
                FilterChip(
                    selected = filter == "Todos",
                    onClick = { },
                    label = { Text(filter, color = if (filter == "Todos") BluePrimary else Color.White) },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = if (filter == "Todos") Color.White else Color.Transparent,
                        selectedContainerColor = Color.White
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = filter == "Todos",
                        borderColor = Color.White
                    )
                )
            }
        }
    }
}

@Composable
fun MapPlaceholder() {
    // Espaço reservado para o Google Maps
    // Dica: Substitua este Box pelo GoogleMap do pacote com.google.maps.android:maps-compose
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color(0xFFE0E0E0)),
        contentAlignment = Alignment.Center
    ) {
        Text("Mapa (Google Maps API)", color = Color.DarkGray, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun StoreListHeader() {
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
                "3 locais",
                color = BluePrimary,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun StoreCard(name: String, isOpen: Boolean, deliveryTime: String, distance: String, rating: String) {
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
                    .background(Color.Gray) // Substituir pelo AsyncImage (Coil) carregando as fotos do estabelecimento
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
                        Text(name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }

                    Surface(
                        color = if (isOpen) GreenOpen else RedClosed,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (isOpen) "• ABERTO" else "FECHADO",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Informações (Tempo, Distância, Avaliação)
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(deliveryTime, fontSize = 12.sp, color = Color.Gray)
                    Text(distance, fontSize = 12.sp, color = Color.Gray)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color(0xFFFFC107), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(rating, fontSize = 12.sp, color = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Produtos
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ProductChip("Água", "R$ 12.00")
                    ProductChip("Gás", "R$ 115.00")
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

        // Trilho (track)
        drawRoundRect(
            color = Color.Gray.copy(alpha = 0.15f),
            size = Size(size.width, trackHeight),
            cornerRadius = CornerRadius(size.width / 2)
        )

        // Indicador (thumb)
        drawRoundRect(
            color = BluePrimary.copy(alpha = 0.6f),
            topLeft = Offset(0f, thumbTop),
            size = Size(size.width, thumbHeight),
            cornerRadius = CornerRadius(size.width / 2)
        )
    }
}
