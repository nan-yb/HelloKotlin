package com.example.hello.domain.chat.contoller

import com.example.hello.domain.chat.dto.ChatDTO
import com.example.hello.domain.chat.service.ChatService
import lombok.RequiredArgsConstructor
import org.bson.types.ObjectId
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers


@RestController
@RequiredArgsConstructor
class ChatController (
    val chatService : ChatService
){
    /**채팅목록 불러오기 */

    @CrossOrigin
    @GetMapping(value = ["/chats/{roomId}"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun findByRoomId(@PathVariable roomId: ObjectId?): Flux<ChatDTO.Chat> {
        return chatService.findChatsByRoomId(roomId).subscribeOn(Schedulers.boundedElastic())
    }
}