package io.lsdconsulting.stub.processor

import io.lsdconsulting.stub.model.ControllerModel
import io.lsdconsulting.stub.model.Model

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
                annotatedMethod.stubMethodArgumentList = stubArgumentList(annotatedMethod)
                annotatedMethod.stubMethodArgumentListWithRequest = stubArgumentListWithRequest(annotatedMethod)
                annotatedMethod.stubMethodArgumentListForCustomResponse = stubArgumentListForCustomResponse(annotatedMethod)
                annotatedMethod.verifyMethodArgumentList = verifyArgumentList(annotatedMethod)
                annotatedMethod.verifyMethodArgumentListWithTimes = verifyArgumentListWithTimes(annotatedMethod)
                annotatedMethod.verifyMethodArgumentListPathVariablesOnly = pathVariables(annotatedMethod)
                annotatedMethod.verifyStubCallArgumentList = verifyStubCallArgumentList(annotatedMethod)
            }
        }
    }

    private fun updateResponseStatusOnResources(controllerModel: ControllerModel) {
        if (controllerModel.responseStatus != null) {
            controllerModel.resources.values.forEach {
                if (it.responseStatus == null) it.responseStatus = controllerModel.responseStatus
            }
        }
    }
}
