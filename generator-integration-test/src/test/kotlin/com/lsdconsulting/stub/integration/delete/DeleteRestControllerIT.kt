package com.lsdconsulting.stub.integration.delete

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.VerificationException
import com.lsdconsulting.stub.integration.BaseRestControllerIT
import com.lsdconsulting.stub.integration.DELETE_CONTROLLER_URL
import com.lsdconsulting.stub.integration.controller.delete.DeleteRestControllerStub
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpEntity
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
    @Disabled
    fun `should handle delete mapping with request body`() {
        underTest.verifyDeleteResourceWithRequestBodyNoInteraction(greetingRequest)
        underTest.deleteResourceWithRequestBody()
        val request = HttpEntity(greetingRequest)
        restTemplate.delete("$DELETE_CONTROLLER_URL/resourceWithRequestBody", request) // TODO Fix delete with body
        underTest.verifyDeleteResourceWithRequestBody(1, greetingRequest)
        underTest.verifyDeleteResourceWithRequestBody(greetingRequest)
        assertThrows<VerificationException> { underTest.verifyDeleteResourceWithRequestBodyNoInteraction(greetingRequest) }
    }

    @Test
    @Disabled
    fun `should handle delete mapping with request body and path variable`() {
        underTest.verifyDeleteResourceWithRequestBodyAndPathVariableNoInteraction(param, greetingRequest)
        underTest.deleteResourceWithRequestBodyAndPathVariable(param)
        val request = HttpEntity(greetingRequest)
        restTemplate.delete("$DELETE_CONTROLLER_URL/resourceWithRequestBodyAndPathVariable/$param", request) // TODO Fix delete with body
        underTest.verifyDeleteResourceWithRequestBodyAndPathVariable(1, param, greetingRequest)
        underTest.verifyDeleteResourceWithRequestBodyAndPathVariable(param, greetingRequest)
        assertThrows<VerificationException> { underTest.verifyDeleteResourceWithRequestBodyAndPathVariableNoInteraction(param, greetingRequest) }
    }
}
