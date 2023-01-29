package io.lsdconsulting.stub.handler

import io.lsdconsulting.stub.model.Model
import org.apache.commons.lang3.StringUtils.capitalize
import org.springframework.http.HttpMethod
import javax.lang.model.element.Element

class MethodMappingAnnotationHandler {
    fun handle(
        element: Element,
        model: Model,
        path: Array<String>,
        value: Array<String>,
        httpMethod: HttpMethod,
        responseType: String? = null
    ) {
        val methodModelKey = element.toString()
        val methodName = element.simpleName.toString()
        val controllerModel = model.getControllerModel(element.enclosingElement.toString())
        controllerModel.getResourceModel(methodModelKey).subResource = subResource(path, value)
        controllerModel.getResourceModel(methodModelKey).httpMethod = httpMethod
        controllerModel.getResourceModel(methodModelKey).methodName = capitalize(methodName)
        controllerModel.getResourceModel(methodModelKey).responseType = responseType
    }

    private fun subResource(path: Array<String>, value: Array<String>) =
        if (path.isNotEmpty()) {
            path[0]
        } else if (value.isNotEmpty()) {
            value[0]
        } else null
}