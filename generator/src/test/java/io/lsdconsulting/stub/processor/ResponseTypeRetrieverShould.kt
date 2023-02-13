package io.lsdconsulting.stub.processor

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class ResponseTypeRetrieverShould {

    @Test
    fun `Should strip annotations from response type`() {
        val result = retrieveResponseType("(@javax.validation.Valid,@javax.validation.constraints.Email com.lsdconsulting.stub.integration.model.GreetingRequest)com.lsdconsulting.stub.integration.model.GreetingResponse")
        assertThat(result, `is`("com.lsdconsulting.stub.integration.model.GreetingResponse"))
    }

    @Test
    fun `Should handle void response type`() {
        val result = retrieveResponseType("()void")
        assertThat(result, nullValue())
    }
}