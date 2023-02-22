package io.lsdconsulting.stub.processor

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test

class ResponseEntityRemoverShould {

    @Test
    fun `Should remove encoded ResponseEntity`() {
        val result = "org.springframework.http.ResponseEntity&lt;com.lsdconsulting.stub.integration.model.GreetingResponse&gt;".removeResponseEntity()
        assertThat(result, `is`("com.lsdconsulting.stub.integration.model.GreetingResponse"))
    }

    @Test
    fun `Should remove ResponseEntity`() {
        val result = "org.springframework.http.ResponseEntity<com.lsdconsulting.stub.integration.model.GreetingResponse>".removeResponseEntity()
        assertThat(result, `is`("com.lsdconsulting.stub.integration.model.GreetingResponse"))
    }

    @Test
    fun `Should return original if no ResponseEntity`() {
        val result = "com.lsdconsulting.stub.integration.model.GreetingResponse".removeResponseEntity()
        assertThat(result, `is`("com.lsdconsulting.stub.integration.model.GreetingResponse"))
    }
}