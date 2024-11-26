package com.example.nomadaflow

import jakarta.validation.constraints.*

data class CreateRouteRequest(
    @field:NotBlank(message = "Name is required")
    val name: String,
    val description: String? = null,

    @field:Size(min = 1, message = "Route must have at least one stop")
    val stops: List<Stop>
) {
    data class Stop(
        @field:NotBlank(message = "Name is required")
        val name: String,
        val description: String? = null,

        @field:Min(-90)
        @field:Max(90)
        val latitude: Double,

        @field:Min(-180)
        @field:Max(180)
        val longitude: Double,
        val stopType: String? = null
    )
}
