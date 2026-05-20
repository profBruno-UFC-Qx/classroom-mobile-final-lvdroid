package com.levi.qxdapp.presentation.client.order

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Refresh
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.levi.qxdapp.presentation.client.home.VerticalScrollbar

// Cores do tema (reutilizando as mesmas da HomeView)
private val BluePrimary = Color(0xFF1964C3)
private val BackgroundGray = Color(0xFFF5F6FA)
private val TextDark = Color(0xFF1A1A1A)
private val GreenDelivered = Color(0xFF34A853)
private val RedCanceled = Color(0xFFEA4335)
private val OrangeInProgress = Color(0xFFFF9800)
private val GraySubtle = Color(0xFF8A8F9E)



enum class OrderStatus(val label: String, val color: Color) {
    ENTREGUE("Entregue", GreenDelivered),
    EM_ANDAMENTO("Em andamento", OrangeInProgress),
    CANCELADO("Cancelado", RedCanceled)
}

data class OrderItem(
    val quantity: Int,
    val description: String
)

data class Order(
    val supplierName: String,
    val supplierInitials: String,
    val date: String,
    val status: OrderStatus,
    val items: List<OrderItem>,
    val total: String
)

// --- Dados de exemplo (substituir por dados reais do Firebase/API) ---

fun getSampleOrders(): List<Order> = listOf(
    Order(
        supplierName = "JP Águas e Gás",
        supplierInitials = "JP",
        date = "Hoje 15:35",
        status = OrderStatus.EM_ANDAMENTO,
        items = listOf(
            OrderItem(quantity = 1, description = "Água 20L - Garrafão")
        ),
        total = "R$ 13,00"
    ),
    Order(
        supplierName = "Depósito Água Viva",
        supplierInitials = "DA",
        date = "01/04/2026 14:30",
        status = OrderStatus.ENTREGUE,
        items = listOf(
            OrderItem(quantity = 2, description = "Água 20L - Garrafão")
        ),
        total = "R$ 24,00"
    ),
    Order(
        supplierName = "Gás Express Quixadá",
        supplierInitials = "GE",
        date = "28/03/2026 09:15",
        status = OrderStatus.CANCELADO,
        items = listOf(
            OrderItem(quantity = 1, description = "Gás P13 - Botijão")
        ),
        total = "R$ 115,00"
    )
)



@Composable
fun OrdersView() {
    val listState = rememberLazyListState()
    var selectedTab by remember { mutableIntStateOf(0) }
    val allOrders = remember { mutableStateListOf(*getSampleOrders().toTypedArray()) }

    val filteredOrders = when (selectedTab) {
        1 -> allOrders.filter { it.status == OrderStatus.EM_ANDAMENTO }
        2 -> allOrders.filter { it.status == OrderStatus.ENTREGUE || it.status == OrderStatus.CANCELADO }
        else -> allOrders.toList()
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
                OrdersHeaderSection(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            items(filteredOrders, key = { it.supplierName + it.date }) { order ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    OrderCard(
                        order = order,
                        showDelete = selectedTab == 0 || selectedTab == 2,
                        onDelete = { allOrders.remove(order) }
                    )
                }
            }

            // Espaço inferior para não ficar colado à bottom bar
            item { Spacer(modifier = Modifier.height(24.dp)) }
        }


        VerticalScrollbar(
            listState = listState,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .padding(end = 4.dp, top = 4.dp, bottom = 4.dp)
        )
    }
}

// --- Header azul com título e abas ---

@Composable
fun OrdersHeaderSection(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = BluePrimary,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
            .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 20.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Meus Pedidos",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Surface(
                shape = RoundedCornerShape(50),
                color = Color.White.copy(alpha = 0.20f),
                modifier = Modifier.clickable { /* TODO: ação de ajuda */ }
            ) {
                Text(
                    text = "Precisa de ajuda?",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))


        val tabs = listOf("Todos", "Em andamento", "Concluídos")

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(50)
                )
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            tabs.forEachIndexed { index, title ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (selectedTab == index) Color.White else Color.Transparent
                        )
                        .clickable { onTabSelected(index) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        color = if (selectedTab == index) BluePrimary else Color.White,
                        fontSize = 13.sp,
                        fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}



@Composable
fun OrderCard(
    order: Order,
    showDelete: Boolean = false,
    onDelete: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0E0E0)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = order.supplierInitials,
                        color = GraySubtle,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Nome e data
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = order.supplierName,
                        color = TextDark,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = order.date,
                        color = GraySubtle,
                        fontSize = 12.sp
                    )
                }

                // Badge de status
                OrderStatusBadge(status = order.status)


                if (showDelete) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(RedCanceled.copy(alpha = 0.08f))
                            .clickable { onDelete() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.DeleteOutline,
                            contentDescription = "Apagar pedido",
                            tint = RedCanceled,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            HorizontalDivider(
                color = Color(0xFFF0F0F0),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(12.dp))


            Text(
                text = if (order.items.size == 1) "1 item" else "${order.items.size} itens",
                color = GraySubtle,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Lista de itens do pedido
            order.items.forEach { item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .border(
                                width = 1.dp,
                                color = Color(0xFFDDDDDD),
                                shape = RoundedCornerShape(4.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${item.quantity}",
                            color = TextDark,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = item.description,
                        color = TextDark,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Coluna do Total
                Column {
                    Text(
                        text = "Total",
                        color = GraySubtle,
                        fontSize = 12.sp
                    )
                    Text(
                        text = order.total,
                        color = TextDark,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.weight(1f))


                if (order.status == OrderStatus.ENTREGUE) {
                    // Botão Avaliar
                    OutlinedButton(
                        onClick = { /* TODO: ação de avaliar */ },
                        shape = RoundedCornerShape(50),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            Color(0xFFFF9800)
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFFFF9800)
                        ),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Avaliar",
                            modifier = Modifier.size(14.dp),
                            tint = Color(0xFFFF9800)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Avaliar",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Botão Repetir
                    OutlinedButton(
                        onClick = { /* TODO: ação de repetir */ },
                        shape = RoundedCornerShape(50),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            GreenDelivered
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = GreenDelivered
                        ),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Refresh,
                            contentDescription = "Repetir",
                            modifier = Modifier.size(14.dp),
                            tint = GreenDelivered
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Repetir",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                if (order.status == OrderStatus.EM_ANDAMENTO) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Schedule,
                            contentDescription = "Em andamento",
                            tint = GreenDelivered,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "A caminho",
                            color = GreenDelivered,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        LinearProgressIndicator(
                            progress = 0.6f,
                            color = GreenDelivered,
                            trackColor = GreenDelivered.copy(alpha = 0.15f),
                            modifier = Modifier
                                .weight(1f)
                                .height(6.dp)
                                .clip(RoundedCornerShape(50))
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))


                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            color = Color(0xFFF5F6FA),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Ver detalhes",
                        tint = GraySubtle,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}



@Composable
fun OrderStatusBadge(status: OrderStatus) {
    Surface(
        shape = RoundedCornerShape(50),
        color = status.color.copy(alpha = 0.1f)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = when (status) {
                    OrderStatus.ENTREGUE -> Icons.Default.CheckCircle
                    OrderStatus.CANCELADO -> Icons.Default.Cancel
                    OrderStatus.EM_ANDAMENTO -> Icons.Default.CheckCircle
                },
                contentDescription = status.label,
                tint = status.color,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = status.label,
                color = status.color,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

