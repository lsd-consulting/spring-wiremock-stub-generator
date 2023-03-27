package com.lsdconsulting.stub.integration.controller.put

import com.lsdconsulting.stub.integration.model.GreetingRequest
import com.lsdconsulting.stub.integration.model.GreetingResponse
import io.lsdconsulting.stub.annotation.GenerateWireMockStub
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.web.bind.annotation.*

@GenerateWireMockStub
@RestController
@RequestMapping("/putController")
@ResponseStatus(code = NO_CONTENT)
class PutRestController {

    @Suppress("UNUSED_PARAMETER")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    @PutMapping("/resourceWithNoBody")
    fun resourceWithNoBody() = GreetingResponse(name = randomAlphabetic(10))

    @Suppress("UNUSED_PARAMETER")
    @PutMapping("/resourceWithRequestBody")
    fun resourceWithRequestBody(@RequestBody greetingRequest: GreetingRequest) {
    }

    @Suppress("UNUSED_PARAMETER")
    @ResponseStatus(code = NO_CONTENT)
    @PutMapping("/resourceWithRequestBodyAndPathVariable/{param}")
    fun resourceWithRequestBodyAndPathVariable(
        @RequestBody greetingRequest: GreetingRequest,
        @PathVariable param: String,
    ) {
    }
}
