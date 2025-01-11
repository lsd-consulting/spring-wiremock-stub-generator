package io.lsdconsulting.stub.processor

import io.lsdconsulting.stub.model.ControllerModel
import io.lsdconsulting.stub.model.Model

class PostProcessor {
    fun update(model: Model) {
        model.controllers.values.forEach { controllerModel ->
            updateResponseStatusOnResources(controllerModel)
            setContainsDateTimeFormat(controllerModel)
            setHasMultipleHttpMethods(controllerModel)
            controllerModel.resources.values.forEach { resource ->
                resource.values.forEach { annotatedMethod ->
                    if (annotatedMethod.urlHasPathVariable) {
                        annotatedMethod.pathVariables.values.forEach { pathVariable ->
                            annotatedMethod.subResource =
                                annotatedMethod.subResource?.replace("{${pathVariable.name}}", "%s")
                        }
                    }
                    annotatedMethod.stubMethodArgumentList = stubArgumentList(annotatedMethod)
                    annotatedMethod.stubMethodArgumentListWithRequest = stubArgumentListWithRequest(annotatedMethod)
                    annotatedMethod.stubMethodArgumentListForCustomResponse =
                        stubArgumentListForCustomResponse(annotatedMethod)
                    annotatedMethod.verifyMethodArgumentList = verifyArgumentList(annotatedMethod)
                    annotatedMethod.verifyMethodArgumentListWithTimes = verifyArgumentListWithTimes(annotatedMethod)
                    annotatedMethod.verifyMethodArgumentListWithTimesWithoutBody =
                        verifyArgumentListWithTimesWithoutBody(annotatedMethod)
                    annotatedMethod.verifyMethodArgumentListPathVariablesOnly = pathVariables(annotatedMethod)
                    annotatedMethod.verifyStubCallArgumentList = verifyStubCallArgumentList(annotatedMethod)
                }
            }
        }
    }

    private fun updateResponseStatusOnResources(controllerModel: ControllerModel) =
        controllerModel.responseStatus?.let {
            controllerModel.resources.values.forEach { resource ->
                resource.values.forEach {
                    it.responseStatus = it.responseStatus ?: controllerModel.responseStatus
                }
            }
        }

    private fun setHasMultipleHttpMethods(controllerModel: ControllerModel) =
        controllerModel.resources.values.forEach { resource ->
            if (resource.values.isNotEmpty() && resource.values.size > 1) {
                controllerModel.hasMultipleHttpMethods = true
                return
            }
        }

    private fun setContainsDateTimeFormat(controllerModel: ControllerModel) {
        controllerModel.resources.values.forEach { resource ->
            resource.values.forEach {
                val controllerContainsDateTimeFormat =
                    it.requestParameters.values.map { argumentModel -> argumentModel.dateTimeFormatAnnotation != null }
                        .contains(true) ||
                            it.pathVariables.values.map { argumentModel -> argumentModel.dateTimeFormatAnnotation != null }
                                .contains(true) ||
                            it.requestBody?.dateTimeFormatAnnotation != null
                if (controllerContainsDateTimeFormat) {
                    controllerModel.containsDateTimeFormat = true
                    return
                }
            }
        }
    }
}
