package com.lsdconsulting.stub.integration.get

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.VerificationException
import com.lsdconsulting.stub.integration.BaseRestControllerIT
import com.lsdconsulting.stub.integration.GET_CONTROLLER_URL
import com.lsdconsulting.stub.integration.controller.get.GetRestControllerStub
import com.lsdconsulting.stub.integration.model.GreetingResponse
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpEntity.EMPTY
import org.springframework.http.HttpMethod.GET

class GetRestControllerStandardResponseIT : BaseRestControllerIT() {
    private val underTest = GetRestControllerStub(ObjectMapper())

    @Test
    fun `should handle get mapping with no param`() {
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
    fun `should handle get mapping with request param`() {
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
    fun `should handle get mapping with multiple request params`() {
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
    fun `should handle get mapping with mapped request params`() {
        underTest.verifyGetResourceWithMappedRequestParamsNoInteraction(param1, param2)
        underTest.verifyGetResourceWithMappedRequestParamsNoInteractionWithUrl()
        underTest.getResourceWithMappedRequestParams(greetingResponse, param1, param2)
        val response = restTemplate.getForEntity(
            "$GET_CONTROLLER_URL/resourceWithMappedRequestParams?parameter1=$param1&parameter2=$param2",
            GreetingResponse::class.java
        )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        assertThrows<VerificationException> {
            underTest.verifyGetResourceWithMappedRequestParamsNoInteraction(param1, param2)
        }
        assertThrows<VerificationException> { underTest.verifyGetResourceWithMappedRequestParamsNoInteractionWithUrl() }
    }

    @Test
    fun `should handle get mapping with path variable`() {
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
    fun `should handle get mapping with multiple path variables`() {
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
    fun `should handle get mapping with mapped path variables`() {
        underTest.verifyGetResourceWithMappedPathVariablesNoInteraction(param1, param2)
        underTest.verifyGetResourceWithMappedPathVariablesNoInteractionWithUrl(param1, param2)
        underTest.getResourceWithMappedPathVariables(greetingResponse, param1, param2)
        val response = restTemplate.getForEntity(
            "$GET_CONTROLLER_URL/resourceWithMappedPathVariables/$param1/$param2",
            GreetingResponse::class.java
        )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        assertThrows<VerificationException> {
            underTest.verifyGetResourceWithMappedPathVariablesNoInteraction(param1, param2)
        }
        assertThrows<VerificationException> {
            underTest.verifyGetResourceWithMappedPathVariablesNoInteractionWithUrl(param1, param2)
        }
    }

    @Test
    fun `should handle get mapping with multiple path variables and request params`() {
        underTest.verifyGetResourceWithMultiplePathVariablesAndRequestParamsNoInteraction(
            param1, param2, param3, param4
        )
        underTest.verifyGetResourceWithMultiplePathVariablesAndRequestParamsNoInteractionWithUrl(param1, param2)
        underTest.getResourceWithMultiplePathVariablesAndRequestParams(
            greetingResponse, param1, param2, param3, param4
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
                param1, param2, param3, param4
            )
        }
        assertThrows<VerificationException> {
            underTest.verifyGetResourceWithMultiplePathVariablesAndRequestParamsNoInteractionWithUrl(param1, param2)
        }
    }

    @Test
    fun `should handle get mapping with no subresource`() {
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

    @Test
    fun `should handle get mapping with request param set`() {
        underTest.verifyGetResourceWithParamSetNoInteraction(paramSet)
        underTest.getResourceWithParamSet(greetingResponse, paramSet)

        val response = restTemplate.exchange(
            "$GET_CONTROLLER_URL/resourceWithParamSet?paramSet=$param1&paramSet=$param2&paramSet=$param3&paramSet=$param4",
            GET, HttpEntity(mapOf<String, String>()), GreetingResponse::class.java
        )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithParamSet(1, paramSet)
        underTest.verifyGetResourceWithParamSet(paramSet)
        assertThrows<VerificationException> { underTest.verifyGetResourceWithParamSetNoInteraction(paramSet) }
    }

    @Test
    fun `should handle get mapping with request param list`() {
        underTest.verifyGetResourceWithParamListNoInteraction(paramLong, paramList, paramInt)
        underTest.getResourceWithParamList(greetingResponse, paramLong, paramList, paramInt)

        val response = restTemplate.exchange(
            "$GET_CONTROLLER_URL/resourceWithParamList?parameter1=$paramLong&param2=$param2&param2=$param3&parameter3=$paramInt",
            GET, HttpEntity(mapOf<String, String>()), GreetingResponse::class.java
        )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithParamList(1, paramLong, paramList, paramInt)
        underTest.verifyGetResourceWithParamList(paramLong, paramList, paramInt)
        assertThrows<VerificationException> { underTest.verifyGetResourceWithParamListNoInteraction(paramLong, paramList, paramInt) }
    }

    @Test
    fun `should handle get mapping with true boolean request param`() {
        underTest.verifyGetResourceWithBooleanRequestParamNoInteraction(true)
        underTest.verifyGetResourceWithBooleanRequestParamNoInteractionWithUrl()
        underTest.getResourceWithBooleanRequestParam(greetingResponse, true)

        val response = restTemplate.exchange(
            "$GET_CONTROLLER_URL/resourceWithBooleanRequestParam?param=true",
            GET, HttpEntity(mapOf<String, String>()), GreetingResponse::class.java
        )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        assertThrows<VerificationException> { underTest.verifyGetResourceWithBooleanRequestParamNoInteraction(true) }
        assertThrows<VerificationException> { underTest.verifyGetResourceWithBooleanRequestParamNoInteractionWithUrl() }
    }

    @Test
    fun `should handle get mapping with false boolean request param`() {
        underTest.verifyGetResourceWithBooleanRequestParamNoInteraction(false)
        underTest.verifyGetResourceWithBooleanRequestParamNoInteractionWithUrl()
        underTest.getResourceWithBooleanRequestParam(greetingResponse, false)

        val response = restTemplate.exchange(
            "$GET_CONTROLLER_URL/resourceWithBooleanRequestParam?param=false",
            GET, HttpEntity(mapOf<String, String>()), GreetingResponse::class.java
        )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        assertThrows<VerificationException> { underTest.verifyGetResourceWithBooleanRequestParamNoInteraction(false) }
        assertThrows<VerificationException> { underTest.verifyGetResourceWithBooleanRequestParamNoInteractionWithUrl() }
    }

    @Test
    fun `should handle get mapping with missing non-optional boolean request param`() {
        underTest.verifyGetResourceWithBooleanRequestParamNoInteraction(null)
        underTest.verifyGetResourceWithBooleanRequestParamNoInteractionWithUrl()
        underTest.getResourceWithBooleanRequestParam(greetingResponse, null)

        val response = restTemplate.exchange(
            "$GET_CONTROLLER_URL/resourceWithBooleanRequestParam?param=null",
            GET, HttpEntity(mapOf<String, String>()), GreetingResponse::class.java
        )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        assertThrows<VerificationException> { underTest.verifyGetResourceWithBooleanRequestParamNoInteraction(null) }
        assertThrows<VerificationException> { underTest.verifyGetResourceWithBooleanRequestParamNoInteractionWithUrl() }
    }

    @Test
    fun `should handle get mapping with optional empty boolean request param`() {
        underTest.verifyGetResourceWithOptionalBooleanRequestParamNoInteraction(null)
        underTest.verifyGetResourceWithOptionalBooleanRequestParamNoInteractionWithUrl()
        underTest.getResourceWithOptionalBooleanRequestParam(greetingResponse, null)

        val response = restTemplate.exchange(
            "$GET_CONTROLLER_URL/resourceWithOptionalBooleanRequestParam",
            GET, HttpEntity(mapOf<String, String>()), GreetingResponse::class.java
        )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        assertThrows<VerificationException> { underTest.verifyGetResourceWithOptionalBooleanRequestParamNoInteraction(null) }
        assertThrows<VerificationException> { underTest.verifyGetResourceWithOptionalBooleanRequestParamNoInteractionWithUrl() }
    }

    @Test
    fun `should handle get mapping with optional boolean request param`() {
        underTest.verifyGetResourceWithOptionalBooleanRequestParamNoInteraction(false)
        underTest.verifyGetResourceWithOptionalBooleanRequestParamNoInteractionWithUrl()
        underTest.getResourceWithOptionalBooleanRequestParam(greetingResponse, false)

        val response = restTemplate.exchange(
            "$GET_CONTROLLER_URL/resourceWithOptionalBooleanRequestParam?param=false",
            GET, HttpEntity(mapOf<String, String>()), GreetingResponse::class.java
        )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        assertThrows<VerificationException> { underTest.verifyGetResourceWithOptionalBooleanRequestParamNoInteraction(false) }
        assertThrows<VerificationException> { underTest.verifyGetResourceWithOptionalBooleanRequestParamNoInteractionWithUrl() }
    }

    @Test
    fun `should handle get mapping with empty optional int request params`() {
        underTest.verifyGetResourceWithOptionalIntRequestParamsNoInteraction(true, null, null, 11L)
        underTest.verifyGetResourceWithOptionalIntRequestParamsNoInteractionWithUrl()
        underTest.getResourceWithOptionalIntRequestParams(greetingResponse, true, null, null, 11L)

        val response = restTemplate.exchange(
            "$GET_CONTROLLER_URL/resourceWithOptionalIntRequestParams?param1=true&parameter4=11",
            GET, HttpEntity(mapOf<String, String>()), GreetingResponse::class.java
        )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        assertThrows<VerificationException> { underTest.verifyGetResourceWithOptionalIntRequestParamsNoInteraction(true, null, null, 11L) }
        assertThrows<VerificationException> { underTest.verifyGetResourceWithOptionalIntRequestParamsNoInteractionWithUrl() }
    }

    @Test
    fun `should handle get mapping with optional int request params`() {
        underTest.verifyGetResourceWithOptionalIntRequestParamsNoInteraction(true, 5, 7, 11L)
        underTest.verifyGetResourceWithOptionalIntRequestParamsNoInteractionWithUrl()
        underTest.getResourceWithOptionalIntRequestParams(greetingResponse, true, 5, 7, 11L)

        val response = restTemplate.exchange(
            "$GET_CONTROLLER_URL/resourceWithOptionalIntRequestParams?param1=true&param2=5&param3=7&parameter4=11",
            GET, HttpEntity(mapOf<String, String>()), GreetingResponse::class.java
        )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        assertThrows<VerificationException> { underTest.verifyGetResourceWithOptionalIntRequestParamsNoInteraction(true, 5, 7, 11L) }
        assertThrows<VerificationException> { underTest.verifyGetResourceWithOptionalIntRequestParamsNoInteractionWithUrl() }
    }

    @Test
    fun `should handle get mapping with optional and multi-value request params`() {
        underTest.verifyGetResourceWithOptionalMultiValueRequestParamsNoInteraction(true, 5, setOf(33, 44), setOf(11, 22))
        underTest.getResourceWithOptionalMultiValueRequestParams(greetingResponse, true, 5, setOf(33, 44), setOf(11, 22))

        val response = restTemplate.exchange(
            "$GET_CONTROLLER_URL/resourceWithOptionalMultiValueRequestParams?required=true&optional=5&multiValue=33&multiValue=44&parameter4=11&parameter4=22",
            GET, HttpEntity(mapOf<String, String>()), GreetingResponse::class.java
        )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        assertThrows<VerificationException> { underTest.verifyGetResourceWithOptionalMultiValueRequestParamsNoInteraction(true, 5, setOf(33, 44), setOf(11, 22)) }
    }

    @Test
    @Disabled
    fun `should handle get mapping with missing optional multi-value request params`() {
        underTest.verifyGetResourceWithOptionalMultiValueRequestParamsNoInteraction(true, null, setOf(33, 44), null)
        underTest.getResourceWithOptionalMultiValueRequestParams(greetingResponse, true, null, setOf(33, 44), null)

        val response = restTemplate.exchange(
            "$GET_CONTROLLER_URL/resourceWithOptionalMultiValueRequestParams?required=true&multiValue=33&multiValue=44",
            GET, HttpEntity(mapOf<String, String>()), GreetingResponse::class.java
        )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        assertThrows<VerificationException> { underTest.verifyGetResourceWithOptionalMultiValueRequestParamsNoInteraction(true, null, setOf(33, 44), null) }
    }
}
