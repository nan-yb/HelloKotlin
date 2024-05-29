package com.example.hello.domain.chat.service

import com.example.hello.domain.chat.dto.ChatDTO
import com.example.hello.domain.chat.entity.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service("chatUserService")
class UserService @Autowired constructor(
    private val mongoTemplate: ReactiveMongoTemplate
){
    fun createUser(user: User) : Mono<User>{
        return mongoTemplate.save(user , "user" );
    }
}