package com.lsdconsulting.stub.integration.controller

import com.lsdconsulting.stub.integration.model.GreetingRequest
import com.lsdconsulting.stub.integration.model.GreetingResponse
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("/postController")
class PostRestController {

    @PostMapping("/resourceWithNoBody")
    fun resourceWithNoBody() = GreetingResponse(name = randomAlphabetic(10))

    @PostMapping("/resourceWithBody")
    fun resourceWithBody(@Suppress("UNUSED_PARAMETER") @RequestBody greetingRequest: GreetingRequest) = GreetingResponse(name = randomAlphabetic(10))
}