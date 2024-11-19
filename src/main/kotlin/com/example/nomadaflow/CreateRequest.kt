package com.example.nomadaflow

data class CreateRouteRequest(
    val name: String,
    val description: String? = null,
    val stops: List<CreateStopRequest>
)

data class CreateStopRequest(
    val name: String,
    val description: String? = null,
    val latitude: Double,
    val longitude: Double,
    val stopType: String? = null
)

