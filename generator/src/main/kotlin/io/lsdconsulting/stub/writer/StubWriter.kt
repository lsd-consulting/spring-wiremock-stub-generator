package io.lsdconsulting.stub.writer

import com.mitchellbosecke.pebble.PebbleEngine
import io.lsdconsulting.stub.model.ControllerModel
import java.io.IOException
import java.io.PrintWriter
import java.nio.file.Files
import java.nio.file.Path
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic

class StubWriter(processingEnv: ProcessingEnvironment) {
    private val engine: PebbleEngine = PebbleEngine.Builder().build()
    private val stubTemplate = engine.getTemplate("templates/Stub.tpl")
    private val stubBaseTemplate = engine.getTemplate("templates/StubBase.tpl")
    private val processingEnv: ProcessingEnvironment
    private var messager: Messager

    init {
        this.processingEnv = processingEnv
        messager = processingEnv.messager
    }

    fun writeStubBaseFile(controllerModel: ControllerModel) {
        try {
            val builderFile = processingEnv.filer.createSourceFile(controllerModel.stubBaseFullyQualifiedName)
            val stubBasePathName = builderFile.toUri().path.replace(
                "generated/source/kapt/main",
                "generated-stub-sources"
            )
            val directory = stubBasePathName.replace("StubBase.java", "")
            messager.printMessage(Diagnostic.Kind.NOTE, "Creating directory:$directory")
            Files.createDirectories(Path.of(directory))
            messager.printMessage(Diagnostic.Kind.NOTE, "Creating file:$stubBasePathName")
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


    fun writeStubFile(controllerModel: ControllerModel) {
        try {
            val builderFile = processingEnv.filer.createSourceFile(controllerModel.stubFullyQualifiedName)
            messager.printMessage(Diagnostic.Kind.NOTE, "builderFile:$builderFile")
            messager.printMessage(Diagnostic.Kind.NOTE, "builderFile.toUri().path:${builderFile.toUri().path}")
            val stubBasePathName = builderFile.toUri().path.replace(
                "generated/source/kapt/main",
                "generated-stub-sources"
            )
            messager.printMessage(Diagnostic.Kind.NOTE, "stubBasePathName:$stubBasePathName")
            val directory: String = stubBasePathName.replace(controllerModel.stubClassName + ".java", "")
            messager.printMessage(Diagnostic.Kind.NOTE, "Creating directory:$directory")
            Files.createDirectories(Path.of(directory))
            messager.printMessage(Diagnostic.Kind.NOTE, "Creating file:$stubBasePathName")
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