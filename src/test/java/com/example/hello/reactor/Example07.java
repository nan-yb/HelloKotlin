package com.example.hello.reactor;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.util.Arrays;

public class Example07 {

    Logger log = LoggerFactory.getLogger(Example07.class);

    @Test
    void Example7_1() throws InterruptedException {
        Flux<String> coldFlux = Flux.fromIterable(Arrays.asList("KOREA" , "JAPAN" , "CHINESE")).map(String::toLowerCase);
        coldFlux.subscribe(country -> log.info("# Subscriber1: {}" , country));
        log.info("--------------------------------------");
        Thread.sleep(2000L);
        coldFlux.subscribe(country -> log.info("# Subscriber2: {}" , country));

    }

    @Test
    void Example7_2() throws InterruptedException {
        String[] singers = {"Singers A" , "Singers B" , "Singers C" , "Singers D" , "Singers E"};

        log.info("# Begin concert : ");

        Flux<String> concertFlux = Flux.fromArray(singers)
                .delayElements(Duration.ofSeconds(1))
                .share();

        concertFlux.subscribe(
                singer -> log.info("# Subscriber1 is watching {}'s song" , singer)
        );

        Thread.sleep(2000);

        concertFlux.subscribe(
                singer -> log.info("# Subscriber2 is watching {}'s song" , singer)
        );

        Thread.sleep(3000);

    }

    @Test
    @DisplayName("Cold Sequence")
    void Example7_3() throws InterruptedException {
        URI worldTimeUri = UriComponentsBuilder.newInstance().scheme("http")
                .host("worldtimeapi.org")
                .port(80)
                .path("/api/timezone/Asia/Seoul")
                .build()
                .encode()
                .toUri();

        Mono<String> mono = getWorldTime(worldTimeUri);
        mono.subscribe(dateTime -> log.info("# dateTime 1: {}" , dateTime));
        Thread.sleep(2000);
        mono.subscribe(dateTime -> log.info("# dateTime 2: {}" , dateTime));

        Thread.sleep(2000);
    }

    @Test
    @DisplayName("Hot Sequence")
    void Example7_4() throws InterruptedException {
        URI worldTimeUri = UriComponentsBuilder.newInstance().scheme("http")
                .host("worldtimeapi.org")
                .port(80)
                .path("/api/timezone/Asia/Seoul")
                .build()
                .encode()
                .toUri();

        Mono<String> mono = getWorldTime(worldTimeUri).cache();
        mono.subscribe(dateTime -> log.info("# dateTime 1: {}" , dateTime));
        Thread.sleep(2000);
        mono.subscribe(dateTime -> log.info("# dateTime 2: {}" , dateTime));

        Thread.sleep(2000);
    }

    public Mono<String> getWorldTime(URI worldTimeUri) {
        return WebClient.create()
                .get()
                .uri(worldTimeUri)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    DocumentContext jsonContext = JsonPath.parse(response);
                    String dateTime = jsonContext.read("$.datetime");
                    return dateTime;
                });
    }

}
