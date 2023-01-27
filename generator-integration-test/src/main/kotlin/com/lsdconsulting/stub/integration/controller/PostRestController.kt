package com.lsdconsulting.stub.integration.controller

import com.lsdconsulting.stub.integration.model.GreetingResponse
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/postController")
class PostRestController {

    @PostMapping("/resourceWithNoParams")
    fun resourceWithNoParams() = GreetingResponse(name = randomAlphabetic(10))
}