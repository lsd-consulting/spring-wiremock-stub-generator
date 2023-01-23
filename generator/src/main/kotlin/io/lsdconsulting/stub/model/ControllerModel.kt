package io.lsdconsulting.stub.model

data class ControllerModel(
    var packageName: String? = null,

    // Stub base
    var stubBaseFullyQualifiedName: String? = null,

    // Stub
    var stubFullyQualifiedName: String? = null,
    var stubClassName: String? = null,
    var methodName: String? = null,
    var responseType: String? = null,
    var rootResource: String? = null,
    var subResource: String? = null
)