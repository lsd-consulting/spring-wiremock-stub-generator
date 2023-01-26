package com.lsdconsulting.stub.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.lsdconsulting.stub.integration.controller.GetRestControllerStub
import com.lsdconsulting.stub.integration.model.Greeting
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
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
        val name = randomAlphabetic(10)
        underTest.getResourceWithNoParams(Greeting(name = name))
        val response =
            restTemplate.getForEntity("http://localhost:8080/getController/resourceWithNoParams", Greeting::class.java)
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithNoParams(1)
    }

    @Test
    fun shouldHandleGetMappingWithRequestParam() {
        val name = randomAlphabetic(10)
        val param = randomAlphabetic(10)
        underTest.getResourceWithParam(Greeting(name = name), param)
        val response =
            restTemplate.getForEntity(
                "http://localhost:8080/getController/resourceWithParam?param=$param",
                Greeting::class.java
            )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithParam(1, param)
    }

    @Test
    fun shouldHandleGetMappingWithMultipleRequestParams() {
        val name = randomAlphabetic(10)
        val param1 = randomAlphabetic(10)
        val param2 = randomAlphabetic(10)
        underTest.getResourceWithMultipleParams(Greeting(name = name), param1, param2)
        val response =
            restTemplate.getForEntity(
                "http://localhost:8080/getController/resourceWithMultipleParams?param1=$param1&param2=$param2",
                Greeting::class.java
            )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithMultipleParams(1, param1, param2)
    }

    @Test
    fun shouldHandleGetMappingWithPathVariable() {
        val name = randomAlphabetic(10)
        val param = randomAlphabetic(10)
        underTest.getResourceWithPathVariable(Greeting(name = name), param)
        val response =
            restTemplate.getForEntity(
                "http://localhost:8080/getController/resourceWithParam/$param",
                Greeting::class.java
            )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithPathVariable(1, param)
    }

    @Test
    fun shouldHandleGetMappingWithMultiplePathVariables() {
        val name = randomAlphabetic(10)
        val param1 = randomAlphabetic(10)
        val param2 = randomAlphabetic(10)
        underTest.getResourceWithMultiplePathVariables(Greeting(name = name), param1, param2)
        val response =
            restTemplate.getForEntity(
                "http://localhost:8080/getController/resourceWithParam/$param1/$param2",
                Greeting::class.java
            )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithMultiplePathVariables(1, param1, param2)
    }

    @Test
    fun shouldHandleGetMappingWithPathVariableAndRequestParam() {
        val name = randomAlphabetic(10)
        val param1 = randomAlphabetic(10)
        val param2 = randomAlphabetic(10)
        underTest.getResourceWithPathVariableAndRequestParam(Greeting(name = name), param2, param1)
        val response =
            restTemplate.getForEntity(
                "http://localhost:8080/getController/resourceWithParam/$param1?param2=$param2",
                Greeting::class.java
            )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithPathVariableAndRequestParam(1, param2, param1)
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
