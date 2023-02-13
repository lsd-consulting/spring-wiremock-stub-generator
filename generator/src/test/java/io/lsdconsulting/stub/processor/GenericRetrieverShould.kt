package io.lsdconsulting.stub.processor

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test

class GenericRetrieverShould {
    @Test
    fun `find encoded generic type`() {
        val result = getGeneric("java.util.List&lt;java.time.ZonedDateTime&gt;")

        assertThat(result, `is`("java.time.ZonedDateTime"))
    }
    @Test
    fun `find encoded nested generic type`() {
        val result = getGeneric("java.util.List&lt;java.util.Set&lt;java.time.ZonedDateTime&gt;&gt;")

        assertThat(result, `is`("java.util.Set&lt;java.time.ZonedDateTime&gt;"))
    }

    @Test
    fun `find generic type`() {
        val result = getGeneric("java.util.List<java.time.ZonedDateTime>")

        assertThat(result, `is`("java.time.ZonedDateTime"))
    }

    @Test
    fun `find nested generic type`() {
        val result = getGeneric("java.util.List<java.util.Set<java.time.ZonedDateTime>>")

        assertThat(result, `is`("java.util.Set<java.time.ZonedDateTime>"))
    }

    @Test
    fun `find original type`() {
        val result = getGeneric("java.time.ZonedDateTime")

        assertThat(result, `is`("java.time.ZonedDateTime"))
    }
}
