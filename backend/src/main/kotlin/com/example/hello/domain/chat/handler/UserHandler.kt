package com.example.hello.domain.book.v10

import com.example.hello.domain.chat.dto.ChatDTO
import com.example.hello.domain.chat.entity.Chat
import com.example.hello.domain.chat.entity.User
import com.example.hello.domain.chat.service.ChatService
import com.example.hello.domain.chat.service.UserService
import lombok.extern.slf4j.Slf4j
import org.bson.types.ObjectId
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import org.springframework.web.util.UriBuilder
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.util.function.Tuple2
import reactor.util.function.Tuples
import java.net.URI
import java.time.Duration

@Slf4j
@Component
class UserHandler(
//    private val mapper: BookMapper,
    private val userService: UserService
) {
    private val LOCAL_URL = "http://localhost:8080"


    fun getUser(request: ServerRequest) : Mono<ServerResponse> {
        val userId = request.pathVariable("user-id").toString()

        return ServerResponse.ok().build()
    }


    fun createUser(request: ServerRequest) : Mono<ServerResponse> {

        // validate 번호 검증

        return request.bodyToMono(ChatDTO.User::class.java) //
            .flatMap {
                user -> ServerResponse.ok().body(userService.createUser(User.fromEntity(user)));
            }
    }
}