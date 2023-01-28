package com.lsdconsulting.stub.integration.controller.put

import com.lsdconsulting.stub.integration.model.GreetingRequest
import com.lsdconsulting.stub.integration.model.GreetingResponse
import io.lsdconsulting.stub.annotation.GenerateWireMockStub
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.web.bind.annotation.*

@GenerateWireMockStub
@RestController("/putController")
class PutRestController {

    @PutMapping("/resourceWithNoBody")
    fun resourceWithNoBody() = GreetingResponse(name = randomAlphabetic(10))

    @PutMapping("/resourceWithRequestBody")
    fun resourceWithRequestBody(@Suppress("UNUSED_PARAMETER") @RequestBody greetingRequest: GreetingRequest) {
    }

    @ResponseStatus(code = NO_CONTENT)
    @PutMapping("/resourceWithRequestBodyAndPathVariable/{param}")
    fun resourceWithRequestBodyAndPathVariable(
        @Suppress("UNUSED_PARAMETER") @RequestBody greetingRequest: GreetingRequest,
        @Suppress("UNUSED_PARAMETER") @PathVariable param: String
    ) {
    }
}
