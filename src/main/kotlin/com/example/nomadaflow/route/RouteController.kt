package com.example.nomadaflow.route

import com.example.nomadaflow.CreateRouteRequest
import com.example.nomadaflow.RouteView
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/routes")
class RouteController(
    private val routeService: RouteService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createRoute(@Valid @RequestBody request: CreateRouteRequest): RouteView {
        return routeService.createRoute(request)
    }

    @PutMapping("/{id}")
    fun updateRoute(
        @PathVariable id: Long,
        @Valid @RequestBody request: CreateRouteRequest
    ): ResponseEntity<RouteView> {
        val updatedRoute = routeService.updateRoute(id, request)
        return ResponseEntity.ok(updatedRoute)
    }

    @DeleteMapping("/{id}")
    fun deleteRoute(@PathVariable id: Long): ResponseEntity<Void> {
        routeService.deleteRoute(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun getAllRoutes(): ResponseEntity<List<RouteView>> {
        val routes = routeService.getAllRoutes()
        return ResponseEntity.ok(routes)
    }

    @PatchMapping("/{routeId}/stops")
    fun addStopToRoute(
        @PathVariable routeId: Long,
        @Valid @RequestBody stopRequest: CreateRouteRequest.Stop
    ): ResponseEntity<RouteView> {
        val updatedRoute = routeService.addStopToRoute(routeId, stopRequest)
        return ResponseEntity.ok(updatedRoute)
    }

    @DeleteMapping("/{routeId}/stops/{stopId}")
    fun removeStopFromRoute(
        @PathVariable routeId: Long,
        @PathVariable stopId: Long
    ): ResponseEntity<RouteView> {
        val updatedRoute = routeService.removeStopFromRoute(routeId, stopId)
        return ResponseEntity.ok(updatedRoute)
    }

}
