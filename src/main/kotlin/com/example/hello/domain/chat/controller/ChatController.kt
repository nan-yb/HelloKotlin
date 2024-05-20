//package com.example.hello.domain.chat.controller
//
//import com.example.hello.domain.chat.entity.Chat
//import com.example.hello.domain.chat.service.ChatService
//import com.mongodb.client.result.UpdateResult
//import lombok.RequiredArgsConstructor
//import lombok.extern.slf4j.Slf4j
//import org.bson.types.ObjectId
//import org.springframework.http.MediaType
//import org.springframework.web.bind.annotation.*
//import reactor.core.publisher.Flux
//import reactor.core.publisher.Mono
//import reactor.core.scheduler.Schedulers
//import java.time.LocalDateTime
//
//
//@RequiredArgsConstructor
//@RestController
//@Slf4j
//@RequestMapping("/chat-service/chat")
//class ChatController (
//    val chatService: ChatService
//){
//
//    //GET Method
//    /**채팅목록 불러오기 */
//    @CrossOrigin
//    @GetMapping(value = ["/list/room/{roomId}"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
//    fun findByRoomId(@PathVariable roomId: ObjectId?): Flux<Chat> {
//        return chatService.findChatsByRoomId(roomId).subscribeOn(Schedulers.boundedElastic())
//    }
//
//    //POST Method
//    /**메세지 전송 */
//    @CrossOrigin
//    @PostMapping("/")
//    fun sendMsg(@RequestBody chat: Chat): Mono<Chat> {
//        chat.createdAt = LocalDateTime.now();
//
//        return chatService.createChat(chat)
//    }
//
//    //PUT Method
//    /**메세지 읽음 요청 */
//    @CrossOrigin
//    @PutMapping(value = ["/read/room/{roomId}/user/{userId}"])
//    fun updateIsRead(@PathVariable roomId: ObjectId?, @PathVariable userId: Int?): Mono<UpdateResult> {
//        return chatService.updateIsRead(roomId, userId)
//    }
//
//    /**채팅 마지막 채팅 날짜 업데이트 요청 */
//    @CrossOrigin
//    @PutMapping(value = ["/update/room/{roomId}"])
//    fun updateUpdatedAt(@PathVariable roomId: ObjectId?): Mono<UpdateResult> {
//        return chatService.updateTime(roomId)
//    }
//}