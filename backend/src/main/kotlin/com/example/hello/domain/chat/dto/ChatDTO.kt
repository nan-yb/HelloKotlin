package com.example.hello.domain.chat.dto

import com.example.hello.domain.chat.entity.User
import jakarta.validation.constraints.NotBlank
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Getter
import lombok.Setter
import org.bson.types.ObjectId
import java.time.LocalDateTime

class ChatDTO {

    @Getter
    class Chat {
//        val _id: @NotBlank ObjectId? = null
//        var senderId: @NotBlank Number = 0
        val roomId: @NotBlank String? = null
        val msg: @NotBlank String? = null
        val isRead: @NotBlank Boolean = false
        val createdAt: @NotBlank LocalDateTime? = null
    }

    @Getter
    @Builder
    class Room {
        @Setter
        var _id: @NotBlank ObjectId? = null
        val title: @NotBlank String? = null
        val postId: @NotBlank Number = 0
        val isSold: @NotBlank Boolean = false
        val users: @NotBlank List<User>? = null
        val createdAt: @NotBlank LocalDateTime? = null
        val updatedAt: @NotBlank LocalDateTime? = null
    }

    @AllArgsConstructor
    @Getter
    @Builder
    class Response {
        val _id: @NotBlank ObjectId? = null
        var senderId: @NotBlank Number = 0
        val roomId: @NotBlank ObjectId? = null
        val msg: @NotBlank String? = null
        val isRead: @NotBlank Boolean = false
        val createdAt: @NotBlank LocalDateTime? = null
    }

    class User {
        val userId : @NotBlank String = ""
        val userName : @NotBlank  String = ""

    }
}
