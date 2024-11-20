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
        route.stops = stops.toMutableList()

        val savedRoute = routeRepository.save(route)
        return savedRoute.toView()
    }

    @Transactional
    fun updateRoute(id: Long, request: CreateRouteRequest): RouteView {
        val existingRoute = routeRepository.findById(id).orElseThrow {
            IllegalArgumentException("Route with id $id not found")
        }

        existingRoute.name = request.name
        existingRoute.description = request.description

        existingRoute.stops.clear()
        val newStops = request.stops.toStops(existingRoute)
        existingRoute.stops.addAll(newStops)

        val updatedRoute = routeRepository.save(existingRoute)
        return updatedRoute.toView()
    }

    @Transactional
    fun deleteRoute(id: Long) {
        if (!routeRepository.existsById(id)) {
            throw IllegalArgumentException("Route with id $id not found")
        }
        routeRepository.deleteById(id)
    }

    @Transactional(readOnly = true)
    fun getAllRoutes(): List<RouteView> {
        return routeRepository.findAll().map { it.toView() }
    }
}
