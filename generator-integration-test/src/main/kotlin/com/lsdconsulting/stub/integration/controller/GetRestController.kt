package com.lsdconsulting.stub.integration.controller

import com.lsdconsulting.stub.integration.model.Greeting
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/getController")
class GetRestController {

    @GetMapping("/resourceWithNoParams")
    fun resourceWithNoParams(): Greeting {
        return Greeting.builder().name("name").build()
    }
}