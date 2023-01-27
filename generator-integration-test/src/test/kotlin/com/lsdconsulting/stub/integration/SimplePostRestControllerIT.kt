package com.lsdconsulting.stub.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.VerificationException
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.lsdconsulting.stub.integration.controller.post.SimplePostRestControllerStub
import com.lsdconsulting.stub.integration.model.GreetingResponse
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.*
import org.springframework.http.HttpEntity
import org.springframework.web.client.RestTemplate

class SimplePostRestControllerIT {
    private val restTemplate = RestTemplate()
    private val underTest = SimplePostRestControllerStub(ObjectMapper())

    private val name = randomAlphabetic(10)

    @BeforeEach
    fun setup() {
        WireMock.reset()
    }

    @Test
    fun `should handle post mapping with no body`() {
        underTest.verifyPostResourceWithNoBodyNoInteraction()
        underTest.postResourceWithNoBody(GreetingResponse(name = name))
        val response =
            restTemplate.postForEntity(
                "http://localhost:8080/postController/resourceWithNoBody",
                HttpEntity(""),
                GreetingResponse::class.java
            )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyPostResourceWithNoBody(1)
        underTest.verifyPostResourceWithNoBody()
        assertThrows<VerificationException> { underTest.verifyPostResourceWithNoBodyNoInteraction() }
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
