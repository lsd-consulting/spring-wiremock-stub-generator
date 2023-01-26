package com.lsdconsulting.stub.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.lsdconsulting.stub.integration.controller.GetRestControllerStub
import com.lsdconsulting.stub.integration.model.Greeting
import org.apache.commons.lang3.RandomStringUtils
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.web.client.RestTemplate

class RestControllerIT {
    private val restTemplate = RestTemplate()
    private val underTest = GetRestControllerStub(ObjectMapper())

    @Test
    fun shouldHandleGetMappingWithNoParam() {
        val name = RandomStringUtils.randomAlphabetic(10)
        underTest.getResourceWithNoParams_(Greeting(name = name))
        val response =
            restTemplate.getForEntity("http://localhost:8080/getController/resourceWithNoParams", Greeting::class.java)
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithNoParams_()
    }

    @Test
    fun shouldHandleGetMappingWithParam() {
        val name = RandomStringUtils.randomAlphabetic(10)
        underTest.getResourceWithParam_(Greeting(name = name), "test")
        val response =
            restTemplate.getForEntity("http://localhost:8080/getController/resourceWithParam?param=test", Greeting::class.java)
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithParam_("test")
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
