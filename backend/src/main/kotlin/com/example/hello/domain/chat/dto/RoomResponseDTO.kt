package com.example.hello.domain.chat.dto

import com.example.hello.domain.chat.entity.User
import java.time.LocalDateTime

data class RoomResponseDTO (
    val _id : String?,
    val title: String?,
    val postId: Number,
    val isSold: Boolean,
    val users: List<User>?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)