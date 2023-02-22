package io.lsdconsulting.stub.processor

import io.lsdconsulting.stub.model.ArgumentModel
import io.lsdconsulting.stub.model.ControllerModel
import io.lsdconsulting.stub.model.Model
import io.lsdconsulting.stub.model.ResourceModel
import org.apache.commons.lang3.RandomUtils.nextInt
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.hasProperty
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test

internal class PostProcessorShould {
    private val underTest = PostProcessor()

    private val responseStatus = nextInt()

    @Test
    fun `set response status on unannotated methods when class annotated`() {
        val controllerModel = ControllerModel(responseStatus = responseStatus)
        controllerModel.resources["1"] = ResourceModel()
        controllerModel.resources["2"] = ResourceModel()
        controllerModel.resources["3"] = ResourceModel()
        val model = Model()
        model.controllers["someClass"] = controllerModel

        underTest.update(model)

        model.controllers.values.forEach { controller ->
            controller.resources.values.forEach {
                assertThat(it.responseStatus, `is`(responseStatus))
            }
        }
    }

    @Test
    fun `not override response status on annotated methods when class annotated`() {
        val controllerModel = ControllerModel(responseStatus = responseStatus)
        val resourceModelWithResponseStatus = ResourceModel()
        val resourceResponseStatus = nextInt()
        resourceModelWithResponseStatus.responseStatus = resourceResponseStatus
        controllerModel.resources["1"] = resourceModelWithResponseStatus
        controllerModel.resources["2"] = ResourceModel()
        val model = Model()
        model.controllers["someClass"] = controllerModel

        underTest.update(model)

        assertThat(
            model.controllers.values.first().resources.values,
            hasItem(hasProperty<ResourceModel>("responseStatus", equalTo(resourceResponseStatus)))
        )
        assertThat(
            model.controllers.values.first().resources.values,
            hasItem(hasProperty<ResourceModel>("responseStatus", equalTo(responseStatus)))
        )
    }

    @Test
    fun `handle unannotated class`() {
        val controllerModel = ControllerModel()
        val resourceModelWithResponseStatus = ResourceModel()
        val resourceResponseStatus = nextInt()
        resourceModelWithResponseStatus.responseStatus = resourceResponseStatus
        controllerModel.resources["1"] = resourceModelWithResponseStatus
        controllerModel.resources["2"] = resourceModelWithResponseStatus
        val model = Model()
        model.controllers["someClass"] = controllerModel

        underTest.update(model)

        assertThat(
            model.controllers.values.first().resources.values,
            hasItem(hasProperty<ResourceModel>("responseStatus", equalTo(resourceResponseStatus)))
        )
        assertThat(
            model.controllers.values.first().resources.values,
            hasItem(hasProperty<ResourceModel>("responseStatus", equalTo(resourceResponseStatus)))
        )
    }

    @Test
    fun `update resource URL when URL has path variables`() {
        val controllerModel = ControllerModel(responseStatus = responseStatus)
        val resourceModel =
            ResourceModel(
                urlHasPathVariable = true,
                subResource = "/subResource/{param1}/{param2}",
                pathVariables = mutableMapOf("1" to ArgumentModel("param1"), "2" to ArgumentModel("param2"))
            )
        controllerModel.resources["1"] = resourceModel
        val model = Model()
        model.controllers["someClass"] = controllerModel

        underTest.update(model)

        assertThat(model.controllers.values.first().resources.values.first().subResource, `is`("/subResource/%s/%s"))
    }

    @Test
    fun `keep original resource URL when URL does not have path variables`() {
        val controllerModel = ControllerModel(responseStatus = responseStatus)
        val resourceModel =
            ResourceModel(
                urlHasPathVariable = false,
                subResource = "/subResource/{param1}/{param2}"
            )
        controllerModel.resources["1"] = resourceModel
        val model = Model()
        model.controllers["someClass"] = controllerModel

        underTest.update(model)

        assertThat(model.controllers.values.first().resources.values.first().subResource, `is`("/subResource/{param1}/{param2}"))
    }
}
