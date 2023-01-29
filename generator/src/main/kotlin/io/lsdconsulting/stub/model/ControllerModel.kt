package io.lsdconsulting.stub.model

import org.springframework.http.HttpMethod

data class Model(
    val controllers: MutableMap<String, ControllerModel> = mutableMapOf()
) {
    fun getControllerModel(name: String): ControllerModel = controllers.getOrPut(name) {ControllerModel()}
}

data class ControllerModel(
    var packageName: String? = null,

    // Stub
    var stubFullyQualifiedName: String? = null,
    var stubClassName: String? = null,
    var rootResource: String? = null,
    var responseStatus: Int? = null,
    val resources: MutableMap<String, ResourceModel> = mutableMapOf()
) {
    fun getResourceModel(name: String): ResourceModel = resources.getOrPut(name) {ResourceModel()}
}

data class ResourceModel(
    var httpMethod: HttpMethod? = null,
    var methodName: String? = null,
    var responseType: String? = null,
    var responseStatus: Int? = null,
    var subResource: String? = null,
    var urlHasPathVariable: Boolean = false,
    val requestParameters: MutableMap<String, ArgumentModel> = mutableMapOf(),
    val pathVariables: MutableMap<String, ArgumentModel> = mutableMapOf(),
    var requestBody: ArgumentModel? = null
) {
    fun getArgumentModel(name: String): ArgumentModel = requestParameters.getOrPut(name) {ArgumentModel()}
    fun getPathVariableModel(name: String): ArgumentModel = pathVariables.getOrPut(name) {ArgumentModel()}
}

data class ArgumentModel(
    var name: String? = null,
    var type: String? = null
)
