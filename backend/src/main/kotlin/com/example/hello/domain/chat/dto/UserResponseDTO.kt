package com.example.hello.domain.chat.dto

import com.example.hello.domain.chat.entity.User
import jakarta.validation.constraints.NotBlank
import org.bson.types.ObjectId
import java.time.LocalDateTime

data class UserResponseDTO (
    val _id : String ,
    val userId : String,
    val userName : String,
    val createdAt : LocalDateTime
){
    companion object {
        fun from(user : User) : UserResponseDTO {
            return UserResponseDTO(user._id.toString() , user.userId , user.userName , user.createdAt)
        }
    }
}