package com.example.hello.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HelloController {

    @GetMapping("/")
    fun hello(model : Model){
        model["title"] = "Blog"
        return "blog";
    }
}