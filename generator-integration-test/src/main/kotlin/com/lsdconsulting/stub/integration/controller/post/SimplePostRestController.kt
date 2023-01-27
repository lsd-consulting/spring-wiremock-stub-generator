package com.lsdconsulting.stub.integration.controller.post

import com.lsdconsulting.stub.integration.model.GreetingResponse
import io.lsdconsulting.stub.annotation.GenerateWireMockStub
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@GenerateWireMockStub
@RestController("/postController")
class SimplePostRestController {

    @PostMapping("/resourceWithNoBody")
    fun resourceWithNoBody() = GreetingResponse(name = randomAlphabetic(10))
}
