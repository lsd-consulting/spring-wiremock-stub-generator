package com.lsdconsulting.stub.integration.get

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.VerificationException
import com.lsdconsulting.stub.integration.BaseRestControllerIT
import com.lsdconsulting.stub.integration.GET_CONTROLLER_URL
import com.lsdconsulting.stub.integration.controller.get.JavaGetRestControllerStub
import com.lsdconsulting.stub.integration.model.GreetingResponse
import com.oneeyedmen.okeydoke.Approver
import com.oneeyedmen.okeydoke.junit5.ApprovalsExtension
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.io.File
import kotlin.text.Charsets.UTF_8

internal class CustomApprovalsExtension private constructor() :
    ApprovalsExtension("src/test/resources/approval")

@ExtendWith(CustomApprovalsExtension::class)
class JavaPostRestControllerIT : BaseRestControllerIT() {
    private val underTest = JavaGetRestControllerStub(ObjectMapper())

    @Test
    fun `should strip unwanted annotations from any arguments`(approver: Approver) {
        approver.assertApproved(loadGeneratedFile())
    }

    @Test
    fun `should handle get mapping with request param`() {
        underTest.verifyGetResourceWithParamAndAnnotationsNoInteraction(param)
        underTest.verifyGetResourceWithParamAndAnnotationsNoInteractionWithUrl()
        underTest.getResourceWithParamAndAnnotations(greetingResponse, param)
        val response = restTemplate.getForEntity(
            "$GET_CONTROLLER_URL/resourceWithParamAndAnnotations?param=$param",
            GreetingResponse::class.java
        )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithParamAndAnnotations(1, param)
        underTest.verifyGetResourceWithParamAndAnnotations(param)
        assertThrows<VerificationException> { underTest.verifyGetResourceWithParamAndAnnotationsNoInteraction(param) }
        assertThrows<VerificationException> { underTest.verifyGetResourceWithParamAndAnnotationsNoInteractionWithUrl() }
    }

    private fun loadGeneratedFile(): String {
        val file = File(javaClass.classLoader.getResource(".")?.file ?: "")
        val fileName = file.absolutePath.replace(
            "classes/kotlin/test",
            "generated/source/kapt/main/com/lsdconsulting/stub/integration/controller/get/JavaGetRestControllerStub.java"
        )
        return File(fileName).readText(UTF_8)
    }
}
