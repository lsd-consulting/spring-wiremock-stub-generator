package com.lsdconsulting.stub.integration.controller.post

import com.lsdconsulting.stub.integration.model.GreetingResponse
import io.lsdconsulting.stub.annotation.GenerateWireMockStub
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@GenerateWireMockStub
@RestController
class SimplePostRestController {

    @PostMapping("/postController/resourceWithNoBody")
    fun resourceWithNoBody() = GreetingResponse(name = randomAlphabetic(10))

    @ResponseStatus(CREATED)
    @PostMapping("/postController/resourceWithNoBodyButWithResponseStatus")
    fun resourceWithNoBodyButWithResponseStatus() = GreetingResponse(name = randomAlphabetic(10))
}
