package com.example.nomadaflow

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/stops")
class StopController(
    private val stopRepository: StopRepository,
    private val routeRepository: RouteRepository
) {

    // Получение всех остановок
    @GetMapping
    fun getAllStops(): ResponseEntity<List<StopView>> {
        val stops = stopRepository.findAll()
        val stopViews = stops.map { stop ->
            StopView(
                id = stop.id,
                name = stop.name,
                description = stop.description,
                latitude = stop.latitude,
                longitude = stop.longitude,
                stopType = stop.stopType
            )
        }
        return ResponseEntity.ok(stopViews)
    }

    // Создание новой остановки
    @PostMapping
    fun createStop(
        @RequestBody request: CreateStopRequest,
        @RequestParam routeId: Long
    ): ResponseEntity<StopView> {
        val route = routeRepository.findById(routeId).orElseThrow {
            IllegalArgumentException("Route with id $routeId not found")
        }

        val stop = Stop(
            name = request.name,
            description = request.description,
            latitude = request.latitude,
            longitude = request.longitude,
            stopType = request.stopType,
            route = route
        )

        val savedStop = stopRepository.save(stop)

        val stopView = StopView(
            id = savedStop.id,
            name = savedStop.name,
            description = savedStop.description,
            latitude = savedStop.latitude,
            longitude = savedStop.longitude,
            stopType = savedStop.stopType
        )

        return ResponseEntity.status(HttpStatus.CREATED).body(stopView)
    }

    // Удаление остановки
    @DeleteMapping("/{id}")
    fun deleteStop(@PathVariable id: Long): ResponseEntity<Void> {
        if (!stopRepository.existsById(id)) {
            throw IllegalArgumentException("Stop with id $id not found")
        }
        stopRepository.deleteById(id)
        return ResponseEntity.noContent().build()
    }

    // Обновление остановки
    @PutMapping("/{id}")
    fun updateStop(
        @PathVariable id: Long,
        @RequestBody request: CreateStopRequest
    ): ResponseEntity<StopView> {
        val existingStop = stopRepository.findById(id).orElseThrow {
            IllegalArgumentException("Stop with id $id not found")
        }

        // Обновляем данные остановки
        existingStop.name = request.name
        existingStop.description = request.description
        existingStop.latitude = request.latitude
        existingStop.longitude = request.longitude
        existingStop.stopType = request.stopType

        val updatedStop = stopRepository.save(existingStop)

        val stopView = StopView(
            id = updatedStop.id,
            name = updatedStop.name,
            description = updatedStop.description,
            latitude = updatedStop.latitude,
            longitude = updatedStop.longitude,
            stopType = updatedStop.stopType
        )

        return ResponseEntity.ok(stopView)
    }
}
