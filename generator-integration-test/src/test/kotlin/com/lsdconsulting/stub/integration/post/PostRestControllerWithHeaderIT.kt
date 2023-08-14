package com.lsdconsulting.stub.integration.post

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.VerificationException
import com.lsdconsulting.stub.integration.BaseRestControllerIT
import com.lsdconsulting.stub.integration.POST_CONTROLLER_URL
import com.lsdconsulting.stub.integration.controller.post.PostRestControllerWithHeaderStub
import com.lsdconsulting.stub.integration.model.GreetingResponse
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import java.nio.charset.StandardCharsets.UTF_8
import java.util.*

class PostRestControllerWithHeaderIT : BaseRestControllerIT() {
    private val underTest = PostRestControllerWithHeaderStub(ObjectMapper())
    private val token = Base64.getEncoder().encodeToString(randomAlphabetic(10).toByteArray(UTF_8))
    private val bearerAuthorizationHeaderValue = "Bearer $token"
    private val customHeaderName = "custom_header"
    private val customHeaderValue = "custom_header_value"

    @Test
    fun `should handle post mapping with no request body and no response but with request header`() {
        underTest.verifyResourceWithNoBodyNoResponseWithMappedHeaderNoInteraction()
        underTest.verifyResourceWithNoBodyNoResponseWithMappedHeaderNoInteraction(bearerAuthorizationHeaderValue)
        underTest.resourceWithNoBodyNoResponseWithMappedHeader()

        val headers = HttpHeaders()
        headers.setBearerAuth(token)
        val response = restTemplate.postForEntity(
            "$POST_CONTROLLER_URL/resourceWithNoBodyNoResponseWithMappedHeader",
            HttpEntity("", headers),
            Unit::class.java
        )

        assertThat(response.statusCode, Matchers.`is`(HttpStatus.OK))
        underTest.verifyResourceWithNoBodyNoResponseWithMappedHeader(bearerAuthorizationHeaderValue)
        underTest.verifyResourceWithNoBodyNoResponseWithMappedHeader(1, bearerAuthorizationHeaderValue)
        assertThrows<VerificationException> { underTest.verifyResourceWithNoBodyNoResponseWithMappedHeaderNoInteraction() }
        assertThrows<VerificationException> {
            underTest.verifyResourceWithNoBodyNoResponseWithMappedHeaderNoInteraction(
                bearerAuthorizationHeaderValue
            )
        }
    }

    @Test
    fun `should handle post mapping with no request body but with response status and request header`() {
        underTest.verifyResourceWithNoBodyButWithResponseStatusWithHeaderNoInteraction()
        underTest.verifyResourceWithNoBodyButWithResponseStatusWithHeaderNoInteraction(customHeaderValue)
        underTest.resourceWithNoBodyButWithResponseStatusWithHeader(GreetingResponse(name = name))

        val headers = HttpHeaders()
        headers.add(customHeaderName, customHeaderValue)
        val response = restTemplate.postForEntity(
            "$POST_CONTROLLER_URL/resourceWithNoBodyButWithResponseStatusWithHeader",
            HttpEntity("", headers),
            GreetingResponse::class.java
        )

        assertThat(response.body, Matchers.notNullValue())
        assertThat(response.body?.name, Matchers.`is`(name))
        assertThat(response.statusCode, Matchers.`is`(HttpStatus.CREATED))
        underTest.verifyResourceWithNoBodyButWithResponseStatusWithHeader(customHeaderValue)
        underTest.verifyResourceWithNoBodyButWithResponseStatusWithHeader(1, customHeaderValue)
        assertThrows<VerificationException> { underTest.verifyResourceWithNoBodyButWithResponseStatusWithHeaderNoInteraction() }
        assertThrows<VerificationException> {
            underTest.verifyResourceWithNoBodyButWithResponseStatusWithHeaderNoInteraction(
                customHeaderValue
            )
        }
    }

    @Test
    fun `should handle post mapping with request body and multiple path variable and request header`() {
        underTest.verifyResourceWithBodyAndMultiplePathVariablesWithMultipleHeadersNoInteraction(param1, param2)
        underTest.verifyResourceWithBodyAndMultiplePathVariablesWithMultipleHeadersNoInteraction(
            param1, param2, bearerAuthorizationHeaderValue, customHeaderValue, greetingRequest
        )
        underTest.resourceWithBodyAndMultiplePathVariablesWithMultipleHeaders(
            GreetingResponse(name = name), param1, param2
        )

        val headers = HttpHeaders()
        headers.setBearerAuth(token)
        headers.add(customHeaderName, customHeaderValue)
        val response = restTemplate.postForEntity(
            "$POST_CONTROLLER_URL/resourceWithBodyAndMultiplePathVariablesWithMultipleHeaders/$param1/$param2",
            HttpEntity(greetingRequest, headers),
            GreetingResponse::class.java
        )

        assertThat(response.body, Matchers.notNullValue())
        assertThat(response.body?.name, Matchers.`is`(name))
        underTest.verifyResourceWithBodyAndMultiplePathVariablesWithMultipleHeaders(
            param1, param2, bearerAuthorizationHeaderValue, customHeaderValue, greetingRequest
        )
        underTest.verifyResourceWithBodyAndMultiplePathVariablesWithMultipleHeaders(
            1, param1, param2, bearerAuthorizationHeaderValue, customHeaderValue, greetingRequest
        )
        assertThrows<VerificationException> {
            underTest.verifyResourceWithBodyAndMultiplePathVariablesWithMultipleHeadersNoInteraction(param1, param2)
        }
        assertThrows<VerificationException> {
            underTest.verifyResourceWithBodyAndMultiplePathVariablesWithMultipleHeadersNoInteraction(
                param1, param2, bearerAuthorizationHeaderValue, customHeaderValue, greetingRequest
            )
        }
    }
}
