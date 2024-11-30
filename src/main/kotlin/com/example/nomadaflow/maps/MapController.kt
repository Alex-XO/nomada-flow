package com.example.nomadaflow.maps

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
        @RequestParam origin: String,
        @RequestParam destination: String
    ): Map<String, Any> {
        return try {
            // Получаем координаты для origin и destination
            val originCoordinates = nominatimService.geocode(origin)
            val destinationCoordinates = nominatimService.geocode(destination)

            // Получаем маршрут с помощью OSRM
            val route = osrmService.getRoute(originCoordinates, destinationCoordinates)

            // Формируем GeoJSON для маршрута
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
        return mapOf(
            "type" to "FeatureCollection",
            "features" to listOf(
                mapOf(
                    "type" to "Feature",
                    "geometry" to mapOf(
                        "type" to "LineString",
                        "coordinates" to route.map { listOf(it.second, it.first) }
                    ),
                    "properties" to mapOf("name" to "Route")
                ),
                mapOf(
                    "type" to "Feature",
                    "geometry" to mapOf(
                        "type" to "Point",
                        "coordinates" to listOf(origin.second, origin.first)
                    ),
                    "properties" to mapOf("name" to "Origin")
                ),
                mapOf(
                    "type" to "Feature",
                    "geometry" to mapOf(
                        "type" to "Point",
                        "coordinates" to listOf(destination.second, destination.first)
                    ),
                    "properties" to mapOf("name" to "Destination")
                )
            )
        )
    }
}