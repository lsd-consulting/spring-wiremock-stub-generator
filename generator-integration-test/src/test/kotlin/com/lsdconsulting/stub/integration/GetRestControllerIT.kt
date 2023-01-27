package com.lsdconsulting.stub.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.lsdconsulting.stub.integration.controller.get.GetRestControllerStub
import com.lsdconsulting.stub.integration.model.GreetingResponse
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
    private val param3 = randomAlphabetic(10)
    private val param4 = randomAlphabetic(10)
    private val customResponseBody = randomAlphabetic(10)
    private val httpStatus = HttpStatus.valueOf(RandomUtils.nextInt(500, 511))

    @BeforeEach
    fun setup() {
        WireMock.reset()
    }

    @Test
    fun `should Handle Get Mapping With No Param`() {
        underTest.getResourceWithNoParams(GreetingResponse(name = name))
        val response =
            restTemplate.getForEntity("http://localhost:8080/getController/resourceWithNoParams", GreetingResponse::class.java)
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithNoParams(1)
        underTest.verifyGetResourceWithNoParams()
    }

    @Test
    fun `should Handle Get Mapping With Request Param`() {
        underTest.getResourceWithParam(GreetingResponse(name = name), param)
        val response =
            restTemplate.getForEntity(
                "http://localhost:8080/getController/resourceWithParam?param=$param",
                GreetingResponse::class.java
            )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithParam(1, param)
        underTest.verifyGetResourceWithParam(param)
    }

    @Test
    fun `should Handle Get Mapping With Multiple Request Params`() {
        underTest.getResourceWithMultipleParams(GreetingResponse(name = name), param1, param2)
        val response =
            restTemplate.getForEntity(
                "http://localhost:8080/getController/resourceWithMultipleParams?param1=$param1&param2=$param2",
                GreetingResponse::class.java
            )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithMultipleParams(1, param1, param2)
        underTest.verifyGetResourceWithMultipleParams(param1, param2)
    }

    @Test
    fun `should Handle Get Mapping With Path Variable`() {
        underTest.getResourceWithPathVariable(GreetingResponse(name = name), param)
        val response =
            restTemplate.getForEntity(
                "http://localhost:8080/getController/resourceWithParam/$param",
                GreetingResponse::class.java
            )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithPathVariable(1, param)
        underTest.verifyGetResourceWithPathVariable(param)
    }

    @Test
    fun `should Handle Get Mapping With Multiple Path Variables`() {
        underTest.getResourceWithMultiplePathVariables(GreetingResponse(name = name), param1, param2)
        val response =
            restTemplate.getForEntity(
                "http://localhost:8080/getController/resourceWithParam/$param1/$param2",
                GreetingResponse::class.java
            )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithMultiplePathVariables(1, param1, param2)
        underTest.verifyGetResourceWithMultiplePathVariables(param1, param2)
    }

    @Test
    fun `should handle get mapping with path variable and request param`() {
        underTest.getResourceWithPathVariableAndRequestParam(GreetingResponse(name = name), param1, param2)
        val response =
            restTemplate.getForEntity(
                "http://localhost:8080/getController/resourceWithParam/$param1?param2=$param2",
                GreetingResponse::class.java
            )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithPathVariableAndRequestParam(1, param1, param2)
        underTest.verifyGetResourceWithPathVariableAndRequestParam(param1, param2)
    }

    @Test
    fun `should handle get mapping with multiple path variables and request params`() {
        underTest.getResourceWithMultiplePathVariablesAndRequestParams(GreetingResponse(name = name), param1, param2, param3, param4)
        val response =
            restTemplate.getForEntity(
                "http://localhost:8080/getController/resourceWithParam/$param1/$param2?param3=$param3&param4=$param4",
                GreetingResponse::class.java
            )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithMultiplePathVariablesAndRequestParams(1, param1, param2, param3, param4)
        underTest.verifyGetResourceWithMultiplePathVariablesAndRequestParams(param1, param2, param3, param4)
    }

    @Test
    fun `should handle get mapping with no subresource`() {
        underTest.getResourceWithNoSubresource(GreetingResponse(name = name))
        val response =
            restTemplate.getForEntity("http://localhost:8080/getController", GreetingResponse::class.java)
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithNoSubresource(1)
        underTest.verifyGetResourceWithNoSubresource()
    }

    @Test
    fun `should handle get mapping with no param and custom response`() {
        underTest.getResourceWithNoParams(httpStatus.value(), customResponseBody)
        val ex = assertThrows<HttpServerErrorException> {
            restTemplate.getForEntity(
                "http://localhost:8080/getController/resourceWithNoParams",
                GreetingResponse::class.java
            )
        }
        assertThat(ex.statusCode, `is`(httpStatus))
        assertThat(ex.responseBodyAsString, notNullValue())
        assertThat(ex.responseBodyAsString, `is`(customResponseBody))
        underTest.verifyGetResourceWithNoParams(1)
        underTest.verifyGetResourceWithNoParams()
    }

    @Test
    fun `should handle get mapping with request param and custom response`() {
        underTest.getResourceWithParam(httpStatus.value(), customResponseBody, param)
        val ex = assertThrows<HttpServerErrorException> {
            restTemplate.getForEntity(
                "http://localhost:8080/getController/resourceWithParam?param=$param",
                GreetingResponse::class.java
            )
        }
        assertThat(ex.statusCode, `is`(httpStatus))
        assertThat(ex.responseBodyAsString, notNullValue())
        assertThat(ex.responseBodyAsString, `is`(customResponseBody))
        underTest.verifyGetResourceWithParam(1, param)
        underTest.verifyGetResourceWithParam(param)
    }

    @Test
    fun `should handle get mapping with multiple request params and custom response`() {
        underTest.getResourceWithMultipleParams(httpStatus.value(), customResponseBody, param1, param2)
        val ex = assertThrows<HttpServerErrorException> {
            restTemplate.getForEntity(
                "http://localhost:8080/getController/resourceWithMultipleParams?param1=$param1&param2=$param2",
                GreetingResponse::class.java
            )
        }
        assertThat(ex.statusCode, `is`(httpStatus))
        assertThat(ex.responseBodyAsString, notNullValue())
        assertThat(ex.responseBodyAsString, `is`(customResponseBody))
        underTest.verifyGetResourceWithMultipleParams(1, param1, param2)
        underTest.verifyGetResourceWithMultipleParams(param1, param2)
    }

    @Test
    fun `should handle get mapping with path variable and custom response`() {
        underTest.getResourceWithPathVariable(httpStatus.value(), customResponseBody, param)
        val ex = assertThrows<HttpServerErrorException> {
            restTemplate.getForEntity(
                "http://localhost:8080/getController/resourceWithParam/$param",
                GreetingResponse::class.java
            )
        }
        assertThat(ex.statusCode, `is`(httpStatus))
        assertThat(ex.responseBodyAsString, notNullValue())
        assertThat(ex.responseBodyAsString, `is`(customResponseBody))
        underTest.verifyGetResourceWithPathVariable(1, param)
        underTest.verifyGetResourceWithPathVariable(param)
    }

    @Test
    fun `should handle get mapping with multiple path variables and custom response`() {
        underTest.getResourceWithMultiplePathVariables(httpStatus.value(), customResponseBody, param1, param2)
        val ex = assertThrows<HttpServerErrorException> {
            restTemplate.getForEntity(
                "http://localhost:8080/getController/resourceWithParam/$param1/$param2",
                GreetingResponse::class.java
            )
        }
        assertThat(ex.statusCode, `is`(httpStatus))
        assertThat(ex.responseBodyAsString, notNullValue())
        assertThat(ex.responseBodyAsString, `is`(customResponseBody))
        underTest.verifyGetResourceWithMultiplePathVariables(1, param1, param2)
        underTest.verifyGetResourceWithMultiplePathVariables(param1, param2)
    }

    @Test
    fun `should handle get mapping with path variable and request param and custom response`() {
        underTest.getResourceWithPathVariableAndRequestParam(httpStatus.value(), customResponseBody, param1, param2)
        val ex = assertThrows<HttpServerErrorException> {
            restTemplate.getForEntity(
                "http://localhost:8080/getController/resourceWithParam/$param1?param2=$param2",
                GreetingResponse::class.java
            )
        }
        assertThat(ex.statusCode, `is`(httpStatus))
        assertThat(ex.responseBodyAsString, notNullValue())
        assertThat(ex.responseBodyAsString, `is`(customResponseBody))
        underTest.verifyGetResourceWithPathVariableAndRequestParam(1, param1, param2)
        underTest.verifyGetResourceWithPathVariableAndRequestParam(param1, param2)
    }

    @Test
    fun `should handle get mapping with no subresource and custom response`() {
        underTest.getResourceWithNoSubresource(httpStatus.value(), customResponseBody)
        val ex = assertThrows<HttpServerErrorException> {
            restTemplate.getForEntity("http://localhost:8080/getController", GreetingResponse::class.java)
        }
        assertThat(ex.statusCode, `is`(httpStatus))
        assertThat(ex.responseBodyAsString, notNullValue())
        assertThat(ex.responseBodyAsString, `is`(customResponseBody))
        underTest.verifyGetResourceWithNoSubresource(1)
        underTest.verifyGetResourceWithNoSubresource()
    }

    @Test
    fun `should handle get mapping with multiple path variables and request params and custom response`() {
        underTest.getResourceWithMultiplePathVariablesAndRequestParams(httpStatus.value(), customResponseBody, param1, param2, param3, param4)
        val ex = assertThrows<HttpServerErrorException> {
            restTemplate.getForEntity(
                "http://localhost:8080/getController/resourceWithParam/$param1/$param2?param3=$param3&param4=$param4",
                GreetingResponse::class.java
            )
        }
        assertThat(ex.statusCode, `is`(httpStatus))
        assertThat(ex.responseBodyAsString, notNullValue())
        assertThat(ex.responseBodyAsString, `is`(customResponseBody))
        underTest.verifyGetResourceWithMultiplePathVariablesAndRequestParams(1, param1, param2, param3, param4)
        underTest.verifyGetResourceWithMultiplePathVariablesAndRequestParams(param1, param2, param3, param4)
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
