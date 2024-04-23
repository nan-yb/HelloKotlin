package com.example.hello.entity

import org.springframework.data.annotation.Id

class Reservation (

//    @javax.persistence.Id
    @Id
    var id: Long? = null,
    var userId: Long,
    var number: Int
){
    constructor(userId : Long , number : Int) : this(null , userId, number);
}
