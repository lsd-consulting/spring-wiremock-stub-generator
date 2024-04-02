package com.lsdconsulting.stub.integration.get

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.VerificationException
import com.lsdconsulting.stub.integration.BaseRestControllerIT
import com.lsdconsulting.stub.integration.GET_CONTROLLER_URL
import com.lsdconsulting.stub.integration.controller.get.GetRestControllerStub
import com.lsdconsulting.stub.integration.model.GreetingResponse
import org.apache.hc.client5.http.classic.methods.HttpGet
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.format.datetime.standard.Jsr310DateTimeFormatAnnotationFormatterFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod.GET
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter.ISO_DATE_TIME
import java.time.format.DateTimeFormatter.ofPattern

class GetRestControllerAnnotationFormatterFactoryIT : BaseRestControllerIT() {
    private val underTest = GetRestControllerStub(ObjectMapper(), Jsr310DateTimeFormatAnnotationFormatterFactory())

    @Test
    fun `should handle get mapping with ZonedDateTime request param`() {
        val param = ZonedDateTime.now(ZoneId.of("UTC"))
        underTest.verifyResourceWithZonedDatetimeNoInteraction(param)
        underTest.verifyResourceWithZonedDatetimeNoInteraction()
        underTest.resourceWithZonedDatetime(greetingResponse, param)

        val response = restTemplate.exchange(
            "$GET_CONTROLLER_URL/resourceWithZonedDatetime?param=${param.format(ISO_DATE_TIME)}",
            GET, HttpEntity(mapOf<String, String>()), GreetingResponse::class.java
        )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyResourceWithZonedDatetime(param)
        assertThrows<VerificationException> { underTest.verifyResourceWithZonedDatetimeNoInteraction(param) }
        assertThrows<VerificationException> { underTest.verifyResourceWithZonedDatetimeNoInteraction() }
    }

    @Test
    @Disabled
    fun `should handle get mapping with OffsetDateTime request param for different timezone`() {
        val param = OffsetDateTime.now(ZoneId.of("CET"))
        underTest.verifyResourceWithOffsetDateTimeAndMultiValueNoInteraction(param)
        underTest.verifyResourceWithOffsetDateTimeAndMultiValueNoInteraction()
        underTest.resourceWithOffsetDateTimeAndMultiValue(greetingResponse, param)

        val response = restTemplate.exchange(
            "$GET_CONTROLLER_URL/resourceWithOffsetDateTime?param=${param.format(ISO_DATE_TIME)}",
            GET, HttpEntity(mapOf<String, String>()), GreetingResponse::class.java
        )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyResourceWithOffsetDateTimeAndMultiValue(param)
        assertThrows<VerificationException> { underTest.verifyResourceWithOffsetDateTimeAndMultiValueNoInteraction(param) }
        assertThrows<VerificationException> { underTest.verifyResourceWithOffsetDateTimeAndMultiValueNoInteraction() }
    }

    @Test
    fun `should handle get mapping with ZonedDateTime request param and all DateTimeFormat arguments`() {
        val param = ZonedDateTime.now(ZoneId.of("UTC"))
        underTest.verifyResourceWithZonedDatetimeAndAllDateTimeFormatArgumentsNoInteraction(param)
        underTest.verifyResourceWithZonedDatetimeAndAllDateTimeFormatArgumentsNoInteraction()
        underTest.resourceWithZonedDatetimeAndAllDateTimeFormatArguments(greetingResponse, param)

        val response = restTemplate.exchange(
            "$GET_CONTROLLER_URL/resourceWithZonedDatetimeAndAllDateTimeFormatArguments?param=${param.format(
                ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"))}",
            GET, HttpEntity(mapOf<String, String>()), GreetingResponse::class.java
        )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyResourceWithZonedDatetimeAndAllDateTimeFormatArguments(param)
        assertThrows<VerificationException> { underTest.verifyResourceWithZonedDatetimeAndAllDateTimeFormatArgumentsNoInteraction(param) }
        assertThrows<VerificationException> { underTest.verifyResourceWithZonedDatetimeAndAllDateTimeFormatArgumentsNoInteraction() }
    }

    @Test
    fun `should handle get mapping with multi-value ZonedDateTime`() {
        val multiValue = listOf(ZonedDateTime.now(), ZonedDateTime.now().plusHours(1))
        underTest.verifyResourceWithMultiValueZonedDatetimeNoInteraction(multiValue)
        underTest.resourceWithMultiValueZonedDatetime(greetingResponse, multiValue)

        val request = HttpGet(
            "$GET_CONTROLLER_URL/resourceWithMultiValueZonedDatetime?multiValue=${
                multiValue[0].format(ISO_DATE_TIME)
            }&multiValue=${
                multiValue[1].format(ISO_DATE_TIME)
            }"
        )
        HttpClients.createDefault().use { client -> client.execute(request) { } }

        underTest.verifyResourceWithMultiValueZonedDatetime(multiValue)
        assertThrows<VerificationException> { underTest.verifyResourceWithMultiValueZonedDatetimeNoInteraction(multiValue) }
    }
}
