package com.levi.qxdapp.presentation.client.products

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.levi.qxdapp.R


private val ProductBluePrimary = Color(0xFF1964C3)
private val BackgroundGray = Color(0xFFF5F6FA)


data class ProductInfo(
    val id: Int,
    val name: String,
    val subtitle: String,
    val distance: String,
    val rating: String,
    val price: Double,
    val imageResId: Int,
    val isGas: Boolean
)

// ── Dados fixos (hardcoded) ──
val productsData = listOf(
    ProductInfo(
        id = 1,
        name = "Água 20L - Garrafão",
        subtitle = "Gago águas e gás ",
        distance = "1.2km",
        rating = "4.8",
        price = 6.50,
        imageResId = R.drawable.agua,
        isGas = false
    ),
    ProductInfo(
        id = 2,
        name = "Gás P13 - Botijão",
        subtitle = "Gago  águas e gás ",
        distance = "1.2km",
        rating = "4.8",
        price = 115.00,
        imageResId = R.drawable.gas,
        isGas = true
    ),
    ProductInfo(
        id = 3,
        name = "Água 20L - Garrafão",
        subtitle = "JP Águas e Gás",
        distance = "3.8km",
        rating = "4.2",
        price = 6.50,
        imageResId = R.drawable.agua,
        isGas = false
    ),
    ProductInfo(
        id = 4,
        name = "Gás P13 - Botijão",
        subtitle = "Gás Express Quixadá",
        distance = "2.5km",
        rating = "4.5",
        price = 115.00,
        imageResId = R.drawable.gas,
        isGas = true
    )
)


@Composable
fun ProductsScreen(initialFilter: String = "Todos", onBackClick: () -> Unit = {}) {
    var selectedFilter by remember { mutableStateOf(initialFilter) }
    var selectedProduct by remember { mutableStateOf<ProductInfo?>(null) }

    val filteredProducts = productsData.filter {
        when (selectedFilter) {
            "Água" -> !it.isGas
            "Gás" -> it.isGas
            else -> true
        }
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .statusBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.Black
                        )
                    }
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        placeholder = {
                            Text(
                                "Buscar água, gás, marcas...",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Buscar",
                                tint = Color.Gray
                            )
                        },
                        shape = RoundedCornerShape(25.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = BackgroundGray,
                            unfocusedContainerColor = BackgroundGray,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        singleLine = true
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Todos", "Água", "Gás").forEach { filter ->
                        ProductFilterChip(
                            text = filter,
                            isSelected = selectedFilter == filter,
                            onClick = { selectedFilter = filter }
                        )
                    }
                }
                HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(BackgroundGray)
        ) {
            Text(
                text = "${filteredProducts.size} resultados encontrados",
                modifier = Modifier.padding(16.dp),
                color = Color.Gray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredProducts) { product ->
                    ProductItemCard(
                        product = product,
                        onClick = { selectedProduct = product }
                    )
                }
            }
        }
    }

    if (selectedProduct != null) {
        ProductDetailsDialog(
            product = selectedProduct!!,
            onDismiss = { selectedProduct = null }
        )
    }
}


@Composable
private fun ProductFilterChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clickable { onClick() }
            .background(
                color = if (isSelected) ProductBluePrimary else Color.White,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else Color.DarkGray,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}

// ── Card de produto ──
@Composable
private fun ProductItemCard(product: ProductInfo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (product.isGas) Color(0xFFFFF3E0) else Color(0xFFE3F2FD)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = product.imageResId),
                    contentDescription = product.name,
                    modifier = Modifier.size(40.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.width(16.dp))


            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    text = product.subtitle,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.LocationOn,
                        contentDescription = "Distância",
                        tint = ProductBluePrimary,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = product.distance,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 2.dp, end = 8.dp)
                    )
                    Icon(
                        Icons.Default.Star,
                        contentDescription = "Avaliação",
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = product.rating,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }
            }


            Text(
                text = "R$ " + String.format("%.2f", product.price).replace(".", ","),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF2E7D32)
            )
        }
    }
}


@Composable
private fun ProductDetailsDialog(product: ProductInfo, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = {
            Text(text = "Detalhes do Produto", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (product.isGas) Color(0xFFFFF3E0) else Color(0xFFE3F2FD)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = product.imageResId),
                        contentDescription = product.name,
                        modifier = Modifier.size(64.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                HorizontalDivider(color = Color(0xFFEEEEEE))

                if (product.isGas) {

                    Text(
                        text = "💰 Formas de pagamento:",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("PIX (com desconto)", fontWeight = FontWeight.Medium)
                            Text(
                                "R$ 115,00",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                        }
                    }
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Cartão de Crédito", fontWeight = FontWeight.Medium)
                            Text(
                                "R$ 130,00",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE65100)
                            )
                        }
                    }
                    Text(
                        text = "Gás de cozinha 13kg. Preço varia de R$ 115,00 a R$ 130,00 dependendo da forma de pagamento.",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                } else {

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Preço único", fontWeight = FontWeight.Medium)
                            Text(
                                "R$ 6,50",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                        }
                    }
                    Text(
                        text = "Galão de água mineral de 20 litros vendido em Quixadá - CE.",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = ProductBluePrimary)
            ) {
                Text("Fechar")
            }
        }
    )
}
