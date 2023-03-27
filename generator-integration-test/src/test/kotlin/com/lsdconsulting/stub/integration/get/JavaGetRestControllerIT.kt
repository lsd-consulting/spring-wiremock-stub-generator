package com.lsdconsulting.stub.integration.get

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.VerificationException
import com.lsdconsulting.stub.integration.BaseRestControllerIT
import com.lsdconsulting.stub.integration.GET_CONTROLLER_URL
import com.lsdconsulting.stub.integration.controller.get.JavaGetRestControllerStub
import com.lsdconsulting.stub.integration.model.GreetingResponse
import com.oneeyedmen.okeydoke.Approver
import com.oneeyedmen.okeydoke.junit5.ApprovalsExtension
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.format.datetime.standard.Jsr310DateTimeFormatAnnotationFormatterFactory
import java.io.File
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.text.Charsets.UTF_8

internal class CustomApprovalsExtension private constructor() : ApprovalsExtension("src/test/resources/approval")

@ExtendWith(CustomApprovalsExtension::class)
class JavaGetRestControllerIT : BaseRestControllerIT() {
    private val underTest = JavaGetRestControllerStub(ObjectMapper(), Jsr310DateTimeFormatAnnotationFormatterFactory())

    @Test
    fun `should remove unwanted annotations from any arguments`(approver: Approver) {
        approver.assertApproved(loadGeneratedFile())
    }

    @Test
    fun `should handle get mapping with request param`() {
        underTest.verifyResourceWithParamAndAnnotationsNoInteraction(param)
        underTest.verifyResourceWithParamAndAnnotationsNoInteractionWithUrl()
        underTest.resourceWithParamAndAnnotations(greetingResponse, param)

        val response = restTemplate.getForEntity(
            "$GET_CONTROLLER_URL/resourceWithParamAndAnnotations?param=$param",
            GreetingResponse::class.java
        )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyResourceWithParamAndAnnotations(1, param)
        underTest.verifyResourceWithParamAndAnnotations(param)
        assertThrows<VerificationException> { underTest.verifyResourceWithParamAndAnnotationsNoInteraction(param) }
        assertThrows<VerificationException> { underTest.verifyResourceWithParamAndAnnotationsNoInteractionWithUrl() }
    }

    @Test
    fun `should handle get mapping with ZonedDateTime and multi-value request parameters`() {
        val param = ZonedDateTime.now()
        underTest.verifyResourceWithZonedDatetimeAndMultiValueNoInteraction(param, setOf(33, 44))
        underTest.resourceWithZonedDatetimeAndMultiValue(greetingResponse, param, setOf(33, 44))

        val request = HttpGet(
            "$GET_CONTROLLER_URL/resourceWithZonedDatetimeAndMultiValue?param=${param.format(DateTimeFormatter.ISO_DATE_TIME)
            }&multiValue=33&multiValue=44"
        )
        HttpClientBuilder.create().build().use { client -> client.execute(request) as CloseableHttpResponse }

        underTest.verifyResourceWithZonedDatetimeAndMultiValue(param, setOf(33, 44))
        assertThrows<VerificationException> { underTest.verifyResourceWithZonedDatetimeAndMultiValueNoInteraction(param, setOf(33, 44)) }
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
