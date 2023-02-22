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
        underTest.verifyPutResourceWithNoBodyNoInteraction()
        underTest.putResourceWithNoBody()
        val responseEntity = restTemplate.exchange("$PUT_CONTROLLER_URL/resourceWithNoBody", PUT, HttpEntity<String>(LinkedMultiValueMap()), Unit::class.java)
        assertThat(responseEntity.statusCode, `is`(ACCEPTED))
        underTest.verifyPutResourceWithNoBody(1)
        underTest.verifyPutResourceWithNoBody()
        assertThrows<VerificationException> { underTest.verifyPutResourceWithNoBodyNoInteraction() }
    }

    @Test
    fun `should handle put mapping with request body`() {
        underTest.verifyPutResourceWithRequestBodyNoInteraction(greetingRequest)
        underTest.putResourceWithRequestBody()
        val request = HttpEntity(greetingRequest)
        val responseEntity = restTemplate.exchange("$PUT_CONTROLLER_URL/resourceWithRequestBody", PUT, request, Unit::class.java)
        assertThat(responseEntity.body, `is`(nullValue()))
        assertThat(responseEntity.statusCode, `is`(NO_CONTENT))
        underTest.verifyPutResourceWithRequestBody(1, greetingRequest)
        underTest.verifyPutResourceWithRequestBody(greetingRequest)
        assertThrows<VerificationException> { underTest.verifyPutResourceWithRequestBodyNoInteraction(greetingRequest) }
    }

    @Test
    fun `should stub a put method with a mapping matching the request body`() {
        val additionalGreetingRequest = GreetingRequest(name = randomAlphabetic(10))
        underTest.verifyPutResourceWithRequestBodyNoInteraction(greetingRequest)
        underTest.putResourceWithRequestBody(greetingRequest)

        val responseEntity1 = restTemplate.exchange(
            "$PUT_CONTROLLER_URL/resourceWithRequestBody",
            PUT,
            HttpEntity(greetingRequest),
            Unit::class.java
        )
        assertThat(responseEntity1.body, `is`(nullValue()))
        assertThat(responseEntity1.statusCode, `is`(NO_CONTENT))

        val exception = assertThrows<HttpClientErrorException> { restTemplate.exchange("$PUT_CONTROLLER_URL/resourceWithRequestBody", PUT, HttpEntity(additionalGreetingRequest), Unit::class.java) }
        assertThat(exception.statusCode, `is`(NOT_FOUND))

        underTest.verifyPutResourceWithRequestBody(1, greetingRequest)
        underTest.verifyPutResourceWithRequestBody(greetingRequest)
        assertThrows<VerificationException> { underTest.verifyPutResourceWithRequestBodyNoInteraction(greetingRequest) }
    }

    @Test
    fun `should handle put mapping with request body and path variable`() {
        underTest.verifyPutResourceWithRequestBodyAndPathVariableNoInteraction(param, greetingRequest)
        underTest.putResourceWithRequestBodyAndPathVariable(param)
        val request = HttpEntity(greetingRequest)
        val responseEntity = restTemplate.exchange(
            "$PUT_CONTROLLER_URL/resourceWithRequestBodyAndPathVariable/$param", PUT, request, Unit::class.java
        )
        assertThat(responseEntity.body, `is`(nullValue()))
        assertThat(responseEntity.statusCode, `is`(NO_CONTENT))
        underTest.verifyPutResourceWithRequestBodyAndPathVariable(1, param, greetingRequest)
        underTest.verifyPutResourceWithRequestBodyAndPathVariable(param, greetingRequest)
        assertThrows<VerificationException> {
            underTest.verifyPutResourceWithRequestBodyAndPathVariableNoInteraction(
                param,
                greetingRequest
            )
        }
    }
}
