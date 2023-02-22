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
        underTest.verifyPostResourceWithNoBodyNoInteraction()
        underTest.verifyPostResourceWithNoBodyNoInteractionWithUrl()
        underTest.postResourceWithNoBody(GreetingResponse(name = name))
        val response = restTemplate.postForEntity(
            "$POST_CONTROLLER_URL/resourceWithNoBody",
            HttpEntity(""),
            GreetingResponse::class.java
        )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyPostResourceWithNoBody(1)
        underTest.verifyPostResourceWithNoBody()
        assertThat(response.statusCode, `is`(OK))
        assertThrows<VerificationException> { underTest.verifyPostResourceWithNoBodyNoInteraction() }
        assertThrows<VerificationException> { underTest.verifyPostResourceWithNoBodyNoInteractionWithUrl() }
    }

    @Test
    fun `should handle post mapping with no request body but with response status`() {
        underTest.verifyPostResourceWithNoBodyButWithResponseStatusNoInteraction()
        underTest.postResourceWithNoBodyButWithResponseStatus(GreetingResponse(name = name))
        val response = restTemplate.postForEntity(
            "$POST_CONTROLLER_URL/resourceWithNoBodyButWithResponseStatus",
            HttpEntity(""),
            GreetingResponse::class.java
        )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        assertThat(response.statusCode, `is`(CREATED))
        underTest.verifyPostResourceWithNoBodyButWithResponseStatus(1)
        underTest.verifyPostResourceWithNoBodyButWithResponseStatus()
        assertThrows<VerificationException> { underTest.verifyPostResourceWithNoBodyButWithResponseStatusNoInteraction() }
    }

    @Test
    fun `should handle post mapping with no request body and no response`() {
        underTest.verifyPostResourceWithNoBodyNoResponseNoInteraction()
        underTest.verifyPostResourceWithNoBodyNoResponseNoInteractionWithUrl()
        underTest.postResourceWithNoBodyNoResponse()
        val response = restTemplate.postForEntity(
            "$POST_CONTROLLER_URL/resourceWithNoBodyNoResponse",
            HttpEntity(""),
            Unit::class.java
        )
        assertThat(response.statusCode, `is`(OK))
        assertThrows<VerificationException> { underTest.verifyPostResourceWithNoBodyNoResponseNoInteraction() }
        assertThrows<VerificationException> { underTest.verifyPostResourceWithNoBodyNoResponseNoInteractionWithUrl() }
    }
}
