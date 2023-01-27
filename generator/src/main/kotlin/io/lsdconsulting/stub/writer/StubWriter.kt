package io.lsdconsulting.stub.writer

import io.lsdconsulting.stub.model.Model
import io.pebbletemplates.pebble.PebbleEngine
import java.io.IOException
import java.io.PrintWriter
import java.nio.file.Files
import java.nio.file.Path
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic.Kind.NOTE

class StubWriter(processingEnv: ProcessingEnvironment) {
    private val engine: PebbleEngine = PebbleEngine.Builder().build()
    private val stubTemplate = engine.getTemplate("templates/Stub.peb")
    private val stubBaseTemplate = engine.getTemplate("templates/StubBase.peb")
    private val processingEnv: ProcessingEnvironment
    private var messager: Messager

    init {
        this.processingEnv = processingEnv
        messager = processingEnv.messager
    }

    fun writeStubBaseFile(model: Model) {
        val controllerModel = model.controllers.values.first()
        try {
            val builderFile = processingEnv.filer.createSourceFile(controllerModel.stubBaseFullyQualifiedName)
            val stubBasePathName = builderFile.toUri().path
                .replace("generated/source/kapt/main", "generated-stub-sources")
                .replace("generated/sources/annotationProcessor/java/main", "generated-stub-sources")
            val directory = stubBasePathName.replace("StubBase.java", "")
            messager.printMessage(NOTE, "Creating directory:$directory")
            Files.createDirectories(Path.of(directory))
            messager.printMessage(NOTE, "Creating file:$stubBasePathName")
            val path = Files.createFile(Path.of(stubBasePathName))
            PrintWriter(builderFile.openWriter()).use { writer ->
                stubBaseTemplate.evaluate(writer, mapOf("packageName" to controllerModel.packageName))
            }
            PrintWriter(path.toFile()).use { writer ->
                stubBaseTemplate.evaluate(writer, mapOf("packageName" to controllerModel.packageName))
            }
        } catch (e: IOException) {
            e.printStackTrace()
            throw e
        }
    }

    fun writeStubFile(model: Model) {
        model.controllers.values.forEach { controllerModel ->
            try {
                val builderFile = processingEnv.filer.createSourceFile(controllerModel.stubFullyQualifiedName)
                messager.printMessage(NOTE, "builderFile:$builderFile")
                messager.printMessage(NOTE, "builderFile.toUri().path:${builderFile.toUri().path}")
                val stubBasePathName = builderFile.toUri().path
                    .replace("generated/source/kapt/main", "generated-stub-sources")
                    .replace("generated/sources/annotationProcessor/java/main", "generated-stub-sources")
                messager.printMessage(NOTE, "stubBasePathName:$stubBasePathName")
                val directory: String = stubBasePathName.replace(controllerModel.stubClassName + ".java", "")
                messager.printMessage(NOTE, "Creating directory:$directory")
                Files.createDirectories(Path.of(directory))
                messager.printMessage(NOTE, "Creating file:$stubBasePathName")
                val path = Files.createFile(Path.of(stubBasePathName))
                PrintWriter(builderFile.openWriter()).use { writer ->
                    stubTemplate.evaluate(writer, mapOf("model" to controllerModel))
                }
                PrintWriter(path.toFile()).use { writer ->
                    stubTemplate.evaluate(writer, mapOf("model" to controllerModel))
                }
            } catch (e: IOException) {
                e.printStackTrace()
                throw e
            }
        }
    }
}