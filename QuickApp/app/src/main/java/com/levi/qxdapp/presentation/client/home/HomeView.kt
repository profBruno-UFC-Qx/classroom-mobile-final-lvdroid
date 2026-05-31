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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.levi.qxdapp.data.local.StoreRepository
import com.levi.qxdapp.domain.model.StoreType
import com.levi.qxdapp.domain.model.WaterGasStore
import com.levi.qxdapp.presentation.client.map.QuixadaMapView

// cores para o tema
private val BluePrimary = Color(0xFF1964C3)
private val BackgroundGray = Color(0xFFF5F6FA)
private val TextDark = Color(0xFF1A1A1A)
private val GreenOpen = Color(0xFF34A853)
private val RedClosed = Color(0xFFEA4335)
private val OrangeGas = Color(0xFFFF6D00)
private val BlueWater = Color(0xFF039BE5)

@Composable
fun HomeView(onSearchClick: (String) -> Unit = {}) {
    val listState = rememberLazyListState()
    var selectedFilter by remember { mutableStateOf("Todos") }
    var searchQuery by remember { mutableStateOf("") }
    
    val filteredStores = remember(selectedFilter, searchQuery) {
        val baseStores = when (selectedFilter) {
            "Água" -> StoreRepository.getStoresByType(StoreType.WATER)
            "Gás" -> StoreRepository.getStoresByType(StoreType.GAS)
            "Abertos agora" -> StoreRepository.getOpenStores()
            else -> StoreRepository.getStores()
        }

        if (searchQuery.isBlank()) {
            baseStores
        } else {
            val query = searchQuery.lowercase().trim()
            baseStores.filter { store ->
                val matchesName = store.name.lowercase().contains(query)
                val isWaterQuery = query.contains("água") || query.contains("agua")
                val isGasQuery = query.contains("gás") || query.contains("gas")
                
                val matchesWater = isWaterQuery && (store.type == StoreType.WATER || store.type == StoreType.BOTH)
                val matchesGas = isGasQuery && (store.type == StoreType.GAS || store.type == StoreType.BOTH)
                
                matchesName || matchesWater || matchesGas
            }
        }
    }

    // Determina o modo de exibição baseado na busca ou filtro
    val effectiveFilterMode = remember(selectedFilter, searchQuery) {
        val query = searchQuery.lowercase().trim()
        when {
            query.contains("água") || query.contains("agua") -> "Água"
            query.contains("gás") || query.contains("gas") -> "Gás"
            else -> selectedFilter
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            item { 
                HeaderSection(
                    selectedFilter = selectedFilter,
                    onFilterChange = { 
                        selectedFilter = it
                        searchQuery = "" // Limpa a busca ao trocar de categoria
                    },
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it }
                ) 
            }
            item { QuixadaMapView(stores = filteredStores) }
            item { StoreListHeader(storeCount = filteredStores.size, filterName = effectiveFilterMode) }
            items(filteredStores) { store ->
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
                        distance = distance,
                        filterMode = effectiveFilterMode
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(80.dp)) }
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
fun HeaderSection(
    selectedFilter: String,
    onFilterChange: (String) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchClick: (String) -> Unit = {}
) {
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
        
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar água, gás ou fornecedor...", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color.Gray) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(Icons.Default.Close, contentDescription = "Limpar", tint = Color.Gray)
                    }
                }
            },
            shape = RoundedCornerShape(50),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = Color.Black
            ),
            textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
            singleLine = true,
            enabled = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
        )

        Spacer(modifier = Modifier.height(16.dp))


        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val filtros = listOf("Todos", "Água", "Gás", "Abertos agora")
            items(filtros) { filter ->
                FilterChipCustom(
                    text = filter,
                    isSelected = filter == selectedFilter,
                    colorPrimary = BluePrimary,
                    onClick = {
                        onFilterChange(filter)
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
fun StoreListHeader(storeCount: Int, filterName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val title = when(filterName) {
            "Água" -> "Distribuidoras de Água"
            "Gás" -> "Pontos de Venda de Gás"
            "Abertos agora" -> "Abertos no Momento"
            else -> "Locais de Venda"
        }
        Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDark)
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
fun StoreCard(store: WaterGasStore, distance: String, filterMode: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Header Image/Color Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(
                        when (store.type) {
                            StoreType.WATER -> BlueWater.copy(alpha = 0.8f)
                            StoreType.GAS -> OrangeGas.copy(alpha = 0.8f)
                            StoreType.BOTH -> BluePrimary.copy(alpha = 0.8f)
                        }
                    )
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
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            val initial = store.name.take(1).uppercase()
                            Text(initial, color = BluePrimary, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            store.name,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.widthIn(max = 200.dp)
                        )
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

                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        Icons.Outlined.LocationOn,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp).padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        store.address,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 1
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Schedule, null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(store.deliveryTime, fontSize = 12.sp, color = Color.Gray)
                    }
                    
                    Text(distance, fontSize = 12.sp, color = Color.Gray)
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color(0xFFFFC107), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(String.format("%.1f", store.rating), fontSize = 12.sp, color = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))


                // Prices based on filter
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    when (filterMode) {
                        "Água" -> {
                            if (store.waterPrice.isNotEmpty()) {
                                ProductChip("Garrafão 20L", store.waterPrice, BlueWater)
                            }
                        }
                        "Gás" -> {
                            if (store.gasPrice.isNotEmpty()) {
                                ProductChip("Botijão 13kg", store.gasPrice, OrangeGas)
                            }
                        }
                        else -> {
                            if (store.waterPrice.isNotEmpty()) {
                                ProductChip("Água", store.waterPrice, BlueWater)
                            }
                            if (store.gasPrice.isNotEmpty()) {
                                ProductChip("Gás", store.gasPrice, OrangeGas)
                            }
                        }
                    }
                }
                

                if (filterMode == "Gás") {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Phone, null, tint = BluePrimary, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(store.phone, fontSize = 12.sp, color = BluePrimary, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

@Composable
fun ProductChip(type: String, price: String, color: Color = BluePrimary) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.3f)),
        color = color.copy(alpha = 0.05f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(type, fontSize = 12.sp, color = Color.DarkGray)
            Spacer(modifier = Modifier.width(8.dp))
            Text(price, fontSize = 13.sp, color = color, fontWeight = FontWeight.Bold)
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
