package io.lsdconsulting.stub.writer

import io.lsdconsulting.stub.model.ControllerModel
import io.lsdconsulting.stub.model.Model
import io.pebbletemplates.pebble.PebbleEngine
import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.io.Writer
import java.nio.file.Files
import java.nio.file.Path
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic.Kind.NOTE

class StubWriter(processingEnv: ProcessingEnvironment) {
    private val engine: PebbleEngine = PebbleEngine.Builder().build()
    private val stubTemplate = engine.getTemplate("templates/Stub.peb")
    private val processingEnv: ProcessingEnvironment
    private var messager: Messager

    init {
        this.processingEnv = processingEnv
        messager = processingEnv.messager
    }

    fun writeStubFile(model: Model) {
        model.controllers.values.forEach { controllerModel ->
            try {
                messager.printMessage(NOTE, "Generating stub file: ${controllerModel.stubFullyQualifiedName}")
                val builderFile = processingEnv.filer.createSourceFile(controllerModel.stubFullyQualifiedName)
                writeFileForCompilation(builderFile.openWriter(), controllerModel)

                val path = createLocationForPackaging(builderFile.toUri().path, controllerModel.stubClassName)
                writeFileForPackaging(path.toFile(), controllerModel)
            } catch (e: IOException) {
                e.printStackTrace()
                throw e
            }
        }
    }

    private fun createLocationForPackaging(compilationPath: String, stubClassName: String?): Path {
        val stubBasePathName = compilationPath
            .replace("generated/source/kapt/main", "generated-stub-sources")
            .replace("generated/sources/annotationProcessor/java/main", "generated-stub-sources")
        val directory: String = stubBasePathName.replace("$stubClassName.java", "")
        Files.createDirectories(Path.of(directory))
        Files.deleteIfExists(Path.of(stubBasePathName))
        return Files.createFile(Path.of(stubBasePathName))
    }

    private fun writeFileForPackaging(file: File, controllerModel: ControllerModel) =
        PrintWriter(file).use { writer ->
            stubTemplate.evaluate(writer, mapOf("model" to controllerModel))
        }

    // The file for packaging needs to be placed in a separate location, so it doesn't get mixed up with other generated files
    private fun writeFileForCompilation(openWriter1: Writer, controllerModel: ControllerModel) =
        PrintWriter(openWriter1).use { writer ->
            stubTemplate.evaluate(writer, mapOf("model" to controllerModel))
        }
}