package com.lsdconsulting.stub.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.lsdconsulting.stub.integration.controller.GetRestControllerStub
import com.lsdconsulting.stub.integration.model.Greeting
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.apache.commons.lang3.RandomUtils
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.*
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestTemplate

class GetRestControllerIT {
    private val restTemplate = RestTemplate()
    private val underTest = GetRestControllerStub(ObjectMapper())

    private val name = randomAlphabetic(10)
    private val param = randomAlphabetic(10)
    private val param1 = randomAlphabetic(10)
    private val param2 = randomAlphabetic(10)
    private val customResponseBody = randomAlphabetic(10)
    private val httpStatus = HttpStatus.valueOf(RandomUtils.nextInt(500, 511))

    @BeforeEach
    fun setup() {
        WireMock.reset()
    }

    @Test
    fun shouldHandleGetMappingWithNoParam() {
        underTest.getResourceWithNoParams(Greeting(name = name))
        val response =
            restTemplate.getForEntity("http://localhost:8080/getController/resourceWithNoParams", Greeting::class.java)
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithNoParams(1)
    }

    @Test
    fun shouldHandleGetMappingWithRequestParam() {
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

    @Test
    fun shouldHandleGetMappingWithNoSubresource() {
        underTest.getResourceWithNoSubresource(Greeting(name = name))
        val response =
            restTemplate.getForEntity("http://localhost:8080/getController", Greeting::class.java)
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithNoSubresource(1)
    }

    @Test
    fun shouldHandleGetMappingWithNoParamAndCustomResponse() {
        underTest.getResourceWithNoParams(httpStatus.value(), customResponseBody)
        val ex = assertThrows<HttpServerErrorException> { restTemplate.getForEntity("http://localhost:8080/getController/resourceWithNoParams", Greeting::class.java) }
        assertThat(ex.statusCode, `is`(httpStatus))
        assertThat(ex.responseBodyAsString, notNullValue())
        assertThat(ex.responseBodyAsString, `is`(customResponseBody))
        underTest.verifyGetResourceWithNoParams(1)
    }

    @Test
    fun shouldHandleGetMappingWithRequestParamAndCustomResponse() {
        underTest.getResourceWithParam(httpStatus.value(), customResponseBody, param)
        val ex = assertThrows<HttpServerErrorException> {
            restTemplate.getForEntity(
                "http://localhost:8080/getController/resourceWithParam?param=$param",
                Greeting::class.java
            )
        }
        assertThat(ex.statusCode, `is`(httpStatus))
        assertThat(ex.responseBodyAsString, notNullValue())
        assertThat(ex.responseBodyAsString, `is`(customResponseBody))
        underTest.verifyGetResourceWithParam(1, param)
    }

    @Test
    fun shouldHandleGetMappingWithMultipleRequestParamsAndCustomResponse() {
        underTest.getResourceWithMultipleParams(httpStatus.value(), customResponseBody, param1, param2)
        val ex = assertThrows<HttpServerErrorException> {
            restTemplate.getForEntity(
                "http://localhost:8080/getController/resourceWithMultipleParams?param1=$param1&param2=$param2",
                Greeting::class.java
            )
        }
        assertThat(ex.statusCode, `is`(httpStatus))
        assertThat(ex.responseBodyAsString, notNullValue())
        assertThat(ex.responseBodyAsString, `is`(customResponseBody))
        underTest.verifyGetResourceWithMultipleParams(1, param1, param2)
    }

    @Test
    fun shouldHandleGetMappingWithPathVariableAndCustomResponse() {
        underTest.getResourceWithPathVariable(httpStatus.value(), customResponseBody, param)
        val ex = assertThrows<HttpServerErrorException> {
            restTemplate.getForEntity(
                "http://localhost:8080/getController/resourceWithParam/$param",
                Greeting::class.java
            )
        }
        assertThat(ex.statusCode, `is`(httpStatus))
        assertThat(ex.responseBodyAsString, notNullValue())
        assertThat(ex.responseBodyAsString, `is`(customResponseBody))
        underTest.verifyGetResourceWithPathVariable(1, param)
    }

    @Test
    fun shouldHandleGetMappingWithMultiplePathVariablesAndCustomResponse() {
        underTest.getResourceWithMultiplePathVariables(httpStatus.value(), customResponseBody, param1, param2)
        val ex = assertThrows<HttpServerErrorException> {
            restTemplate.getForEntity(
                "http://localhost:8080/getController/resourceWithParam/$param1/$param2",
                Greeting::class.java
            )
        }
        assertThat(ex.statusCode, `is`(httpStatus))
        assertThat(ex.responseBodyAsString, notNullValue())
        assertThat(ex.responseBodyAsString, `is`(customResponseBody))
        underTest.verifyGetResourceWithMultiplePathVariables(1, param1, param2)
    }

    @Test
    fun shouldHandleGetMappingWithPathVariableAndRequestParamAndCustomResponse() {
        underTest.getResourceWithPathVariableAndRequestParam(httpStatus.value(), customResponseBody, param2, param1)
        val ex = assertThrows<HttpServerErrorException> {
            restTemplate.getForEntity(
                "http://localhost:8080/getController/resourceWithParam/$param1?param2=$param2",
                Greeting::class.java
            )
        }
        assertThat(ex.statusCode, `is`(httpStatus))
        assertThat(ex.responseBodyAsString, notNullValue())
        assertThat(ex.responseBodyAsString, `is`(customResponseBody))
        underTest.verifyGetResourceWithPathVariableAndRequestParam(1, param2, param1)
    }

    @Test
    fun shouldHandleGetMappingWithNoSubresourceAndCustomResponse() {
        underTest.getResourceWithNoSubresource(httpStatus.value(), customResponseBody)
        val ex = assertThrows<HttpServerErrorException> {
            restTemplate.getForEntity("http://localhost:8080/getController", Greeting::class.java)
        }
        assertThat(ex.statusCode, `is`(httpStatus))
        assertThat(ex.responseBodyAsString, notNullValue())
        assertThat(ex.responseBodyAsString, `is`(customResponseBody))
        underTest.verifyGetResourceWithNoSubresource(1)
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
