package com.levi.qxdapp.domain.model


data class WaterGasStore(
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val type: StoreType,
    val isOpen: Boolean,
    val phone: String,
    val address: String,
    val rating: Float = 0f,
    val deliveryTime: String = "",
    val waterPrice: String = "",
    val gasPrice: String = ""
)

enum class StoreType(val label: String) {
    WATER("Água"),
    GAS("Gás"),
    BOTH("Água e Gás")
}
