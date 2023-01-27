package com.lsdconsulting.stub.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.lsdconsulting.stub.integration.controller.PostRestControllerStub
import com.lsdconsulting.stub.integration.model.GreetingRequest
import com.lsdconsulting.stub.integration.model.GreetingResponse
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.apache.commons.lang3.RandomUtils
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.web.client.RestTemplate

class PostRestControllerIT {
    private val restTemplate = RestTemplate()
    private val underTest = PostRestControllerStub(ObjectMapper())

    private val name = randomAlphabetic(10)
    private val param = randomAlphabetic(10)
    private val param1 = randomAlphabetic(10)
    private val param2 = randomAlphabetic(10)
    private val param3 = randomAlphabetic(10)
    private val param4 = randomAlphabetic(10)
    private val customResponseBody = randomAlphabetic(10)
    private val httpStatus = HttpStatus.valueOf(RandomUtils.nextInt(500, 511))

    @BeforeEach
    fun setup() {
        WireMock.reset()
    }

    @Test
    fun shouldHandlePostMappingWithNoParam() {
        underTest.postResourceWithNoParams(GreetingResponse(name = name))
        val greetingRequest = GreetingRequest(name = name)
        val request  = HttpEntity(greetingRequest)
        val response =
            restTemplate.postForEntity("http://localhost:8080/postController/resourceWithNoParams", request, GreetingResponse::class.java)
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyPostResourceWithNoParams(1)
        underTest.verifyPostResourceWithNoParams()
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
