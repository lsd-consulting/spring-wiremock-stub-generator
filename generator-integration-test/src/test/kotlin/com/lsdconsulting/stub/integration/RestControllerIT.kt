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
        underTest.verifyGetResourceWithNoParams(1,)
    }

    @Test
    fun shouldHandleGetMappingWithRequestParam() {
        val name = randomAlphabetic(10)
        underTest.getResourceWithParam(Greeting(name = name), "test")
        val response =
            restTemplate.getForEntity("http://localhost:8080/getController/resourceWithParam?param=test", Greeting::class.java)
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithParam(1,"test")
    }

    @Test
    fun shouldHandleGetMappingWithMultipleRequestParams() {
        val name = randomAlphabetic(10)
        underTest.getResourceWithMultipleParams(Greeting(name = name), "test1", "test2")
            val response =
                restTemplate.getForEntity("http://localhost:8080/getController/resourceWithMultipleParams?param1=test1&param2=test2", Greeting::class.java)
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithMultipleParams(1,"test1", "test2")
    }

    @Test
    fun shouldHandleGetMappingWithPathVariable() {
        val name = randomAlphabetic(10)
        underTest.getResourceWithPathVariable(Greeting(name = name), "test")
        val response =
            restTemplate.getForEntity("http://localhost:8080/getController/resourceWithParam/test", Greeting::class.java)
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithPathVariable(1,"test")
    }

    @Test
    fun shouldHandleGetMappingWithMultiplePathVariables() {
        val name = randomAlphabetic(10)
        underTest.getResourceWithMultiplePathVariables(Greeting(name = name), "test1", "test2")
        val response =
            restTemplate.getForEntity("http://localhost:8080/getController/resourceWithParam/test1/test2", Greeting::class.java)
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithMultiplePathVariables(1,"test1", "test2")
    }

    @Test
    fun shouldHandleGetMappingWithPathVariableAndRequestParam() {
        val name = randomAlphabetic(10)
        underTest.getResourceWithPathVariableAndRequestParam(Greeting(name = name), "test2", "test1")
        val response =
            restTemplate.getForEntity("http://localhost:8080/getController/resourceWithParam/test1?param2=test2", Greeting::class.java)
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithPathVariableAndRequestParam(1,"test2", "test1")
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
