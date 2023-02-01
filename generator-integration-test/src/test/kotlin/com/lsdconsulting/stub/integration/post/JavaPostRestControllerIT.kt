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

internal class CustomApprovalsExtension private constructor() :
    ApprovalsExtension("src/test/resources/approval")

@ExtendWith(CustomApprovalsExtension::class)
class JavaPostRestControllerIT : BaseRestControllerIT() {
    private val underTest = JavaPostRestControllerStub(ObjectMapper())

    @Test
    fun `should handle post mapping with body`(approver: Approver) {
        underTest.verifyPostResourceWithBodyAndAnnotationsNoInteraction(greetingRequest)
        underTest.verifyPostResourceWithBodyAndAnnotationsNoInteractionWithUrl()
        underTest.postResourceWithBodyAndAnnotations(greetingResponse)
        val request = HttpEntity(greetingRequest)
        val response =
            restTemplate.postForEntity(
                "$POST_CONTROLLER_URL/resourceWithBodyAndAnnotations",
                request,
                GreetingResponse::class.java
            )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyPostResourceWithBodyAndAnnotations(1, greetingRequest)
        underTest.verifyPostResourceWithBodyAndAnnotations(greetingRequest)
        assertThrows<VerificationException> { underTest.verifyPostResourceWithBodyAndAnnotationsNoInteraction(greetingRequest) }
        assertThrows<VerificationException> { underTest.verifyPostResourceWithBodyAndAnnotationsNoInteractionWithUrl() }

        approver.assertApproved(loadGeneratedFile())
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
