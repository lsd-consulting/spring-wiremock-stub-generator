package com.lsdconsulting.stub.integration.post

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.VerificationException
import com.lsdconsulting.stub.integration.BaseRestControllerIT
import com.lsdconsulting.stub.integration.POST_CONTROLLER_URL
import com.lsdconsulting.stub.integration.controller.post.SimplePostRestControllerStub
import com.lsdconsulting.stub.integration.model.GreetingResponse
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK

class SimplePostRestControllerIT : BaseRestControllerIT() {
    private val underTest = SimplePostRestControllerStub(ObjectMapper())

    @Test
    fun `should handle post mapping with no request body`() {
        underTest.verifyResourceWithNoBodyNoInteraction()
        underTest.verifyResourceWithNoBodyNoInteractionWithUrl()
        underTest.resourceWithNoBody(GreetingResponse(name = name))

        val response = restTemplate.postForEntity(
            "$POST_CONTROLLER_URL/resourceWithNoBody",
            HttpEntity(""),
            GreetingResponse::class.java
        )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyResourceWithNoBody(1)
        underTest.verifyResourceWithNoBody()
        assertThat(response.statusCode, `is`(OK))
        assertThrows<VerificationException> { underTest.verifyResourceWithNoBodyNoInteraction() }
        assertThrows<VerificationException> { underTest.verifyResourceWithNoBodyNoInteractionWithUrl() }
    }

    @Test
    fun `should handle post mapping with no request body but with response status`() {
        underTest.verifyResourceWithNoBodyButWithResponseStatusNoInteraction()
        underTest.resourceWithNoBodyButWithResponseStatus(GreetingResponse(name = name))

        val response = restTemplate.postForEntity(
            "$POST_CONTROLLER_URL/resourceWithNoBodyButWithResponseStatus",
            HttpEntity(""),
            GreetingResponse::class.java
        )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        assertThat(response.statusCode, `is`(CREATED))
        underTest.verifyResourceWithNoBodyButWithResponseStatus(1)
        underTest.verifyResourceWithNoBodyButWithResponseStatus()
        assertThrows<VerificationException> { underTest.verifyResourceWithNoBodyButWithResponseStatusNoInteraction() }
    }

    @Test
    fun `should handle post mapping with no request body and no response`() {
        underTest.verifyResourceWithNoBodyNoResponseNoInteraction()
        underTest.verifyResourceWithNoBodyNoResponseNoInteractionWithUrl()
        underTest.resourceWithNoBodyNoResponse()

        val response = restTemplate.postForEntity(
            "$POST_CONTROLLER_URL/resourceWithNoBodyNoResponse",
            HttpEntity(""),
            Unit::class.java
        )

        assertThat(response.statusCode, `is`(OK))
        assertThrows<VerificationException> { underTest.verifyResourceWithNoBodyNoResponseNoInteraction() }
        assertThrows<VerificationException> { underTest.verifyResourceWithNoBodyNoResponseNoInteractionWithUrl() }
    }
}
