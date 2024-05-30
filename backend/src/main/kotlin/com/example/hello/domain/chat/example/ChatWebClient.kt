package com.example.hello.domain.chat.example

import com.example.hello.domain.book.example.BookDto
import com.example.hello.domain.chat.dto.ChatDTO
import com.example.hello.domain.chat.entity.Chat
import com.example.hello.domain.chat.entity.Room
import org.bson.types.ObjectId
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.ResponseEntity
import org.springframework.web.reactive.function.client.WebClient
import java.util.function.Function

@Configuration
class ChatWebClient {

    val URI : String = "http://localhost:8080"
    val ROOM_ID : ObjectId = ObjectId("664d946dba9b8e3a4a4a0013");

    @Bean
    fun examplesWebClient(): ApplicationRunner {
        return ApplicationRunner { arguments: ApplicationArguments? ->
//            deleteRoom()
//            createRoom()
//            getRooms()
//            getChatsByRoom()
//            createChat()
        }
    }

    private fun deleteRoom() {
        val webClient = WebClient.create();
        val response =
            webClient
                .delete()
                .uri("${URI}/room/1")
                .retrieve()
                .toEntity(Void::class.java)

        response.subscribe{ res : ResponseEntity<Void> ->
            println("response status: ${res.statusCode}" )
        }
    }

    private fun createRoom() {
//        val requestBody = Room.createRoom("sample");
//
//        val webClient = WebClient.create()
//        val response =
//            webClient
//                .post()
//                .uri("${URI}/room")
//                .bodyValue(requestBody)
//                .retrieve()
//                .toEntity(Void::class.java)
//
//        response.subscribe { res: ResponseEntity<Void> ->
//            println("response status: ${res.statusCode}" )
//            println("Header Location: ${res.headers["Location"]}" )
//        }
    }

    private fun getRooms(){
        val webClient = WebClient.create()
        val response =
            webClient
                .get()
                .uri("${URI}/room/1")
                .retrieve()
                .bodyToFlux(ChatDTO.Room::class.java)


        response.map{ room -> room }
            .subscribe{room -> println(room._id) }
    }

    private fun createChat(){
//        val chat : Chat = Chat(ROOM_ID , "test")
//
//        val webClient = WebClient.create()
//        val response =
//            webClient
//                .post()
//                .uri("${URI}/chats")
//                .bodyValue(chat)
//                .retrieve()
//                .toEntity(Void::class.java)
//
//        response.subscribe{ res : ResponseEntity<Void> ->
//            println("Create Chat : " + res.statusCode)
//        }

    }

    private fun getChatsByRoom(){
        val webClient = WebClient.create()
        val response =
            webClient
                .get()
                .uri("${URI}/chats/${ROOM_ID}")
                .retrieve()
                .bodyToFlux(ChatDTO.Chat::class.java)

        response.map { chat -> chat }
            .subscribe { chat -> println(chat.msg) }
    }


}