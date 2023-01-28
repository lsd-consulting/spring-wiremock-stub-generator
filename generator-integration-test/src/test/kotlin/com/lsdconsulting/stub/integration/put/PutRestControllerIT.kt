package com.lsdconsulting.stub.integration.put

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.VerificationException
import com.lsdconsulting.stub.integration.BaseRestControllerIT
import com.lsdconsulting.stub.integration.PUT_CONTROLLER_URL
import com.lsdconsulting.stub.integration.controller.put.PutRestControllerStub
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpEntity
import org.springframework.util.LinkedMultiValueMap

class PutRestControllerIT : BaseRestControllerIT() {
    private val underTest = PutRestControllerStub(ObjectMapper())

    @Test
    fun `should handle put mapping with no body`() {
        underTest.verifyPutResourceWithNoBodyNoInteraction()
        underTest.putResourceWithNoBody()
        restTemplate.put("$PUT_CONTROLLER_URL/resourceWithNoBody", HttpEntity<String>(LinkedMultiValueMap()))
        underTest.verifyPutResourceWithNoBody(1)
        underTest.verifyPutResourceWithNoBody()
        assertThrows<VerificationException> { underTest.verifyPutResourceWithNoBodyNoInteraction() }
    }

    @Test
    fun `should handle put mapping with request body`() {
        underTest.verifyPutResourceWithRequestBodyNoInteraction(greetingRequest)
        underTest.putResourceWithRequestBody()
        val request = HttpEntity(greetingRequest)
        restTemplate.put("$PUT_CONTROLLER_URL/resourceWithRequestBody", request)
        underTest.verifyPutResourceWithRequestBody(1, greetingRequest)
        underTest.verifyPutResourceWithRequestBody(greetingRequest)
        assertThrows<VerificationException> { underTest.verifyPutResourceWithRequestBodyNoInteraction(greetingRequest) }
    }

    @Test
    fun `should handle put mapping with request body and path variable`() {
        underTest.verifyPutResourceWithRequestBodyAndPathVariableNoInteraction(param, greetingRequest)
        underTest.putResourceWithRequestBodyAndPathVariable(param)
        val request = HttpEntity(greetingRequest)
        restTemplate.put("$PUT_CONTROLLER_URL/resourceWithRequestBodyAndPathVariable/$param", request)
        underTest.verifyPutResourceWithRequestBodyAndPathVariable(1, param, greetingRequest)
        underTest.verifyPutResourceWithRequestBodyAndPathVariable(param, greetingRequest)
        assertThrows<VerificationException> { underTest.verifyPutResourceWithRequestBodyAndPathVariableNoInteraction(param, greetingRequest) }
    }
}
