package com.example.hello.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

public class Example {

    @Test
    void Example5_1(){
        Flux<String> sequence = Flux.just("Hello" , "Reactor");
        sequence.map(String::toLowerCase).subscribe(System.out::println);
    }
}
