package com.lsdconsulting.stub.integration.post

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.VerificationException
import com.lsdconsulting.stub.integration.BaseRestControllerIT
import com.lsdconsulting.stub.integration.POST_CONTROLLER_URL
import com.lsdconsulting.stub.integration.controller.post.AdvancedPostRestControllerStub
import com.lsdconsulting.stub.integration.model.GreetingRequest
import com.lsdconsulting.stub.integration.model.GreetingResponse
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.client.HttpClientErrorException

class AdvancedPostRestControllerIT : BaseRestControllerIT() {
    private val underTest = AdvancedPostRestControllerStub(ObjectMapper())

    @Test
    fun `should handle post mapping with request body`() {
        underTest.verifyPostResourceWithBodyNoInteraction(greetingRequest)
        underTest.verifyPostResourceWithBodyNoInteractionWithUrl()
        underTest.postResourceWithBody(greetingResponse)
        val request = HttpEntity(greetingRequest)
        val response =
            restTemplate.postForEntity("$POST_CONTROLLER_URL/resourceWithBody", request, GreetingResponse::class.java)
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyPostResourceWithBody(1, greetingRequest)
        underTest.verifyPostResourceWithBody(greetingRequest)
        underTest.verifyPostResourceWithBodyAndPathVariableNoInteraction(param, greetingRequest)
        assertThrows<VerificationException> { underTest.verifyPostResourceWithBodyNoInteraction(greetingRequest) }
        assertThrows<VerificationException> { underTest.verifyPostResourceWithBodyNoInteractionWithUrl() }
    }

    @Test
    fun `should stub a post method with a mapping matching the request body`() {
        val additionalGreetingRequest = GreetingRequest(name = randomAlphabetic(10))
        underTest.verifyPostResourceWithBodyNoInteraction(greetingRequest)
        underTest.verifyPostResourceWithBodyNoInteraction(additionalGreetingRequest)
        underTest.verifyPostResourceWithBodyNoInteractionWithUrl()
        underTest.postResourceWithBody(greetingRequest, greetingResponse)

        val response1 = restTemplate.postForEntity(
            "$POST_CONTROLLER_URL/resourceWithBody",
            HttpEntity(greetingRequest),
            GreetingResponse::class.java
        )
        assertThat(response1.body, notNullValue())
        assertThat(response1.body?.name, `is`(name))

        val exception = assertThrows<HttpClientErrorException> {
            restTemplate.postForEntity(
                "$POST_CONTROLLER_URL/resourceWithBody",
                HttpEntity(additionalGreetingRequest),
                GreetingResponse::class.java
            )
        }
        assertThat(exception.statusCode, `is`(NOT_FOUND))


        underTest.verifyPostResourceWithBody(1, greetingRequest)
        underTest.verifyPostResourceWithBody(1, additionalGreetingRequest)
        underTest.verifyPostResourceWithBody(greetingRequest)
        underTest.verifyPostResourceWithBody(additionalGreetingRequest)
    }

    @Test
    fun `should handle post mapping with request body and path variable`() {
        underTest.verifyPostResourceWithBodyAndPathVariableNoInteraction(param, greetingRequest)
        underTest.verifyPostResourceWithBodyAndPathVariableNoInteractionWithUrl(param)
        underTest.postResourceWithBodyAndPathVariable(GreetingResponse(name = name), param)
        val request = HttpEntity(greetingRequest)
        val response = restTemplate.postForEntity(
            "$POST_CONTROLLER_URL/resourceWithBody/$param",
            request,
            GreetingResponse::class.java
        )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyPostResourceWithBodyAndPathVariable(1, param, greetingRequest)
        underTest.verifyPostResourceWithBodyAndPathVariable(param, greetingRequest)
        underTest.verifyPostResourceWithBodyNoInteraction(greetingRequest)
        assertThrows<VerificationException> {
            underTest.verifyPostResourceWithBodyAndPathVariableNoInteraction(param, greetingRequest)
        }
        assertThrows<VerificationException> {
            underTest.verifyPostResourceWithBodyAndPathVariableNoInteractionWithUrl(param)
        }
    }

    @Test
    fun `should handle post mapping with request body and multiple path variable`() {
        underTest.verifyPostResourceWithBodyAndMultiplePathVariablesNoInteraction(param1, param2, greetingRequest)
        underTest.verifyPostResourceWithBodyAndMultiplePathVariablesNoInteractionWithUrl(param1, param2)
        underTest.postResourceWithBodyAndMultiplePathVariables(GreetingResponse(name = name), param1, param2)
        val request = HttpEntity(greetingRequest)
        val response = restTemplate.postForEntity(
            "$POST_CONTROLLER_URL/resourceWithBodyAndMultiplePathVariables/$param1/$param2",
            request,
            GreetingResponse::class.java
        )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyPostResourceWithBodyAndMultiplePathVariables(1, param1, param2, greetingRequest)
        underTest.verifyPostResourceWithBodyAndMultiplePathVariables(param1, param2, greetingRequest)
        assertThrows<VerificationException> {
            underTest.verifyPostResourceWithBodyAndMultiplePathVariablesNoInteraction(param1, param2, greetingRequest)
        }
        assertThrows<VerificationException> {
            underTest.verifyPostResourceWithBodyAndMultiplePathVariablesNoInteractionWithUrl(param1, param2)
        }
    }
}
