package com.lsdconsulting.stub.integration.controller

import com.lsdconsulting.stub.integration.model.Greeting
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController("/getController")
class GetRestController {

    @GetMapping("/resourceWithNoParams")
    fun resourceWithNoParams() = Greeting(name = randomAlphabetic(10))

    @GetMapping("/resourceWithParam")
    fun resourceWithParam(@Suppress("UNUSED_PARAMETER") @RequestParam param: String) =
        Greeting(name = randomAlphabetic(10))

    @Suppress("UNUSED_PARAMETER")
    @GetMapping("/resourceWithMultipleParams")
    fun resourceWithMultipleParams(@RequestParam param1: String, @RequestParam param2: String) =
        Greeting(name = randomAlphabetic(10))

    @GetMapping("/resourceWithParam/{param}")
    fun resourceWithPathVariable(@Suppress("UNUSED_PARAMETER") @PathVariable param: String) =
        Greeting(name = randomAlphabetic(10))

    @Suppress("UNUSED_PARAMETER")
    @GetMapping("/resourceWithParam/{param1}/{param2}")
    fun resourceWithMultiplePathVariables(@PathVariable param1: String, @PathVariable param2: String) =
        Greeting(name = randomAlphabetic(10))

    @Suppress("UNUSED_PARAMETER")
    @GetMapping("/resourceWithParam/{param1}")
    fun resourceWithPathVariableAndRequestParam(
        @Suppress("UNUSED_PARAMETER") @PathVariable param1: String,
        @RequestParam param2: String
    ) =
        Greeting(name = randomAlphabetic(10))
}