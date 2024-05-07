package com.example.hello.reactor;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class Example11 {

    Logger log = LoggerFactory.getLogger(Example11.class);

    @Test
    void Example11_1() throws InterruptedException {
        Mono.deferContextual(ctx ->
                        Mono.just("Hello" + "   " + ctx.get("firstName"))
                                .doOnNext(data -> log.info(" # just doOnNext : {}", data))
                ).subscribeOn(Schedulers.boundedElastic())
                .publishOn(Schedulers.parallel())
                .transformDeferredContextual((mono, ctx) -> mono.map(data -> data + " " + ctx.get("lastName")))
                .contextWrite(context -> context.put("lastName", "Jobs"))
                .contextWrite(context -> context.put("firstName", "Steve"))
                .subscribe(data -> log.info("# onNext: {}", data));

        Thread.sleep(100L);
    }
}
