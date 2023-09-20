package com.example.edu

import chap02.section1.Person as User

fun main(){
    val user1 = User("Kildong" , 30) // chap02.section1.Person as User
    val user2 = Person("A123" , "Kildong") // 이 파일 내의 Person


    println(user1.name)
    println(user1.age)
}

class Person(val id: String , val name: String)