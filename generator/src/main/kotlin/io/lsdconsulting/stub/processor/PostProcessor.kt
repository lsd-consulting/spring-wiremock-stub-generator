package io.lsdconsulting.stub.processor

import io.lsdconsulting.stub.model.ControllerModel
import io.lsdconsulting.stub.model.Model
import io.lsdconsulting.stub.model.ResourceModel

class PostProcessor {
    fun update(model: Model) {
        model.controllers.values.forEach { controllerModel ->
            updateResponseStatusOnResources(controllerModel)
            controllerModel.resources.values.forEach { annotatedMethod ->
                if (annotatedMethod.urlHasPathVariable) {
                    annotatedMethod.pathVariables.values.forEach { pathVariable ->
                        annotatedMethod.subResource =
                            annotatedMethod.subResource?.replace("{${pathVariable.name}}", "%s")
                    }
                }
                annotatedMethod.stubMethodArgumentList = generateArgumentList(annotatedMethod)
                annotatedMethod.stubMethodArgumentListWithRequest = generateArgumentListWithRequest(annotatedMethod)
                annotatedMethod.stubMethodArgumentListForCustomResponse =
                    generateArgumentListForCustomResponse(annotatedMethod)
            }
        }
    }

    private fun generateArgumentList(annotatedMethod: ResourceModel): MutableList<String> {
        val stubMethodArgumentList = mutableListOf<String>()
        annotatedMethod.responseType?.let {
            stubMethodArgumentList.add(annotatedMethod.responseType + " response")
        }
        stubMethodArgumentList.addAll(generatePathVariables(annotatedMethod))
        stubMethodArgumentList.addAll(generateRequestParameters(annotatedMethod))
        return stubMethodArgumentList
    }

    private fun generateArgumentListWithRequest(annotatedMethod: ResourceModel): MutableList<String> {
        val stubMethodArgumentList = mutableListOf<String>()
        annotatedMethod.requestBody?.let {
            stubMethodArgumentList.add("${annotatedMethod.requestBody!!.type} ${annotatedMethod.requestBody!!.name} ")
        }
        stubMethodArgumentList.addAll(generateArgumentList(annotatedMethod))
        return stubMethodArgumentList
    }

    private fun generateArgumentListForCustomResponse(annotatedMethod: ResourceModel): MutableList<String> {
        val stubMethodArgumentList = mutableListOf<String>()
        stubMethodArgumentList.add("int httpStatus")
        stubMethodArgumentList.add("String errorResponse")
        stubMethodArgumentList.addAll(generatePathVariables(annotatedMethod))
        stubMethodArgumentList.addAll(generateRequestParameters(annotatedMethod))
        return stubMethodArgumentList
    }

    private fun generatePathVariables(annotatedMethod: ResourceModel) =
        annotatedMethod.pathVariables.map { "${it.value.type} ${it.value.name}" }

    private fun generateRequestParameters(annotatedMethod: ResourceModel) =
        annotatedMethod.requestParameters.map { "${it.value.type} ${it.value.name}" }

    private fun updateResponseStatusOnResources(controllerModel: ControllerModel) {
        if (controllerModel.responseStatus != null) {
            controllerModel.resources.values.forEach {
                if (it.responseStatus == null) it.responseStatus = controllerModel.responseStatus
            }
        }
    }
}
