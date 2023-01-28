package io.lsdconsulting.stub.processor

import com.fasterxml.jackson.databind.ObjectMapper
import io.lsdconsulting.stub.annotation.GenerateWireMockStub
import io.lsdconsulting.stub.handler.RestControllerAnnotationHandler
import io.lsdconsulting.stub.model.ArgumentModel
import io.lsdconsulting.stub.model.Model
import io.lsdconsulting.stub.writer.StubWriter
import org.apache.commons.lang3.StringUtils.capitalize
import org.springframework.http.HttpMethod.*
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
        GenerateWireMockStub::class.java.canonicalName,
        RestController::class.java.canonicalName,
        GetMapping::class.java.canonicalName,
        PostMapping::class.java.canonicalName,
        PutMapping::class.java.canonicalName,
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

        val model = Model()

        messager.printMessage(NOTE, "annotations = $annotations")

        annotations.forEach { messager.printMessage(NOTE, "it.asType = ${it.asType()}") }

        val stubAnnotations = annotations.filter { it.asType().toString() == GenerateWireMockStub::class.qualifiedName }
        messager.printMessage(NOTE, "stubAnnotations = $stubAnnotations")

        val annotatedClasses = stubAnnotations.flatMap { roundEnv.getElementsAnnotatedWith(it) }
        messager.printMessage(NOTE, "annotatedClasses == $annotatedClasses")

        for (annotation in annotations) {
            messager.printMessage(NOTE, ">>>>>>>>>>>>>>>>>>>>>>>>>>>")
            messager.printMessage(NOTE, "Processing annotation = $annotation")
            val annotatedElements = roundEnv.getElementsAnnotatedWith(annotation)
            messager.printMessage(NOTE, "annotatedElements = $annotatedElements")
            annotatedElements
                .filter {
                    annotatedClasses.contains(it) || annotatedClasses.contains(it.enclosingElement) || annotatedClasses.contains(
                        it.enclosingElement.enclosingElement
                    ) || annotatedClasses.contains(it.enclosingElement.enclosingElement.enclosingElement)
                }
                .forEach { element: Element ->
                    if (element.getAnnotation(RestController::class.java) != null) {
                        messager.printMessage(NOTE, "Processing RestController annotation")
                        messager.printMessage(NOTE, "Finding controller model: ${element}")
                        val controllerModel = model.getControllerModel(element.toString())
                        restControllerAnnotationHandler.handle(element, controllerModel)
                    } else if (element.getAnnotation(GetMapping::class.java) != null) {
                        messager.printMessage(NOTE, "Processing GetMapping annotation")
                        val path: Array<String> = element.getAnnotation(GetMapping::class.java).path
                        val value: Array<String> = element.getAnnotation(GetMapping::class.java).value
                        val methodModelKey = element.toString()
                        messager.printMessage(NOTE, "methodModelKey = $methodModelKey")
                        val methodName = element.simpleName.toString()
                        messager.printMessage(NOTE, "methodName = $methodName")
                        messager.printMessage(NOTE, "Finding controller model: ${element.enclosingElement}")
                        val controllerModel = model.getControllerModel(element.enclosingElement.toString())
                        if (path.isNotEmpty()) {
                            controllerModel.getResourceModel(methodModelKey).subResource = path[0]
                        } else if (value.isNotEmpty()) {
                            controllerModel.getResourceModel(methodModelKey).subResource = value[0]
                        }
                        controllerModel.getResourceModel(methodModelKey).httpMethod = GET
                        controllerModel.getResourceModel(methodModelKey).methodName =
                            capitalize(methodName)

                        controllerModel.getResourceModel(methodModelKey).responseType =
                            element.asType().toString().replace(Regex("\\(.*\\)"), "")
                    } else if (element.getAnnotation(PostMapping::class.java) != null) {
                        messager.printMessage(NOTE, "Processing PostMapping annotation")
                        val path: Array<String> = element.getAnnotation(PostMapping::class.java).path
                        val value: Array<String> = element.getAnnotation(PostMapping::class.java).value
                        val methodModelKey = element.toString()
                        messager.printMessage(NOTE, "methodModelKey = $methodModelKey")
                        val methodName = element.simpleName.toString()
                        messager.printMessage(NOTE, "methodName = $methodName")
                        messager.printMessage(NOTE, "Finding controller model: ${element.enclosingElement}")
                        val controllerModel = model.getControllerModel(element.enclosingElement.toString())
                        if (path.isNotEmpty()) {
                            controllerModel.getResourceModel(methodModelKey).subResource = path[0]
                        } else if (value.isNotEmpty()) {
                            controllerModel.getResourceModel(methodModelKey).subResource = value[0]
                        }
                        controllerModel.getResourceModel(methodModelKey).httpMethod = POST
                        controllerModel.getResourceModel(methodModelKey).methodName =
                            capitalize(methodName)

                        controllerModel.getResourceModel(methodModelKey).responseType =
                            element.asType().toString().replace(Regex("\\(.*\\)"), "")
                    } else if (element.getAnnotation(PutMapping::class.java) != null) {
                        messager.printMessage(NOTE, "Processing PutMapping annotation")
                        val path: Array<String> = element.getAnnotation(PutMapping::class.java).path
                        val value: Array<String> = element.getAnnotation(PutMapping::class.java).value
                        val methodModelKey = element.toString()
                        messager.printMessage(NOTE, "methodModelKey = $methodModelKey")
                        val methodName = element.simpleName.toString()
                        messager.printMessage(NOTE, "methodName = $methodName")
                        messager.printMessage(NOTE, "Finding controller model: ${element.enclosingElement}")
                        val controllerModel = model.getControllerModel(element.enclosingElement.toString())
                        if (path.isNotEmpty()) {
                            controllerModel.getResourceModel(methodModelKey).subResource = path[0]
                        } else if (value.isNotEmpty()) {
                            controllerModel.getResourceModel(methodModelKey).subResource = value[0]
                        }
                        controllerModel.getResourceModel(methodModelKey).httpMethod = PUT
                        controllerModel.getResourceModel(methodModelKey).methodName =
                            capitalize(methodName)
                    } else if (element.getAnnotation(RequestParam::class.java) != null) {
                        messager.printMessage(NOTE, "Processing RequestParam annotation")

                        val methodName = element.enclosingElement.toString()
                        messager.printMessage(NOTE, "methodName = $methodName")

                        val argumentName = element.simpleName.toString()
                        val argumentType = element.asType().toString()
                        messager.printMessage(NOTE, "argumentName = $argumentName")
                        messager.printMessage(NOTE, "argumentType = $argumentType")

                        messager.printMessage(
                            NOTE,
                            "Finding controller model: ${element.enclosingElement.enclosingElement}"
                        )
                        val controllerModel =
                            model.getControllerModel(element.enclosingElement.enclosingElement.toString())
                        controllerModel.getResourceModel(methodName).getArgumentModel(argumentName).type = argumentType
                        controllerModel.getResourceModel(methodName).getArgumentModel(argumentName).name = argumentName
                    } else if (element.getAnnotation(PathVariable::class.java) != null) {
                        messager.printMessage(NOTE, "Processing PathVariable annotation")

                        val methodName = element.enclosingElement.toString()
                        messager.printMessage(NOTE, "methodName = $methodName")

                        val argumentName = element.simpleName.toString()
                        val argumentType = element.asType().toString()
                        messager.printMessage(NOTE, "argumentName = $argumentName")
                        messager.printMessage(NOTE, "argumentType = $argumentType")

                        messager.printMessage(
                            NOTE,
                            "Finding controller model: ${element.enclosingElement.enclosingElement}"
                        )
                        val controllerModel =
                            model.getControllerModel(element.enclosingElement.enclosingElement.toString())
                        controllerModel.getResourceModel(methodName).urlHasPathVariable = true
                        controllerModel.getResourceModel(methodName).getPathVariableModel(argumentName).type =
                            argumentType
                        controllerModel.getResourceModel(methodName).getPathVariableModel(argumentName).name =
                            argumentName
                    } else if (element.getAnnotation(RequestBody::class.java) != null) {
                        messager.printMessage(NOTE, "Processing RequestBody annotation")

                        val methodName = element.enclosingElement.toString()
                        messager.printMessage(NOTE, "methodName = $methodName")

                        val argumentName = element.simpleName.toString()
                        val argumentType = element.asType().toString()
                        messager.printMessage(NOTE, "argumentName = $argumentName")
                        messager.printMessage(NOTE, "argumentType = $argumentType")

                        messager.printMessage(
                            NOTE,
                            "Finding controller model: ${element.enclosingElement.enclosingElement}"
                        )
                        val controllerModel =
                            model.getControllerModel(element.enclosingElement.enclosingElement.toString())
                        controllerModel.getResourceModel(methodName).urlHasPathVariable = true
                        val requestBody = ArgumentModel(type = argumentType, name = argumentName)
                        controllerModel.getResourceModel(methodName).requestBody = requestBody
                    } else {
                        messager.printMessage(NOTE, "Unknown annotation")
                    }
                    messager.printMessage(NOTE, "Elements end -------------------------")
                }
            messager.printMessage(NOTE, "Annotations end ++++++++++++++++++++++++++")
        }

        // Post-processing
        model.controllers.values.forEach { controllerModel ->
            controllerModel.resources.values.forEach { annotatedMethod ->
                if (annotatedMethod.urlHasPathVariable) {
                    annotatedMethod.pathVariables.values.forEach { pathVariable ->
                        annotatedMethod.subResource =
                            annotatedMethod.subResource?.replace("{${pathVariable.name}}", "%s")
                    }
                }
            }
        }

        messager.printMessage(NOTE, "")
        messager.printMessage(NOTE, "")
        messager.printMessage(NOTE, "")
        messager.printMessage(NOTE, "")
        messager.printMessage(NOTE, "Writing files for model:${objectWriter.writeValueAsString(model)}")

        if (annotations.isNotEmpty()) {
            stubWriter.writeStubFile(model)
        }

        return true
    }
}
