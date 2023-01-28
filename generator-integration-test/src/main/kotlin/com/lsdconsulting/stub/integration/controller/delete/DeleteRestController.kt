package com.lsdconsulting.stub.integration.controller.delete

import com.lsdconsulting.stub.integration.model.GreetingRequest
import com.lsdconsulting.stub.integration.model.GreetingResponse
import io.lsdconsulting.stub.annotation.GenerateWireMockStub
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@GenerateWireMockStub
@RestController("/deleteController")
class DeleteRestController {

    @DeleteMapping("/resourceWithNoBody")
    fun resourceWithNoBody() = GreetingResponse(name = randomAlphabetic(10))

    @DeleteMapping("/resourceWithRequestBody")
    fun resourceWithRequestBody(@Suppress("UNUSED_PARAMETER") @RequestBody greetingRequest: GreetingRequest) {
    }

    @DeleteMapping("/resourceWithRequestBodyAndPathVariable/{param}")
    fun resourceWithRequestBodyAndPathVariable(
        @Suppress("UNUSED_PARAMETER") @RequestBody greetingRequest: GreetingRequest,
        @Suppress("UNUSED_PARAMETER") @PathVariable param: String
    ) {
    }
}
