package com.example.nomadaflow

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StopService(
    private val stopRepository: StopRepository,
    private val routeRepository: RouteRepository
) {
    @Transactional(readOnly = true)
    fun getAllStops(): List<StopView> {
        return stopRepository.findAll().map { it.toView() }
    }

    @Transactional
    fun createStop(stop: Stop, routeId: Long): StopView {
        val route = routeRepository.findById(routeId).orElseThrow {
            IllegalArgumentException("Route with id $routeId not found")
        }

        stop.route = route
        val savedStop = stopRepository.save(stop)

        return savedStop.toView()
    }

    @Transactional
    fun deleteStop(id: Long) {
        if (!stopRepository.existsById(id)) {
            throw IllegalArgumentException("Stop with id $id not found")
        }
        stopRepository.deleteById(id)
    }

    @Transactional
    fun updateStop(id: Long, updatedStop: Stop): StopView {
        val existingStop = stopRepository.findById(id).orElseThrow {
            IllegalArgumentException("Stop with id $id not found")
        }

        existingStop.apply {
            name = updatedStop.name
            description = updatedStop.description
            latitude = updatedStop.latitude
            longitude = updatedStop.longitude
            stopType = updatedStop.stopType
        }

        val savedStop = stopRepository.save(existingStop)
        return savedStop.toView()
    }

}
