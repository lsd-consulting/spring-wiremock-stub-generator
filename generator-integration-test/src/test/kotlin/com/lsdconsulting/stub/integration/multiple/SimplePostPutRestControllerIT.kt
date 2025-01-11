package com.lsdconsulting.stub.integration.multiple

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.VerificationException
import com.lsdconsulting.stub.integration.BaseRestControllerIT
import com.lsdconsulting.stub.integration.MULTIPLE_METHOD_CONTROLLER_URL
import com.lsdconsulting.stub.integration.controller.multiple.SimplePostPutRestControllerStub
import com.lsdconsulting.stub.integration.model.GreetingResponse
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod.PUT
import org.springframework.http.HttpStatus.OK


class SimplePostPutRestControllerIT : BaseRestControllerIT() {
    private val underTest = SimplePostPutRestControllerStub(ObjectMapper())

    @Test
    fun `should handle post mapping with no request body`() {
        underTest.verifyPostPutResourceWitNoRequestBodyNoInteraction_POST()
        underTest.postPutResourceWitNoRequestBody_POST(GreetingResponse(name = name))

        val response = restTemplate.postForEntity(
            "$MULTIPLE_METHOD_CONTROLLER_URL/postPutResource",
            HttpEntity(""),
            GreetingResponse::class.java
        )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyPostPutResourceWitNoRequestBody_POST(1)
        underTest.verifyPostPutResourceWitNoRequestBody_POST()
        assertThat(response.statusCode, `is`(OK))
        assertThrows<VerificationException> { underTest.verifyPostPutResourceWitNoRequestBodyNoInteraction_POST() }
    }

    @Test
    fun `should handle put mapping with no request body`() {
        underTest.verifyPostPutResourceWitNoRequestBodyNoInteraction_PUT()
        underTest.postPutResourceWitNoRequestBody_PUT(GreetingResponse(name = name))

        val response = restTemplate.exchange(
            "$MULTIPLE_METHOD_CONTROLLER_URL/postPutResource", PUT,
            HttpEntity(""),
            GreetingResponse::class.java
        )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyPostPutResourceWitNoRequestBody_PUT(1)
        underTest.verifyPostPutResourceWitNoRequestBody_PUT()
        assertThat(response.statusCode, `is`(OK))
        assertThrows<VerificationException> { underTest.verifyPostPutResourceWitNoRequestBodyNoInteraction_PUT() }
    }
}
