package com.example.hello.reactor;

import com.example.hello.reactor.exam.CryptoCurrencyPriceEmitter;
import com.example.hello.reactor.exam.CryptoCurrencyPriceListener;
import com.example.hello.reactor.exam.SampleData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@DisplayName("Chapter14 Operator")
public class Example14_1 {

    private static Logger log = LoggerFactory.getLogger(Example14_1.class);

    @Test
    public void Example14_01(){
        Mono.justOrEmpty(null)
                .subscribe(data -> {} , error -> {} , () -> log.info("# onComplete"));
    }

    @Test
    public void Example14_02(){
        Flux.fromIterable(SampleData.coins)
                .subscribe( coin -> log.info("coin 명 : {} , 현재가 : {}" , coin.getT1() , coin.getT2()));
    }

    @Test
    public void Example14_03(){
        Flux.fromStream(() -> SampleData.coinNames.stream()
                .filter(coin -> coin.equals("BTC") || coin.equals("ETH")))
                .subscribe(data -> log.info("{}"  , data));
    }

    @Test
    public void Example14_04(){
        Flux.range(5,10)
                .subscribe(data -> log.info("{}" , data));
    }

    @Test
    public void Example14_05(){
        Flux.range(7,5)
                .map(idx -> SampleData.btcTopPricesPerYear.get(idx))
                .subscribe(tuple -> log.info("{}'s {}" , tuple.getT1() , tuple.getT2()));
    }

    @Test
    public void Example14_06() throws InterruptedException {
        log.info("# start : {}" , LocalDateTime.now());
        Mono<LocalDateTime> justMono = Mono.just(LocalDateTime.now());
        Mono<LocalDateTime> deferMono = Mono.defer(() -> Mono.just(LocalDateTime.now()));

        Thread.sleep(2000);

        justMono.subscribe( data -> log.info("# onNext just1 : {}" , data));
        deferMono.subscribe( data -> log.info("# onNext defer1 : {}" , data));

        Thread.sleep(2000);

        justMono.subscribe( data -> log.info("# onNext just2 : {}" , data));
        deferMono.subscribe( data -> log.info("# onNext defer2 : {}" , data));
    }

    @Test
    public void Example14_07() throws InterruptedException {
        log.info("# start : {}" , LocalDateTime.now());

        Mono.just("Hello")
                .delayElement(Duration.ofSeconds(3))
                .switchIfEmpty(sayDefault())
//                .switchIfEmpty(Mono.defer(()->sayDefault()))
                .subscribe(data -> log.info("# onNext : {}" , data));

        Thread.sleep(3500);
    }

    @Test
    public void Example14_08(){
        Path path = Paths.get("");
        Flux.using(() -> Files.lines(path) , Flux::fromStream , Stream::close)
            .subscribe(log::info);
    }

    @Test
    public void Example14_09(){
        Flux.generate(() -> 0  , (state , sink) -> {
           sink.next(state);
           if(state == 10) sink.complete();
           return ++ state;
        })
        .subscribe(data -> log.info("#on Next : {}" , data));
    }

    @Test
    public void Example14_10(){
       final int dan = 3;
       Flux.generate(() -> Tuples.of(dan , 1) , (state , sink) ->{
           sink.next(state.getT1() + " * " + state.getT2() + " = " + state.getT1() * state.getT2());
           if(state.getT2() == 9) sink.complete();
           return Tuples.of(state.getT1() , state.getT2() + 1);
       } , state -> log.info("# 구구단 {} 종료!" , state.getT1()))
       .subscribe(data -> log.info("# onNext : {}" , data));
    }

    @Test
    public void Example14_11(){
        Map<Integer , Tuple2<Integer , Long>> map = SampleData.getBtcTopPricesPerYearMap();
        Flux.generate(()-> 2019, (state , sink) -> {
            if(state > 2021){
                sink.complete();
            }else{
                sink.next(map.get(state));
            }

            return ++state;
        })
        .subscribe(data -> log.info("# onNext : {}" , data));
    }

    static int SIZE = 0;
    static int COUNT = -1;
    final static List<Integer> DATA_SOURCE =  Arrays.asList(1,2,3,4,5,6,7,8,9,10);

    @Test
    public void Example14_12(){
        log.info("# start");
        Flux.create((FluxSink<Integer> sink) -> {
            sink.onRequest(n -> {
                try {
                    Thread.sleep(1000L);
                    for (int i= 0; i< n ; i++){
                        if(COUNT >= 9){
                            sink.complete();
                        }else{
                            COUNT++;
                            sink.next(DATA_SOURCE.get(COUNT));
                        }
                    }
                }catch(InterruptedException e){}
            });

            sink.onDispose(() -> log.info("# clean Up"));
        }).subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                request(2);
            }

            @Override
            protected void hookOnNext(Integer value) {
                SIZE++;
                log.info("# onNext : {}" , value );
                if(SIZE == 2) {
                    request(2);
                    SIZE = 0;
                }
            }

            @Override
            protected void hookOnComplete() {
                log.info("# OnComplete");
            }
        });
    }

    @Test
    public void Example14_13() throws InterruptedException {
        CryptoCurrencyPriceEmitter priceEmitter = new CryptoCurrencyPriceEmitter();

        Flux.create((FluxSink<Integer> sink) -> {
            priceEmitter.setListener(new CryptoCurrencyPriceListener() {
                @Override
                public void onPrice(List<Integer> priceList) {
                    priceList.stream().forEach(price -> {
                        sink.next(price);
                    });
                }

                @Override
                public void onComplete() {
                    sink.complete();
                }
            });
        }).publishOn(Schedulers.parallel())
                .subscribe(
                        data -> log.info("# onNext : {}" , data),
                        error -> {} ,
                        () -> log.info(" # onComplete")
                );

        Thread.sleep(3000L);
        priceEmitter.flowInto();
        Thread.sleep(2000L);
        priceEmitter.complete();
    }

    static int start = 1;
    static int end = 4;

    @Test
    public void Example14_14() throws InterruptedException {
        Flux.create((FluxSink<Integer> sink) -> {
            sink.onRequest(n -> {
                log.info("# requested : " + n);
                try {
                    Thread.sleep(500L);
                    for(int i = start ; i<=end; i++){
                        sink.next(i);
                    }
                    start +=4;
                    end +=4;
                }catch(InterruptedException e) {}
            });

            sink.onDispose(() -> {
                log.info("# on clean up");
            });
        } , FluxSink.OverflowStrategy.DROP)
            .subscribeOn(Schedulers.boundedElastic())
            .publishOn(Schedulers.parallel() , 2)
            .subscribe(data -> log.info("# onNext : {}" , data));

        Thread.sleep(3000L);
    }

    @Test
    public void Example14_15(){
        Flux.range(1,20)
                .filter(num -> num % 2 !=0)
                .subscribe(data -> log.info("# onNext:{}" , data));
    }

    @Test
    public void Example14_16(){
        Flux.fromIterable(SampleData.btcTopPricesPerYear)
                .filter(tuple -> tuple.getT2() > 20_000_000)
                .subscribe(data -> log.info(data.getT1() + ":" + data.getT2()));
    }

    @Test
    public void Example14_17() throws InterruptedException {
        Map<SampleData.CovidVaccine , Tuple2<SampleData.CovidVaccine , Integer>> vaccineMap = SampleData.getCovidVaccines();
        Flux.fromIterable(SampleData.coronaVaccineNames)
                .filterWhen(vaccin -> Mono.just(vaccineMap.get(vaccin).getT2() >= 3_000_000)
                        .publishOn(Schedulers.parallel()))
                .subscribe(data -> log.info("# onNext : {}" , data));

        Thread.sleep(1000);
    }

    @Test
    public void Example14_18() throws InterruptedException {
        Flux.interval(Duration.ofSeconds(1))
                .skip(2)
                .subscribe(data -> log.info("# onNext : {}" , data));

        Thread.sleep(5500L);
    }

    @Test
    public void Example14_19() throws  InterruptedException{
        Flux.interval(Duration.ofMillis(300))
                .skip(Duration.ofSeconds(1))
                .subscribe(data -> log.info("# onNext : {}" ,data));

        Thread.sleep(2000L);
    }

    @Test
    public void Example14_20(){
        Flux.fromIterable(SampleData.btcTopPricesPerYear)
                .filter(tuple -> tuple.getT2() >= 20_000_000)
                .skip(2)
                .subscribe(tuple -> log.info("{} , {}" , tuple.getT1() , tuple.getT2()));
    }

    @Test
    public void Example14_21() throws InterruptedException {
        Flux.interval(Duration.ofSeconds(1))
                .take(3)
                .subscribe(data -> log.info("# onNext : {}" , data));

        Thread.sleep(4000L);
    }

    @Test
    public void Example14_22() throws InterruptedException {
        Flux.interval(Duration.ofSeconds(1))
                .take(Duration.ofMillis(2500))
                .subscribe(data -> log.info("# onNext : {}" , data));

        Thread.sleep(3000L);
    }

    @Test
    public void Example14_23() throws InterruptedException{
        Flux.fromIterable(SampleData.btcTopPricesPerYear)
                .takeLast(2)
                .subscribe(tuple -> log.info("# onNext : {} , {}" , tuple.getT1() , tuple.getT2()));
    }

    @Test
    public void Example14_24() throws InterruptedException {
        Flux.fromIterable(SampleData.btcTopPricesPerYear)
                .takeUntil(tuple -> tuple.getT2() > 20_000_000)
                .subscribe(tuple -> log.info("# onNext : {} ,{}" , tuple.getT1() , tuple.getT2()));
    }

    @Test
    public void Example14_25(){
        Flux.fromIterable(SampleData.btcTopPricesPerYear)
                .takeWhile(tuple -> tuple.getT2() < 20_000_000)
                .subscribe(tuple -> log.info("# onNext : {} ,{}" , tuple.getT1() , tuple.getT2()));
    }

    @Test
    public void Example14_26(){
        Flux.fromIterable(SampleData.btcTopPricesPerYear)
                .next()
                .subscribe(tuple -> log.info("# onNext : {} ,{}" , tuple.getT1() , tuple.getT2()));
    }

    @Test
    public void Example14_27(){
        Flux.just("1-Circle" , "3-Circle" , "5-Circle")
                .map(circle -> circle.replace("Circle" , "Rectangle"))
                .subscribe(data -> log.info("onNext :{}" ,data));
    }

    @Test
    public void Example14_28(){
        final double buyPrice = 50_000_000;
        Flux.fromIterable(SampleData.btcTopPricesPerYear)
            .filter(tuple -> tuple.getT1() == 2021)
            .doOnNext(data -> log.info("# doOnNext: {}", data))
            .map(tuple -> calculateProfitRate(buyPrice, tuple.getT2()))
            .subscribe(data -> log.info("# onNext: {}%", data));
    }

    private static double calculateProfitRate(final double buyPrice, Long topPrice) {
        return (topPrice - buyPrice) / buyPrice * 100;
    }


    @Test
    public void Example14_29(){
        Flux
            .just("Good", "Bad")
            .flatMap(feeling -> Flux
                    .just("Morning", "Afternoon", "Evening")
                    .map(time -> feeling + " " + time))
            .subscribe(log::info);
    }

    @Test
    public void Example14_30()throws InterruptedException{
        Flux
                .range(2, 8)
                .flatMap(dan -> Flux
                        .range(1, 9)
                        .publishOn(Schedulers.parallel())
                        .map(n -> dan + " * " + n + " = " + dan * n))
                .subscribe(log::info);

        Thread.sleep(100L);
    }

    @Test
    public void Example14_31()throws InterruptedException{
        Flux
                .concat(Flux.just(1,2,3) , Flux.just(4,5))
                .subscribe(data -> log.info("# onNext: {}", data));
    }

    @Test
    public void Example14_32()throws InterruptedException{     Flux
            .concat(
                    Flux.fromIterable(getViralVector()),
                    Flux.fromIterable(getMRNA()),
                    Flux.fromIterable(getSubunit()))
            .subscribe(data -> log.info("# onNext: {}", data));
    }

    private static List<Tuple2<SampleData.CovidVaccine, Integer>> getViralVector() {
        return SampleData.viralVectorVaccines;
    }

    private static List<Tuple2<SampleData.CovidVaccine, Integer>> getMRNA() {
        return SampleData.mRNAVaccines;
    }

    private static List<Tuple2<SampleData.CovidVaccine, Integer>> getSubunit() {
        return SampleData.subunitVaccines;
    }

    @Test
    public void Example14_33()throws InterruptedException{
        Flux
                .merge(
                        Flux.just(1, 2, 3, 4).delayElements(Duration.ofMillis(300L)),
                        Flux.just(5, 6, 7).delayElements(Duration.ofMillis(500L))
                )
                .subscribe(data -> log.info("# onNext: {}", data));

        Thread.sleep(2000L);
    }

    @Test
    public void Example14_34()throws InterruptedException{    String[] usaStates = {
            "Ohio", "Michigan", "New Jersey", "Illinois", "New Hampshire",
            "Virginia", "Vermont", "North Carolina", "Ontario", "Georgia"
    };

        Flux
                .merge(getMeltDownRecoveryMessage(usaStates))
                .subscribe(log::info);

        Thread.sleep(2000L);
    }

    private static List<Mono<String>> getMeltDownRecoveryMessage(String[] usaStates) {
        List<Mono<String>> messages = new ArrayList<>();
        for (String state : usaStates) {
            messages.add(SampleData.nppMap.get(state));
        }

        return messages;
    }

    @Test
    public void Example14_35()throws InterruptedException{
        Flux
                .zip(
                        Flux.just(1, 2, 3).delayElements(Duration.ofMillis(300L)),
                        Flux.just(4, 5, 6).delayElements(Duration.ofMillis(500L))
                )
                .subscribe(tuple2 -> log.info("# onNext: {}", tuple2));

        Thread.sleep(2500L);
    }

    @Test
    public void Example14_36()throws InterruptedException{
        Flux
                .zip(
                        Flux.just(1, 2, 3).delayElements(Duration.ofMillis(300L)),
                        Flux.just(4, 5, 6).delayElements(Duration.ofMillis(500L)),
                        (n1, n2) -> n1 * n2
                )
                .subscribe(data -> log.info("# onNext: {}", data));

        Thread.sleep(2500L);
    }

    @Test
    public void Example14_37()throws InterruptedException{
        getInfectedPersonsPerHour(10, 21)
                .subscribe(tuples -> {
                    Tuple3<Tuple2, Tuple2, Tuple2> t3 = (Tuple3) tuples;
                    int sum = (int) t3.getT1().getT2() +
                            (int) t3.getT2().getT2() + (int) t3.getT3().getT2();
                    log.info("# onNext: {}, {}", t3.getT1().getT1(), sum);
                });
    }

    private Flux getInfectedPersonsPerHour(int start, int end) {
        return Flux.zip(
                Flux.fromIterable(SampleData.seoulInfected)
                        .filter(t2 -> t2.getT1() >= start && t2.getT1() <= end),
                Flux.fromIterable(SampleData.incheonInfected)
                        .filter(t2 -> t2.getT1() >= start && t2.getT1() <= end),
                Flux.fromIterable(SampleData.suwonInfected)
                        .filter(t2 -> t2.getT1() >= start && t2.getT1() <= end)
        );
    }

    @Test
    public void Example14_38()throws InterruptedException{
        Mono
                .just("Task 1")
                .delayElement(Duration.ofSeconds(1))
                .doOnNext(data -> log.info("# Mono doOnNext: {}", data))
                .and(
                        Flux
                                .just("Task 2", "Task 3")
                                .delayElements(Duration.ofMillis(600))
                                .doOnNext(data -> log.info("# Flux doOnNext: {}", data))
                )
                .subscribe(
                        data -> log.info("# onNext: {}", data),
                        error -> log.error("# onError:", error),
                        () -> log.info("# onComplete")
                );

        Thread.sleep(5000);
    }

    @Test
    public void Example14_39()throws InterruptedException{
        restartApplicationServer()
            .and(restartDBServer())
            .subscribe(
                    data -> log.info("# onNext: {}", data),
                    error -> log.error("# onError:", error),
                    () -> log.info("# sent an email to Administrator: " +
                            "All Servers are restarted successfully")
            );

        Thread.sleep(6000L);
    }

    private static Mono<String> restartApplicationServer() {
        return Mono
                .just("Application Server was restarted successfully.")
                .delayElement(Duration.ofSeconds(2))
                .doOnNext(log::info);
    }

    private static Publisher<String> restartDBServer() {
        return Mono
                .just("DB Server was restarted successfully.")
                .delayElement(Duration.ofSeconds(4))
                .doOnNext(log::info);
    }

    @Test
    public void Example14_40()throws InterruptedException{   Flux
            .just("...", "---", "...")
            .map(code -> transformMorseCode(code))
            .collectList()
            .subscribe(list -> log.info(list.stream().collect(Collectors.joining())));
    }

    public static String transformMorseCode(String morseCode) {
        return SampleData.morseCodeMap.get(morseCode);
    }

    @Test
    public void Example14_41() throws InterruptedException{ Flux
            .range(0, 26)
            .collectMap(key -> SampleData.morseCodes[key],
                    value -> transformToLetter(value))
            .subscribe(map -> log.info("# onNext: {}", map));
    }

    private static String transformToLetter(int value) {
        return Character.toString((char) ('a' + value));
    }

    @Test
    public void Example14_42()throws InterruptedException{}
    @Test
    public void Example14_43()throws InterruptedException{}


    private Mono<String> sayDefault(){
        log.info("# Say Hi");
        return Mono.just("Hi");
    }

}
