package com.example.hello.controller

import com.example.hello.entity.Reservation
import com.example.hello.service.ReservationService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import reactor.core.publisher.Mono

@Controller
class HelloController @Autowired constructor(
    private val reservationService : ReservationService
){

    val log = LoggerFactory.getLogger(HelloController::class.java)

    @GetMapping("/")
    @ResponseBody
    fun hello(model : Model) : Any{
        model["title"] = "Blog"

        var modelTitle = model.getAttribute("title") as Any;
        var title : String = ""

        if(modelTitle is String){
            title = modelTitle;
        }

        reservationService.getReservationList().map { i -> log.debug("1 {}" , i.toString()) };


        return title;
    }
}