package io.lsdconsulting.stub.processor

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test

internal class PrimitiveReplacerShould {

    @Test
    fun `replace boolean with Boolean`() {
        assertThat("boolean".replacePrimitive(), `is`("Boolean"))
    }

    @Test
    fun `replace int with Integer`() {
        assertThat("int".replacePrimitive(), `is`("Integer"))
    }

    @Test
    fun `replace long with Long`() {
        assertThat("long".replacePrimitive(), `is`("Long"))
    }

    @Test
    fun `replace byte with Byte`() {
        assertThat("byte".replacePrimitive(), `is`("Byte"))
    }

    @Test
    fun `replace short with Short`() {
        assertThat("short".replacePrimitive(), `is`("Short"))
    }

    @Test
    fun `replace float with Float`() {
        assertThat("float".replacePrimitive(), `is`("Float"))
    }

    @Test
    fun `replace double with Double`() {
        assertThat("double".replacePrimitive(), `is`("Double"))
    }

    @Test
    fun `replace char with Char`() {
        assertThat("char".replacePrimitive(), `is`("Char"))
    }

    @Test
    fun `unknown type`() {
        assertThat("someTime".replacePrimitive(), `is`("someTime"))
    }
}
