package io.lsdconsulting.stub.processor

import javax.lang.model.element.Element

fun getArgumentType(element: Element): String {
    var argumentType = element.asType().toString()
    element.asType().annotationMirrors.forEach {
        argumentType = argumentType.replace(it.toString(), "")
    }
    return argumentType.replace(",", "").trim()
}

fun getGeneric(type: String): String {
    return if (type.contains("<") && type.contains(">")) {
        type.substring(type.indexOf("<") + 1, type.lastIndexOf(">"))
    } else if (type.contains("&lt;") && type.contains("&gt;")) {
        type.substring(type.indexOf("&lt;") + 4, type.lastIndexOf("&gt;"))
    } else {
        type
    }
}

fun replacePrimitive(originalArgument: String): String {
    return when (originalArgument) {
        "boolean" -> "Boolean"
        "int" -> "Integer"
        "long" -> "Long"
        "byte" -> "Byte"
        "short" -> "Short"
        "float" -> "Float"
        "double" -> "Double"
        "char" -> "Char"
        else -> originalArgument
    }
}