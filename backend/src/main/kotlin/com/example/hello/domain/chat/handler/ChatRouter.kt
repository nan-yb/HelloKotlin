package com.example.hello.domain.chat.handler

import com.example.hello.domain.book.v10.ChatHandler
import com.example.hello.domain.chat.service.ChatService
import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions


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
