package com.example.hello.domain.chat.repository

import com.example.hello.domain.chat.entity.Chat
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.data.mongodb.repository.Tailable
import reactor.core.publisher.Flux


interface ChatRepository : ReactiveMongoRepository<Chat , ObjectId>{

    @Tailable //커서를 안 닫고 계속 유지한다.
    @Query("{roomId:?0}")
    fun findChatsByRoomId(roomId: ObjectId) : Flux<Chat>
}