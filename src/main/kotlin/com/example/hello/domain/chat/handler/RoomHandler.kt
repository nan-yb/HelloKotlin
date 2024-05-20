package com.example.hello.domain.chat.handler

import com.example.hello.domain.book.v10.Book
import com.example.hello.domain.chat.dto.ChatDTO
import com.example.hello.domain.chat.entity.Chat
import com.example.hello.domain.chat.entity.Room
import com.example.hello.domain.chat.service.RoomService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToFlux
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.net.URI

@Component
class RoomHandler(
    private val roomService: RoomService
) {
    private val LOCAL_URL = "http://localhost:8080"

    fun createRooms(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(ChatDTO.Room::class.java) //                .doOnNext(post -> validator.validate(post))
            .flatMap { room: ChatDTO.Room -> roomService.createChatRoom(Room.of(room)) }
            .flatMap { room: Room ->
                ServerResponse
                    .created(URI.create("/rooms/" + room._id))
                    .build()
            }
    }

//    fun getRooms(request: ServerRequest): <Mono><ServerResponse> {
//        return request.bodyToFlux(ChatDTO.Room::class.java)
//            .flatMap { room:ChatDTO.Room -> roomService.findListByUserId(1) }

//            .flatMap { room:List<Book> -> ServerResponse.ok().bodyValue(room)}
//        books: List<Book?>? ->
//                ServerResponse
//                    .ok()
//                    .bodyValue(mapper.booksToResponse(books))
//    }

}