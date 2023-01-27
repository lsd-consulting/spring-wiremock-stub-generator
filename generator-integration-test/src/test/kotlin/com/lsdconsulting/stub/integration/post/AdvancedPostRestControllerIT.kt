package com.lsdconsulting.stub.integration.post

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.VerificationException
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.lsdconsulting.stub.integration.controller.post.AdvancedPostRestControllerStub
import com.lsdconsulting.stub.integration.model.GreetingRequest
import com.lsdconsulting.stub.integration.model.GreetingResponse
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.*
import org.springframework.http.HttpEntity
import org.springframework.web.client.RestTemplate

class AdvancedPostRestControllerIT {
    private val restTemplate = RestTemplate()
    private val underTest = AdvancedPostRestControllerStub(ObjectMapper())

    private val name = randomAlphabetic(10)
    private val param = randomAlphabetic(10)
    private val greetingResponse = GreetingResponse(name = name)
    private val greetingRequest = GreetingRequest(name = name)

    @BeforeEach
    fun setup() {
        WireMock.reset()
    }

    @Test
    fun `should handle post mapping with body`() {
        underTest.verifyPostResourceWithBodyNoInteraction(greetingRequest)
        underTest.postResourceWithBody(greetingResponse)
        val request = HttpEntity(greetingRequest)
        val response =
            restTemplate.postForEntity(
                "http://localhost:8080/postController/resourceWithBody",
                request,
                GreetingResponse::class.java
            )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyPostResourceWithBody(1, greetingRequest)
        underTest.verifyPostResourceWithBody(greetingRequest)
        underTest.verifyPostResourceWithBodyAndPathVariableNoInteraction(param, greetingRequest)
        assertThrows<VerificationException> { underTest.verifyPostResourceWithBodyNoInteraction(greetingRequest) }
    }

    @Test
    fun `should handle post mapping with body and path variable`() {
        underTest.verifyPostResourceWithBodyAndPathVariableNoInteraction(param, greetingRequest)
        underTest.postResourceWithBodyAndPathVariable(GreetingResponse(name = name), param)
        val request = HttpEntity(greetingRequest)
        val response =
            restTemplate.postForEntity(
                "http://localhost:8080/postController/resourceWithBody/$param",
                request,
                GreetingResponse::class.java
            )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyPostResourceWithBodyAndPathVariable(1, param, greetingRequest)
        underTest.verifyPostResourceWithBodyAndPathVariable(param, greetingRequest)
        underTest.verifyPostResourceWithBodyNoInteraction(greetingRequest)
        assertThrows<VerificationException> { underTest.verifyPostResourceWithBodyAndPathVariableNoInteraction(param, greetingRequest) }
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
