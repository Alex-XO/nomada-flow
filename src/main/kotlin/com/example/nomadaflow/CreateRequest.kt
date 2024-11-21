package com.example.nomadaflow

data class CreateRouteRequest(
    val name: String,
    val description: String? = null,
    val stops: List<Stop>
) {
    data class Stop(
        val name: String,
        val description: String? = null,
        val latitude: Double,
        val longitude: Double,
        val stopType: String? = null
    )
}
