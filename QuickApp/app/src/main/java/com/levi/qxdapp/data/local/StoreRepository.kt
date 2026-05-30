package com.levi.qxdapp.data.local

import com.levi.qxdapp.domain.model.StoreType
import com.levi.qxdapp.domain.model.WaterGasStore


object StoreRepository {

    /* Coordenadas do centro de Quixadá-CE */
    const val QUIXADA_LAT = -4.9711
    const val QUIXADA_LNG = -39.0153
    const val DEFAULT_ZOOM = 14.5f

    fun getStores(): List<WaterGasStore> = stores

    fun getStoresByType(type: StoreType): List<WaterGasStore> =
        stores.filter { it.type == type || it.type == StoreType.BOTH }

    fun getOpenStores(): List<WaterGasStore> =
        stores.filter { it.isOpen }

    private val stores = listOf(
        WaterGasStore(
            id = 1,
            name = "JP Águas e Gás",
            latitude = -4.9685,
            longitude = -39.0165,
            type = StoreType.BOTH,
            isOpen = true,
            phone = "(88) 99999-0001",
            address = "Rua Epitácio Pessoa, 450 - Centro",
            rating = 4.8f,
            deliveryTime = "~15 min",
            waterPrice = "R$ 12,00",
            gasPrice = "R$ 115,00"
        ),
        WaterGasStore(
            id = 2,
            name = "Gás Express Quixadá",
            latitude = -4.9732,
            longitude = -39.0120,
            type = StoreType.GAS,
            isOpen = true,
            phone = "(88) 99999-0002",
            address = "Av. José de Borba Vasconcelos, 812 - Campo Novo",
            rating = 4.5f,
            deliveryTime = "~25 min",
            waterPrice = "",
            gasPrice = "R$ 110,00"
        ),
        WaterGasStore(
            id = 3,
            name = "Depósito Central de Água",
            latitude = -4.9700,
            longitude = -39.0190,
            type = StoreType.WATER,
            isOpen = true,
            phone = "(88) 99999-0003",
            address = "Rua Padre Mororó, 230 - Centro",
            rating = 4.6f,
            deliveryTime = "~10 min",
            waterPrice = "R$ 10,00",
            gasPrice = ""
        ),
        WaterGasStore(
            id = 4,
            name = "Quixadá Gás e Água",
            latitude = -4.9755,
            longitude = -39.0100,
            type = StoreType.BOTH,
            isOpen = false,
            phone = "(88) 99999-0004",
            address = "Rua Juvêncio Alves, 180 - Campo Velho",
            rating = 4.2f,
            deliveryTime = "~30 min",
            waterPrice = "R$ 11,00",
            gasPrice = "R$ 118,00"
        ),
        WaterGasStore(
            id = 5,
            name = "Distribuidora Monólitos",
            latitude = -4.9660,
            longitude = -39.0210,
            type = StoreType.BOTH,
            isOpen = true,
            phone = "(88) 99999-0005",
            address = "Av. Plácido Castelo, 1200 - Cedro",
            rating = 4.9f,
            deliveryTime = "~20 min",
            waterPrice = "R$ 13,00",
            gasPrice = "R$ 112,00"
        ),
        WaterGasStore(
            id = 6,
            name = "Água Pura Quixadá",
            latitude = -4.9720,
            longitude = -39.0230,
            type = StoreType.WATER,
            isOpen = true,
            phone = "(88) 99999-0006",
            address = "Rua Senador Pompeu, 320 - Centro",
            rating = 4.7f,
            deliveryTime = "~12 min",
            waterPrice = "R$ 9,00",
            gasPrice = ""
        ),
        WaterGasStore(
            id = 7,
            name = "GasLog Distribuidora",
            latitude = -4.9640,
            longitude = -39.0140,
            type = StoreType.GAS,
            isOpen = true,
            phone = "(88) 99999-0007",
            address = "Rua Marechal Deodoro, 560 - Alto São Francisco",
            rating = 4.3f,
            deliveryTime = "~35 min",
            waterPrice = "",
            gasPrice = "R$ 108,00"
        ),
        WaterGasStore(
            id = 8,
            name = "Sertão Água e Gás",
            latitude = -4.9780,
            longitude = -39.0175,
            type = StoreType.BOTH,
            isOpen = false,
            phone = "(88) 99999-0008",
            address = "Av. Universitária, 890 - Jardim Monólitos",
            rating = 4.1f,
            deliveryTime = "~40 min",
            waterPrice = "R$ 14,00",
            gasPrice = "R$ 120,00"
        )
    )
}
