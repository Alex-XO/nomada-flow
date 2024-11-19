package com.example.nomadaflow

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/stops")
class StopController(private val stopRepository: StopRepository) {

    @GetMapping
    fun getAllStops() = stopRepository.findAll()

    @PostMapping
    fun createStop(@RequestBody stop: Stop) = stopRepository.save(stop)
}
