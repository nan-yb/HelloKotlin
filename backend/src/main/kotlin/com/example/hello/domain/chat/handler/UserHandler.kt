package com.example.hello.domain.book.v10

import com.example.hello.domain.chat.dto.ChatDTO
import com.example.hello.domain.chat.dto.UserResponseDTO
import com.example.hello.domain.chat.entity.Chat
import com.example.hello.domain.chat.entity.Room
import com.example.hello.domain.chat.entity.User
import com.example.hello.domain.chat.service.ChatService
import com.example.hello.domain.chat.service.UserService
import io.netty.channel.unix.Errors
import lombok.extern.slf4j.Slf4j
import net.sf.jsqlparser.schema.Server
import org.bson.types.ObjectId
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import org.springframework.web.server.ServerWebInputException
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

    fun joinUser(request : ServerRequest) : Mono<ServerResponse> {
        return request.bodyToMono(ChatDTO.User::class.java)
            .flatMap { user->
                checkIfUserExists(user.userId)
                    .flatMap { existsUser ->
                        if (existsUser) {
                            ServerResponse.ok().body(userService.findByUserId(user.userId))
                        } else {
                            ServerResponse.ok().body(
                                userService.createUser(User.fromEntity(user))
                                    .map { i -> UserResponseDTO.from(i) }
                            )
                        }
                    }
            }
    }

    fun checkIfUserExists(userId: String): Mono<Boolean> {
        // 예시로 userRepository를 사용한 id 중복 체크 로직
        return userService.findByUserId(userId).hasElement() // 결과가 존재하는지 여부를 반환
    }

}