package com.example.hello.repository//package com.example.hello

import com.example.hello.entity.Reservation
import com.example.hello.repository.reactive.ReservationReactiveRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.client.RestTemplate

@DataJpaTest
class HealthCheckTest @Autowired constructor(
    val entityManager: TestEntityManager ,
    val reservationRepository: ReservationReactiveRepository
){

    @Test
    fun  basicHealthCheck() {
        val restTemplate = RestTemplate();
        val response = restTemplate.exchange<String>(
            "http://localhost:8080",
            HttpMethod.GET ,
            HttpEntity.EMPTY ,
            String::class.java
        )

        assert(response.statusCode == HttpStatus.OK)
    }

    @Test
    fun `When findByIdOrNull then return Article` () {
        val reservation  = Reservation(2 ,1);

        entityManager.persist(reservation);
        entityManager.flush()

        val newReservation = reservationRepository.findById(1L);

        assertThat(newReservation).isEqualTo(reservation)
    }
}