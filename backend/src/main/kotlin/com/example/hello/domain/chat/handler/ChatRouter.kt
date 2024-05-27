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
            .POST("/chats", handler::createChats)
            .GET("/chats/{room-id}"  , handler::findChatsByRoomId)
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
            .GET("/room/{user-id}", handler::getRooms)
            .POST("/room", handler::createRooms)
            .DELETE("/room/{room-id}" , handler::deleteRooms)
            .build()
    }
}
