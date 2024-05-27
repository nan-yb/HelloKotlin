package com.example.hello

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import java.util.*
import java.util.function.Predicate
import kotlin.Comparator

class Example01 {
    @Test
    fun `code1-1`(){
        val numbers : List<Int> = listOf(1 , 3 , 21 , 10 ,8 ,11);

        val sum = numbers.stream()
            .filter{number -> number > 6 && (number % 2 !=0)}
            .mapToInt{number -> number}
            .sum();

        println(sum);
    }

    @Test
    fun `code2-5`(){
        Flux
            .just(1 ,2 ,3 ,4 ,5 ,6)
            .filter{n-> n%2 ==0}
            .map{n -> n*2}
            .subscribe{println()}
    }





}