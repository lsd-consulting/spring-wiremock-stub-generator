package com.lsdconsulting.stub.integration.post

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.VerificationException
import com.lsdconsulting.stub.integration.BaseRestControllerIT
import com.lsdconsulting.stub.integration.POST_CONTROLLER_URL
import com.lsdconsulting.stub.integration.controller.post.JavaPostRestControllerStub
import com.lsdconsulting.stub.integration.model.GreetingResponse
import com.oneeyedmen.okeydoke.Approver
import com.oneeyedmen.okeydoke.junit5.ApprovalsExtension
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpEntity
import java.io.File
import kotlin.text.Charsets.UTF_8

@ExtendWith(ApprovalsExtension::class)
class JavaPostRestControllerIT : BaseRestControllerIT() {
    private val underTest = JavaPostRestControllerStub(ObjectMapper())

    @Test
    fun `should remove unwanted annotations from any arguments`(approver: Approver) {
        approver.assertApproved(loadGeneratedFile())
    }

    @Test
    fun `should handle post mapping with request body`() {
        underTest.verifyResourceWithBodyAndAnnotationsNoInteraction(greetingRequest)
        underTest.verifyResourceWithBodyAndAnnotationsNoInteraction()
        underTest.resourceWithBodyAndAnnotations(greetingResponse)

        val response =
            restTemplate.postForEntity(
                "$POST_CONTROLLER_URL/resourceWithBodyAndAnnotations",
                HttpEntity(greetingRequest),
                GreetingResponse::class.java
            )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyResourceWithBodyAndAnnotations(1, greetingRequest)
        underTest.verifyResourceWithBodyAndAnnotations(1)
        underTest.verifyResourceWithBodyAndAnnotations(greetingRequest)
        assertThrows<VerificationException> {
            underTest.verifyResourceWithBodyAndAnnotationsNoInteraction(greetingRequest)
        }
        assertThrows<VerificationException> { underTest.verifyResourceWithBodyAndAnnotationsNoInteraction() }
    }

    @Test
    fun `should handle post mapping with request body and path variable`() {
        underTest.verifyResourceWithBodyAndAnnotationsOnPathVariablesNoInteraction(param, greetingRequest)
        underTest.verifyResourceWithBodyAndAnnotationsOnPathVariablesNoInteraction(param)
        underTest.resourceWithBodyAndAnnotationsOnPathVariables(greetingResponse, param)

        val response =
            restTemplate.postForEntity(
                "$POST_CONTROLLER_URL/resourceWithBodyAndAnnotationsOnPathVariables/$param",
                HttpEntity(greetingRequest),
                GreetingResponse::class.java
            )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyResourceWithBodyAndAnnotationsOnPathVariables(1, param, greetingRequest)
        underTest.verifyResourceWithBodyAndAnnotationsOnPathVariables(1, param)
        underTest.verifyResourceWithBodyAndAnnotationsOnPathVariables(param, greetingRequest)
        assertThrows<VerificationException> {
            underTest.verifyResourceWithBodyAndAnnotationsOnPathVariablesNoInteraction(param, greetingRequest)
        }
        assertThrows<VerificationException> {
            underTest.verifyResourceWithBodyAndAnnotationsOnPathVariablesNoInteraction(param)
        }
    }

    private fun loadGeneratedFile(): String {
        val file = File(javaClass.classLoader.getResource(".")?.file ?: "")
        val fileName = file.absolutePath.replace(
            "classes/kotlin/test",
            "generated/source/kapt/main/com/lsdconsulting/stub/integration/controller/post/JavaPostRestControllerStub.java"
        )
        return File(fileName).readText(UTF_8)
    }
}
