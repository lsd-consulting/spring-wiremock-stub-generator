package io.lsdconsulting.stub.processor

import javax.lang.model.element.Element

fun getArgumentType(element: Element): String {
    var argumentType = element.asType().toString()
    element.asType().annotationMirrors.forEach {
        argumentType = argumentType.replace(it.toString(), "")
    }
    return argumentType.replace(",", "").trim()
}
