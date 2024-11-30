package com.example.nomadaflow.maps

import okhttp3.OkHttpClient
import okhttp3.Request
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Service
import com.example.nomadaflow.*

@Service
class OSRMService(
    private val httpClient: OkHttpClient // HTTP-клиент для запросов
) {

    private val objectMapper = jacksonObjectMapper()

    // Получение маршрута между двумя точками
    fun getRoute(origin: Pair<Double, Double>, destination: Pair<Double, Double>): List<Pair<Double, Double>> {
        throwIfInvalidCoordinates(origin) // Проверяем координаты
        throwIfInvalidCoordinates(destination)

        val (lon1, lat1) = origin
        val (lon2, lat2) = destination

        val url = "https://router.project-osrm.org/route/v1/driving/$lon1,$lat1;$lon2,$lat2?overview=full&geometries=geojson"
        val request = Request.Builder().url(url).build()

        httpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throwIfResponseUnsuccessful(response.code, "routing")

            val responseBody = response.body?.string()
                ?: throw IllegalStateException("Response body is null.")

            println("OSRM Response: $responseBody") // Логируем ответ для отладки

            val json = objectMapper.readTree(responseBody)

            val routes = json["routes"]
            throwIfEmptyJson(routes, "OSRM") // Проверяем, что маршруты не пустые

            val coordinates = routes[0]["geometry"]?.get("coordinates")
            throwIfNullField(coordinates, "Coordinates", "the route")

            return coordinates!!.map {
                val lon = it[0].asDouble()
                val lat = it[1].asDouble()
                lat to lon
            }
        }
    }
}