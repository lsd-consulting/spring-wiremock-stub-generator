package io.lsdconsulting.stub.model

data class ControllerModel(
    var packageName: String? = null,

    // Stub base
    var stubBaseFullyQualifiedName: String? = null,

    // Stub
    var stubFullyQualifiedName: String? = null,
    var stubClassName: String? = null,
    var rootResource: String? = null,
    val resources: MutableMap<String, ResourceModel> = mutableMapOf()
) {
    fun getResourceModel(name: String): ResourceModel = resources.getOrPut(name) {ResourceModel()}
}

data class ResourceModel(
    var methodName: String? = null,
    var responseType: String? = null,
    var subResource: String? = null,
    var urlHasPathVariable: Boolean = false,
    val requestParameters: MutableMap<String, ArgumentModel> = mutableMapOf(),
    val pathVariables: MutableMap<String, ArgumentModel> = mutableMapOf()
) {
    fun getArgumentModel(name: String): ArgumentModel = requestParameters.getOrPut(name) {ArgumentModel()}
    fun getPathVariableModel(name: String): ArgumentModel = pathVariables.getOrPut(name) {ArgumentModel()}
}

data class ArgumentModel(
    var name: String? = null,
    var type: String? = null
)