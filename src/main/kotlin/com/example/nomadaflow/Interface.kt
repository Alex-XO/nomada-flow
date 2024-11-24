package com.example.nomadaflow

import com.example.nomadaflow.route.Route
import com.example.nomadaflow.stop.Stop
import org.springframework.data.jpa.repository.JpaRepository

interface RouteRepository : JpaRepository<Route, Long>

interface StopRepository : JpaRepository<Stop, Long>
