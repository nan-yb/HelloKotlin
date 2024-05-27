package com.example.hello

import com.example.hello.domain.chat.entity.Room
import com.example.hello.domain.chat.service.RoomService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import reactor.core.publisher.Flux

@SpringBootTest
class ChatTest @Autowired constructor(
     val roomService : RoomService
){


    @Test
    fun `채팅방 생성`(){
        val createRoom = Room.createRoom("sd");
        roomService.createChatRoom(createRoom);
    }

    @Test
    fun `채팅방 리스트 가져오기`(){

    }
}

