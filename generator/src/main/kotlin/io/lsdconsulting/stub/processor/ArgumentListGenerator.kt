package io.lsdconsulting.stub.processor

import io.lsdconsulting.stub.model.ResourceModel

fun stubArgumentList(annotatedMethod: ResourceModel): MutableList<String> {
    val stubMethodArgumentList = mutableListOf<String>()
    annotatedMethod.responseType?.let {
        stubMethodArgumentList.add(annotatedMethod.responseType + " response")
    }
    stubMethodArgumentList.addAll(pathVariables(annotatedMethod))
    stubMethodArgumentList.addAll(requestParameters(annotatedMethod))
    return stubMethodArgumentList
}

fun stubArgumentListWithRequest(annotatedMethod: ResourceModel): MutableList<String> {
    val stubMethodArgumentList = mutableListOf<String>()
    annotatedMethod.requestBody?.let {
        stubMethodArgumentList.add("${annotatedMethod.requestBody!!.type} ${annotatedMethod.requestBody!!.name}")
    }
    stubMethodArgumentList.addAll(stubArgumentList(annotatedMethod))
    return stubMethodArgumentList
}

fun stubArgumentListForCustomResponse(annotatedMethod: ResourceModel): MutableList<String> {
    val stubMethodArgumentList = mutableListOf<String>()
    stubMethodArgumentList.add("int httpStatus")
    stubMethodArgumentList.add("String response")
    stubMethodArgumentList.addAll(pathVariables(annotatedMethod))
    stubMethodArgumentList.addAll(requestParameters(annotatedMethod))
    return stubMethodArgumentList
}

fun pathVariables(annotatedMethod: ResourceModel) =
    annotatedMethod.pathVariables.map { "${it.value.type} ${it.value.name}" }

private fun requestParameters(annotatedMethod: ResourceModel) =
    annotatedMethod.requestParameters.map {
        val type = if ("java.lang.String\\[]".toRegex().containsMatchIn(it.value.type!!)) {
            "java.util.List<String>"
        } else {
            "${it.value.type}"
        }
        "$type ${it.value.name}"
    }

private fun requestHeaders(annotatedMethod: ResourceModel) =
    annotatedMethod.requestHeaders.map { "${it.value.type} ${it.value.name}" }

fun verifyArgumentList(annotatedMethod: ResourceModel): MutableList<String> {
    val stubMethodArgumentList = mutableListOf<String>()
    stubMethodArgumentList.addAll(pathVariables(annotatedMethod))
    stubMethodArgumentList.addAll(requestParameters(annotatedMethod))
    stubMethodArgumentList.addAll(requestHeaders(annotatedMethod))
    annotatedMethod.requestBody?.let {
        stubMethodArgumentList.add("${annotatedMethod.requestBody!!.type} ${annotatedMethod.requestBody!!.name}")
    }
    return stubMethodArgumentList
}

fun verifyArgumentListWithTimes(annotatedMethod: ResourceModel): MutableList<String> {
    val stubMethodArgumentList = mutableListOf<String>()
    stubMethodArgumentList.add("final int times")
    stubMethodArgumentList.addAll(verifyArgumentList(annotatedMethod))
    return stubMethodArgumentList
}

fun verifyArgumentListWithTimesWithoutBody(annotatedMethod: ResourceModel): MutableList<String> {
    val stubMethodArgumentList = mutableListOf<String>()
    stubMethodArgumentList.add("final int times")
    stubMethodArgumentList.addAll(pathVariables(annotatedMethod))
    stubMethodArgumentList.addAll(requestParameters(annotatedMethod))
    stubMethodArgumentList.addAll(requestHeaders(annotatedMethod))
    return stubMethodArgumentList
}

fun verifyStubCallArgumentList(annotatedMethod: ResourceModel): MutableList<String> {
    val stubMethodArgumentList = mutableListOf<String>()
    stubMethodArgumentList.add("ONCE")
    stubMethodArgumentList.addAll(annotatedMethod.pathVariables.map { "${it.value.name}" })
    stubMethodArgumentList.addAll(annotatedMethod.requestParameters.map { "${it.value.name}" })
    stubMethodArgumentList.addAll(annotatedMethod.requestHeaders.map { "${it.value.name}" })
    annotatedMethod.requestBody?.let {
        stubMethodArgumentList.add("${annotatedMethod.requestBody!!.name}")
    }
    return stubMethodArgumentList
}
