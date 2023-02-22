package com.lsdconsulting.stub.integration.controller.delete

import com.lsdconsulting.stub.integration.model.GreetingRequest
import com.lsdconsulting.stub.integration.model.GreetingResponse
import io.lsdconsulting.stub.annotation.GenerateWireMockStub
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.web.bind.annotation.*

@GenerateWireMockStub
@RestController
@RequestMapping("/deleteController")
class DeleteRestController {

    @Suppress("UNUSED_PARAMETER")
    @DeleteMapping("/resourceWithNoBody")
    fun resourceWithNoBody() = GreetingResponse(name = randomAlphabetic(10))

    @Suppress("UNUSED_PARAMETER")
    @DeleteMapping("/resourceWithRequestBody")
    fun resourceWithRequestBody(@RequestBody greetingRequest: GreetingRequest) {
    }

    @Suppress("UNUSED_PARAMETER")
    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("/resourceWithRequestBodyAndPathVariable/{param}")
    fun resourceWithRequestBodyAndPathVariable(
        @RequestBody greetingRequest: GreetingRequest,
        @PathVariable param: String
    ) {
    }
}
