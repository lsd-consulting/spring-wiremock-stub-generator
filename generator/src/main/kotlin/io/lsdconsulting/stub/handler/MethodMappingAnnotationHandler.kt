package io.lsdconsulting.stub.handler

import io.lsdconsulting.stub.model.Model
import org.apache.commons.lang3.StringUtils
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
        if (path.isNotEmpty()) {
            controllerModel.getResourceModel(methodModelKey).subResource = path[0]
        } else if (value.isNotEmpty()) {
            controllerModel.getResourceModel(methodModelKey).subResource = value[0]
        }
        controllerModel.getResourceModel(methodModelKey).httpMethod = httpMethod
        controllerModel.getResourceModel(methodModelKey).methodName = StringUtils.capitalize(methodName)
        controllerModel.getResourceModel(methodModelKey).responseType = responseType
    }
}