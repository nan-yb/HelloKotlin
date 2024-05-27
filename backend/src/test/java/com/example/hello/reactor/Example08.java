package com.example.hello.reactor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.BufferOverflowStrategy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class Example08 {

    Logger log = LoggerFactory.getLogger(Example08.class);

    @Test
    void Example8_1(){
        Flux.range(1,5)
                .doOnRequest(data -> log.info("# doOnRequest : {}" , data))
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(1);
                    }

                   @Override
                   protected void hookOnNext(Integer value) {
                       try {
                           Thread.sleep(2000);
                           log.info("# hookOnNext : {}" ,value);
                           request(1);
                       } catch (InterruptedException e) {
                           throw new RuntimeException(e);
                       }
                   }
               }
            );
    }

    @Test
    @DisplayName("Backpressure ERROR 전략 예제")
    void Example8_2() throws InterruptedException {
        Flux.interval(Duration.ofMillis(1L))
                .onBackpressureError()
                .doOnNext(data -> log.info("# doOnNext : {}" ,data))
                .publishOn(Schedulers.parallel())
                .subscribe(data -> {
                    try {
                        Thread.sleep(5L);
                    } catch (InterruptedException e) {}
                    log.info("# OnNext : {}" ,data);
                } , error -> log.error("# onError {} " , error.getMessage() ));

        Thread.sleep(2000L);
    }

    @Test
    @DisplayName("Backpressure DROP 전략 예제")
    void Example8_3() throws InterruptedException {
        Flux.interval(Duration.ofMillis(1L))
                .onBackpressureDrop(dropped -> log.info("# dropped : {}" , dropped))
                .publishOn(Schedulers.parallel())
                .subscribe(data->{
                    try {
                        Thread.sleep(5L);
                    } catch (InterruptedException e) {}
                    log.info("# onNext : {}" , data);
                } , error -> log.error("# onError" , error));

        Thread.sleep(2000L);
    }

    @Test
    @DisplayName("Backpressure Latest 전략 예제")
    void Example8_4() throws InterruptedException {
        Flux.interval(Duration.ofMillis(1L))
                .onBackpressureLatest()
                .publishOn(Schedulers.parallel())
                .subscribe(data->{
                    try {
                        Thread.sleep(5L);
                    } catch (InterruptedException e) {}
                    log.info("# onNext : {}" , data);
                } , error -> log.error("# onError" , error));

        Thread.sleep(2000L);
    }

    @Test
    @DisplayName("Backpressure DROP_OLDEST 전략 예제")
    void Example8_6() throws InterruptedException {
        Flux.interval(Duration.ofMillis(300))
                .onBackpressureBuffer( 2 , dropped -> log.info("** Overflow & Dropped : {} **" , dropped) , BufferOverflowStrategy.DROP_OLDEST)
                .doOnNext(data-> log.info(" [ # emitted by buffer : {}]" , data))
                .publishOn(Schedulers.parallel() , false , 1)
                .subscribe(data->{
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {}
                    log.info("# onNext : {}" , data);
                } , error -> log.error("# onError" , error));

        Thread.sleep(2500);
    }

}
