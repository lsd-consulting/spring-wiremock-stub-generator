package com.lsdconsulting.stub.integration.get

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.VerificationException
import com.lsdconsulting.stub.integration.BaseRestControllerIT
import com.lsdconsulting.stub.integration.GET_CONTROLLER_URL
import com.lsdconsulting.stub.integration.controller.get.GetRestControllerStub
import com.lsdconsulting.stub.integration.model.GreetingResponse
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.web.client.HttpServerErrorException

class GetRestControllerCustomResponseIT : BaseRestControllerIT() {
    private val underTest = GetRestControllerStub(ObjectMapper())

    @Test
    fun `should handle get mapping with no param and custom response`() {
        underTest.resourceWithNoParams(httpStatus.value(), customResponseBody)
        val ex = assertThrows<HttpServerErrorException> {
            restTemplate.getForEntity(
                "$GET_CONTROLLER_URL/resourceWithNoParams",
                GreetingResponse::class.java
            )
        }
        assertThat(ex.statusCode, `is`(httpStatus))
        assertThat(ex.responseBodyAsString, notNullValue())
        assertThat(ex.responseBodyAsString, `is`(customResponseBody))
        underTest.verifyResourceWithNoParams(1)
        underTest.verifyResourceWithNoParams()
    }

    @Test
    fun `should handle get mapping with request param and custom response`() {
        underTest.resourceWithParam(httpStatus.value(), customResponseBody, param)
        val ex = assertThrows<HttpServerErrorException> {
            restTemplate.getForEntity(
                "$GET_CONTROLLER_URL/resourceWithParam?param=$param",
                GreetingResponse::class.java
            )
        }
        assertThat(ex.statusCode, `is`(httpStatus))
        assertThat(ex.responseBodyAsString, notNullValue())
        assertThat(ex.responseBodyAsString, `is`(customResponseBody))
        underTest.verifyResourceWithParam(1, param)
        underTest.verifyResourceWithParam(param)
    }

    @Test
    fun `should handle get mapping with multiple request params and custom response`() {
        underTest.resourceWithMultipleParams(httpStatus.value(), customResponseBody, param1, param2)
        val ex = assertThrows<HttpServerErrorException> {
            restTemplate.getForEntity(
                "$GET_CONTROLLER_URL/resourceWithMultipleParams?param1=$param1&param2=$param2",
                GreetingResponse::class.java
            )
        }
        assertThat(ex.statusCode, `is`(httpStatus))
        assertThat(ex.responseBodyAsString, notNullValue())
        assertThat(ex.responseBodyAsString, `is`(customResponseBody))
        underTest.verifyResourceWithMultipleParams(1, param1, param2)
        underTest.verifyResourceWithMultipleParams(param1, param2)
    }

    @Test
    fun `should handle get mapping with path variable and custom response`() {
        underTest.resourceWithPathVariable(httpStatus.value(), customResponseBody, param)
        val ex = assertThrows<HttpServerErrorException> {
            restTemplate.getForEntity(
                "$GET_CONTROLLER_URL/resourceWithParam/$param",
                GreetingResponse::class.java
            )
        }
        assertThat(ex.statusCode, `is`(httpStatus))
        assertThat(ex.responseBodyAsString, notNullValue())
        assertThat(ex.responseBodyAsString, `is`(customResponseBody))
        underTest.verifyResourceWithPathVariable(1, param)
        underTest.verifyResourceWithPathVariable(param)
    }

    @Test
    fun `should handle get mapping with multiple path variables and custom response`() {
        underTest.resourceWithMultiplePathVariables(httpStatus.value(), customResponseBody, param1, param2)
        val ex = assertThrows<HttpServerErrorException> {
            restTemplate.getForEntity(
                "$GET_CONTROLLER_URL/resourceWithParam/$param1/$param2",
                GreetingResponse::class.java
            )
        }
        assertThat(ex.statusCode, `is`(httpStatus))
        assertThat(ex.responseBodyAsString, notNullValue())
        assertThat(ex.responseBodyAsString, `is`(customResponseBody))
        underTest.verifyResourceWithMultiplePathVariables(1, param1, param2)
        underTest.verifyResourceWithMultiplePathVariables(param1, param2)
    }

    @Test
    fun `should handle get mapping with path variable and request param and custom response`() {
        underTest.resourceWithPathVariableAndRequestParam(httpStatus.value(), customResponseBody, param1, param2)
        val ex = assertThrows<HttpServerErrorException> {
            restTemplate.getForEntity(
                "$GET_CONTROLLER_URL/resourceWithParam/$param1?param2=$param2",
                GreetingResponse::class.java
            )
        }
        assertThat(ex.statusCode, `is`(httpStatus))
        assertThat(ex.responseBodyAsString, notNullValue())
        assertThat(ex.responseBodyAsString, `is`(customResponseBody))
        underTest.verifyResourceWithPathVariableAndRequestParam(1, param1, param2)
        underTest.verifyResourceWithPathVariableAndRequestParam(param1, param2)
    }

    @Test
    fun `should handle get mapping with no subResource and custom response`() {
        underTest.resourceWithNoSubResource(httpStatus.value(), customResponseBody)
        val ex = assertThrows<HttpServerErrorException> {
            restTemplate.getForEntity(
                GET_CONTROLLER_URL,
                GreetingResponse::class.java
            )
        }
        assertThat(ex.statusCode, `is`(httpStatus))
        assertThat(ex.responseBodyAsString, notNullValue())
        assertThat(ex.responseBodyAsString, `is`(customResponseBody))
        underTest.verifyResourceWithNoSubResource(1)
        underTest.verifyResourceWithNoSubResource()
    }

    @Test
    fun `should handle get mapping with multiple path variables and request params and custom response`() {
        underTest.resourceWithMultiplePathVariablesAndRequestParams(
            httpStatus.value(),
            customResponseBody,
            param1,
            param2,
            param3,
            param4
        )
        val ex = assertThrows<HttpServerErrorException> {
            restTemplate.getForEntity(
                "$GET_CONTROLLER_URL/resourceWithParam/$param1/$param2?param3=$param3&param4=$param4",
                GreetingResponse::class.java
            )
        }
        assertThat(ex.statusCode, `is`(httpStatus))
        assertThat(ex.responseBodyAsString, notNullValue())
        assertThat(ex.responseBodyAsString, `is`(customResponseBody))
        underTest.verifyResourceWithMultiplePathVariablesAndRequestParams(1, param1, param2, param3, param4)
        underTest.verifyResourceWithMultiplePathVariablesAndRequestParams(param1, param2, param3, param4)
        assertThrows<VerificationException> { underTest.verifyResourceWithMultiplePathVariablesAndRequestParamsNoInteraction(param1, param2, param3, param4) }
        assertThrows<VerificationException> { underTest.verifyResourceWithMultiplePathVariablesAndRequestParamsNoInteractionWithUrl(param1, param2) }
    }
}
