package com.example.nomadaflow

data class RouteView(
    val id: Long,
    val name: String,
    val description: String?,
    val stops: List<StopView>
)

data class StopView(
    val id: Long,
    val name: String,
    val description: String?,
    val latitude: Double,
    val longitude: Double,
    val stopType: String?
)
