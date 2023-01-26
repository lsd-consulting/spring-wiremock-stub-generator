package com.lsdconsulting.stub.integration.controller

import com.lsdconsulting.stub.integration.model.Greeting
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController("/getController")
class GetRestController {

    @GetMapping("/resourceWithNoParams")
    fun resourceWithNoParams_(): Greeting {
        return Greeting(name = "name")
    }

    @GetMapping("/resourceWithParam")
    fun resourceWithParam_(@RequestParam param: String): Greeting {
        return Greeting(name = "name")
    }

    @GetMapping("/resourceWithParam/{param}")
    fun resourceWithPathVariable(@PathVariable param: String): Greeting {
        return Greeting(name = "name")
    }
}