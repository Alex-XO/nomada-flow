package com.example.nomadaflow

import org.springframework.http.*
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/stops")
class StopController(private val stopService: StopService) {

    @GetMapping
    fun getAllStops(): ResponseEntity<List<StopView>> {
        val stops = stopService.getAllStops()
        return ResponseEntity.ok(stops)
    }

    @PostMapping
    fun createStop(
        @RequestBody stop: Stop,
        @RequestParam routeId: Long
    ): ResponseEntity<StopView> {
        val stopView = stopService.createStop(stop, routeId)
        return ResponseEntity.status(HttpStatus.CREATED).body(stopView)
    }

    @DeleteMapping("/{id}")
    fun deleteStop(@PathVariable id: Long): ResponseEntity<Void> {
        stopService.deleteStop(id)
        return ResponseEntity.noContent().build()
    }

    @PutMapping("/{id}")
    fun updateStop(
        @PathVariable id: Long,
        @RequestBody stop: Stop
    ): ResponseEntity<StopView> {
        val updatedStop = stopService.updateStop(id, stop)
        return ResponseEntity.ok(updatedStop)
    }
}
