package com.example.hello.domain.chat.service

import com.example.hello.domain.chat.entity.Room
import com.mongodb.client.result.UpdateResult
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


@Service
class RoomService @Autowired constructor(
    val mongoTemplate: ReactiveMongoTemplate
){

    /**채팅방 목록 마지막 대화 순서대로 불러오기 */
    fun findListByUserId(userId: Int?): Flux<Room> {
        return mongoTemplate.find(
            Query.query(
                Criteria.where("users.userId").`is`(userId)
                    .and("users.isAttendance").`is`(true)
            )
                .with(Sort.by(Sort.Direction.DESC, "updatedAt")), Room::class.java
        )
    }

    /**채팅방 생성 */
    fun createChatRoom(room: Room): Mono<Room> {
        return mongoTemplate.save(room, "room")
    }

    /**채팅방 나가기 */
    fun updateIsAttendance(roomId: ObjectId?, userId: Int?): Mono<UpdateResult> {
        val query: Query = Query(
            Criteria.where("_id").`is`(roomId)
                .and("users.userId").`in`(userId)
        )

        val update: Update = Update()
        update.set("users.$.isAttendance", false)

        return mongoTemplate.updateFirst(query, update, Room::class.java)
    }

    /**게시글 판매완료 */
    fun updateIsSold(postId: Int?): Mono<UpdateResult> {
        val query: Query = Query(Criteria.where("postId").`is`(postId))
        val update: Update = Update.update("isSold", true)

        return mongoTemplate.updateMulti(query, update, Room::class.java)
    }

    /**게시글 제목 변경 */
    fun updateTitle(postId: Int?, title: String?): Mono<UpdateResult> {
        val query: Query = Query(Criteria.where("postId").`is`(postId))
        val update: Update = Update.update("title", title)

        return mongoTemplate.updateMulti(query, update, Room::class.java)
    }
}