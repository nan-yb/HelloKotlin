package com.example.hello.domain.chat.repository

import com.example.hello.domain.chat.entity.Room
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.data.mongodb.repository.Tailable
import reactor.core.publisher.Flux
import java.time.LocalDateTime

interface RoomRepository : ReactiveMongoRepository<Room, ObjectId> {

    @Tailable //커서를 안 닫고 계속 유지한다.
    @Query("{postId:?0}")
    fun findAllByPostId(postId : Number): Flux<Room>
}