package com.example.hello.domain.chat.handler

import com.example.hello.domain.book.v10.*
import com.example.hello.domain.book.v10.BookRouter
import com.example.hello.domain.chat.dto.ChatDTO
import com.example.hello.domain.chat.entity.Chat
import com.example.hello.domain.chat.service.ChatService
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.server.*
import org.springframework.web.util.UriBuilder
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration


@Configuration("ChatHandler")
@RequiredArgsConstructor
class ChatRouter (
    var chatService: ChatService,
    var chatHandler: ChatHandler
){

    @Bean
    fun routeChat(handler: ChatHandler): RouterFunction<*> {
        return RouterFunctions.route()
            .POST("/chats") { request: ServerRequest? -> handler.createChats(request!!) }
//            .PATCH("/chats/{chat-id}") { request: ServerRequest? -> handler.updateBook(request!!) }
//            .GET("/chats") { request: ServerRequest? -> handler.getBooks(request!!) }
//            .GET("/chats/{chat-id}") { request: ServerRequest? -> handler.getBook(request!!) }
//            .GET("/chats/test/exampleWebClient01") { serverRequest: ServerRequest? ->
//                handler.exampleWebClient01(
//                    serverRequest
//                )
//            }
//            .GET("/books/test/exampleWebClient02") { serverRequest: ServerRequest? ->
//                handler.exampleWebClient02(
//                    serverRequest
//                )
//            }
//            .GET("/books/test/exampleWebClient03") { serverRequest: ServerRequest? ->
//                handler.exampleWebClient03(
//                    serverRequest
//                )
//            }
            .build()
    }

    @Bean
    fun routeRoom(handler: RoomHandler): RouterFunction<*> {
        return RouterFunctions.route()
            .POST("/rooms") { request: ServerRequest? -> handler.createRooms(request!!) }
//            .PATCH("/rooms/{room-id}") { request: ServerRequest? -> handler.updateBook(request!!) }
//            .GET("/rooms") { request: ServerRequest? -> handler.getBooks(request!!) }
//            .GET("/rooms/{room-id}") { request: ServerRequest? -> handler.getBook(request!!) }
            .build()
    }
}
