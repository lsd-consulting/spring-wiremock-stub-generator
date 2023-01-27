package com.lsdconsulting.stub.integration.get

import com.fasterxml.jackson.databind.ObjectMapper
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
        underTest.getResourceWithNoParams(httpStatus.value(), customResponseBody)
        val ex = assertThrows<HttpServerErrorException> {
            restTemplate.getForEntity(
                "$GET_CONTROLLER_URL/resourceWithNoParams",
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
                "$GET_CONTROLLER_URL/resourceWithParam?param=$param",
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
                "$GET_CONTROLLER_URL/resourceWithMultipleParams?param1=$param1&param2=$param2",
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
                "$GET_CONTROLLER_URL/resourceWithParam/$param",
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
                "$GET_CONTROLLER_URL/resourceWithParam/$param1/$param2",
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
                "$GET_CONTROLLER_URL/resourceWithParam/$param1?param2=$param2",
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
            restTemplate.getForEntity(
                GET_CONTROLLER_URL,
                GreetingResponse::class.java
            )
        }
        assertThat(ex.statusCode, `is`(httpStatus))
        assertThat(ex.responseBodyAsString, notNullValue())
        assertThat(ex.responseBodyAsString, `is`(customResponseBody))
        underTest.verifyGetResourceWithNoSubresource(1)
        underTest.verifyGetResourceWithNoSubresource()
    }

    @Test
    fun `should handle get mapping with multiple path variables and request params and custom response`() {
        underTest.getResourceWithMultiplePathVariablesAndRequestParams(
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
        underTest.verifyGetResourceWithMultiplePathVariablesAndRequestParams(1, param1, param2, param3, param4)
        underTest.verifyGetResourceWithMultiplePathVariablesAndRequestParams(param1, param2, param3, param4)
    }
}
