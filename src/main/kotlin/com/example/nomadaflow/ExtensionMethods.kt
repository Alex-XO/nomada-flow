package com.example.nomadaflow

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
