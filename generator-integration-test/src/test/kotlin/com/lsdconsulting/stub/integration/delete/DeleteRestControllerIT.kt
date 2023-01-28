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
        underTest.verifyDeleteResourceWithNoBodyNoInteraction()
        underTest.deleteResourceWithNoBody()
        restTemplate.delete("$DELETE_CONTROLLER_URL/resourceWithNoBody", HttpEntity<String>(LinkedMultiValueMap()))
        underTest.verifyDeleteResourceWithNoBody(1)
        underTest.verifyDeleteResourceWithNoBody()
        assertThrows<VerificationException> { underTest.verifyDeleteResourceWithNoBodyNoInteraction() }
    }

    @Test
    fun `should handle delete mapping with request body`() {
        underTest.verifyDeleteResourceWithRequestBodyNoInteraction(greetingRequest)
        underTest.deleteResourceWithRequestBody()
        val request = HttpEntity(greetingRequest)
        val responseEntity = restTemplate.exchange("$DELETE_CONTROLLER_URL/resourceWithRequestBody", DELETE, request, Unit::class.java)
        assertThat(responseEntity.body, `is`(nullValue()))
        assertThat(responseEntity.statusCode, `is`(OK))
        underTest.verifyDeleteResourceWithRequestBody(1, greetingRequest)
        underTest.verifyDeleteResourceWithRequestBody(greetingRequest)
        assertThrows<VerificationException> { underTest.verifyDeleteResourceWithRequestBodyNoInteraction(greetingRequest) }
    }

    @Test
    fun `should handle delete mapping with request body and path variable`() {
        underTest.verifyDeleteResourceWithRequestBodyAndPathVariableNoInteraction(param, greetingRequest)
        underTest.deleteResourceWithRequestBodyAndPathVariable(param)
        val request = HttpEntity(greetingRequest)
        val responseEntity = restTemplate.exchange(
            "$DELETE_CONTROLLER_URL/resourceWithRequestBodyAndPathVariable/$param",
            DELETE,
            request,
            Unit::class.java
        )
        assertThat(responseEntity.body, `is`(nullValue()))
        assertThat(responseEntity.statusCode, `is`(NO_CONTENT))
        underTest.verifyDeleteResourceWithRequestBodyAndPathVariable(1, param, greetingRequest)
        underTest.verifyDeleteResourceWithRequestBodyAndPathVariable(param, greetingRequest)
        assertThrows<VerificationException> { underTest.verifyDeleteResourceWithRequestBodyAndPathVariableNoInteraction(param, greetingRequest) }
    }
}
