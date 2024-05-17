package com.example.hello.domain.book.v10;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.net.URI;
import java.time.Duration;

@Slf4j
@Component("BookHandlerV10")
public class BookHandler {
    private final BookMapper mapper;
    private final BookValidator validator;
    private final BookService bookService;

    private final String LOCAL_URL = "http://localhost:8080";

    public BookHandler(BookMapper mapper, BookValidator validator, BookService bookService) {
        this.mapper = mapper;
        this.validator = validator;
        this.bookService = bookService;
    }

    public Mono<ServerResponse> createBook(ServerRequest request) {
        return request.bodyToMono(BookDto.Post.class)
//                .doOnNext(post -> validator.validate(post))
                .flatMap(post -> bookService.createBook(mapper.bookPostToBook(post)))
                .flatMap(book -> ServerResponse
                        .created(URI.create("/v10/books/" + book.getBookId()))
                        .build());
    }

    public Mono<ServerResponse> updateBook(ServerRequest request) {
        final long bookId = Long.valueOf(request.pathVariable("book-id"));
        return request
                .bodyToMono(BookDto.Patch.class)
                .doOnNext(patch -> validator.validate(patch))
                .flatMap(patch -> {
                    patch.setBookId(bookId);
                    return bookService.updateBook(mapper.bookPatchToBook(patch));
                })
                .flatMap(book -> ServerResponse.ok()
                                        .bodyValue(mapper.bookToResponse(book)));
    }

    public Mono<ServerResponse> getBook(ServerRequest request) {
        long bookId = Long.valueOf(request.pathVariable("book-id"));

        log.info( "bookID : {}" , bookId);
        return bookService.findBook(bookId)
                        .flatMap(book -> ServerResponse
                                .ok()
                                .bodyValue(mapper.bookToResponse(book)));
    }

    public Mono<ServerResponse> getBooks(ServerRequest request) {
        Tuple2<Long, Long> pageAndSize = getPageAndSize(request);
        return bookService.findBooks(pageAndSize.getT1(), pageAndSize.getT2())
                .flatMap(books -> ServerResponse
                        .ok()
                        .bodyValue(mapper.booksToResponse(books)));
    }

    private Tuple2<Long, Long> getPageAndSize(ServerRequest request) {
        long page = request.queryParam("page").map(Long::parseLong).orElse(0L);
        long size = request.queryParam("size").map(Long::parseLong).orElse(0L);
        return Tuples.of(page, size);
    }

    public Mono<ServerResponse>  exampleWebClient01(ServerRequest serverRequest) {
        BookDto.Post requestBody = new BookDto.Post(
                "Java 중급" ,
                "Intermediate Java" ,
                "Java 중급 프로그래밍 마스터" ,
                "Keven1" ,
                "222-22-2222-222-2" ,
                "2022-03-22"
        );

        WebClient webClient = WebClient.create();

        Mono<ResponseEntity<Void>> response = webClient.post().uri(LOCAL_URL + "/v10/books")
                .bodyValue(requestBody)
                .retrieve()
                .toEntity(Void.class);

        response.subscribe(res -> {
            log.info("response Status : {}" , res.getStatusCode());
            log.info("Header Location : {}" , res.getHeaders().get("Location"));
        });

        return ServerResponse.ok().bodyValue(response);
    }


    public Mono<ServerResponse> exampleWebClient02(ServerRequest serverRequest) {
        BookDto.Patch requestBody = BookDto.Patch.builder()
                .titleEnglish("Advanced Java")
                .titleKorean("Java 고급")
                .description("Java 고급 프로그래밍 마스터")
                .author("Tom")
                .build();

        WebClient webClient = WebClient.create();

        Mono<BookDto.Response> response = webClient.patch().uri(LOCAL_URL + "/v10/books/{book-id}" , 20)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(BookDto.Response.class);

        response.subscribe(book -> {
            log.info("bookID : {} "  , book.getBookId());
            log.info("titleKorean : {} "  , book.getTitleKorean());
            log.info("titleEnglish : {} "  , book.getTitleEnglish());
            log.info("description : {} "  , book.getDescription());
            log.info("author : {} "  , book.getAuthor());
        });

        return ServerResponse.ok().bodyValue(response);
    }

    public Mono<ServerResponse> exampleWebClient03(ServerRequest serverRequest) {
        Mono<BookDto.Response> response = WebClient.create(LOCAL_URL)
                .get()
                .uri(uriBuilder -> uriBuilder.path(LOCAL_URL + "/v10/books/{book-id}").build(21))
                .retrieve()
                .bodyToMono(BookDto.Response.class);

        response.subscribe(book -> {
            log.info("bookID : {} "  , book.getBookId());
            log.info("titleKorean : {} "  , book.getTitleKorean());
            log.info("titleEnglish : {} "  , book.getTitleEnglish());
            log.info("description : {} "  , book.getDescription());
            log.info("author : {} "  , book.getAuthor());
        });

        return ServerResponse.ok().bodyValue(response);
    }

    public Flux<Book> exampleWebClient04() {
        Flux<Book> response = WebClient.create(LOCAL_URL)
                .get()
                .uri(uriBuilder -> uriBuilder.path("/v10/books")
                        .queryParam("page" , "1")
                        .queryParam("size" , "10")
                        .build())
                .retrieve()
                .bodyToFlux(Book.class);

        return response.delayElements(Duration.ofSeconds(2L));
    }

}
