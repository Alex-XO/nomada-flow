package com.example.nomadaflow

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/routes")
class RouteController(
    private val routeRepository: RouteRepository
) {
    @PostMapping
    fun createRoute(@RequestBody request: CreateRouteRequest): ResponseEntity<RouteView> {
        // Создаём маршрут
        val route = Route(
            name = request.name,
            description = request.description
        )

        // Устанавливаем маршрут для каждой остановки
        val stops = request.stops.map { stopRequest ->
            Stop(
                name = stopRequest.name,
                description = stopRequest.description,
                latitude = stopRequest.latitude,
                longitude = stopRequest.longitude,
                stopType = stopRequest.stopType,
                route = route // Устанавливаем ссылку на маршрут
            )
        }
        route.stops = stops

        // Сохраняем маршрут
        val savedRoute = routeRepository.save(route)

        val routeView = RouteView(
            id = savedRoute.id,
            name = savedRoute.name,
            description = savedRoute.description,
            stops = savedRoute.stops.map { stop ->
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

        return ResponseEntity.status(HttpStatus.CREATED).body(routeView)
    }

    @GetMapping
    fun getAllRoutes(): ResponseEntity<List<RouteView>> {
        val routes = routeRepository.findAll()
        val routeViews = routes.map { route ->
            RouteView(
                id = route.id,
                name = route.name,
                description = route.description,
                stops = route.stops.map { stop ->
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
        return ResponseEntity.ok(routeViews)
    }
}

