package com.lsdconsulting.stub.integration.controller.get

import com.lsdconsulting.stub.integration.model.GreetingResponse
import org.apache.commons.lang3.RandomStringUtils.secure
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/notStubbedGetController")
class NotStubbedGetRestController {

    @Suppress("UNUSED_PARAMETER")
    @GetMapping("/resourceWithParam/{param1}/{param2}")
    fun resourceWithMultiplePathVariablesAndRequestParams(
        @PathVariable param1: String,
        @PathVariable param2: String,
        @RequestParam param3: String,
        @RequestParam param4: String,
    ) = GreetingResponse(name = secure().nextAlphabetic(10))
}
