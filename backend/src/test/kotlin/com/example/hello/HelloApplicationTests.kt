package com.example.hello

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

//@SpringBootTest
class HelloApplicationTests {

    @Test
    fun contextLoads() {
        var numbers : List<Int> = Arrays.asList(1 , 3 , 21 , 10 ,8 ,11);

        var sum = numbers.stream()
            .filter{number -> number > 6 && (number % 2 !=0)}
            .mapToInt{number -> number}
            .sum();

        println(sum);
    }

}