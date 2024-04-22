package com.example.hello.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class HelloController {

    @GetMapping("/")
    @ResponseBody
    fun hello(model : Model) : Any{
        model["title"] = "Blog"

        var modelTitle = model.getAttribute("title") as Any;
        var title : String = ""

        if(modelTitle is String){
            title = modelTitle;
        }

        return title;
    }
}