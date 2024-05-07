package com.example.hello.reactor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.util.stream.IntStream;

public class Example09 {


    Logger log = LoggerFactory.getLogger(Example09.class);

    @Test
    @DisplayName("")
    void Example9_1() throws InterruptedException {
        int tasks = 6;
        Flux.create((FluxSink<String> sink) -> IntStream.range(1, tasks).forEach(n -> sink.next(doTask(n))))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(n -> log.info("# create(): {}", n))
                .publishOn(Schedulers.parallel())
                .map(result -> result + " success!")
                .doOnNext(n -> log.info("# map() : {}", n))
                .publishOn(Schedulers.parallel())
                .subscribe(data -> log.info("# onNext : {}", data));

        Thread.sleep(500L);
    }

    @Test
    @DisplayName("")
    void Example9_2() throws InterruptedException {
        int tasks = 6;
        Sinks.Many<String> unicastSink = Sinks.many().unicast().onBackpressureBuffer();
        Flux<String> fluxView = unicastSink.asFlux();

        IntStream.range(1, tasks).forEach( n -> {
            try {
                new Thread(()->{
                    unicastSink.emitNext(doTask(n) , Sinks.EmitFailureHandler.FAIL_FAST);
                    log.info("# emitted : {}" , n);
                }).start();
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        fluxView.publishOn(Schedulers.parallel())
                .map(result -> result + " success!")
                .doOnNext(n -> log.info("# map() : {}", n))
                .publishOn(Schedulers.parallel())
                .subscribe(data -> log.info("# onNext : {}", data));

        Thread.sleep(200L);
    }

    private String doTask(int taskNumber) {
        return "task " + taskNumber + " result";
    }

    @Test
    void Example9_4(){
        Sinks.One<String> sinkOne = Sinks.one();
        Mono<String> mono = sinkOne.asMono();
        sinkOne.emitValue("Hello Reactor" , Sinks.EmitFailureHandler.FAIL_FAST);

        mono.subscribe(data -> log.info("# Subscriber1 {} " , data));
        mono.subscribe(data -> log.info("# Subscriber2 {} " , data));
    }

    @Test
    void Example9_8(){
        Sinks.Many<Integer> unicastSink = Sinks.many().unicast().onBackpressureBuffer();
        Flux<Integer> fluxView = unicastSink.asFlux();

        unicastSink.emitNext(1 , Sinks.EmitFailureHandler.FAIL_FAST);
        unicastSink.emitNext(2 , Sinks.EmitFailureHandler.FAIL_FAST);

        fluxView.subscribe(data  -> log.info("# Subscriber1 : {}" , data));
        unicastSink.emitNext(3 , Sinks.EmitFailureHandler.FAIL_FAST);
        unicastSink.emitNext(4 , Sinks.EmitFailureHandler.FAIL_FAST);
//        fluxView.subscribe(data  -> log.info("# Subscriber2 : {}" , data));

    }

    @Test
    void Example9_9(){
        Sinks.Many<Integer> multicastSink = Sinks.many().multicast().onBackpressureBuffer();
        Flux<Integer> fluxView = multicastSink.asFlux();

        multicastSink.emitNext(1 , Sinks.EmitFailureHandler.FAIL_FAST);
        multicastSink.emitNext(2 , Sinks.EmitFailureHandler.FAIL_FAST);

        fluxView.subscribe(data  -> log.info("# Subscriber1 : {}" , data));
        fluxView.subscribe(data  -> log.info("# Subscriber2 : {}" , data));

        multicastSink.emitNext(3 , Sinks.EmitFailureHandler.FAIL_FAST);

    }

    @Test
    void Example9_10(){
        Sinks.Many<Integer> replaySink = Sinks.many().replay().limit(2);
        Flux<Integer> fluxView = replaySink.asFlux();

        replaySink.emitNext(1 , Sinks.EmitFailureHandler.FAIL_FAST);
        replaySink.emitNext(2 , Sinks.EmitFailureHandler.FAIL_FAST);
        replaySink.emitNext(3 , Sinks.EmitFailureHandler.FAIL_FAST);

        fluxView.subscribe(data -> log.info("# Subscriber1 {}"  ,data));

        replaySink.emitNext(4 , Sinks.EmitFailureHandler.FAIL_FAST);

        fluxView.subscribe(data -> log.info("# Subscriber2 {}"  ,data));
    }

}
