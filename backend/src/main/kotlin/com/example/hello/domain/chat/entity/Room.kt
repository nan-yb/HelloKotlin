package com.example.hello.domain.chat.entity

import com.example.hello.domain.chat.dto.ChatDTO
import com.example.hello.domain.chat.dto.RoomResponseDTO
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
        fun createRoom(title: String , users :List<User>): Room {
            val room = Room(null, title, 0, false, users, LocalDateTime.now(), LocalDateTime.now());
            return room
        }

        fun fromEntity(room : ChatDTO.Room ) : Room{
            val users : List<User> = room.users!!.stream().map {
                user -> User.fromEntity(user);
            }.toList();

            return Room(null , room.title , 0 , false , users , LocalDateTime.now() , LocalDateTime.now())
        }

    }

}