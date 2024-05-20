package com.example.hello.reactor;

import com.example.hello.reactor.exam.Book;
import com.example.hello.reactor.exam.SampleData;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.Disposable;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

@DisplayName("Chapter14 Operator")
public class Example14_2 {

    private static Logger log = LoggerFactory.getLogger(Example14_2.class);

    @Test
    public void Example14_42() throws InterruptedException {
        Flux.range(1, 5)
                .doFinally(signalType -> log.info("# doFinally 1: {}", signalType))
                .doFinally(signalType -> log.info("# doFinally 2: {}", signalType))
                .doOnNext(data -> log.info("# range > doOnNext(): {}", data))
                .doOnRequest(data -> log.info("# doOnRequest: {}", data))
                .doOnSubscribe(subscription -> log.info("# doOnSubscribe 1"))
                .doFirst(() -> log.info("# doFirst()"))
                .filter(num -> num % 2 == 1)
                .doOnNext(data -> log.info("# filter > doOnNext(): {}", data))
                .doOnComplete(() -> log.info("# doOnComplete()"))
                .subscribe(new BaseSubscriber<>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(1);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        log.info("# hookOnNext: {}", value);
                        request(1);
                    }
                });
    }

    @Test
    public void Example14_43() throws InterruptedException {
        Flux
                .range(1, 5)
                .flatMap(num -> {
                    if ((num * 2) % 3 == 0) {
                        return Flux.error(
                                new IllegalArgumentException("Not allowed multiple of 3"));
                    } else {
                        return Mono.just(num * 2);
                    }
                })
                .subscribe(data -> log.info("# onNext: {}", data),
                        error -> log.error("# onError: ", error));
    }

    @Test
    public void Example14_44() throws InterruptedException {
        Flux
                .just('a', 'b', 'c', '3', 'd')
                .flatMap(letter -> {
                    try {
                        return convert(letter);
                    } catch (DataFormatException e) {
                        return Flux.error(e);
                    }
                })
                .subscribe(data -> log.info("# onNext: {}", data),
                        error -> log.error("# onError: ", error));
    }

    private static Mono<String> convert(char ch) throws DataFormatException {
        if (!Character.isAlphabetic(ch)) {
            throw new DataFormatException("Not Alphabetic");
        }
        return Mono.just("Converted to " + Character.toUpperCase(ch));
    }

    @Test
    public void Example14_45() throws InterruptedException {
        getBooks()
                .map(book -> book.getPenName().toUpperCase())
                .onErrorReturn("No pen name")
                .subscribe(log::info);
    }

    public static Flux<Book> getBooks() {
        return Flux.fromIterable(SampleData.books);
    }

    @Test
    public void Example14_46() throws InterruptedException {
        getBooks()
                .map(book -> book.getPenName().toUpperCase())
                .onErrorReturn(NullPointerException.class, "no pen name")
                .onErrorReturn(IllegalFormatException.class, "Illegal pen name")
                .subscribe(log::info);
    }

    @Test
    public void Example14_47() throws InterruptedException {
        final String keyword = "DDD";
        getBooksFromCache(keyword)
                .onErrorResume(error -> getBooksFromDatabase(keyword))
                .subscribe(data -> log.info("# onNext: {}", data.getBookName()),
                        error -> log.error("# onError: ", error));
    }

    public static Flux<Book> getBooksFromCache(final String keyword) {
        return Flux
                .fromIterable(SampleData.books)
                .filter(book -> book.getBookName().contains(keyword))
                .switchIfEmpty(Flux.error(new NoSuchBookException("No such Book")));
    }

    public static Flux<Book> getBooksFromDatabase(final String keyword) {
        List<Book> books = new ArrayList<>(SampleData.books);
        books.add(new Book("DDD: Domain Driven Design",
                "Joy", "ddd-man", 35000, 200));
        return Flux
                .fromIterable(books)
                .filter(book -> book.getBookName().contains(keyword))
                .switchIfEmpty(Flux.error(new NoSuchBookException("No such Book")));
    }

    private static class NoSuchBookException extends RuntimeException {
        NoSuchBookException(String message) {
            super(message);
        }
    }

    @Test
    public void Example14_48() throws InterruptedException {
        Flux
                .just(1, 2, 4, 0, 6, 12)
                .map(num -> 12 / num)
                .onErrorContinue((error, num) ->
                        log.error("error: {}, num: {}", error, num))
                .subscribe(data -> log.info("# onNext: {}", data),
                        error -> log.error("# onError: ", error));
    }

    @Test
    public void Example14_49() throws InterruptedException {
        final int[] count = {1};
        Flux
                .range(1, 3)
                .delayElements(Duration.ofSeconds(1))
                .map(num -> {
                    try {
                        if (num == 3 && count[0] == 1) {
                            count[0]++;
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException e) {
                    }

                    return num;
                })
                .timeout(Duration.ofMillis(1500))
                .retry(1)
                .subscribe(data -> log.info("# onNext: {}", data),
                        (error -> log.error("# onError: ", error)),
                        () -> log.info("# onComplete"));

        Thread.sleep(7000);
    }

    @Test
    public void Example14_50() throws InterruptedException {
        getBooks2()
                .collect(Collectors.toSet())
                .subscribe(bookSet -> bookSet.stream()
                        .forEach(book -> log.info("book name: {}, price: {}",
                                book.getBookName(), book.getPrice())));

        Thread.sleep(12000);
    }


    private static Flux<Book> getBooks2() {
        final int[] count = {0};
        return Flux
                .fromIterable(SampleData.books)
                .delayElements(Duration.ofMillis(500))
                .map(book -> {
                    try {
                        count[0]++;
                        if (count[0] == 3) {
                            Thread.sleep(2000);
                        }
                    } catch (InterruptedException e) {
                    }

                    return book;
                })
                .timeout(Duration.ofSeconds(2))
                .retry(1)
                .doOnNext(book -> log.info("# getBooks > doOnNext: {}, price: {}",
                        book.getBookName(), book.getPrice()));
    }


    @Test
    public void Example14_51() throws InterruptedException {
        Flux
                .range(1, 5)
                .delayElements(Duration.ofSeconds(1))
                .elapsed()
                .subscribe(data -> log.info("# onNext: {}, time: {}",
                        data.getT2(), data.getT1()));

        Thread.sleep(6000);
    }

    @Test
    public void Example14_52() throws InterruptedException {
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


        Mono.defer(() -> Mono.just(
                                restTemplate
                                        .exchange(worldTimeUri,
                                                HttpMethod.GET,
                                                new HttpEntity<String>(headers),
                                                String.class)
                        )
                )
                .repeat(4)
                .elapsed()
                .map(response -> {
                    DocumentContext jsonContext =
                            JsonPath.parse(response.getT2().getBody());
                    String dateTime = jsonContext.read("$.datetime");
                    return Tuples.of(dateTime, response.getT1());
                })
                .subscribe(
                        data -> log.info("now: {}, elapsed time: {}", data.getT1(), data.getT2()),
                        error -> log.error("# onError:", error),
                        () -> log.info("# onComplete")
                );
    }

    @Test
    public void Example14_53() throws InterruptedException {
        Flux.range(1, 11)
                .window(3)
                .flatMap(flux -> {
                    log.info("======================");
                    return flux;
                })
                .subscribe(new BaseSubscriber<>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        subscription.request(2);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        log.info("# onNext: {}", value);
                        request(2);
                    }
                });
    }

    @Test
    public void Example14_54() throws InterruptedException {
//        Flux.fromIterable(SampleData.monthlyBookSales2021)
//                .window(3)
//                .flatMap(flux -> MathFlux.sumInt(flux))
//                .subscribe(new BaseSubscriber<>() {
//                    @Override
//                    protected void hookOnSubscribe(Subscription subscription) {
//                        subscription.request(2);
//                    }
//
//                    @Override
//                    protected void hookOnNext(Integer value) {
//                        log.info("# onNext: {}", value);
//                        request(2);
//                    }
//                });
    }

    @Test
    public void Example14_55() throws InterruptedException {
        Flux.range(1, 95)
                .buffer(10)
                .subscribe(buffer -> log.info("# onNext: {}", buffer));
    }

    @Test
    public void Example14_56() throws InterruptedException {
        Flux
                .range(1, 20)
                .map(num -> {
                    try {
                        if (num < 10) {
                            Thread.sleep(100L);
                        } else {
                            Thread.sleep(300L);
                        }
                    } catch (InterruptedException e) {
                    }
                    return num;
                })
                .bufferTimeout(3, Duration.ofMillis(400L))
                .subscribe(buffer -> log.info("# onNext: {}", buffer));
    }

    @Test
    public void Example14_57() throws InterruptedException {
        Flux.fromIterable(SampleData.books)
                .groupBy(book -> book.getAuthorName())
                .flatMap(groupedFlux ->
                        groupedFlux
                                .map(book -> book.getBookName() +
                                        "(" + book.getAuthorName() + ")")
                                .collectList()
                )
                .subscribe(bookByAuthor ->
                        log.info("# book by author: {}", bookByAuthor));

    }

    @Test
    public void Example14_58() throws InterruptedException {
        Flux.fromIterable(SampleData.books)
                .groupBy(book ->
                                book.getAuthorName(),
                        book -> book.getBookName() + "(" + book.getAuthorName() + ")")
                .flatMap(groupedFlux -> groupedFlux.collectList())
                .subscribe(bookByAuthor ->
                        log.info("# book by author: {}", bookByAuthor));
    }

    @Test
    public void Example14_59() throws InterruptedException {
        Flux.fromIterable(SampleData.books)
            .groupBy(book -> book.getAuthorName())
            .flatMap(groupedFlux -> Mono
                        .just(groupedFlux.key())
                        .zipWith(
                                groupedFlux
                                        .map(book ->
                                                (int) (book.getPrice() * book.getStockQuantity() * 0.1))
                                        .reduce((y1, y2) -> y1 + y2),
                                (authorName, sumRoyalty) ->
                                        authorName + "'s royalty: " + sumRoyalty)
            )
            .subscribe(log::info);

    }

    @Test
    public void Example14_60() throws InterruptedException {
        ConnectableFlux<Integer> flux =
                Flux
                        .range(1, 5)
                        .delayElements(Duration.ofMillis(300L))
                        .publish();

        Thread.sleep(500L);
        flux.subscribe(data -> log.info("# subscriber1: {}", data));

        Thread.sleep(200L);
        flux.subscribe(data -> log.info("# subscriber2: {}", data));

        flux.connect();

        Thread.sleep(1000L);
        flux.subscribe(data -> log.info("# subscriber3: {}", data));

        Thread.sleep(2000L);
    }

    private static ConnectableFlux<String> publisher;
    private static int checkedAudience;
    static {
        publisher =
                Flux
                        .just("Concert part1", "Concert part2", "Concert part3")
                        .delayElements(Duration.ofMillis(300L))
                        .publish();
    }

    @Test
    public void Example14_61() throws InterruptedException {

        checkAudience();
        Thread.sleep(500L);
        publisher.subscribe(data -> log.info("# audience 1 is watching {}", data));
        checkedAudience++;

        Thread.sleep(500L);
        publisher.subscribe(data -> log.info("# audience 2 is watching {}", data));
        checkedAudience++;

        checkAudience();

        Thread.sleep(500L);
        publisher.subscribe(data -> log.info("# audience 3 is watching {}", data));

        Thread.sleep(1000L);
    }

    public static void checkAudience() {
        if (checkedAudience >= 2) {
            publisher.connect();
        }
    }

    @Test
    public void Example14_62() throws InterruptedException {
        Flux<String> publisher =
                Flux
                        .just("Concert part1", "Concert part2", "Concert part3")
                        .delayElements(Duration.ofMillis(300L))
                        .publish()
                        .autoConnect(2);

        Thread.sleep(500L);
        publisher.subscribe(data -> log.info("# audience 1 is watching {}", data));

        Thread.sleep(500L);
        publisher.subscribe(data -> log.info("# audience 2 is watching {}", data));

        Thread.sleep(500L);
        publisher.subscribe(data -> log.info("# audience 3 is watching {}", data));

        Thread.sleep(1000L);
    }

    @Test
    public void Example14_63() throws InterruptedException {
        Flux<Long> publisher =
                Flux
                        .interval(Duration.ofMillis(500))

                        .publish().autoConnect(1);
//                    .publish().refCount(1);
        Disposable disposable =
                publisher.subscribe(data -> log.info("# subscriber 1: {}", data));

        Thread.sleep(2100L);
        disposable.dispose();

        publisher.subscribe(data -> log.info("# subscriber 2: {}", data));

        Thread.sleep(2500L);
    }

}
