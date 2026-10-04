package br.com.cepa.mobile.data

data class Wine(
    val name: String,
    val type: String,
    val style: String,
    val region: String,
    val pairing: String,
    val description: String,
    val priceInCents: Int,
    val darkLabel: Boolean,
)

val demoWines = listOf(
    Wine("Quinta do Vale — Reserva 2021", "Tinto", "Encorpado", "Vale dos Vinhedos, RS", "Carnes vermelhas", "Frutas maduras, especiarias e taninos macios.", 8990, true),
    Wine("Serra Alta Sauvignon Blanc", "Branco", "Leve", "Campanha Gaúcha, RS", "Peixes e frutos do mar", "Fresco, cítrico e de acidez viva.", 6200, false),
    Wine("Casa Bruno Brut Rosé", "Espumante", "Leve", "Pinto Bandeira, RS", "Queijos", "Delicado e refrescante para celebrações.", 7450, true),
    Wine("Herdade Nobile Cabernet 2020", "Tinto", "Encorpado", "Serra Gaúcha, RS", "Carnes vermelhas", "Frutas negras e final prolongado.", 11900, true),
    Wine("Jardim das Uvas Rosé", "Rosé", "Leve", "Vale dos Vinhedos, RS", "Queijos", "Aromático e versátil.", 5590, false),
    Wine("Aurora do Sul Chardonnay", "Branco", "Suave", "Campanha Gaúcha, RS", "Peixes e frutos do mar", "Macio e equilibrado.", 6900, false),
)
