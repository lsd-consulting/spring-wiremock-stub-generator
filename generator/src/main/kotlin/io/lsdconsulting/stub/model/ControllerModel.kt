package io.lsdconsulting.stub.model

import org.springframework.http.HttpMethod

data class Model(
    val controllers: MutableMap<String, ControllerModel> = mutableMapOf()
) {
    fun getControllerModel(name: String): ControllerModel = controllers.getOrPut(name) {ControllerModel()}
}

data class ControllerModel(
    var packageName: String? = null,
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
    var hasMultiValueRequestParams: Boolean = false,
    val requestParameters: MutableMap<String, ArgumentModel> = mutableMapOf(),
    val pathVariables: MutableMap<String, ArgumentModel> = mutableMapOf(),
    var requestBody: ArgumentModel? = null,
    var stubMethodArgumentList: MutableList<String> = mutableListOf(),
    var stubMethodArgumentListWithRequest: MutableList<String> = mutableListOf(),
    var stubMethodArgumentListForCustomResponse: MutableList<String> = mutableListOf(),
    var verifyMethodArgumentList: MutableList<String> = mutableListOf(),
    var verifyMethodArgumentListWithTimes: MutableList<String> = mutableListOf(),
    var verifyMethodArgumentListRequestParametersOnly: List<String> = mutableListOf()
) {
    fun getRequestParamModel(name: String): ArgumentModel = requestParameters.getOrPut(name) {ArgumentModel()}
    fun getPathVariableModel(name: String): ArgumentModel = pathVariables.getOrPut(name) {ArgumentModel()}
}

data class ArgumentModel(
    var name: String? = null,
    var type: String? = null,
    var iterable: Boolean = false,
    var optional: Boolean = false,
    var dateTimeFormatAnnotation: DateTimeFormatAnnotation? = null
) {
}

data class DateTimeFormatAnnotation(
    val iso: String?,
    val fallbackPatterns: Array<String>?,
    val pattern: String?,
    val style: String?,
    val clazz: String?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DateTimeFormatAnnotation

        if (iso != other.iso) return false
        if (!fallbackPatterns.contentEquals(other.fallbackPatterns)) return false
        if (pattern != other.pattern) return false
        if (style != other.style) return false

        return true
    }

    override fun hashCode(): Int {
        var result = iso.hashCode()
        result = 31 * result + fallbackPatterns.contentHashCode()
        result = 31 * result + pattern.hashCode()
        result = 31 * result + style.hashCode()
        return result
    }
}
