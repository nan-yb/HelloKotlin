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
    var senderId : ObjectId? = null,
    var senderName : String? = null,
    var roomId: ObjectId? = null,
    var msg: String? = null,
    var isRead :Boolean = false,
    var createdAt: LocalDateTime? = null
) {
    constructor(roomId : ObjectId , msg : String , user: ObjectId , senderName : String) : this(
        null,  user , senderName , roomId , msg , false , LocalDateTime.now());

    companion object {
        fun createChat(msg: String , roomId : String , senderId: String , senderName : String): Chat {
            val chat = Chat(ObjectId(roomId) , msg , ObjectId(senderId) , senderName);
            return chat;
        }
    }


}