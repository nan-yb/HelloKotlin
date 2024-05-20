package com.example.hello.domain.chat.entity

import com.example.hello.domain.chat.dto.ChatDTO
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
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
        fun of(room: ChatDTO.Room): Room {
            return Room(null , room.title ,  room.postId , room.isSold , room.users , room.createdAt , room.updatedAt);
        }
        fun createRoom(title: String): Room {
            val usr1 = User(1 , true);
            val usr2 = User(2 , true);
            val list : List<User> = listOf(usr1 , usr2);
            val room = Room(null, title, 0, false, list, LocalDateTime.now(), LocalDateTime.now());
            return room
        }
    }

}