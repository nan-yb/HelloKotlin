//package com.example.hello.domain.chat.controller
//
//
//import com.example.hello.domain.chat.dto.RoomDTO
//import com.example.hello.domain.chat.entity.Room
//import com.example.hello.domain.chat.service.RoomService
//import com.mongodb.client.result.UpdateResult
//import lombok.RequiredArgsConstructor
//import lombok.extern.slf4j.Slf4j
//import org.bson.types.ObjectId
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.web.bind.annotation.*
//import org.springframework.web.reactive.function.server.EntityResponse.fromObject
//import org.springframework.web.reactive.function.server.ServerResponse.badRequest
//import reactor.core.publisher.Flux
//import reactor.core.publisher.Mono
//import reactor.core.scheduler.Schedulers
//import java.lang.Exception
//import java.time.LocalDateTime
//
//
//@RestController
//@RequestMapping("/chat-service/room")
//class RoomController @Autowired constructor(
//    val roomService: RoomService
//){
//    //GET Method
//    /**채팅방 목록 불러오기 */
//    @CrossOrigin
//    @GetMapping(value = ["/list/user/{userId}"])
//    fun findListByUserId(@PathVariable userId: Int?): Flux<Room> {
//        //데이터를 계속 보내줘야 되면 "chat"처럼 스레드 사용 고려
//        return roomService.findListByUserId(userId).subscribeOn(Schedulers.boundedElastic())
//    }
//
//    //POST Method
//    /**채팅방 생성 */
//    @CrossOrigin
//    @PostMapping(value = ["/new"])
//    fun createRoom(@RequestBody room: Mono<Room>): Mono<Room> {
////        room.createdAt = LocalDateTime.now()
////        room.updatedAt = LocalDateTime.now()
//
//
//        return roomService.createChatRoom(room).onErrorResume { error("123") }
//
//    }
//
//    //POST Method
//    /**채팅방 생성 */
//    @PostMapping(value = ["/new2"])
//    fun createRoom2(@RequestBody room:RoomDTO): String {
//        return "123"
////        return Mono.just("Hello");
//    }
//
//    //PUT Method
//    /**채팅방 나가기 */ //ObjectId는 Object("")를 빼고 쌍따옴표 안의 값만 전송
//    @CrossOrigin
//    @PutMapping(value = ["/exit/room/{roomId}/user/{userId}"])
//    fun completedTransaction(@PathVariable roomId: ObjectId?, @PathVariable userId: Int?): Mono<UpdateResult> {
//        return roomService.updateIsAttendance(roomId, userId)
//    }
//
//    /**채팅방 해당 장터글 거래완료 */
//    @CrossOrigin
//    @PutMapping(value = ["/sold-out/post/{postId}"])
//    fun completedTransaction(@PathVariable postId: Int?): Mono<UpdateResult> {
//        return roomService.updateIsSold(postId)
//    }
//
//    /**채팅방 제목 업데이트(장터글 제목 변경 시) */
//    @CrossOrigin
//    @PutMapping(value = ["/update-title/post/{postId}/title/{title}"])
//    fun updateTitle(@PathVariable postId: Int?, @PathVariable title: String?): Mono<UpdateResult> {
//        return roomService.updateTitle(postId, title)
//    }
//}