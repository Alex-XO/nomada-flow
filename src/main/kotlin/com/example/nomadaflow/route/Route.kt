package com.example.nomadaflow.route

import com.example.nomadaflow.stop.Stop
import jakarta.persistence.*

@Entity
data class Route(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var name: String,

    var description: String? = null,

    @OneToMany(mappedBy = "route", cascade = [CascadeType.ALL], orphanRemoval = true)
    var stops: List<Stop> = emptyList()
)
