package com.example.hello.domain.chat.entity

import com.example.hello.domain.chat.dto.ChatDTO
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "room")
data class Room(
    @Id
    var _id: ObjectId?,
    val title: String?,
    val postId: Number,
    val isSold: Boolean,
    val users: List<User>?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)

{
    companion object {
        fun createRoom(title: String): Room {
            val usr1 = User("test1" , "test1");
            val usr2 = User("test2" , "test2");
            val list : List<User> = listOf(usr1 , usr2);
            val room = Room(null, title, 0, false, list, LocalDateTime.now(), LocalDateTime.now());
            return room
        }
    }

}