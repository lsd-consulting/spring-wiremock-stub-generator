package io.lsdconsulting.stub.processor

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test

internal class PrimitiveReplacerShould {

    @Test
    fun `replace boolean with Boolean`() {
        assertThat(replacePrimitive("boolean"), `is`("Boolean"))
    }

    @Test
    fun `replace int with Integer`() {
        assertThat(replacePrimitive("int"), `is`("Integer"))
    }

    @Test
    fun `replace long with Long`() {
        assertThat(replacePrimitive("long"), `is`("Long"))
    }

    @Test
    fun `replace byte with Byte`() {
        assertThat(replacePrimitive("byte"), `is`("Byte"))
    }

    @Test
    fun `replace short with Short`() {
        assertThat(replacePrimitive("short"), `is`("Short"))
    }

    @Test
    fun `replace float with Float`() {
        assertThat(replacePrimitive("float"), `is`("Float"))
    }

    @Test
    fun `replace double with Double`() {
        assertThat(replacePrimitive("double"), `is`("Double"))
    }

    @Test
    fun `replace char with Char`() {
        assertThat(replacePrimitive("char"), `is`("Char"))
    }

    @Test
    fun `unknown type`() {
        assertThat(replacePrimitive("someTime"), `is`("someTime"))
    }
}
