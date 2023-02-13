package com.lsdconsulting.stub.integration.controller.get

import com.lsdconsulting.stub.integration.model.GreetingResponse
import io.lsdconsulting.stub.annotation.GenerateWireMockStub
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.format.annotation.DateTimeFormat.ISO.DATE
import org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME
import org.springframework.web.bind.annotation.*
import java.time.ZonedDateTime


@GenerateWireMockStub
@RestController
@RequestMapping("/getController")
class GetRestController {

    @GetMapping("/resourceWithNoParams")
    fun resourceWithNoParams() = GreetingResponse(name = randomAlphabetic(10))

    @Suppress("UNUSED_PARAMETER")
    @GetMapping("/resourceWithParam")
    fun resourceWithParam(@RequestParam param: String) =
        GreetingResponse(name = randomAlphabetic(10))

    @Suppress("UNUSED_PARAMETER")
    @GetMapping("/filteredResourceWithParam")
    fun filteredResourceWithParam(@RequestParam param: String) =
        listOf(GreetingResponse(name = randomAlphabetic(10)))

    @Suppress("UNUSED_PARAMETER")
    @GetMapping("/resourceWithMultipleParams")
    fun resourceWithMultipleParams(@RequestParam param1: String, @RequestParam param2: String) =
        GreetingResponse(name = randomAlphabetic(10))

    @Suppress("UNUSED_PARAMETER")
    @GetMapping("/resourceWithMappedRequestParams")
    fun resourceWithMappedRequestParams(@RequestParam("parameter1") param1: String, @RequestParam(name = "parameter2") param2: String) =
        GreetingResponse(name = randomAlphabetic(10))

    @Suppress("UNUSED_PARAMETER")
    @GetMapping("/resourceWithParam/{param}")
    fun resourceWithPathVariable(@PathVariable param: String) =
        GreetingResponse(name = randomAlphabetic(10))

    @Suppress("UNUSED_PARAMETER")
    @GetMapping("/resourceWithParam/{param1}/{param2}")
    fun resourceWithMultiplePathVariables(@PathVariable param1: String, @PathVariable param2: String) =
        GreetingResponse(name = randomAlphabetic(10))

    @Suppress("UNUSED_PARAMETER")
    @GetMapping("/resourceWithMappedPathVariables/{parameter1}/{parameter2}")
    fun resourceWithMappedPathVariables(
        @PathVariable("parameter1") param1: String, @PathVariable(name = "parameter2") param2: String
    ) = GreetingResponse(name = randomAlphabetic(10))

    @Suppress("UNUSED_PARAMETER")
    @GetMapping("/resourceWithParam/{param1}")
    fun resourceWithPathVariableAndRequestParam(
        @PathVariable param1: String,
        @RequestParam param2: String
    ) =
        GreetingResponse(name = randomAlphabetic(10))

    @Suppress("UNUSED_PARAMETER")
    @GetMapping("/resourceWithParam/{param1}/{param2}")
    fun resourceWithMultiplePathVariablesAndRequestParams(
        @PathVariable param1: String,
        @PathVariable param2: String,
        @RequestParam param3: String,
        @RequestParam param4: String
    ) =
        GreetingResponse(name = randomAlphabetic(10))

    @GetMapping
    fun resourceWithNoSubResource() = GreetingResponse(name = randomAlphabetic(10))

    @Suppress("UNUSED_PARAMETER")
    @GetMapping("/resourceWithParamSet")
    fun resourceWithParamSet(@RequestParam paramSet: Set<String>) =
        GreetingResponse(name = randomAlphabetic(10))

    @Suppress("UNUSED_PARAMETER")
    @GetMapping("/resourceWithParamList")
    fun resourceWithParamList(@RequestParam("parameter1") param1: Long, @RequestParam param2: List<String>,
                              @RequestParam(name = "parameter3") param3: Int) =
        GreetingResponse(name = randomAlphabetic(10))

    @Suppress("UNUSED_PARAMETER")
    @GetMapping("/resourceWithBooleanRequestParam")
    fun resourceWithBooleanRequestParam(@RequestParam param: Boolean) =
        GreetingResponse(name = randomAlphabetic(10))

    @Suppress("UNUSED_PARAMETER")
    @GetMapping("/resourceWithOptionalBooleanRequestParam")
    fun resourceWithOptionalBooleanRequestParam(@RequestParam(required = false) param: Boolean) =
        GreetingResponse(name = randomAlphabetic(10))

    @Suppress("UNUSED_PARAMETER")
    @GetMapping("/resourceWithOptionalIntRequestParams")
    fun resourceWithOptionalIntRequestParams(@RequestParam param1: Boolean,
                                             @RequestParam(required = false) param2: Int,
                                             @RequestParam(required = false) param3: Int,
                                             @RequestParam("parameter4") param4: Long) =
        GreetingResponse(name = randomAlphabetic(10))

    @Suppress("UNUSED_PARAMETER")
    @GetMapping("/resourceWithOptionalMultiValueRequestParams")
    fun resourceWithOptionalMultiValueRequestParams(@RequestParam required: Boolean,
                                             @RequestParam(required = false) optional: Int,
                                             @RequestParam multiValue: Set<Int>,
                                             @RequestParam(name = "parameter4", required = false) optionalMultiValue: Set<Int>) =
        GreetingResponse(name = randomAlphabetic(10))

    @Suppress("UNUSED_PARAMETER")
    @GetMapping("/resourceWithZonedDatetime")
    fun resourceWithZonedDatetime(@RequestParam @DateTimeFormat(iso = DATE_TIME) param: ZonedDateTime) =
        GreetingResponse(name = randomAlphabetic(10))

    @Suppress("UNUSED_PARAMETER")
    @GetMapping("/resourceWithZonedDatetimeAndAllDateTimeFormatArguments")
    fun resourceWithZonedDatetimeAndAllDateTimeFormatArguments(@RequestParam @DateTimeFormat(iso = DATE,
        fallbackPatterns = ["pattern1", "pattern2"], style = "SS", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX") param: ZonedDateTime) =
        GreetingResponse(name = randomAlphabetic(10))

    @Suppress("UNUSED_PARAMETER")
    @GetMapping("/resourceWithZonedDatetimeAndMultiValue")
    fun resourceWithZonedDatetimeAndMultiValue(@RequestParam @DateTimeFormat(iso = DATE_TIME) param: ZonedDateTime,
                                               @RequestParam multiValue: Set<Int>) =
        GreetingResponse(name = randomAlphabetic(10))
}
