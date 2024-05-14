package com.example.hello.reactor;


import io.reactivex.internal.util.BackpressureHelper;
import jakarta.persistence.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.test.StepVerifierOptions;
import reactor.test.scheduler.VirtualTimeScheduler;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

@DisplayName(
        "Testing 예제"
)
public class Example13 {

    // expectSubscription : 구독이 이루어짐을 기대한다.
    // expectNext(T t) : onNext Signal 을 통해 전달되는 값이 파라미터로 전달된 값과 같음을 기대한다.
    // exceptComplete() : onComplete Signal 이 전송되기를 기대한다.
    // expectError() : onError Signal 이 전송되기를 기대한다.
    // expectNextCount(long count) : 구독 시점 또는 이전(previous) expectNext()를 통해 기댓값이 평가된 데이터 이후부터 emit 된 수를 기대한다.
    // expectNoEvent(Duration duration) : 주어진 시간 동안 Signal 이벤트가 발생하지 않았음을 기대한다.
    // expectAccessibleContext() : 구독시점 이후에 Context 가 전파되었음을 기대한다.
    // expectNextSequence(Iterable<? extend T> iterable) : emit 된 데이터들이 파라미터로 전달된 Iterable 의 요소와 매치됨을 기대한다.

    private Logger log = LoggerFactory.getLogger(Example13.class);

    @Test
    @DisplayName("StepVerifier 이벤트 테스팅 ")
    public void Example13_1() {
        StepVerifier.create(Mono.just("Hello Reactor"))
                .expectNext("Hello Reactor")
                .expectComplete()
                .verify();
    }

    public static Flux<String> sayHello() {
        return Flux.just("Hello", "Reactor");
    }

    public static Flux<Integer> divideByTwo(Flux<Integer> source) {
        return source.zipWith(Flux.just(2, 2, 2, 2, 0), (x, y) -> x / y);
    }

    public static Flux<Integer> takeNumber(Flux<Integer> source, long n) {
        return source.take(n);
    }

    @Test
    public void Example13_3() {
        StepVerifier
                .create(sayHello())
                .expectSubscription()
                .as("# expect subscription") // 이전 기댓값 평가 단계에 대한 설명
                .expectNext("Hi")
                .as("# expect Hi")
                .expectNext("Reactor")
                .as("# expect Reactor")
                .verifyComplete();
    }

    @Test
    public void Example13_4() {
        Flux<Integer> source = Flux.just(2, 4, 6, 8, 10);
        StepVerifier
                .create(divideByTwo(source))
                .expectSubscription()
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
//                .expectNext(1,2,3,4,)
                .expectError()
                .verify();
    }

    @Test
    public void Example13_5() {
        Flux<Integer> source = Flux.range(0, 1000);
        StepVerifier
                .create(takeNumber(source, 500)
                        , StepVerifierOptions.create().scenarioName("Verify from 0 to 499"))
                .expectSubscription()
                .expectNext(0)
                .expectNextCount(498)
                .expectNext(500)
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("시간 기반 테스트")
    public void Example13_7() {
        StepVerifier
                .withVirtualTime(()-> TimeBasedTestExample.getCOVID19count(Flux.interval(Duration.ofHours(1))).take(1))
                .expectSubscription()
                .then(() -> VirtualTimeScheduler.get()
                        .advanceTimeBy(Duration.ofHours(1)))
                .expectNextCount(1)
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("3초안에 실행 안되면 실패")
    public void Example13_8(){
        StepVerifier
                .create(TimeBasedTestExample.getCOVID19count(Flux.interval(Duration.ofMinutes(1))).take(1)) // 1분뒤에 실행되도록
                .expectSubscription()
                .expectNextCount(11)
                .expectComplete()
                .verify(Duration.ofSeconds(3)); // 3초 설정
    }

    @Test
    @DisplayName("1분에 한번씩 실행되는 함수 (총 5분) 을 시간을 앞당겨서 테스트")
    public void Example13_9(){
        StepVerifier.withVirtualTime(()->TimeBasedTestExample.getVoteCount(Flux.interval(Duration.ofMinutes(1))))
            .expectSubscription()
            .expectNoEvent(Duration.ofMinutes(1))
            .expectNoEvent(Duration.ofMinutes(1))
            .expectNoEvent(Duration.ofMinutes(1))
            .expectNoEvent(Duration.ofMinutes(1))
            .expectNoEvent(Duration.ofMinutes(1)) // 5번 시간을 앞당김 => 5분
            .expectNextCount(5)
            .expectComplete()
            .verify();
    }

    public static Flux<Integer> generateNumber(){
        return Flux.create(emitter -> {
            for (int i = 1; i<=100; i++){
                emitter.next(i);
            }
            emitter.complete();
        } , FluxSink.OverflowStrategy.ERROR);
    }

    @Test
    public void Example13_11(){
        StepVerifier.create(generateNumber() , 1L)
                .thenConsumeWhile(num -> num >=1)
                .verifyComplete();
    }

    @Test
    public void Example13_12(){
        StepVerifier.create(generateNumber() , 1L)
            .thenConsumeWhile(num -> num >= 1)
            .expectError()
            .verifyThenAssertThat()
            .hasDroppedElements();
    }

//    @Test
//    public void Example13_14(){
//        Mono<String> source = Mono.just("hello");
//
//        StepVerifier.create(
//                ContextTestExample.getSecretMessage(source)
//                        .contextWrite(context -> context.put("secretMessage" , "Hello , Reactor"))
//                        .contextWrite(context-> context.put("secretKey" , "awd123123"))
//        )
//                .expectSubscription()
//                .expectAccessibleContext()
//                .hasKey("secretKey")
//                .hasKey("secretMessage")
//                .then()
//                .expectNext("Hello Reactor")
//                .expectComplete()
//                .verify();
//    }
//
//    static class ContextTestExample{
//        public static Mono<String> getSecretMessage(Mono<String> keySource){
//            return keySource
//                    .zipWith(Mono.deferContextual(ctx -> Mono.just((String)ctx.get("secretKey"))))
//                    .filter(tp -> tp.getT1().equals(new String(Base64Utils.decodeFromString(tp.getT2()))))
//                    .transformDeferredContextual((mono , ctx) -> mono.map(notUse -> ctx.get("secretMessage")));
//        }
//    }

    static class TimeBasedTestExample {
        public static Flux<Tuple2<String, Integer>> getCOVID19count(Flux<Long> source) {
            return source.flatMap(notUse -> Flux.just(
                    Tuples.of("서울", 10),
                    Tuples.of("경기도", 5),
                    Tuples.of("강원동", 3),
                    Tuples.of("충청도", 6),
                    Tuples.of("경상도", 5),
                    Tuples.of("전라도", 8),
                    Tuples.of("인천", 2),
                    Tuples.of("대전", 1),
                    Tuples.of("대구", 2),
                    Tuples.of("부산", 3),
                    Tuples.of("제주도", 0)
            ));
        }

        public static Flux<Tuple2<String, Integer>> getVoteCount(Flux<Long> source) {
            return source
                    .zipWith(Flux.just(
                            Tuples.of("중구", 15400),
                            Tuples.of("강서구", 18600),
                            Tuples.of("강동구", 14600),
                            Tuples.of("서대문구", 11400),
                            Tuples.of("서초구", 20020)
                    ))
                    .map(Tuple2::getT2);
        }
    }


}


