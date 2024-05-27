package com.example.hello.domain.reservation.service

import com.example.hello.domain.reservation.controller.HelloController
import com.example.hello.domain.reservation.entity.Reservation
import com.example.hello.domain.reservation.repository.ReservationRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.Optional

@Service
class ReservationService (
    @Autowired val reservationRepository: ReservationRepository,
//    @Autowired private val redissonService: RedissonService
) {

    val log = LoggerFactory.getLogger(ReservationService::class.java)

    fun getReservationList(): Mono<Reservation> {
        log.error("1 : ");
        return reservationRepository.findById(1);
    }


//    fun getReservationList(): Mono<Reservation> {
//        return reservationRepository.findById(1);
//    }

    //    override fun reserve(reserveDtoMono: Mono<ReserveDto>, userIdMono: Mono<Long>): Mono<Boolean> {

//        val countMono = redissonService.getAtomicLong("count")
//        return countMono.get().flatMap { count ->
//            if( count >= Constants.MAX_RESERVATION_COUNT ) Mono.just(false)
//            else reserveDtoMono.flatMap { reserveDto ->
//                userIdMono.flatMap { userId ->
//                    reservationRepository.save(Reservation(userId = userId , number = reserveDto.number)).flatMap {
//                        countMono.getAndSet(count + reserveDto.number).flatMap {
//                            Mono.just(true)
//                        }
//                    }
//                }
//            }
//        }
//    }
}