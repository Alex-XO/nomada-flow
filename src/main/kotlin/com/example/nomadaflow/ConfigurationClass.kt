package com.example.nomadaflow

import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OkHttpClientConfig {

    @Bean
    fun okHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }
}