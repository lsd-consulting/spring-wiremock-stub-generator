package com.lsdconsulting.stub.integration

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.lsdconsulting.stub.integration.model.GreetingRequest
import com.lsdconsulting.stub.integration.model.GreetingResponse
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.apache.commons.lang3.RandomUtils
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.springframework.http.HttpStatus
import org.springframework.web.client.RestTemplate

const val GET_CONTROLLER_URL = "http://localhost:8080/getController"
const val POST_CONTROLLER_URL = "http://localhost:8080/postController"

open class BaseRestControllerIT {
    val restTemplate = RestTemplate()

    val name: String = randomAlphabetic(10)
    val param: String = randomAlphabetic(10)
    val param1: String = randomAlphabetic(10)
    val param2: String = randomAlphabetic(10)
    val param3: String = randomAlphabetic(10)
    val param4: String = randomAlphabetic(10)
    val customResponseBody: String = randomAlphabetic(10)
    val httpStatus: HttpStatus = HttpStatus.valueOf(RandomUtils.nextInt(500, 511))

    val greetingResponse = GreetingResponse(name = name)
    val greetingRequest = GreetingRequest(name = name)

    @BeforeEach
    fun setup() {
        WireMock.reset()
    }

    companion object {
        private lateinit var wireMockServer: WireMockServer

        @JvmStatic
        @BeforeAll
        internal fun setupAll() {
            wireMockServer = WireMockServer(WireMockConfiguration.options().port(8080))
            wireMockServer.start()
            WireMock.reset()
        }

        @JvmStatic
        @AfterAll
        internal fun tearDownAll() {
            wireMockServer.stop()
        }
    }
}
