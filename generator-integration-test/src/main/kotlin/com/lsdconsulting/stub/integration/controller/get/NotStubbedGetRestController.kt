package com.lsdconsulting.stub.integration.controller.get

import com.lsdconsulting.stub.integration.model.GreetingResponse
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController("/notStubbedGetController")
class NotStubbedGetRestController {

    @Suppress("UNUSED_PARAMETER")
    @GetMapping("/resourceWithParam/{param1}/{param2}")
    fun resourceWithMultiplePathVariablesAndRequestParams(
        @PathVariable param1: String,
        @PathVariable param2: String,
        @RequestParam param3: String,
        @RequestParam param4: String
    ) =
        GreetingResponse(name = randomAlphabetic(10))
}
