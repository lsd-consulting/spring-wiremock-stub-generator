package io.lsdconsulting.stub.model

data class ControllerModel(
    var packageName: String? = null,

    // Stub base
    var stubBaseFullyQualifiedName: String? = null,

    // Stub
    var stubFullyQualifiedName: String? = null,
    var stubClassName: String? = null,
    var rootResource: String? = null,
    val annotatedMethods: MutableMap<String, MethodModel> = mutableMapOf()


) {
    fun getMethodModel(name: String): MethodModel = annotatedMethods.getOrPut(name) {MethodModel()}
}

data class MethodModel(
    var methodName: String? = null,
    var responseType: String? = null,
    var subResource: String? = null,
    val annotatedArguments: MutableMap<String, ArgumentModel> = mutableMapOf()

) {
    fun getArgumentModel(name: String): ArgumentModel = annotatedArguments.getOrPut(name) {ArgumentModel()}

}

data class ArgumentModel(
    var name: String? = null,
    var type: String? = null
)