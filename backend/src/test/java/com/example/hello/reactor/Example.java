package com.example.hello.reactor;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;

public class Example {

    Logger log = LoggerFactory.getLogger(Example.class);

    @Test
    void Example5_1() {
        Flux<String> sequence = Flux.just("Hello", "Reactor");
        sequence.map(String::toLowerCase).subscribe(System.out::println);
    }

    @Test
    void Example5_2() {
        Mono.just("Hello Reactor")
                .subscribe(System.out::println);
    }

    @Test
    void Example6_2() {
        Mono.empty().subscribe(
                none -> System.out.println("# emitted onNext signal"),
                error -> {
                },
                () -> System.out.println("# emitted onComplete signal")
        );
    }

    @Test
    void Example6_3() {
        URI worldTimeUri = UriComponentsBuilder.newInstance().scheme("http")
                .host("worldtimeapi.org")
                .port(80)
                .path("/api/timezone/Asia/Seoul")
                .build()
                .encode()
                .toUri();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Mono.just(restTemplate.exchange(worldTimeUri, HttpMethod.GET, new HttpEntity<String>(headers), String.class))
                .map(response -> {
                    DocumentContext jsonContext = JsonPath.parse(response.getBody());
                    String dateTime = jsonContext.read("$.datetime");
                    return dateTime;
                })
                .subscribe(data -> System.out.println("# emitted data : " + data),
                        error -> System.out.println(error),
                        () -> System.out.println("# emitted onComplete signal")
                );

    }

    @Test
    void Example6_6(){
        Flux<String> flux = Mono.justOrEmpty("Steve").concatWith(Mono.justOrEmpty("Jobs"));
        flux.subscribe(System.out::println);
    }

    @Test
    void Example6_7(){
        Flux.concat(
                Flux.just("Mercury1" ,"Venus1" , "Earth1"),
                Flux.just("Mercury2" ,"Venus2" , "Earth2"),
                Flux.just("Mercury3" ,"Venus3" , "Earth3")
        )
        .collectList()
        .subscribe(planets -> System.out.println(planets));
    }



}
