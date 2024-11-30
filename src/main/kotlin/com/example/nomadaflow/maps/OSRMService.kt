package com.example.nomadaflow.maps

import okhttp3.OkHttpClient
import okhttp3.Request
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Service

@Service
class OSRMService(
    private val httpClient: OkHttpClient // HTTP-клиент для запросов
) {

    private val objectMapper = jacksonObjectMapper()

    // Получение маршрута между двумя точками
    fun getRoute(origin: Pair<Double, Double>, destination: Pair<Double, Double>): List<Pair<Double, Double>> {
        val (lon1, lat1) = origin
        val (lon2, lat2) = destination

        // Строим URL для запроса к OSRM
        val url = "https://router.project-osrm.org/route/v1/driving/$lon1,$lat1;$lon2,$lat2?overview=full&geometries=geojson"
        val request = Request.Builder().url(url).build()

        httpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw IllegalStateException("Ошибка маршрутизации: ${response.code}")
            }

            // Проверяем, что тело ответа не null
            val responseBody = response.body?.string()
                ?: throw IllegalStateException("Тело ответа отсутствует")

            println("OSRM Response: $responseBody") // Логируем ответ для отладки

            // Парсим JSON
            val json = objectMapper.readTree(responseBody)

            // Проверяем наличие маршрутов
            val routes = json["routes"]
            if (routes == null || !routes.isArray || routes.size() == 0) {
                throw IllegalStateException("OSRM вернул пустой список маршрутов")
            }

            // Получаем координаты маршрута
            val coordinates = routes[0]["geometry"]?.get("coordinates")
                ?: throw IllegalStateException("Координаты отсутствуют в маршруте")

            // Преобразуем координаты в список
            return coordinates.map {
                val lon = it[0].asDouble()
                val lat = it[1].asDouble()
                lat to lon
            }
        }
    }
}