package io.lsdconsulting.stub.processor

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test

class GenericRetrieverShould {
    @Test
    fun `find encoded generic type`() {
        val result = "java.util.List&lt;java.time.ZonedDateTime&gt;".retrieveGeneric()
        assertThat(result, `is`("java.time.ZonedDateTime"))
    }

    @Test
    fun `find encoded nested generic type`() {
        val result = "java.util.List&lt;java.util.Set&lt;java.time.ZonedDateTime&gt;&gt;".retrieveGeneric()
        assertThat(result, `is`("java.util.Set&lt;java.time.ZonedDateTime&gt;"))
    }

    @Test
    fun `find generic type`() {
        val result = "java.util.List<java.time.ZonedDateTime>".retrieveGeneric()
        assertThat(result, `is`("java.time.ZonedDateTime"))
    }

    @Test
    fun `find nested generic type`() {
        val result = "java.util.List<java.util.Set<java.time.ZonedDateTime>>".retrieveGeneric()
        assertThat(result, `is`("java.util.Set<java.time.ZonedDateTime>"))
    }

    @Test
    fun `find original type`() {
        val result = "java.time.ZonedDateTime".retrieveGeneric()
        assertThat(result, `is`("java.time.ZonedDateTime"))
    }
}
