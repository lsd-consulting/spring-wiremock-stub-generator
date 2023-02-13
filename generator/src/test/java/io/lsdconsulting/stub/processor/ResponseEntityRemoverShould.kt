package io.lsdconsulting.stub.processor

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.nullValue
import org.junit.jupiter.api.Test

class ResponseEntityRemoverShould {

    @Test
    fun `Should remove encoded ResponseEntity`() {
        val result = removeResponseEntity("org.springframework.http.ResponseEntity&lt;com.lsdconsulting.stub.integration.model.GreetingResponse&gt;")
        assertThat(result, `is`("com.lsdconsulting.stub.integration.model.GreetingResponse"))
    }

    @Test
    fun `Should remove ResponseEntity`() {
        val result = removeResponseEntity("org.springframework.http.ResponseEntity<com.lsdconsulting.stub.integration.model.GreetingResponse>")
        assertThat(result, `is`("com.lsdconsulting.stub.integration.model.GreetingResponse"))
    }

    @Test
    fun `Should return original if no ResponseEntity`() {
        val result = removeResponseEntity("com.lsdconsulting.stub.integration.model.GreetingResponse")
        assertThat(result, `is`("com.lsdconsulting.stub.integration.model.GreetingResponse"))
    }

    @Test
    fun `Should handle empty argument`() {
        val result = removeResponseEntity(null)
        assertThat(result, `is`(nullValue()))
    }
}