package com.example.hello.domain.chat.dto

import com.example.hello.domain.chat.entity.User
import org.bson.types.ObjectId
import java.time.LocalDateTime

data class ChatResponseDTO (
    val id: String? ,
    var senderId : Number ,
    var roomId: ObjectId? ,
    var msg: String? ,
    var isRead :Boolean ,
    var createdAt: LocalDateTime
)