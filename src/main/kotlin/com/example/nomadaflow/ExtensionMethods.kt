package com.example.nomadaflow

import com.example.nomadaflow.route.Route
import com.example.nomadaflow.stop.Stop
import org.springframework.data.jpa.repository.JpaRepository

fun List<CreateRouteRequest.Stop>.toStops(route: Route): List<Stop> {
    return this.map { stopRequest ->
        Stop(
            name = stopRequest.name,
            description = stopRequest.description,
            latitude = stopRequest.latitude,
            longitude = stopRequest.longitude,
            stopType = stopRequest.stopType,
            route = route
        )
    }
}

fun Route.toView(): RouteView {
    return RouteView(
        id = this.id,
        name = this.name,
        description = this.description,
        stops = this.stops.map { stop ->
            StopView(
                id = stop.id,
                name = stop.name,
                description = stop.description,
                latitude = stop.latitude,
                longitude = stop.longitude,
                stopType = stop.stopType
            )
        }
    )
}

fun Stop.toView(): StopView {
    return StopView(
        id = this.id,
        name = this.name,
        description = this.description,
        latitude = this.latitude,
        longitude = this.longitude,
        stopType = this.stopType
    )
}

inline fun <reified T> JpaRepository<T, Long>.findByIdOrThrow(id: Long): T {
    return this.findById(id).orElseThrow {
        IllegalArgumentException("${T::class.simpleName} with id $id not found")
    }
}

fun Route.findStopOrThrow(stopId: Long): Stop {
    return this.stops.find { it.id == stopId }
        ?: throw IllegalArgumentException("Stop with id $stopId not found in route with id ${this.id}")
}

fun throwIfInvalidAddress(address: String) {
    if (address.isBlank()) throw IllegalArgumentException("Address cannot be blank.")
}

fun throwIfResponseUnsuccessful(code: Int, context: String) {
    throw IllegalStateException("Error in $context: HTTP status code $code.")
}

fun throwIfEmptyJson(json: com.fasterxml.jackson.databind.JsonNode, context: String) {
    if (json.isEmpty) throw IllegalStateException("$context returned an empty result.")
}

fun throwIfNullField(field: Any?, fieldName: String, context: String) {
    if (field == null) {
        throw IllegalStateException("$fieldName is missing in $context.")
    }
}

fun throwIfInvalidCoordinates(coordinates: Pair<Double, Double>) {
    val (lat, lon) = coordinates
    if (lat !in -90.0..90.0 || lon !in -180.0..180.0) {
        throw IllegalArgumentException("Invalid coordinates: $coordinates.")
    }
}

