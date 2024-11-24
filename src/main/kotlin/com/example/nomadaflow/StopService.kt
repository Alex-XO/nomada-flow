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
        val route = routeRepository.findByIdOrThrow(routeId)

        stop.route = route
        val savedStop = stopRepository.save(stop)

        return savedStop.toView()
    }

    @Transactional
    fun deleteStop(id: Long) {
        stopRepository.deleteById(id)
    }

    @Transactional
    fun updateStop(id: Long, updatedStop: Stop): StopView {
        val existingStop = stopRepository.findByIdOrThrow(id)

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
