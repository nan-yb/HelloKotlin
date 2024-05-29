package com.example.hello.domain.chat.entity

import com.example.hello.domain.chat.dto.ChatDTO

data class User (
    val userId : String,
    val userName : String ,
//    var isAttendance : Boolean,
){

    companion object {
        fun fromEntity(userDto : ChatDTO.User): User{
            return User(userDto.userId , userDto.userName);
        }
    }
}