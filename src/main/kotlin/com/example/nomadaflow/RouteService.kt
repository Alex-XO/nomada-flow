package com.example.nomadaflow

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RouteService(
    private val routeRepository: RouteRepository
) {
    @Transactional
    fun createRoute(request: CreateRouteRequest): RouteView {
        val route = Route(
            name = request.name,
            description = request.description
        )

        val stops = request.stops.toStops(route)
        route.stops = stops

        val savedRoute = routeRepository.save(route)
        return savedRoute.toView()
    }

    @Transactional
    fun updateRoute(id: Long, request: CreateRouteRequest): RouteView {
        val existingRoute = routeRepository.findByIdOrThrow(id)

        existingRoute.name = request.name
        existingRoute.description = request.description

        existingRoute.stops = request.stops.toStops(existingRoute)

        val updatedRoute = routeRepository.save(existingRoute)
        return updatedRoute.toView()
    }

    @Transactional
    fun deleteRoute(id: Long) {
        routeRepository.deleteById(id)
    }

    @Transactional(readOnly = true)
    fun getAllRoutes(): List<RouteView> {
        return routeRepository.findAll().map { it.toView() }
    }
}
