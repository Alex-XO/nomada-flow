package com.example.nomadaflow

import jakarta.persistence.*

@Entity
data class Stop(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val name: String,

    val description: String? = null,

    @Column(nullable = false)
    val latitude: Double,

    @Column(nullable = false)
    val longitude: Double,

    @Column(name = "stop_type")
    val stopType: String? = null,

    @ManyToOne
    @JoinColumn(name = "route_id", nullable = false)
    var route: Route? = null
)
