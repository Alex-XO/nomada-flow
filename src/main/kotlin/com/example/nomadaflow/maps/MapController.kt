package com.example.nomadaflow.maps

import jakarta.validation.constraints.NotBlank
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.slf4j.LoggerFactory

@RestController
@RequestMapping("/api/maps")
class MapController(
    private val osrmService: OSRMService,
    private val nominatimService: NominatimService
) {
    private val logger = LoggerFactory.getLogger(MapController::class.java)

    @GetMapping("/route")
    fun getRouteAndMap(
        @RequestParam @NotBlank(message = "Origin cannot be blank.") origin: String,
        @RequestParam @NotBlank(message = "Destination cannot be blank.") destination: String
    ): Map<String, Any> {
        return try {
            val originCoordinates = nominatimService.geocode(origin) // Получаем координаты отправной точки
            val destinationCoordinates = nominatimService.geocode(destination) // Получаем координаты конечной точки
            val route = osrmService.getRoute(originCoordinates, destinationCoordinates) // Строим маршрут
            mapOf("geoJson" to generateGeoJson(route, originCoordinates, destinationCoordinates))
        } catch (e: Exception) {
            logger.error("Error while generating route and map: ${e.message}", e)
            mapOf("error" to e.message.orEmpty())
        }
    }

    /**
     * Генерация GeoJSON для маршрута, начальной и конечной точек.
     */
    private fun generateGeoJson(
        route: List<Pair<Double, Double>>,
        origin: Pair<Double, Double>,
        destination: Pair<Double, Double>
    ): Map<String, Any> {
        fun createFeature(type: String, coordinates: Any, name: String): Map<String, Any> {
            return mapOf(
                "type" to "Feature",
                "geometry" to mapOf(
                    "type" to type,
                    "coordinates" to coordinates
                ),
                "properties" to mapOf("name" to name)
            )
        }

        return mapOf(
            "type" to "FeatureCollection",
            "features" to listOf(
                createFeature("LineString", route.map { listOf(it.second, it.first) }, "Route"),
                createFeature("Point", listOf(origin.second, origin.first), "Origin"),
                createFeature("Point", listOf(destination.second, destination.first), "Destination")
            )
        )
    }
}