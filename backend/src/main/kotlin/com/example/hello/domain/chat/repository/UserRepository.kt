package com.example.hello.domain.chat.repository

import com.example.hello.domain.chat.dto.UserResponseDTO
import com.example.hello.domain.chat.entity.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface UserRepository : ReactiveMongoRepository<User , ObjectId> {

    @Query("{userId:?0}")
    fun findByUserId(userId : String) : Mono<UserResponseDTO>
}