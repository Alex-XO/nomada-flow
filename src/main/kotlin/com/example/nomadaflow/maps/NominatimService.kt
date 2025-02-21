package com.example.nomadaflow.maps

import okhttp3.OkHttpClient
import okhttp3.Request
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Service
import com.example.nomadaflow.*
import org.springframework.cache.annotation.Cacheable

@Service
class NominatimService(
    private val httpClient: OkHttpClient // HTTP-клиент для запросов
) {

    private val objectMapper = jacksonObjectMapper()

    // Геокодирование: получение координат по адресу
    @Cacheable("geocode")
    fun geocode(address: String): Pair<Double, Double> {
        throwIfInvalidAddress(address) // Проверяем адрес

        val url = "https://nominatim.openstreetmap.org/search?q=$address&format=json&limit=1"
        val request = Request.Builder()
            .url(url)
            .header("User-Agent", "NomadaFlow/1.0 (example@example.com)")
            .build()

        httpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throwIfResponseUnsuccessful(response.code, "geocoding")

            val json = objectMapper.readTree(response.body?.string())
            throwIfEmptyJson(json, "Geocoding") // Проверяем, что JSON не пустой

            val lat = json[0]["lat"].asDouble()
            val lon = json[0]["lon"].asDouble()

            println("Геокодирование: $address -> [$lat, $lon]") // Логируем координаты
            return lat to lon
        }
    }
}