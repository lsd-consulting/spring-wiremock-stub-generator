package io.lsdconsulting.stub.processor

import io.lsdconsulting.stub.handler.RestControllerAnnotationHandler
import io.lsdconsulting.stub.model.ControllerModel
import io.lsdconsulting.stub.writer.StubWriter
import org.apache.commons.lang3.StringUtils.capitalize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@SupportedSourceVersion(SourceVersion.RELEASE_11)
class ControllerProcessor : AbstractProcessor() {
    private lateinit var restControllerAnnotationHandler: RestControllerAnnotationHandler
    private lateinit var messager: Messager
    private lateinit var stubWriter: StubWriter

    @Synchronized
    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        val elementUtils = processingEnv.elementUtils
        messager = processingEnv.messager
        restControllerAnnotationHandler = RestControllerAnnotationHandler(elementUtils)
        stubWriter = StubWriter(processingEnv)
    }

    override fun getSupportedAnnotationTypes() = mutableSetOf(
        Controller::class.java.canonicalName,
        RestController::class.java.canonicalName,
        GetMapping::class.java.canonicalName,
        PostMapping::class.java.canonicalName,
        ResponseBody::class.java.canonicalName,
        RequestBody::class.java.canonicalName,
        RequestParam::class.java.canonicalName
    )

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.RELEASE_17
    }

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        val controllerModel = ControllerModel()

        for (annotation in annotations) {
            val annotatedElements = roundEnv.getElementsAnnotatedWith(annotation)
            annotatedElements.forEach { element: Element ->
                if (element.getAnnotation(RestController::class.java) != null) {
                    messager.printMessage(Diagnostic.Kind.NOTE, "Processing RestController annotation")
                    restControllerAnnotationHandler.handle(element, controllerModel)
                } else if (element.getAnnotation(GetMapping::class.java) != null) {
                    messager.printMessage(Diagnostic.Kind.NOTE, "Processing GetMapping annotation")
                    val path: Array<String> = element.getAnnotation(GetMapping::class.java).path
                    val value: Array<String> = element.getAnnotation(GetMapping::class.java).value
                    if (path.isNotEmpty()) {
                        controllerModel.subResource = path[0]
                    } else if (value.isNotEmpty()) {
                        controllerModel.subResource = value[0]
                    }
                    controllerModel.methodName = capitalize(element.simpleName.toString())

                    controllerModel.responseType = element.asType().toString().replace("()", "")
                } else {
                    messager.printMessage(Diagnostic.Kind.NOTE, "Unknown annotation")
                }
            }
        }

        if (annotations.isNotEmpty()) {
            messager.printMessage(Diagnostic.Kind.NOTE, "Writing files for model:$controllerModel")
            stubWriter.writeStubFile(controllerModel)
            stubWriter.writeStubBaseFile(controllerModel)
        }

        return true
    }
}
