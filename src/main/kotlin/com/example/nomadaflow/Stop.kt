package com.example.nomadaflow

import jakarta.persistence.*

@Entity
data class Stop(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var name: String,

    var description: String? = null,

    @Column(nullable = false)
    var latitude: Double,

    @Column(nullable = false)
    var longitude: Double,

    @Column(name = "stop_type")
    var stopType: String? = null,

    @ManyToOne
    @JoinColumn(name = "route_id", nullable = false)
    var route: Route? = null
)
