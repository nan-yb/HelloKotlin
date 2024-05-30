package com.example.hello.domain.chat.dto

import com.example.hello.domain.chat.entity.Room
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
){

    companion object {
        fun of(room : Room) : RoomResponseDTO{
            return RoomResponseDTO(room._id.toString() , room.title , room.postId , room.isSold , room.users , room.createdAt , room.updatedAt)
        }
    }
}