package com.lsdconsulting.stub.integration.post

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.VerificationException
import com.lsdconsulting.stub.integration.BaseRestControllerIT
import com.lsdconsulting.stub.integration.POST_CONTROLLER_URL
import com.lsdconsulting.stub.integration.controller.post.AdvancedPostRestControllerStub
import com.lsdconsulting.stub.integration.model.GreetingRequest
import com.lsdconsulting.stub.integration.model.GreetingResponse
import org.apache.commons.lang3.RandomStringUtils.secure
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
        underTest.verifyResourceWithBodyNoInteraction(greetingRequest)
        underTest.verifyResourceWithBodyNoInteraction()
        underTest.resourceWithBody(greetingResponse)

        val response =
            restTemplate.postForEntity(
                "$POST_CONTROLLER_URL/resourceWithBody",
                HttpEntity(greetingRequest),
                GreetingResponse::class.java
            )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyResourceWithBody(1, greetingRequest)
        underTest.verifyResourceWithBody(greetingRequest)
        underTest.verifyResourceWithBodyAndPathVariableNoInteraction(param, greetingRequest)
        assertThrows<VerificationException> { underTest.verifyResourceWithBodyNoInteraction(greetingRequest) }
        assertThrows<VerificationException> { underTest.verifyResourceWithBodyNoInteraction() }
    }

    @Test
    fun `should stub a post method with a mapping matching the request body`() {
        val additionalGreetingRequest = GreetingRequest(name = secure().nextAlphabetic(10))
        underTest.verifyResourceWithBodyNoInteraction(greetingRequest)
        underTest.verifyResourceWithBodyNoInteraction(additionalGreetingRequest)
        underTest.verifyResourceWithBodyNoInteraction()
        underTest.resourceWithBody(greetingRequest, greetingResponse)

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
        underTest.verifyResourceWithBody(1, greetingRequest)
        underTest.verifyResourceWithBody(1, additionalGreetingRequest)
        underTest.verifyResourceWithBody(greetingRequest)
        underTest.verifyResourceWithBody(additionalGreetingRequest)
    }

    @Test
    fun `should handle post mapping with request body and path variable`() {
        underTest.verifyResourceWithBodyAndPathVariableNoInteraction(param, greetingRequest)
        underTest.verifyResourceWithBodyAndPathVariableNoInteraction(param)
        underTest.resourceWithBodyAndPathVariable(GreetingResponse(name = name), param)

        val response = restTemplate.postForEntity(
            "$POST_CONTROLLER_URL/resourceWithBody/$param",
            HttpEntity(greetingRequest),
            GreetingResponse::class.java
        )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyResourceWithBodyAndPathVariable(1, param, greetingRequest)
        underTest.verifyResourceWithBodyAndPathVariable(param, greetingRequest)
        underTest.verifyResourceWithBodyNoInteraction(greetingRequest)
        assertThrows<VerificationException> {
            underTest.verifyResourceWithBodyAndPathVariableNoInteraction(param, greetingRequest)
        }
        assertThrows<VerificationException> {
            underTest.verifyResourceWithBodyAndPathVariableNoInteraction(param)
        }
    }

    @Test
    fun `should handle post mapping with request body and multiple path variable`() {
        underTest.verifyResourceWithBodyAndMultiplePathVariablesNoInteraction(param1, param2, greetingRequest)
        underTest.verifyResourceWithBodyAndMultiplePathVariablesNoInteraction(param1, param2)
        underTest.resourceWithBodyAndMultiplePathVariables(GreetingResponse(name = name), param1, param2)

        val response = restTemplate.postForEntity(
            "$POST_CONTROLLER_URL/resourceWithBodyAndMultiplePathVariables/$param1/$param2",
            HttpEntity(greetingRequest),
            GreetingResponse::class.java
        )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyResourceWithBodyAndMultiplePathVariables(1, param1, param2, greetingRequest)
        underTest.verifyResourceWithBodyAndMultiplePathVariables(param1, param2, greetingRequest)
        assertThrows<VerificationException> {
            underTest.verifyResourceWithBodyAndMultiplePathVariablesNoInteraction(param1, param2, greetingRequest)
        }
        assertThrows<VerificationException> {
            underTest.verifyResourceWithBodyAndMultiplePathVariablesNoInteraction(param1, param2)
        }
    }
}
