package com.example.hello.domain.chat.entity

import com.example.hello.domain.chat.dto.ChatDTO
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "chat")
data class Chat constructor(
    @Id
    val id: ObjectId? = null,
    var senderId : Number = 0,
    var roomId: ObjectId? = null,
    var msg: String? = null,
    var isRead :Boolean = false,
    var createdAt: LocalDateTime? = null
) {
    constructor(roomId : ObjectId , msg : String) : this(
        null, 0 , roomId , msg , false , LocalDateTime.now());

    companion object {
        fun from(chat: ChatDTO.Chat): Chat {
            return Chat(null , chat.senderId ,  chat.roomId , chat.msg , chat.isRead , chat.createdAt)
        }

        fun of(chat: ChatDTO.Chat): Chat {
            return Chat(null , chat.senderId ,  chat.roomId , chat.msg , chat.isRead , chat.createdAt)
        }
    }
}