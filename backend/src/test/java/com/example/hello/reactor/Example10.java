package com.example.hello.reactor;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class Example10 {

    Logger log = LoggerFactory.getLogger(Example10.class);

    @Test
    void Example10_1() throws InterruptedException {
        Flux.fromArray(new Integer[] {1 , 3 , 5 , 7})
                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(data -> log.info("# doOnNext : {} " , data))
                .doOnSubscribe(subscription -> log.info("# Subscription"))
                .subscribe(data ->  log.info("# onNext : {}" , data));

        Thread.sleep(500L);
    }

    @Test
    void Example10_2() throws InterruptedException {
        Flux.fromArray(new Integer[] {1 , 3 , 5 , 7})
                .doOnNext(data -> log.info("# doOnNext : {} " , data))
                .doOnSubscribe(subscription -> log.info("# Subscription"))
                .publishOn(Schedulers.parallel())
                .subscribe(data ->  log.info("# onNext : {}" , data));

        Thread.sleep(500L);
    }

    @Test
    void Example10_3() throws InterruptedException {
        Flux.fromArray(new Integer[] {1 , 3 , 5 , 7 , 9 , 11 , 13 ,15 , 17 , 19})
                .parallel(4)
                .runOn(Schedulers.parallel())
                .subscribe(data -> log.info("# onNext {}"  , data));

        Thread.sleep(100L);
    }

    @Test
    void Example10_5() {
        Flux.fromArray( new Integer[] {1 , 3 ,5 ,7 })
            .doOnNext(data ->log.info("# doOnNext fromArray : {}" , data))
            .filter(data -> data > 3)
            .doOnNext(data -> log.info(" # doOnNext filter : {}" , data ))
            .map(data -> data * 10)
            .doOnNext(data -> log.info("# doOnNext map : {}" , data))
            .subscribe( data -> log.info("on Next {}"  ,data));
    }

    @Test
    void Example10_6() throws InterruptedException {
        Flux.fromArray(new Integer[] {1 , 3 , 5 , 7})
                .doOnNext(data -> log.info("# doOnNext from Array : {}" , data ))
                .publishOn(Schedulers.parallel())
                .filter(data -> data > 3)
                .doOnNext(data -> log.info("# doOnNext filter : {}" , data))
                .map(data -> data  * 10)
                .doOnNext(data -> log.info("# doOnNext map :{}" , data))
                .subscribe(data -> log.info("# onNext : {}" , data));

        Thread.sleep(500L);
    }

    @Test
    void Example10_7() throws InterruptedException {
        Flux.fromArray(new Integer[] {1 , 3 , 5 , 7})
                .doOnNext(data -> log.info("# doOnNext from Array : {}" , data ))
                .publishOn(Schedulers.parallel())
                .filter(data -> data > 3)
                .doOnNext(data -> log.info("# doOnNext filter : {}" , data))
                .publishOn(Schedulers.parallel())
                .map(data -> data  * 10)
                .doOnNext(data -> log.info("# doOnNext map :{}" , data))
                .subscribe(data -> log.info("# onNext : {}" , data));

        Thread.sleep(500L);
    }

    @Test
    void Example10_9() throws InterruptedException {
        Flux.fromArray(new Integer[] {1,3,5,7})
                .publishOn(Schedulers.parallel())
                .filter(data -> data >3)
                .doOnNext( data -> log.info("# doOnNext filter :{}" , data))
                .publishOn(Schedulers.immediate())
                .map(data -> data * 10)
                .doOnNext(data -> log.info("# doOnNext map : {}" , data))
                .subscribe(data -> log.info("# on Next : {}" , data));

        Thread.sleep(200);
    }


    @Test
    void Example10_10() throws InterruptedException {
        doTask("task1").subscribe(data -> log.info("# onNext : {}" , data));

        doTask("task2").subscribe(data -> log.info("# onNext : {}" , data));

        Thread.sleep(200L);
    }


    @Test
    void Example10_11() throws InterruptedException {
        doTaskNewSingle("task1").subscribe(data -> log.info("# onNext : {}" , data));

        doTaskNewSingle("task2").subscribe(data -> log.info("# onNext : {}" , data));

        Thread.sleep(200L);
    }

    private Flux<Integer> doTask(String taskName){
        return Flux.fromArray(new Integer[]{1,3,5,7})
                .publishOn(Schedulers.single())
                .filter(data-> data >3)
                .doOnNext(data -> log.info("#{} doOnNext filter : {}", taskName,data))
                .map(data -> data*10 )
                .doOnNext(data -> log.info("# {} doOnNext map : {}" ,   taskName , data));
    }

    private Flux<Integer> doTaskNewSingle(String taskName){
        return Flux.fromArray(new Integer[]{1,3,5,7})
                .publishOn(Schedulers.newSingle("new-single" , true))
                .filter(data-> data >3)
                .doOnNext(data -> log.info("#{} doOnNext filter : {}", taskName,data))
                .map(data -> data*10 )
                .doOnNext(data -> log.info("# {} doOnNext map : {}" ,   taskName , data));
    }
}
