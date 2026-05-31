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
            name = "Distribuidora de Gás Emanoel",
            latitude = -4.9685,
            longitude = -39.0155,
            type = StoreType.GAS,
            isOpen = true,
            phone = "(88) 3412-0000",
            address = "Rua Basílio Emiliano Pinto, 711 - Centro, Quixadá - CE",
            rating = 4.8f,
            deliveryTime = "~15 min",
            waterPrice = "",
            gasPrice = "R$ 115,00"
        ),
        WaterGasStore(
            id = 2,
            name = "Nacional Gás - Revenda Rui Barbosa",
            latitude = -4.9680,
            longitude = -39.0160,
            type = StoreType.BOTH,
            isOpen = true,
            phone = "(88) 3412-0141",
            address = "Rua Rui Barbosa, 728 - Centro, Quixadá - CE",
            rating = 4.5f,
            deliveryTime = "~25 min",
            waterPrice = "R$ 12,00",
            gasPrice = "R$ 110,00"
        ),
        WaterGasStore(
            id = 3,
            name = "Nacional Gás - Revenda Epitácio Pessoa",
            latitude = -4.9675,
            longitude = -39.0135,
            type = StoreType.GAS,
            isOpen = true,
            phone = "(88) 3412-1566",
            address = "Rua Epitácio Pessoa, S/N - Centro, Quixadá - CE",
            rating = 4.6f,
            deliveryTime = "~10 min",
            waterPrice = "",
            gasPrice = "R$ 112,00"
        ),
        WaterGasStore(
            id = 4,
            name = "Campo Novo Água e Gás",
            latitude = -4.9742,
            longitude = -39.0122,
            type = StoreType.BOTH,
            isOpen = false,
            phone = "(88) 99735-0430",
            address = "Rua da Estrela, 134 - Campo Novo, Quixadá - CE",
            rating = 4.2f,
            deliveryTime = "~30 min",
            waterPrice = "R$ 11,00",
            gasPrice = "R$ 118,00"
        ),
        WaterGasStore(
            id = 5,
            name = "Distribuidora Monólitos Água e Gás",
            latitude = -4.9705,
            longitude = -39.0170,
            type = StoreType.BOTH,
            isOpen = true,
            phone = "(88) 99999-0005",
            address = "Av. Plácido Castelo, 1200 - Centro, Quixadá - CE",
            rating = 4.9f,
            deliveryTime = "~20 min",
            waterPrice = "R$ 13,00",
            gasPrice = "R$ 112,00"
        ),
        WaterGasStore(
            id = 6,
            name = "Água Pura Quixadá",
            latitude = -4.9715,
            longitude = -39.0148,
            type = StoreType.WATER,
            isOpen = true,
            phone = "(88) 99999-0006",
            address = "Rua Senador Pompeu, 320 - Centro, Quixadá - CE",
            rating = 4.7f,
            deliveryTime = "~12 min",
            waterPrice = "R$ 9,00",
            gasPrice = ""
        ),
        WaterGasStore(
            id = 7,
            name = "Depósito Água e Gás Juvêncio Alves",
            latitude = -4.9691,
            longitude = -39.0142,
            type = StoreType.BOTH,
            isOpen = true,
            phone = "(88) 99999-0007",
            address = "Rua Juvêncio Alves, 180 - Centro, Quixadá - CE",
            rating = 4.3f,
            deliveryTime = "~35 min",
            waterPrice = "R$ 10,00",
            gasPrice = "R$ 108,00"
        ),
        WaterGasStore(
            id = 8,
            name = "Gás Express Quixadá",
            latitude = -4.9730,
            longitude = -39.0115,
            type = StoreType.GAS,
            isOpen = false,
            phone = "(88) 99999-0008",
            address = "Av. José de Borba Vasconcelos, 812 - Campo Novo, Quixadá - CE",
            rating = 4.1f,
            deliveryTime = "~40 min",
            waterPrice = "",
            gasPrice = "R$ 120,00"
        )
    )

}

