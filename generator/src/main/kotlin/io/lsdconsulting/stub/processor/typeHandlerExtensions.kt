package io.lsdconsulting.stub.processor

import javax.lang.model.element.Element

fun Element.retrieveArgumentType(): String {
    var argumentType = this.asType().toString()
    this.asType().annotationMirrors.forEach {
        argumentType = argumentType.replace(it.toString(), "")
    }
    return argumentType.replace(",", "").trim()
}

fun String.retrieveGeneric() =
    if (this.contains("<") and this.contains(">")) {
        this.substring(this.indexOf("<") + 1, this.lastIndexOf(">"))
    } else if (this.contains("&lt;") and this.contains("&gt;")) {
        this.substring(this.indexOf("&lt;") + 4, this.lastIndexOf("&gt;"))
    } else {
        this
    }

fun String.removeResponseEntity() =
    if (this.contains("ResponseEntity<") and this.contains(">")) {
        this.substring(this.indexOf("ResponseEntity<") + 15, this.lastIndexOf(">"))
    } else if (this.contains("ResponseEntity&lt;") and this.contains("&gt;")) {
        this.substring(this.indexOf("ResponseEntity&lt;") + 18, this.lastIndexOf("&gt;"))
    } else {
        this
    }

fun String.replacePrimitive() =
    when (this) {
        "boolean" -> "Boolean"
        "int" -> "Integer"
        "long" -> "Long"
        "byte" -> "Byte"
        "short" -> "Short"
        "float" -> "Float"
        "double" -> "Double"
        "char" -> "Char"
        else -> this
    }

fun String.retrieveResponseType(): String? {
    val responseType = this.replace(Regex("\\(.*\\)"), "")
    return if (responseType.equals("void", true)) null else responseType
}
