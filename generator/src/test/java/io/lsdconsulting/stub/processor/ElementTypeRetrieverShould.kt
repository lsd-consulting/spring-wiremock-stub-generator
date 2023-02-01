package io.lsdconsulting.stub.processor

import io.mockk.every
import io.mockk.mockk
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.Element
import javax.lang.model.type.TypeMirror

internal class ElementTypeRetrieverShould {

    @Test
    fun `remove parameter annotation`() {
        val element = mockk<Element>()
        val type = mockk<TypeMirror>()
        val annotationMirror = mockk<AnnotationMirror>()

        every { element.asType() } returns type
        every { type.toString() } returns "@some.Annotation some.Class"
        every { type.annotationMirrors } returns listOf(annotationMirror)
        every { annotationMirror.toString() } returns "@some.Annotation"

        val result = getArgumentType(element)

        assertThat(result, `is`("some.Class"))
    }

    @Test
    fun `remove parameter annotations`() {
        val element = mockk<Element>()
        val type = mockk<TypeMirror>()
        val annotationMirror1 = mockk<AnnotationMirror>()
        val annotationMirror2 = mockk<AnnotationMirror>()

        every { element.asType() } returns type
        every { type.toString() } returns "@some.Annotation1, @some.Annotation2 some.Class"
        every { type.annotationMirrors } returns listOf(annotationMirror1, annotationMirror2)
        every { annotationMirror1.toString() } returns "@some.Annotation1"
        every { annotationMirror2.toString() } returns "@some.Annotation2"

        val result = getArgumentType(element)

        assertThat(result, `is`("some.Class"))
    }

    @Test
    fun `handle element without annotations`() {
        val element = mockk<Element>()
        val type = mockk<TypeMirror>()

        every { element.asType() } returns type
        every { type.toString() } returns "some.Class"
        every { type.annotationMirrors } returns listOf()

        val result = getArgumentType(element)

        assertThat(result, `is`("some.Class"))
    }
}
