package com.lsdconsulting.stub.integration.get

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.VerificationException
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.lsdconsulting.stub.integration.controller.get.GetRestControllerStub
import com.lsdconsulting.stub.integration.model.GreetingResponse
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.*
import org.springframework.web.client.RestTemplate

class GetRestControllerStandardResponseIT {
    private val restTemplate = RestTemplate()
    private val underTest = GetRestControllerStub(ObjectMapper())

    private val name = randomAlphabetic(10)
    private val param = randomAlphabetic(10)
    private val param1 = randomAlphabetic(10)
    private val param2 = randomAlphabetic(10)
    private val param3 = randomAlphabetic(10)
    private val param4 = randomAlphabetic(10)

    @BeforeEach
    fun setup() {
        WireMock.reset()
    }

    @Test
    fun `should Handle Get Mapping With No Param`() {
        underTest.verifyGetResourceWithNoParamsNoInteraction()
        underTest.getResourceWithNoParams(GreetingResponse(name = name))
        val response =
            restTemplate.getForEntity(
                "http://localhost:8080/getController/resourceWithNoParams",
                GreetingResponse::class.java
            )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithNoParams(1)
        underTest.verifyGetResourceWithNoParams()
        assertThrows<VerificationException> { underTest.verifyGetResourceWithNoParamsNoInteraction() }
    }

    @Test
    fun `should Handle Get Mapping With Request Param`() {
        underTest.verifyGetResourceWithParamNoInteraction(param)
        underTest.getResourceWithParam(GreetingResponse(name = name), param)
        val response = restTemplate.getForEntity(
            "http://localhost:8080/getController/resourceWithParam?param=$param",
            GreetingResponse::class.java
        )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithParam(1, param)
        underTest.verifyGetResourceWithParam(param)
        assertThrows<VerificationException> { underTest.verifyGetResourceWithParamNoInteraction(param) }
    }

    @Test
    fun `should Handle Get Mapping With Multiple Request Params`() {
        underTest.verifyGetResourceWithMultipleParamsNoInteraction(param1, param2)
        underTest.getResourceWithMultipleParams(GreetingResponse(name = name), param1, param2)
        val response = restTemplate.getForEntity(
            "http://localhost:8080/getController/resourceWithMultipleParams?param1=$param1&param2=$param2",
            GreetingResponse::class.java
        )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithMultipleParams(1, param1, param2)
        underTest.verifyGetResourceWithMultipleParams(param1, param2)
        assertThrows<VerificationException> {
            underTest.verifyGetResourceWithMultipleParamsNoInteraction(
                param1,
                param2
            )
        }
    }

    @Test
    fun `should Handle Get Mapping With Path Variable`() {
        underTest.verifyGetResourceWithPathVariableNoInteraction(param)
        underTest.getResourceWithPathVariable(GreetingResponse(name = name), param)
        val response = restTemplate.getForEntity(
            "http://localhost:8080/getController/resourceWithParam/$param",
            GreetingResponse::class.java
        )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithPathVariable(1, param)
        underTest.verifyGetResourceWithPathVariable(param)
        assertThrows<VerificationException> { underTest.verifyGetResourceWithPathVariableNoInteraction(param) }
    }

    @Test
    fun `should Handle Get Mapping With Multiple Path Variables`() {
        underTest.verifyGetResourceWithMultiplePathVariablesNoInteraction(param1, param2)
        underTest.getResourceWithMultiplePathVariables(GreetingResponse(name = name), param1, param2)
        val response = restTemplate.getForEntity(
            "http://localhost:8080/getController/resourceWithParam/$param1/$param2",
            GreetingResponse::class.java
        )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithMultiplePathVariables(1, param1, param2)
        underTest.verifyGetResourceWithMultiplePathVariables(param1, param2)
        assertThrows<VerificationException> {
            underTest.verifyGetResourceWithMultiplePathVariablesNoInteraction(
                param1,
                param2
            )
        }
    }

    @Test
    fun `should handle get mapping with path variable and request param`() {
        underTest.verifyGetResourceWithPathVariableAndRequestParamNoInteraction(param1, param2)
        underTest.getResourceWithPathVariableAndRequestParam(GreetingResponse(name = name), param1, param2)
        val response = restTemplate.getForEntity(
            "http://localhost:8080/getController/resourceWithParam/$param1?param2=$param2",
            GreetingResponse::class.java
        )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithPathVariableAndRequestParam(1, param1, param2)
        underTest.verifyGetResourceWithPathVariableAndRequestParam(param1, param2)
        assertThrows<VerificationException> {
            underTest.verifyGetResourceWithPathVariableAndRequestParamNoInteraction(
                param1,
                param2
            )
        }
    }

    @Test
    fun `should handle get mapping with multiple path variables and request params`() {
        underTest.verifyGetResourceWithMultiplePathVariablesAndRequestParamsNoInteraction(
            param1,
            param2,
            param3,
            param4
        )
        underTest.getResourceWithMultiplePathVariablesAndRequestParams(
            GreetingResponse(name = name),
            param1,
            param2,
            param3,
            param4
        )
        val response = restTemplate.getForEntity(
            "http://localhost:8080/getController/resourceWithParam/$param1/$param2?param3=$param3&param4=$param4",
            GreetingResponse::class.java
        )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithMultiplePathVariablesAndRequestParams(1, param1, param2, param3, param4)
        underTest.verifyGetResourceWithMultiplePathVariablesAndRequestParams(param1, param2, param3, param4)
        assertThrows<VerificationException> {
            underTest.verifyGetResourceWithMultiplePathVariablesAndRequestParamsNoInteraction(
                param1,
                param2,
                param3,
                param4
            )
        }
    }

    @Test
    fun `should handle get mapping with no subresource`() {
        underTest.verifyGetResourceWithNoSubresourceNoInteraction()
        underTest.getResourceWithNoSubresource(GreetingResponse(name = name))
        val response = restTemplate.getForEntity("http://localhost:8080/getController", GreetingResponse::class.java)
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithNoSubresource(1)
        underTest.verifyGetResourceWithNoSubresource()
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
