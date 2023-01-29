package com.lsdconsulting.stub.integration.controller.post

import com.lsdconsulting.stub.integration.model.GreetingRequest
import com.lsdconsulting.stub.integration.model.GreetingResponse
import io.lsdconsulting.stub.annotation.GenerateWireMockStub
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@GenerateWireMockStub
@RestController("/postController")
class AdvancedPostRestController {

    @Suppress("UNUSED_PARAMETER")
    @PostMapping("/resourceWithBody")
    fun resourceWithBody(@RequestBody greetingRequest: GreetingRequest) =
        GreetingResponse(name = randomAlphabetic(10))

    @Suppress("UNUSED_PARAMETER")
    @PostMapping("/resourceWithBody/{param}")
    fun resourceWithBodyAndPathVariable(
        @RequestBody greetingRequest: GreetingRequest,
        @PathVariable param: String
    ) = GreetingResponse(name = randomAlphabetic(10))

    @Suppress("UNUSED_PARAMETER")
    @PostMapping("/resourceWithBodyAndMultiplePathVariables/{param1}/{param2}")
    fun resourceWithBodyAndMultiplePathVariables(
        @RequestBody greetingRequest: GreetingRequest,
        @PathVariable param1: String,
        @PathVariable param2: String
    ) = GreetingResponse(name = randomAlphabetic(10))
}
