package com.lsdconsulting.stub.integration.get

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.VerificationException
import com.lsdconsulting.stub.integration.BaseRestControllerIT
import com.lsdconsulting.stub.integration.GET_CONTROLLER_URL
import com.lsdconsulting.stub.integration.controller.get.GetRestControllerStub
import com.lsdconsulting.stub.integration.model.GreetingResponse
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity.EMPTY
import org.springframework.http.HttpMethod.GET

class GetRestControllerStandardResponseIT : BaseRestControllerIT() {
    private val underTest = GetRestControllerStub(ObjectMapper())

    @Test
    fun `should Handle Get Mapping With No Param`() {
        underTest.verifyGetResourceWithNoParamsNoInteraction()
        underTest.verifyGetResourceWithNoParamsNoInteractionWithUrl()
        underTest.getResourceWithNoParams(greetingResponse)
        val response =
            restTemplate.getForEntity("$GET_CONTROLLER_URL/resourceWithNoParams", GreetingResponse::class.java)
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithNoParams(1)
        underTest.verifyGetResourceWithNoParams()
        assertThrows<VerificationException> { underTest.verifyGetResourceWithNoParamsNoInteraction() }
        assertThrows<VerificationException> { underTest.verifyGetResourceWithNoParamsNoInteractionWithUrl() }
    }

    @Test
    fun `should Handle Get Mapping With Request Param`() {
        underTest.verifyGetResourceWithParamNoInteraction(param)
        underTest.verifyGetResourceWithParamNoInteractionWithUrl()
        underTest.getResourceWithParam(greetingResponse, param)
        val response = restTemplate.getForEntity(
            "$GET_CONTROLLER_URL/resourceWithParam?param=$param",
            GreetingResponse::class.java
        )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithParam(1, param)
        underTest.verifyGetResourceWithParam(param)
        assertThrows<VerificationException> { underTest.verifyGetResourceWithParamNoInteraction(param) }
        assertThrows<VerificationException> { underTest.verifyGetResourceWithParamNoInteractionWithUrl() }
    }

    @Test
    fun `should handle collection response for filter queries`() {
        underTest.verifyGetFilteredResourceWithParamNoInteraction(param)
        underTest.verifyGetFilteredResourceWithParamNoInteractionWithUrl()
        underTest.getFilteredResourceWithParam(listOf(greetingResponse), param)
        val response = restTemplate.exchange(
            "$GET_CONTROLLER_URL/filteredResourceWithParam?param=$param", GET, EMPTY,
            object: ParameterizedTypeReference<List<GreetingResponse>>(){}
        )
        assertThat(response.body, notNullValue())
        assertThat(response.body, hasSize(1))
        assertThat(response.body?.get(0)?.name, `is`(name))
        assertThrows<VerificationException> { underTest.verifyGetFilteredResourceWithParamNoInteraction(param) }
        assertThrows<VerificationException> { underTest.verifyGetFilteredResourceWithParamNoInteractionWithUrl() }
    }

    @Test
    fun `should Handle Get Mapping With Multiple Request Params`() {
        underTest.verifyGetResourceWithMultipleParamsNoInteraction(param1, param2)
        underTest.verifyGetResourceWithMultipleParamsNoInteractionWithUrl()
        underTest.getResourceWithMultipleParams(greetingResponse, param1, param2)
        val response = restTemplate.getForEntity(
            "$GET_CONTROLLER_URL/resourceWithMultipleParams?param1=$param1&param2=$param2",
            GreetingResponse::class.java
        )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithMultipleParams(1, param1, param2)
        underTest.verifyGetResourceWithMultipleParams(param1, param2)
        assertThrows<VerificationException> {
            underTest.verifyGetResourceWithMultipleParamsNoInteraction(param1, param2)
        }
        assertThrows<VerificationException> { underTest.verifyGetResourceWithMultipleParamsNoInteractionWithUrl() }
    }

    @Test
    fun `should Handle Get Mapping With Path Variable`() {
        underTest.verifyGetResourceWithPathVariableNoInteraction(param)
        underTest.verifyGetResourceWithPathVariableNoInteractionWithUrl(param)
        underTest.getResourceWithPathVariable(greetingResponse, param)
        val response =
            restTemplate.getForEntity("$GET_CONTROLLER_URL/resourceWithParam/$param", GreetingResponse::class.java)
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithPathVariable(1, param)
        underTest.verifyGetResourceWithPathVariable(param)
        assertThrows<VerificationException> { underTest.verifyGetResourceWithPathVariableNoInteraction(param) }
        assertThrows<VerificationException> { underTest.verifyGetResourceWithPathVariableNoInteractionWithUrl(param) }
    }

    @Test
    fun `should Handle Get Mapping With Multiple Path Variables`() {
        underTest.verifyGetResourceWithMultiplePathVariablesNoInteraction(param1, param2)
        underTest.verifyGetResourceWithMultiplePathVariablesNoInteractionWithUrl(param1, param2)
        underTest.getResourceWithMultiplePathVariables(greetingResponse, param1, param2)
        val response = restTemplate.getForEntity(
            "$GET_CONTROLLER_URL/resourceWithParam/$param1/$param2",
            GreetingResponse::class.java
        )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithMultiplePathVariables(1, param1, param2)
        underTest.verifyGetResourceWithMultiplePathVariables(param1, param2)
        assertThrows<VerificationException> {
            underTest.verifyGetResourceWithMultiplePathVariablesNoInteraction(param1, param2)
        }
        assertThrows<VerificationException> {
            underTest.verifyGetResourceWithMultiplePathVariablesNoInteractionWithUrl(param1, param2)
        }
    }

    @Test
    fun `should handle get mapping with path variable and request param`() {
        underTest.verifyGetResourceWithPathVariableAndRequestParamNoInteraction(param1, param2)
        underTest.verifyGetResourceWithPathVariableAndRequestParamNoInteractionWithUrl(param1)
        underTest.getResourceWithPathVariableAndRequestParam(greetingResponse, param1, param2)
        val response = restTemplate.getForEntity(
            "$GET_CONTROLLER_URL/resourceWithParam/$param1?param2=$param2",
            GreetingResponse::class.java
        )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithPathVariableAndRequestParam(1, param1, param2)
        underTest.verifyGetResourceWithPathVariableAndRequestParam(param1, param2)
        assertThrows<VerificationException> {
            underTest.verifyGetResourceWithPathVariableAndRequestParamNoInteraction(param1, param2)
        }
        assertThrows<VerificationException> {
            underTest.verifyGetResourceWithPathVariableAndRequestParamNoInteractionWithUrl(param1)
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
        underTest.verifyGetResourceWithMultiplePathVariablesAndRequestParamsNoInteractionWithUrl(param1, param2)
        underTest.getResourceWithMultiplePathVariablesAndRequestParams(
            greetingResponse,
            param1,
            param2,
            param3,
            param4
        )
        val response = restTemplate.getForEntity(
            "$GET_CONTROLLER_URL/resourceWithParam/$param1/$param2?param3=$param3&param4=$param4",
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
        assertThrows<VerificationException> {
            underTest.verifyGetResourceWithMultiplePathVariablesAndRequestParamsNoInteractionWithUrl(param1, param2)
        }
    }

    @Test
    fun `should handle get mapping with no subResource`() {
        underTest.verifyGetResourceWithNoSubResourceNoInteraction()
        underTest.verifyGetResourceWithNoSubResourceNoInteractionWithUrl()
        underTest.getResourceWithNoSubResource(greetingResponse)
        val response = restTemplate.getForEntity(GET_CONTROLLER_URL, GreetingResponse::class.java)
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithNoSubResource(1)
        underTest.verifyGetResourceWithNoSubResource()
        assertThrows<VerificationException> {
            underTest.verifyGetResourceWithNoSubResourceNoInteraction()
            underTest.verifyGetResourceWithNoSubResourceNoInteractionWithUrl()
        }
    }
}
