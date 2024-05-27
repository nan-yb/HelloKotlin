package com.example.hello.reactor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.data.cassandra.DataCassandraTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.context.Context;

import java.awt.*;
import java.awt.print.Book;

public class Example11 {

    Logger log = LoggerFactory.getLogger(Example11.class);

    @Test
    void Example11_1() throws InterruptedException {
        Mono.deferContextual(ctx ->
                        Mono.just("Hello" + " " + ctx.get("firstName"))
                                .doOnNext(data -> log.info(" # just doOnNext : {}", data))
                ).subscribeOn(Schedulers.boundedElastic())
                .publishOn(Schedulers.parallel())
                .transformDeferredContextual((mono, ctx) -> mono.map(data -> data + " " + ctx.get("lastName")))
                .contextWrite(context -> context.put("lastName", "Jobs"))
                .contextWrite(context -> context.put("firstName", "Steve"))
                .subscribe(data -> log.info("# onNext: {}", data));

        Thread.sleep(100L);
    }

    @Test
    public void Example11_3() throws InterruptedException {
        final String key1 = "company";
        final String key2 = "firstName";
        final String key3 = "lastName";

        Mono.deferContextual(ctx -> Mono.just(ctx.get(key1) + ", " + ctx.get(key2) + " " + ctx.get(key3)))
                .publishOn(Schedulers.parallel())
                .contextWrite(context -> context.putAll(Context.of(key2, "Steve", key3, "Jobs").readOnly())
                )
                .contextWrite(context -> context.put(key1, "Apple"))
                .subscribe(data -> log.info("# onNext : {}", data));

        Thread.sleep(100L);
    }

    @Test
    public void Example11_4() throws InterruptedException {
        final String key1 = "company";
        final String key2 = "firstName";
        final String key3 = "lastName";

        Mono.deferContextual(ctx ->
                        Mono.just(ctx.get(key1) + ", " +
                                        ctx.getOrEmpty(key2).orElse("no firstName") + " " +
                                        ctx.getOrDefault(key3, "no lastName"))
                                .publishOn(Schedulers.parallel())
                                .contextWrite(context -> context.put(key1, "Apple")))
                .subscribe(data -> log.info("# onNext : {}", data));

        Thread.sleep(100L);
    }


    @Test
    public void Example11_5() throws InterruptedException {
        final String key1 = "company";

        Mono<String> mono = Mono.deferContextual(ctx ->
                        Mono.just("Company : " + " " + ctx.get(key1))
                )
                .publishOn(Schedulers.parallel());

        mono.contextWrite(context -> context.put(key1, "Apple"))
                .subscribe(data -> log.info("# subscribe1 onNext : {}", data));

        mono.contextWrite(context -> context.put(key1, "Microsoft"))
                .subscribe(data -> log.info("# subscribe2 onNext : {}", data));

        Thread.sleep(100L);
    }

    @Test
    public void Example11_6() throws InterruptedException {
        String key1 = "company";
        String key2 = "name";

        Mono.deferContextual(ctx -> Mono.just(ctx.get(key1)))
                .publishOn(Schedulers.parallel())
                .contextWrite(context -> context.put(key2, "Bill"))
                .transformDeferredContextual((mono, ctx) ->
                        mono.map(data -> data + ", " + ctx.getOrDefault(key2, "Steve")))
                .contextWrite(context -> context.put(key1, "Apple"))
                .subscribe(data -> log.info("# onNext : {}", data));

        Thread.sleep(100L);
    }

    @Test
    public void Example11_7() throws InterruptedException {
        String key1 = "company";
        Mono.just("Steve")
//            .transformDeferredContextual((stringMono ,ctx) -> ctx.get("role"))
                .flatMap(name -> Mono.deferContextual(ctx ->
                                Mono
                                        .just(ctx.get(key1) + ", " + name)
                                        .transformDeferredContextual((mono, innerCtx) ->
                                                mono.map(data -> data + ", " + innerCtx.get("role"))
                                        )
                                        .contextWrite(context -> context.put("role", "CEO"))
                        )
                )
                .publishOn(Schedulers.parallel())
                .contextWrite(context -> context.put(key1, "Apple"))
                .subscribe(data -> log.info("# onNext : {}", data));

        Thread.sleep(100L);
    }

    public static final String HEADER_AUTH_TOKEN = "authToken";

    @Test
    @DisplayName("인증된 도서 관리자가 신규 도서를 등록하기 위해 도서 정보와 인증토큰을 서버로 전송한다는 가정")
    public void Example11_8() {
        Mono<String> mono = postBook(Mono.just(
                new Book("abcd-1111-2222-3333",
                        "Reactor's Bible",
                        "Keven")
        ))
                .contextWrite(Context.of(HEADER_AUTH_TOKEN, "eyskdks"));

        mono.subscribe(data -> log.info("# onNext : {}", data));
    }

    private static Mono<String> postBook(Mono<Book> book) {
        return Mono.zip(book, Mono.deferContextual(ctx ->
                Mono.just(ctx.get(HEADER_AUTH_TOKEN)))
        ).flatMap(tuple -> {
            String response = "POST the book(" + tuple.getT1().getBookName() +
                    "," + tuple.getT1().getAuthor() + ") with token : " +
                    tuple.getT2();
            return Mono.just(response);
        });
    }

    class Book {
        private String isbn;
        private String bookName;

        public Book(String isbn, String bookName, String author) {
            this.isbn = isbn;
            this.bookName = bookName;
            this.author = author;
        }

        private String author;

        public String getIsbn() {
            return isbn;
        }

        public String getBookName() {
            return bookName;
        }

        public String getAuthor() {
            return author;
        }
    }
}
