package com.example.nomadaflow

import org.springframework.data.jpa.repository.JpaRepository

interface RouteRepository : JpaRepository<Route, Long>

interface StopRepository : JpaRepository<Stop, Long>
