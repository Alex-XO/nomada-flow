package com.example.nomadaflow.maps

import okhttp3.OkHttpClient
import okhttp3.Request
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Service

@Service
class NominatimService(
    private val httpClient: OkHttpClient // HTTP-клиент для запросов
) {

    private val objectMapper = jacksonObjectMapper()

    // Получение координат по адресу
    fun geocode(address: String): Pair<Double, Double> {
        val url = "https://nominatim.openstreetmap.org/search?q=$address&format=json&limit=1"
        val request = Request.Builder()
            .url(url)
            .header("User-Agent", "NomadaFlow/1.0 (kurianovaaleksandra@gmail.com)")
            .build()

        httpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IllegalStateException("Ошибка геокодирования: ${response.code}")

            val json = objectMapper.readTree(response.body?.string())
            if (json.isEmpty) throw IllegalStateException("Адрес не найден: $address")

            val lat = json[0]["lat"].asDouble()
            val lon = json[0]["lon"].asDouble()

            println("Геокодирование: $address -> [$lat, $lon]") // Лог координат
            return lat to lon
        }
    }
}