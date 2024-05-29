package com.example.hello.domain.chat.handler

import com.example.hello.domain.chat.dto.ChatDTO
import com.example.hello.domain.chat.dto.RoomResponseDTO
import com.example.hello.domain.chat.entity.Room
import com.example.hello.domain.chat.service.RoomService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class RoomHandler(
    private val roomService: RoomService
) {
    private val LOCAL_URL = "http://localhost:8080"

    fun createRooms(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(ChatDTO.Room::class.java) //                .doOnNext(post -> validator.validate(post))
            .flatMap { room: ChatDTO.Room -> ServerResponse.ok().body(roomService.createChatRoom(Room.createRoom(room.title!!))) }
    }


    fun getAllRooms(request: ServerRequest): Mono<ServerResponse> {
        val rooms : Flux<RoomResponseDTO> = roomService.findAllList();
        return ServerResponse.ok().body(rooms);
    }

    fun getRoomsByUserId(request: ServerRequest): Mono<ServerResponse> {
        val userId = request.pathVariable("user-id").toString()
        val rooms : Flux<RoomResponseDTO> = roomService.findListByUserId(userId);
        return ServerResponse.ok().body(rooms);
    }

    fun deleteRooms(request : ServerRequest) : Mono<ServerResponse> {
//        val roomId = request.pathVariable("room-id").toInt()
//        val rooms : Mono<Void> = roomService.delet

        return ServerResponse.ok().build()
    }



}