package com.example.hello.domain.chat.service

import com.example.hello.domain.chat.dto.ChatDTO
import com.example.hello.domain.chat.dto.UserResponseDTO
import com.example.hello.domain.chat.entity.User
import com.example.hello.domain.chat.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.findById
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service("chatUserService")
class UserService @Autowired constructor(
    private val mongoReactiveTemplate: ReactiveMongoTemplate ,
    private val userRepository: UserRepository
){
    fun createUser(user: User) : Mono<User>{
        return mongoReactiveTemplate.save(user , "user" );
    }

    fun findByUserId(userId : String) : Mono<UserResponseDTO> {
        return userRepository.findByUserId(userId)
    }
}