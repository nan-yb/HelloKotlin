package com.example.hello.domain.book.v10;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Slf4j
@Configuration("bookRouterV10")
@RequiredArgsConstructor
public class BookRouter {

    private final BookMapper mapper;

    @Bean
    public RouterFunction<?> routeBookV10(BookHandler handler) {
        log.info("RouteBookV10 Router");

        return route(
                RequestPredicates.GET("/v10/books/test/exampleWebClient04")
                , request -> ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM)
                        .body(handler.exampleWebClient04().map(mapper::bookToResponse) , BookDto.Response.class)
                );
    }

    @Bean
    public RouterFunction<?> routeBookV11(BookHandler handler) {
        log.info("RouteBookV11 Router");

        return route()
                .POST("/v10/books", handler::createBook)
                .PATCH("/v10/books/{book-id}", handler::updateBook)
                .GET("/v10/books", handler::getBooks)
                .GET("/v10/books/{book-id}", handler::getBook)
                .GET("/v10/books/test/exampleWebClient01" , handler::exampleWebClient01)
                .GET("/v10/books/test/exampleWebClient02" , handler::exampleWebClient02)
                .GET("/v10/books/test/exampleWebClient03" , handler::exampleWebClient03)
                .build();
    }



                ;
}
