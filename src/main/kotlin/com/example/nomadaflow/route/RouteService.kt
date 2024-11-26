package com.example.nomadaflow.route

import com.example.nomadaflow.*
import com.example.nomadaflow.stop.Stop
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

    @Transactional
    fun addStopToRoute(routeId: Long, stopRequest: CreateRouteRequest.Stop): RouteView {
        val route = routeRepository.findByIdOrThrow(routeId)

        val newStop = Stop(
            name = stopRequest.name,
            description = stopRequest.description,
            latitude = stopRequest.latitude,
            longitude = stopRequest.longitude,
            stopType = stopRequest.stopType,
            route = route
        )

        route.stops = route.stops.toMutableList().apply { add(newStop) }
        val updatedRoute = routeRepository.save(route)
        return updatedRoute.toView()
    }

    @Transactional
    fun removeStopFromRoute(routeId: Long, stopId: Long): RouteView {
        val route = routeRepository.findByIdOrThrow(routeId)
        val stopToRemove = route.findStopOrThrow(stopId)

        route.stops = route.stops.toMutableList().apply { remove(stopToRemove) }

        val updatedRoute = routeRepository.save(route)

        return updatedRoute.toView()
    }

}
