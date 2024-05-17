package com.example.hello.domain.reservation.repository

import com.example.hello.domain.reservation.entity.Reservation
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository

@Repository
interface ReservationRepository : R2dbcRepository<Reservation, Long> {
}