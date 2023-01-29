package io.lsdconsulting.stub.processor

import io.lsdconsulting.stub.model.Model

class PostProcessor {
    fun process(model: Model) {
        model.controllers.values.forEach { controllerModel ->
            if (controllerModel.responseStatus != null) {
                controllerModel.resources.values.forEach {
                    if (it.responseStatus == null) it.responseStatus = controllerModel.responseStatus
                }
            }
            controllerModel.resources.values.forEach { annotatedMethod ->
                if (annotatedMethod.urlHasPathVariable) {
                    annotatedMethod.pathVariables.values.forEach { pathVariable ->
                        annotatedMethod.subResource =
                            annotatedMethod.subResource?.replace("{${pathVariable.name}}", "%s")
                    }
                }
            }
        }
    }
}