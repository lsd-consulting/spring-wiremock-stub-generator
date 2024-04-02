package com.lsdconsulting.stub.integration.controller.post

import com.lsdconsulting.stub.integration.model.GreetingRequest
import com.lsdconsulting.stub.integration.model.GreetingResponse
import io.lsdconsulting.stub.annotation.GenerateWireMockStub
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@GenerateWireMockStub
@RestController
@RequestMapping("/postController")
class PostRestControllerWithHeader {

    @Suppress("UNUSED_PARAMETER")
    @PostMapping("/resourceWithNoBodyNoResponseWithMappedHeader")
    fun resourceWithNoBodyNoResponseWithMappedHeader(@RequestHeader(AUTHORIZATION) token: String) {
    }

    @Suppress("UNUSED_PARAMETER")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/resourceWithNoBodyButWithResponseStatusWithHeader")
    fun resourceWithNoBodyButWithResponseStatusWithHeader(
        @RequestHeader custom_header: String,
    ) = GreetingResponse(name = randomAlphabetic(10))

    @Suppress("UNUSED_PARAMETER")
    @PostMapping("/resourceWithBodyAndMultiplePathVariablesWithMultipleHeaders/{param1}/{param2}")
    fun resourceWithBodyAndMultiplePathVariablesWithMultipleHeaders(
        @RequestBody greetingRequest: GreetingRequest,
        @PathVariable param1: String,
        @PathVariable param2: String,
        @RequestHeader(CONTENT_TYPE) token: String,
        @RequestHeader custom_header: String,
    ) = GreetingResponse(name = randomAlphabetic(10))
}
