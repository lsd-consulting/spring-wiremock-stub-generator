package com.lsdconsulting.stub.integration.put

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.VerificationException
import com.lsdconsulting.stub.integration.BaseRestControllerIT
import com.lsdconsulting.stub.integration.PUT_CONTROLLER_URL
import com.lsdconsulting.stub.integration.controller.put.PutRestControllerStub
import com.lsdconsulting.stub.integration.model.GreetingRequest
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.nullValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod.PUT
import org.springframework.http.HttpStatus.*
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.HttpClientErrorException

class PutRestControllerIT : BaseRestControllerIT() {
    private val underTest = PutRestControllerStub(ObjectMapper())

    @Test
    fun `should handle put mapping with no body`() {
        underTest.verifyResourceWithNoBodyNoInteraction()
        underTest.resourceWithNoBody()

        val responseEntity = restTemplate.exchange(
            "$PUT_CONTROLLER_URL/resourceWithNoBody",
            PUT,
            HttpEntity<String>(LinkedMultiValueMap()),
            Unit::class.java
        )

        assertThat(responseEntity.statusCode, `is`(ACCEPTED))
        underTest.verifyResourceWithNoBody(1)
        underTest.verifyResourceWithNoBody()
        assertThrows<VerificationException> { underTest.verifyResourceWithNoBodyNoInteraction() }
    }

    @Test
    fun `should handle put mapping with request body`() {
        underTest.verifyResourceWithRequestBodyNoInteraction(greetingRequest)
        underTest.resourceWithRequestBody()

        val responseEntity =
            restTemplate.exchange(
                "$PUT_CONTROLLER_URL/resourceWithRequestBody",
                PUT,
                HttpEntity(greetingRequest),
                Unit::class.java
            )

        assertThat(responseEntity.body, `is`(nullValue()))
        assertThat(responseEntity.statusCode, `is`(NO_CONTENT))
        underTest.verifyResourceWithRequestBody(1, greetingRequest)
        underTest.verifyResourceWithRequestBody(greetingRequest)
        assertThrows<VerificationException> { underTest.verifyResourceWithRequestBodyNoInteraction(greetingRequest) }
    }

    @Test
    fun `should stub a put method with a mapping matching the request body`() {
        val additionalGreetingRequest = GreetingRequest(name = randomAlphabetic(10))
        underTest.verifyResourceWithRequestBodyNoInteraction(greetingRequest)
        underTest.resourceWithRequestBody(greetingRequest)

        val responseEntity1 = restTemplate.exchange(
            "$PUT_CONTROLLER_URL/resourceWithRequestBody",
            PUT,
            HttpEntity(greetingRequest),
            Unit::class.java
        )

        assertThat(responseEntity1.body, `is`(nullValue()))
        assertThat(responseEntity1.statusCode, `is`(NO_CONTENT))
        val exception = assertThrows<HttpClientErrorException> {
            restTemplate.exchange(
                "$PUT_CONTROLLER_URL/resourceWithRequestBody",
                PUT,
                HttpEntity(additionalGreetingRequest),
                Unit::class.java
            )
        }
        assertThat(exception.statusCode, `is`(NOT_FOUND))

        underTest.verifyResourceWithRequestBody(1, greetingRequest)
        underTest.verifyResourceWithRequestBody(greetingRequest)
        assertThrows<VerificationException> { underTest.verifyResourceWithRequestBodyNoInteraction(greetingRequest) }
    }

    @Test
    fun `should handle put mapping with request body and path variable`() {
        underTest.verifyResourceWithRequestBodyAndPathVariableNoInteraction(param, greetingRequest)
        underTest.resourceWithRequestBodyAndPathVariable(param)

        val responseEntity = restTemplate.exchange(
            "$PUT_CONTROLLER_URL/resourceWithRequestBodyAndPathVariable/$param",
            PUT,
            HttpEntity(greetingRequest),
            Unit::class.java
        )

        assertThat(responseEntity.body, `is`(nullValue()))
        assertThat(responseEntity.statusCode, `is`(NO_CONTENT))
        underTest.verifyResourceWithRequestBodyAndPathVariable(1, param, greetingRequest)
        underTest.verifyResourceWithRequestBodyAndPathVariable(param, greetingRequest)
        assertThrows<VerificationException> {
            underTest.verifyResourceWithRequestBodyAndPathVariableNoInteraction(
                param,
                greetingRequest
            )
        }
    }
}
