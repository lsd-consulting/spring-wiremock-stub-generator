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

class SimplePostRestControllerIT : BaseRestControllerIT() {
    private val underTest = SimplePostRestControllerStub(ObjectMapper())

    @Test
    fun `should handle post mapping with no body`() {
        underTest.verifyPostResourceWithNoBodyNoInteraction()
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
        assertThrows<VerificationException> { underTest.verifyPostResourceWithNoBodyNoInteraction() }
    }
}
