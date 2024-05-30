package com.example.hello.domain.chat.entity

import com.example.hello.domain.chat.dto.ChatDTO
import org.bson.types.ObjectId
import java.time.LocalDateTime

data class User (
    val _id : ObjectId?,
    val userId : String,
    val userName : String ,
    val createdAt : LocalDateTime
//    var isAttendance : Boolean,
){

    companion object {
        fun fromEntity(userDto : ChatDTO.User): User{
            return User(userDto._id , userDto.userId , userDto.userName , userDto.createdAt);
        }
    }
}