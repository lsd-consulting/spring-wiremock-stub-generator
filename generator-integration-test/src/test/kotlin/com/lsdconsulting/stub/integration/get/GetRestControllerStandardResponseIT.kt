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
        underTest.verifyResourceWithNoParamsNoInteraction()
        underTest.verifyResourceWithNoParamsNoInteractionWithUrl()
        underTest.resourceWithNoParams(greetingResponse)
        val response =
            restTemplate.getForEntity("$GET_CONTROLLER_URL/resourceWithNoParams", GreetingResponse::class.java)
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyResourceWithNoParams(1)
        underTest.verifyResourceWithNoParams()
        assertThrows<VerificationException> { underTest.verifyResourceWithNoParamsNoInteraction() }
        assertThrows<VerificationException> { underTest.verifyResourceWithNoParamsNoInteractionWithUrl() }
    }

    @Test
    fun `should handle get mapping with request param`() {
        underTest.verifyResourceWithParamNoInteraction(param)
        underTest.verifyResourceWithParamNoInteractionWithUrl()
        underTest.resourceWithParam(greetingResponse, param)
        val response = restTemplate.getForEntity(
            "$GET_CONTROLLER_URL/resourceWithParam?param=$param",
            GreetingResponse::class.java
        )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyResourceWithParam(1, param)
        underTest.verifyResourceWithParam(param)
        assertThrows<VerificationException> { underTest.verifyResourceWithParamNoInteraction(param) }
        assertThrows<VerificationException> { underTest.verifyResourceWithParamNoInteractionWithUrl() }
    }

    @Test
    fun `should handle collection response for filter queries`() {
        underTest.verifyFilteredResourceWithParamNoInteraction(param)
        underTest.verifyFilteredResourceWithParamNoInteractionWithUrl()
        underTest.filteredResourceWithParam(listOf(greetingResponse), param)
        val response = restTemplate.exchange(
            "$GET_CONTROLLER_URL/filteredResourceWithParam?param=$param", GET, EMPTY,
            object: ParameterizedTypeReference<List<GreetingResponse>>(){}
        )
        assertThat(response.body, notNullValue())
        assertThat(response.body, hasSize(1))
        assertThat(response.body?.get(0)?.name, `is`(name))
        assertThrows<VerificationException> { underTest.verifyFilteredResourceWithParamNoInteraction(param) }
        assertThrows<VerificationException> { underTest.verifyFilteredResourceWithParamNoInteractionWithUrl() }
    }

    @Test
    fun `should handle get mapping with multiple request params`() {
        underTest.verifyResourceWithMultipleParamsNoInteraction(param1, param2)
        underTest.verifyResourceWithMultipleParamsNoInteractionWithUrl()
        underTest.resourceWithMultipleParams(greetingResponse, param1, param2)
        val response = restTemplate.getForEntity(
            "$GET_CONTROLLER_URL/resourceWithMultipleParams?param1=$param1&param2=$param2",
            GreetingResponse::class.java
        )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyResourceWithMultipleParams(1, param1, param2)
        underTest.verifyResourceWithMultipleParams(param1, param2)
        assertThrows<VerificationException> {
            underTest.verifyResourceWithMultipleParamsNoInteraction(param1, param2)
        }
        assertThrows<VerificationException> { underTest.verifyResourceWithMultipleParamsNoInteractionWithUrl() }
    }

    @Test
    fun `should handle get mapping with mapped request params`() {
        underTest.verifyResourceWithMappedRequestParamsNoInteraction(param1, param2)
        underTest.verifyResourceWithMappedRequestParamsNoInteractionWithUrl()
        underTest.resourceWithMappedRequestParams(greetingResponse, param1, param2)
        val response = restTemplate.getForEntity(
            "$GET_CONTROLLER_URL/resourceWithMappedRequestParams?parameter1=$param1&parameter2=$param2",
            GreetingResponse::class.java
        )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        assertThrows<VerificationException> {
            underTest.verifyResourceWithMappedRequestParamsNoInteraction(param1, param2)
        }
        assertThrows<VerificationException> { underTest.verifyResourceWithMappedRequestParamsNoInteractionWithUrl() }
    }

    @Test
    fun `should handle get mapping with path variable`() {
        underTest.verifyResourceWithPathVariableNoInteraction(param)
        underTest.verifyResourceWithPathVariableNoInteractionWithUrl(param)
        underTest.resourceWithPathVariable(greetingResponse, param)
        val response =
            restTemplate.getForEntity("$GET_CONTROLLER_URL/resourceWithParam/$param", GreetingResponse::class.java)
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyResourceWithPathVariable(1, param)
        underTest.verifyResourceWithPathVariable(param)
        assertThrows<VerificationException> { underTest.verifyResourceWithPathVariableNoInteraction(param) }
        assertThrows<VerificationException> { underTest.verifyResourceWithPathVariableNoInteractionWithUrl(param) }
    }

    @Test
    fun `should handle get mapping with multiple path variables`() {
        underTest.verifyResourceWithMultiplePathVariablesNoInteraction(param1, param2)
        underTest.verifyResourceWithMultiplePathVariablesNoInteractionWithUrl(param1, param2)
        underTest.resourceWithMultiplePathVariables(greetingResponse, param1, param2)
        val response = restTemplate.getForEntity(
            "$GET_CONTROLLER_URL/resourceWithParam/$param1/$param2",
            GreetingResponse::class.java
        )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyResourceWithMultiplePathVariables(1, param1, param2)
        underTest.verifyResourceWithMultiplePathVariables(param1, param2)
        assertThrows<VerificationException> {
            underTest.verifyResourceWithMultiplePathVariablesNoInteraction(param1, param2)
        }
        assertThrows<VerificationException> {
            underTest.verifyResourceWithMultiplePathVariablesNoInteractionWithUrl(param1, param2)
        }
    }

    @Test
    fun `should handle get mapping with path variable and request param`() {
        underTest.verifyResourceWithPathVariableAndRequestParamNoInteraction(param1, param2)
        underTest.verifyResourceWithPathVariableAndRequestParamNoInteractionWithUrl(param1)
        underTest.resourceWithPathVariableAndRequestParam(greetingResponse, param1, param2)
        val response = restTemplate.getForEntity(
            "$GET_CONTROLLER_URL/resourceWithParam/$param1?param2=$param2",
            GreetingResponse::class.java
        )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyResourceWithPathVariableAndRequestParam(1, param1, param2)
        underTest.verifyResourceWithPathVariableAndRequestParam(param1, param2)
        assertThrows<VerificationException> {
            underTest.verifyResourceWithPathVariableAndRequestParamNoInteraction(param1, param2)
        }
        assertThrows<VerificationException> {
            underTest.verifyResourceWithPathVariableAndRequestParamNoInteractionWithUrl(param1)
        }
    }

    @Test
    fun `should handle get mapping with mapped path variables`() {
        underTest.verifyResourceWithMappedPathVariablesNoInteraction(param1, param2)
        underTest.verifyResourceWithMappedPathVariablesNoInteractionWithUrl(param1, param2)
        underTest.resourceWithMappedPathVariables(greetingResponse, param1, param2)
        val response = restTemplate.getForEntity(
            "$GET_CONTROLLER_URL/resourceWithMappedPathVariables/$param1/$param2",
            GreetingResponse::class.java
        )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        assertThrows<VerificationException> {
            underTest.verifyResourceWithMappedPathVariablesNoInteraction(param1, param2)
        }
        assertThrows<VerificationException> {
            underTest.verifyResourceWithMappedPathVariablesNoInteractionWithUrl(param1, param2)
        }
    }

    @Test
    fun `should handle get mapping with multiple path variables and request params`() {
        underTest.verifyResourceWithMultiplePathVariablesAndRequestParamsNoInteraction(
            param1, param2, param3, param4
        )
        underTest.verifyResourceWithMultiplePathVariablesAndRequestParamsNoInteractionWithUrl(param1, param2)
        underTest.resourceWithMultiplePathVariablesAndRequestParams(
            greetingResponse, param1, param2, param3, param4
        )
        val response = restTemplate.getForEntity(
            "$GET_CONTROLLER_URL/resourceWithParam/$param1/$param2?param3=$param3&param4=$param4",
            GreetingResponse::class.java
        )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyResourceWithMultiplePathVariablesAndRequestParams(1, param1, param2, param3, param4)
        underTest.verifyResourceWithMultiplePathVariablesAndRequestParams(param1, param2, param3, param4)
        assertThrows<VerificationException> {
            underTest.verifyResourceWithMultiplePathVariablesAndRequestParamsNoInteraction(
                param1, param2, param3, param4
            )
        }
        assertThrows<VerificationException> {
            underTest.verifyResourceWithMultiplePathVariablesAndRequestParamsNoInteractionWithUrl(param1, param2)
        }
    }

    @Test
    fun `should handle get mapping with no subresource`() {
        underTest.verifyResourceWithNoSubResourceNoInteraction()
        underTest.verifyResourceWithNoSubResourceNoInteractionWithUrl()
        underTest.resourceWithNoSubResource(greetingResponse)
        val response = restTemplate.getForEntity(GET_CONTROLLER_URL, GreetingResponse::class.java)
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyResourceWithNoSubResource(1)
        underTest.verifyResourceWithNoSubResource()
        assertThrows<VerificationException> {
            underTest.verifyResourceWithNoSubResourceNoInteraction()
            underTest.verifyResourceWithNoSubResourceNoInteractionWithUrl()
        }
    }

    @Test
    fun `should handle get mapping with request param set`() {
        underTest.verifyResourceWithParamSetNoInteraction(paramSet)
        underTest.resourceWithParamSet(greetingResponse, paramSet)

        val response = restTemplate.exchange(
            "$GET_CONTROLLER_URL/resourceWithParamSet?paramSet=$param1&paramSet=$param2&paramSet=$param3&paramSet=$param4",
            GET, HttpEntity(mapOf<String, String>()), GreetingResponse::class.java
        )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyResourceWithParamSet(1, paramSet)
        underTest.verifyResourceWithParamSet(paramSet)
        assertThrows<VerificationException> { underTest.verifyResourceWithParamSetNoInteraction(paramSet) }
    }

    @Test
    fun `should handle get mapping with request param list`() {
        underTest.verifyResourceWithParamListNoInteraction(paramLong, paramList, paramInt)
        underTest.resourceWithParamList(greetingResponse, paramLong, paramList, paramInt)

        val response = restTemplate.exchange(
            "$GET_CONTROLLER_URL/resourceWithParamList?parameter1=$paramLong&param2=$param2&param2=$param3&parameter3=$paramInt",
            GET, HttpEntity(mapOf<String, String>()), GreetingResponse::class.java
        )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyResourceWithParamList(1, paramLong, paramList, paramInt)
        underTest.verifyResourceWithParamList(paramLong, paramList, paramInt)
        assertThrows<VerificationException> { underTest.verifyResourceWithParamListNoInteraction(paramLong, paramList, paramInt) }
    }

    @Test
    fun `should handle get mapping with true boolean request param`() {
        underTest.verifyResourceWithBooleanRequestParamNoInteraction(true)
        underTest.verifyResourceWithBooleanRequestParamNoInteractionWithUrl()
        underTest.resourceWithBooleanRequestParam(greetingResponse, true)

        val response = restTemplate.exchange(
            "$GET_CONTROLLER_URL/resourceWithBooleanRequestParam?param=true",
            GET, HttpEntity(mapOf<String, String>()), GreetingResponse::class.java
        )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyResourceWithBooleanRequestParam(1,true)
        underTest.verifyResourceWithBooleanRequestParam(true)
        assertThrows<VerificationException> { underTest.verifyResourceWithBooleanRequestParamNoInteraction(true) }
        assertThrows<VerificationException> { underTest.verifyResourceWithBooleanRequestParamNoInteractionWithUrl() }
    }

    @Test
    fun `should handle get mapping with false boolean request param`() {
        underTest.verifyResourceWithBooleanRequestParamNoInteraction(false)
        underTest.verifyResourceWithBooleanRequestParamNoInteractionWithUrl()
        underTest.resourceWithBooleanRequestParam(greetingResponse, false)

        val response = restTemplate.exchange(
            "$GET_CONTROLLER_URL/resourceWithBooleanRequestParam?param=false",
            GET, HttpEntity(mapOf<String, String>()), GreetingResponse::class.java
        )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyResourceWithBooleanRequestParam(1,false)
        underTest.verifyResourceWithBooleanRequestParam(false)
        assertThrows<VerificationException> { underTest.verifyResourceWithBooleanRequestParamNoInteraction(false) }
        assertThrows<VerificationException> { underTest.verifyResourceWithBooleanRequestParamNoInteractionWithUrl() }
    }

    @Test
    fun `should handle get mapping with missing non-optional boolean request param`() {
        underTest.verifyResourceWithBooleanRequestParamNoInteraction(null)
        underTest.verifyResourceWithBooleanRequestParamNoInteractionWithUrl()
        underTest.resourceWithBooleanRequestParam(greetingResponse, null)

        val response = restTemplate.exchange(
            "$GET_CONTROLLER_URL/resourceWithBooleanRequestParam?param=null",
            GET, HttpEntity(mapOf<String, String>()), GreetingResponse::class.java
        )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyResourceWithBooleanRequestParam(1,null)
        underTest.verifyResourceWithBooleanRequestParam(null)
        assertThrows<VerificationException> { underTest.verifyResourceWithBooleanRequestParamNoInteraction(null) }
        assertThrows<VerificationException> { underTest.verifyResourceWithBooleanRequestParamNoInteractionWithUrl() }
    }

    @Test
    fun `should handle get mapping with optional empty boolean request param`() {
        underTest.verifyResourceWithOptionalBooleanRequestParamNoInteraction(null)
        underTest.verifyResourceWithOptionalBooleanRequestParamNoInteractionWithUrl()
        underTest.resourceWithOptionalBooleanRequestParam(greetingResponse, null)

        val response = restTemplate.exchange(
            "$GET_CONTROLLER_URL/resourceWithOptionalBooleanRequestParam",
            GET, HttpEntity(mapOf<String, String>()), GreetingResponse::class.java
        )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyResourceWithOptionalBooleanRequestParam(1, null)
        underTest.verifyResourceWithOptionalBooleanRequestParam(null)
        assertThrows<VerificationException> { underTest.verifyResourceWithOptionalBooleanRequestParamNoInteraction(null) }
        assertThrows<VerificationException> { underTest.verifyResourceWithOptionalBooleanRequestParamNoInteractionWithUrl() }
    }

    @Test
    fun `should handle get mapping with optional boolean request param`() {
        underTest.verifyResourceWithOptionalBooleanRequestParamNoInteraction(false)
        underTest.verifyResourceWithOptionalBooleanRequestParamNoInteractionWithUrl()
        underTest.resourceWithOptionalBooleanRequestParam(greetingResponse, false)

        val response = restTemplate.exchange(
            "$GET_CONTROLLER_URL/resourceWithOptionalBooleanRequestParam?param=false",
            GET, HttpEntity(mapOf<String, String>()), GreetingResponse::class.java
        )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyResourceWithOptionalBooleanRequestParam(1, false)
        underTest.verifyResourceWithOptionalBooleanRequestParam(false)
        assertThrows<VerificationException> { underTest.verifyResourceWithOptionalBooleanRequestParamNoInteraction(false) }
        assertThrows<VerificationException> { underTest.verifyResourceWithOptionalBooleanRequestParamNoInteractionWithUrl() }
    }

    @Test
    fun `should handle get mapping with empty optional int request params`() {
        underTest.verifyResourceWithOptionalIntRequestParamsNoInteraction(true, null, null, 11L)
        underTest.verifyResourceWithOptionalIntRequestParamsNoInteractionWithUrl()
        underTest.resourceWithOptionalIntRequestParams(greetingResponse, true, null, null, 11L)

        val response = restTemplate.exchange(
            "$GET_CONTROLLER_URL/resourceWithOptionalIntRequestParams?param1=true&parameter4=11",
            GET, HttpEntity(mapOf<String, String>()), GreetingResponse::class.java
        )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyResourceWithOptionalIntRequestParams(1, true, null, null, 11L)
        underTest.verifyResourceWithOptionalIntRequestParams(true, null, null, 11L)
        assertThrows<VerificationException> { underTest.verifyResourceWithOptionalIntRequestParamsNoInteraction(true, null, null, 11L) }
        assertThrows<VerificationException> { underTest.verifyResourceWithOptionalIntRequestParamsNoInteractionWithUrl() }
    }

    @Test
    fun `should handle get mapping with optional int request params`() {
        underTest.verifyResourceWithOptionalIntRequestParamsNoInteraction(true, 5, 7, 11L)
        underTest.verifyResourceWithOptionalIntRequestParamsNoInteractionWithUrl()
        underTest.resourceWithOptionalIntRequestParams(greetingResponse, true, 5, 7, 11L)

        val response = restTemplate.exchange(
            "$GET_CONTROLLER_URL/resourceWithOptionalIntRequestParams?param1=true&param2=5&param3=7&parameter4=11",
            GET, HttpEntity(mapOf<String, String>()), GreetingResponse::class.java
        )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyResourceWithOptionalIntRequestParams(1, true, 5, 7, 11L)
        underTest.verifyResourceWithOptionalIntRequestParams(true, 5, 7, 11L)
        assertThrows<VerificationException> { underTest.verifyResourceWithOptionalIntRequestParamsNoInteraction(true, 5, 7, 11L) }
        assertThrows<VerificationException> { underTest.verifyResourceWithOptionalIntRequestParamsNoInteractionWithUrl() }
    }

    @Test
    fun `should handle get mapping with optional and multi-value request params`() {
        underTest.verifyResourceWithOptionalMultiValueRequestParamsNoInteraction(true, 5, setOf(33, 44), setOf(11, 22))
        underTest.resourceWithOptionalMultiValueRequestParams(greetingResponse, true, 5, setOf(33, 44), setOf(11, 22))

        val response = restTemplate.exchange(
            "$GET_CONTROLLER_URL/resourceWithOptionalMultiValueRequestParams?required=true&optional=5&multiValue=33&multiValue=44&parameter4=11&parameter4=22",
            GET, HttpEntity(mapOf<String, String>()), GreetingResponse::class.java
        )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyResourceWithOptionalMultiValueRequestParams(1, true, 5, setOf(33, 44), setOf(11, 22))
        underTest.verifyResourceWithOptionalMultiValueRequestParams(true, 5, setOf(33, 44), setOf(11, 22))
        assertThrows<VerificationException> { underTest.verifyResourceWithOptionalMultiValueRequestParamsNoInteraction(true, 5, setOf(33, 44), setOf(11, 22)) }
    }

    @Test
    @Disabled
    fun `should handle get mapping with missing optional multi-value request params`() {
        underTest.verifyResourceWithOptionalMultiValueRequestParamsNoInteraction(true, null, setOf(33, 44), null)
        underTest.resourceWithOptionalMultiValueRequestParams(greetingResponse, true, null, setOf(33, 44), null)

        val response = restTemplate.exchange(
            "$GET_CONTROLLER_URL/resourceWithOptionalMultiValueRequestParams?required=true&multiValue=33&multiValue=44",
            GET, HttpEntity(mapOf<String, String>()), GreetingResponse::class.java
        )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyResourceWithOptionalMultiValueRequestParams(1, true, null, setOf(33, 44), null)
        underTest.verifyResourceWithOptionalMultiValueRequestParams(true, null, setOf(33, 44), null)
        assertThrows<VerificationException> { underTest.verifyResourceWithOptionalMultiValueRequestParamsNoInteraction(true, null, setOf(33, 44), null) }
    }
}
