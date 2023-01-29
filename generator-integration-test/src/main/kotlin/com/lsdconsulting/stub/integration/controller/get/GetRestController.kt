package com.lsdconsulting.stub.integration.controller.get

import com.lsdconsulting.stub.integration.model.GreetingResponse
import io.lsdconsulting.stub.annotation.GenerateWireMockStub
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.springframework.web.bind.annotation.*

@GenerateWireMockStub
@RestController
@RequestMapping("/getController")
class GetRestController {

    @GetMapping("/resourceWithNoParams")
    fun resourceWithNoParams() = GreetingResponse(name = randomAlphabetic(10))

    @Suppress("UNUSED_PARAMETER")
    @GetMapping("/resourceWithParam")
    fun resourceWithParam(@RequestParam param: String) =
        GreetingResponse(name = randomAlphabetic(10))

    @Suppress("UNUSED_PARAMETER")
    @GetMapping("/resourceWithMultipleParams")
    fun resourceWithMultipleParams(@RequestParam param1: String, @RequestParam param2: String) =
        GreetingResponse(name = randomAlphabetic(10))

    @Suppress("UNUSED_PARAMETER")
    @GetMapping("/resourceWithParam/{param}")
    fun resourceWithPathVariable(@PathVariable param: String) =
        GreetingResponse(name = randomAlphabetic(10))

    @Suppress("UNUSED_PARAMETER")
    @GetMapping("/resourceWithParam/{param1}/{param2}")
    fun resourceWithMultiplePathVariables(@PathVariable param1: String, @PathVariable param2: String) =
        GreetingResponse(name = randomAlphabetic(10))

    @Suppress("UNUSED_PARAMETER")
    @GetMapping("/resourceWithParam/{param1}")
    fun resourceWithPathVariableAndRequestParam(
        @PathVariable param1: String,
        @RequestParam param2: String
    ) =
        GreetingResponse(name = randomAlphabetic(10))

    @Suppress("UNUSED_PARAMETER")
    @GetMapping("/resourceWithParam/{param1}/{param2}")
    fun resourceWithMultiplePathVariablesAndRequestParams(
        @PathVariable param1: String,
        @PathVariable param2: String,
        @RequestParam param3: String,
        @RequestParam param4: String
    ) =
        GreetingResponse(name = randomAlphabetic(10))

    @GetMapping
    fun resourceWithNoSubResource() = GreetingResponse(name = randomAlphabetic(10))
}