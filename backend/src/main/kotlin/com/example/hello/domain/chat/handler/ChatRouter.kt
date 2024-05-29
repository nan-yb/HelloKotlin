package com.example.hello.domain.chat.handler

import com.example.hello.domain.book.v10.ChatHandler
import com.example.hello.domain.book.v10.UserHandler
import com.example.hello.domain.chat.service.ChatService
import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions


@Configuration("ChatHandler")
@RequiredArgsConstructor
class ChatRouter {
    @Bean
    fun routeChat(handler: ChatHandler): RouterFunction<*> {
        return RouterFunctions.route()
            .POST("/chats", handler::createChats)
            .GET("/chats/{room-id}", handler::findChatsByRoomId)
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
            .GET("/rooms" , handler::getAllRooms)
            .GET("/room/{user-id}", handler::getRoomsByUserId)
            .POST("/room", handler::createRooms)
            .DELETE("/room/{room-id}" , handler::deleteRooms)
            .build()
    }

    @Bean
    fun routeUser(handler : UserHandler) : RouterFunction<*> {
        return RouterFunctions.route()
            .GET("/user/{user-id}" , handler::getUser )
            .POST("/user" , handler::createUser )
            .build()
    }
}
