package com.lsdconsulting.stub.integration.delete

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.VerificationException
import com.lsdconsulting.stub.integration.BaseRestControllerIT
import com.lsdconsulting.stub.integration.DELETE_CONTROLLER_URL
import com.lsdconsulting.stub.integration.controller.delete.DeleteRestControllerStub
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.nullValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod.DELETE
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.HttpStatus.OK
import org.springframework.util.LinkedMultiValueMap

class DeleteRestControllerIT : BaseRestControllerIT() {
    private val underTest = DeleteRestControllerStub(ObjectMapper())

    @Test
    fun `should handle delete mapping with no body`() {
        underTest.verifyResourceWithNoBodyNoInteraction()
        underTest.resourceWithNoBody()
        restTemplate.delete("$DELETE_CONTROLLER_URL/resourceWithNoBody", HttpEntity<String>(LinkedMultiValueMap()))
        underTest.verifyResourceWithNoBody(1)
        underTest.verifyResourceWithNoBody()
        assertThrows<VerificationException> { underTest.verifyResourceWithNoBodyNoInteraction() }
    }

    @Test
    fun `should handle delete mapping with request body`() {
        underTest.verifyResourceWithRequestBodyNoInteraction(greetingRequest)
        underTest.resourceWithRequestBody()
        val request = HttpEntity(greetingRequest)
        val responseEntity = restTemplate.exchange("$DELETE_CONTROLLER_URL/resourceWithRequestBody", DELETE, request, Unit::class.java)
        assertThat(responseEntity.body, `is`(nullValue()))
        assertThat(responseEntity.statusCode, `is`(OK))
        underTest.verifyResourceWithRequestBody(1, greetingRequest)
        underTest.verifyResourceWithRequestBody(greetingRequest)
        assertThrows<VerificationException> { underTest.verifyResourceWithRequestBodyNoInteraction(greetingRequest) }
    }

    @Test
    fun `should handle delete mapping with request body and path variable`() {
        underTest.verifyResourceWithRequestBodyAndPathVariableNoInteraction(param, greetingRequest)
        underTest.resourceWithRequestBodyAndPathVariable(param)
        val request = HttpEntity(greetingRequest)
        val responseEntity = restTemplate.exchange(
            "$DELETE_CONTROLLER_URL/resourceWithRequestBodyAndPathVariable/$param",
            DELETE,
            request,
            Unit::class.java
        )
        assertThat(responseEntity.body, `is`(nullValue()))
        assertThat(responseEntity.statusCode, `is`(NO_CONTENT))
        underTest.verifyResourceWithRequestBodyAndPathVariable(1, param, greetingRequest)
        underTest.verifyResourceWithRequestBodyAndPathVariable(param, greetingRequest)
        assertThrows<VerificationException> { underTest.verifyResourceWithRequestBodyAndPathVariableNoInteraction(param, greetingRequest) }
    }
}
