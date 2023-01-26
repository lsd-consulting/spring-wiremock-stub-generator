package com.lsdconsulting.stub.integration.controller

import com.lsdconsulting.stub.integration.model.Greeting
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController("/getController")
class GetRestController {

    @GetMapping("/resourceWithNoParams")
    fun resourceWithNoParams(): Greeting {
        return Greeting(name = "name")
    }

    @GetMapping("/resourceWithParam")
    fun resourceWithParam(@RequestParam param: String): Greeting {
        return Greeting(name = "name")
    }

    @GetMapping("/resourceWithMultipleParams")
    fun resourceWithMultipleParams(@RequestParam param1: String, @RequestParam param2: String): Greeting {
        return Greeting(name = "name")
    }

    @GetMapping("/resourceWithParam/{param}")
    fun resourceWithPathVariable(@PathVariable param: String): Greeting {
        return Greeting(name = "name")
    }

    @GetMapping("/resourceWithParam/{param1}/{param2}")
    fun resourceWithMultiplePathVariables(@PathVariable param1: String, @PathVariable param2: String): Greeting {
        return Greeting(name = "name")
    }

    @GetMapping("/resourceWithParam/{param1}")
    fun resourceWithPathVariableAndQueryParam(@PathVariable param1: String, @RequestParam param2: String): Greeting {
        return Greeting(name = "name")
    }
}