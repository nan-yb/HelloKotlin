package com.example.hello.reactor;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

@DisplayName(
"DEBUGGER 예제"
)
public class Example12 {

    private Logger log = LoggerFactory.getLogger(Example12.class);

    public static Map<String , String> fruits = new HashMap<>();

    static {
        fruits.put("banana" , "바나나");
        fruits.put("apple" , "사과");
        fruits.put("pear" , "배");
        fruits.put("grape" , "포도");
    }


    @Test
    public void Example12_1() throws InterruptedException {

        Hooks.onOperatorDebug();

        Flux.fromArray(new String[]{"BANANAS" , "APPLES" , "PEARS" , "MELONS"})
                .subscribeOn(Schedulers.boundedElastic())
                .publishOn(Schedulers.parallel())
                .map(String::toLowerCase)
                .map(fruit -> fruit.substring(0 ,fruit.length() -1))
                .map(fruits::get)
                .map(translate -> "맛있는 " + translate)
                .subscribe(
                        log::info ,
                        error -> log.error("# onError :" , error)
                );

        Thread.sleep(100L);
    }

    @Test
    @DisplayName("checkpoint() Operator를 사용한 디버깅")
    public void Example12_2(){
        Flux.just(2,4,6,8)
                .zipWith(Flux.just(1,2,3,0) , (x,y) -> x/y )
                .checkpoint()
                .map(num -> num +2)
                .checkpoint()
                .subscribe(
                        data -> log.info("# onNext : {}" , data),
                        error -> log.error("# onError: " , error)
                );

    }

    @Test
    @DisplayName("Traceback 출력 없이 식별자를 포함한 Description 을 출력해서 에러 발생 지점을 예상하는 방법")
    public void Example12_4(){
        Flux.just(2,4,6,8)
                .zipWith(Flux.just(1 ,2 , 3, 0) , (x , y) -> x/y)
                .checkpoint("Example12_4.zipWith.checpoint")
                .map(num -> num + 2)
                .checkpoint("Example12_4.amp.checkpoint")
                .subscribe(
                        data -> log.info("# onNext : {}" , data),
                        error -> log.error("# onError :" , error)
                );
    }

    @Test
    @DisplayName("Traceback과 Description 을 모두 출력")
    public void Example12_5(){
        Flux.just(2,4,6,8)
                .zipWith(Flux.just(1,2,3,0) , (x,y) -> x/y)
                .checkpoint("Example12_5.zipWith.checkpoint" , true)
                .map(num -> num + 2)
                .checkpoint("Example12_5.map.checkpoint" , true)
                .subscribe(
                        data -> log.info("# onNext : {}" , data),
                        error -> log.error("# onError :" , error)
                );
    }

    @Test
    @DisplayName("서로 다른 Operator 체인에서의 checkpoint() 활용")
    public void Example12_6(){
        Flux<Integer> source = Flux.just(2,4,6,8);
        Flux<Integer> other = Flux.just(1,2,3,0);

        Flux<Integer> multiplySource = multiply(source , other).checkpoint();
        Flux<Integer> plusSource =plus(multiplySource).checkpoint();

        plusSource.subscribe(
                data -> log.info("# onNext : {}" , data),
                error -> log.error("# onError :" , error)
        );
    }

    private static Flux<Integer> multiply(Flux<Integer> source , Flux<Integer> other){
        return source.zipWith(other , (x , y) -> x/y);
    }

    private static Flux<Integer> plus(Flux<Integer> source){
        return source.map(num -> num + 2);
    }

    @Test
    @DisplayName("log() Operator를 사용한 디버깅")
    public void Example12_7(){

        Flux.fromArray(new String[]{"BANANAS" , "APPLES" , "PEARS" , "MELONS"})
                .map(String::toLowerCase)
                .map(fruit -> fruit.substring(0, fruit.length() -1))
                .log("Fruit.Substring" , Level.FINE)
                .map(fruits::get)
                .subscribe(
                        log::info ,
                        error -> log.error("# onError : " , error)
                );
    }

}


