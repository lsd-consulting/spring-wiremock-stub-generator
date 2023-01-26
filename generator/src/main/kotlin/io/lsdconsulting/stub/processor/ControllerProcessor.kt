package io.lsdconsulting.stub.processor

import com.fasterxml.jackson.databind.ObjectMapper
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
import javax.tools.Diagnostic.Kind.NOTE

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
        RequestParam::class.java.canonicalName,
        PathVariable::class.java.canonicalName
    )

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.RELEASE_11
    }

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        val objectWriter = ObjectMapper().writerWithDefaultPrettyPrinter()

        val controllerModel = ControllerModel()

        messager.printMessage(NOTE, "annotations = $annotations")

        for (annotation in annotations) {
            messager.printMessage(NOTE, ">>>>>>>>>>>>>>>>>>>>>>>>>>>")
            messager.printMessage(NOTE, "Processing annotation = $annotation")
            val annotatedElements = roundEnv.getElementsAnnotatedWith(annotation)
            messager.printMessage(NOTE, "annotatedElements = $annotatedElements")
            annotatedElements.forEach { element: Element ->
                messager.printMessage(NOTE, "##############################")
                messager.printMessage(NOTE, "Processing element = $element")
                messager.printMessage(NOTE, "enclosingElement = ${element.enclosingElement}")
                messager.printMessage(
                    NOTE,
                    "enclosingElement.simpleName = ${element.enclosingElement.simpleName}"
                )
                messager.printMessage(NOTE, "enclosedElements = ${element.enclosedElements}")
                if (element.getAnnotation(RestController::class.java) != null) {
                    messager.printMessage(NOTE, "Processing RestController annotation")
                    restControllerAnnotationHandler.handle(element, controllerModel)
                } else if (element.getAnnotation(GetMapping::class.java) != null) {
                    messager.printMessage(NOTE, "Processing GetMapping annotation")
                    val path: Array<String> = element.getAnnotation(GetMapping::class.java).path
                    val value: Array<String> = element.getAnnotation(GetMapping::class.java).value
                    val methodModelKey = element.toString()
                    messager.printMessage(NOTE, "methodModelKey = $methodModelKey")
                    val methodName = element.simpleName.toString()
                    messager.printMessage(NOTE, "methodName = $methodName")
                    if (path.isNotEmpty()) {
                        controllerModel.getMethodModel(methodModelKey).subResource = path[0]
                    } else if (value.isNotEmpty()) {
                        controllerModel.getMethodModel(methodModelKey).subResource = value[0]
                    }
                    controllerModel.getMethodModel(methodModelKey).methodName =
                        capitalize(methodName)

                    controllerModel.getMethodModel(methodModelKey).responseType =
                        element.asType().toString().replace(Regex("\\(.*\\)"), "")
                } else if (element.getAnnotation(RequestParam::class.java) != null) {
                    messager.printMessage(NOTE, "Processing RequestParam annotation")

                    val methodName = element.enclosingElement.toString()
                    messager.printMessage(NOTE, "methodName = $methodName")

                    val argumentName = element.simpleName.toString()
                    val argumentType = element.asType().toString()
                    messager.printMessage(NOTE, "argumentName = $argumentName")
                    messager.printMessage(NOTE, "argumentType = $argumentType")

                    controllerModel.getMethodModel(methodName).getArgumentModel(argumentName).type = argumentType
                    controllerModel.getMethodModel(methodName).getArgumentModel(argumentName).name = argumentName
                } else {
                    messager.printMessage(NOTE, "Unknown annotation")
                }
//                messager.printMessage(NOTE, "controllerModel:${objectWriter.writeValueAsString(controllerModel)}")
                messager.printMessage(NOTE, "Elements end -------------------------")
            }
            messager.printMessage(NOTE, "Annotations end ++++++++++++++++++++++++++")
        }

        messager.printMessage(NOTE, "")
        messager.printMessage(NOTE, "")
        messager.printMessage(NOTE, "")
        messager.printMessage(NOTE, "")
        messager.printMessage(NOTE, "Writing files for model:${objectWriter.writeValueAsString(controllerModel)}")

        if (annotations.isNotEmpty()) {
//            messager.printMessage(NOTE, "Writing files for model:${objectWriter.writeValueAsString(controllerModel)}")
            stubWriter.writeStubFile(controllerModel)
            stubWriter.writeStubBaseFile(controllerModel)
        }

        return true
    }
}
