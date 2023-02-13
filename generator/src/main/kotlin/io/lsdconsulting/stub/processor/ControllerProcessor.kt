package io.lsdconsulting.stub.processor

import io.lsdconsulting.stub.annotation.GenerateWireMockStub
import io.lsdconsulting.stub.handler.MethodMappingAnnotationHandler
import io.lsdconsulting.stub.handler.RestControllerAnnotationHandler
import io.lsdconsulting.stub.model.ArgumentModel
import io.lsdconsulting.stub.model.DateTimeFormatAnnotation
import io.lsdconsulting.stub.model.Model
import io.lsdconsulting.stub.writer.StubWriter
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpMethod.*
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.web.bind.annotation.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind.CLASS
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic.Kind.NOTE

@SupportedSourceVersion(SourceVersion.RELEASE_11)
class ControllerProcessor : AbstractProcessor() {
    private lateinit var restControllerAnnotationHandler: RestControllerAnnotationHandler
    private lateinit var messager: Messager
    private lateinit var stubWriter: StubWriter
    private var methodMappingAnnotationHandler = MethodMappingAnnotationHandler()
    private var postProcessor = PostProcessor()

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
        DeleteMapping::class.java.canonicalName,
        RequestMapping::class.java.canonicalName,
        ResponseBody::class.java.canonicalName,
        RequestBody::class.java.canonicalName,
        RequestParam::class.java.canonicalName,
        PathVariable::class.java.canonicalName,
        ResponseStatus::class.java.canonicalName,
        DateTimeFormat::class.java.canonicalName
    )

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.RELEASE_11
    }

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {

        val model = Model()

        val stubAnnotations = annotations.filter { it.asType().toString() == GenerateWireMockStub::class.qualifiedName }
        if (stubAnnotations.isNotEmpty()) {
            messager.printMessage(NOTE, "Analysing source code for WireMock stubs")
        }
        val annotatedClasses = stubAnnotations.flatMap { roundEnv.getElementsAnnotatedWith(it) }

        for (annotation in annotations) {
            val annotatedElements = roundEnv.getElementsAnnotatedWith(annotation)
            annotatedElements
                .filter(elementsBelongingToAnnotatedClasses(annotatedClasses))
                .forEach { element: Element ->
                    if (element.getAnnotation(RestController::class.java) != null) {
                        val controllerModel = model.getControllerModel(element.toString())
                        restControllerAnnotationHandler.handle(element, controllerModel)
                    }
                    if (element.getAnnotation(GetMapping::class.java) != null) {
                        methodMappingAnnotationHandler.handle(
                            element = element,
                            model = model,
                            path = element.getAnnotation(GetMapping::class.java).path,
                            value = element.getAnnotation(GetMapping::class.java).value,
                            httpMethod = GET,
                            responseType = element.asType().toString().retrieveResponseType()?.removeResponseEntity()
                        )
                    }
                    if (element.getAnnotation(PostMapping::class.java) != null) {
                        methodMappingAnnotationHandler.handle(
                            element = element,
                            model = model,
                            path = element.getAnnotation(PostMapping::class.java).path,
                            value = element.getAnnotation(PostMapping::class.java).value,
                            httpMethod = POST,
                            responseType = element.asType().toString().retrieveResponseType()?.removeResponseEntity()
                        )
                    }
                    if (element.getAnnotation(PutMapping::class.java) != null) {
                        methodMappingAnnotationHandler.handle(
                            element = element,
                            model = model,
                            path = element.getAnnotation(PutMapping::class.java).path,
                            value = element.getAnnotation(PutMapping::class.java).value,
                            httpMethod = PUT
                        )
                    }
                    if (element.getAnnotation(DeleteMapping::class.java) != null) {
                        methodMappingAnnotationHandler.handle(
                            element = element,
                            model = model,
                            path = element.getAnnotation(DeleteMapping::class.java).path,
                            value = element.getAnnotation(DeleteMapping::class.java).value,
                            httpMethod = DELETE
                        )
                    }
                    if (element.getAnnotation(RequestMapping::class.java) != null) {
                        val path: Array<String> = element.getAnnotation(RequestMapping::class.java).path
                        val value: Array<String> = element.getAnnotation(RequestMapping::class.java).value
                        val controllerModel = model.getControllerModel(element.toString())
                        if (path.isNotEmpty()) {
                            controllerModel.rootResource = path[0]
                        } else if (value.isNotEmpty()) {
                            controllerModel.rootResource = value[0]
                        }
                    }
                    if (element.getAnnotation(ResponseStatus::class.java) != null) {
                        val responseStatusAnnotation = element.getAnnotation(ResponseStatus::class.java)
                        val value: HttpStatus =
                            if (responseStatusAnnotation.code != INTERNAL_SERVER_ERROR) responseStatusAnnotation.code
                            else responseStatusAnnotation.value
                        if (element.kind == CLASS) {
                            val controllerModel = model.getControllerModel(element.toString())
                            controllerModel.responseStatus = value.value()
                        } else {
                            val methodModelKey = element.toString()
                            val controllerModel = model.getControllerModel(element.enclosingElement.toString())
                            controllerModel.getResourceModel(methodModelKey).responseStatus = value.value()
                        }
                    }
                    if (element.getAnnotation(RequestParam::class.java) != null) {
                        val requestParam = element.getAnnotation(RequestParam::class.java)
                        val argumentName = firstNotNull(requestParam.name, requestParam.value, element.simpleName.toString())
                        val methodName = element.enclosingElement.toString()
                        val argumentType = retrieveArgumentType(element).replacePrimitive()
                        val controllerModel = model.getControllerModel(element.enclosingElement.enclosingElement.toString())
                        controllerModel.getResourceModel(methodName).getRequestParamModel(argumentName).type = argumentType
                        controllerModel.getResourceModel(methodName).getRequestParamModel(argumentName).name = argumentName
                        controllerModel.getResourceModel(methodName).getRequestParamModel(argumentName).optional = !requestParam.required

                        if ("java.util.Set<(.*)>|java.util.List<(.*)>".toRegex().containsMatchIn(argumentType)) {
                            controllerModel.getResourceModel(methodName).hasMultiValueRequestParams = true
                            controllerModel.getResourceModel(methodName).getRequestParamModel(argumentName).iterable = true
                        }
                    }
                    if (element.getAnnotation(PathVariable::class.java) != null) {
                        val pathVariable = element.getAnnotation(PathVariable::class.java)
                        val argumentName = firstNotNull(pathVariable.name, pathVariable.value, element.simpleName.toString())
                        val methodName = element.enclosingElement.toString()
                        val argumentType = retrieveArgumentType(element)
                        val controllerModel = model.getControllerModel(element.enclosingElement.enclosingElement.toString())
                        controllerModel.getResourceModel(methodName).urlHasPathVariable = true
                        controllerModel.getResourceModel(methodName).getPathVariableModel(argumentName).type = argumentType
                        controllerModel.getResourceModel(methodName).getPathVariableModel(argumentName).name = argumentName
                    }
                    if (element.getAnnotation(RequestBody::class.java) != null) {
                        val methodName = element.enclosingElement.toString()
                        val argumentName = element.simpleName.toString()
                        val argumentType = retrieveArgumentType(element)
                        val controllerModel = model.getControllerModel(element.enclosingElement.enclosingElement.toString())
                        val requestBody = ArgumentModel(type = argumentType, name = argumentName)
                        controllerModel.getResourceModel(methodName).requestBody = requestBody
                    }
                    if (element.getAnnotation(DateTimeFormat::class.java) != null) {
                        val dateTimeFormatAnnotation = element.getAnnotation(DateTimeFormat::class.java)
                        val methodName = element.enclosingElement.toString()
                        val argumentName = element.simpleName.toString()
                        val controllerModel = model
                            .getControllerModel(element.enclosingElement.enclosingElement.toString())
                            .getResourceModel(methodName)
                            .getRequestParamModel(argumentName)
                        controllerModel.dateTimeFormatAnnotation =
                            DateTimeFormatAnnotation(
                                iso = dateTimeFormatAnnotation.iso.name,
                                fallbackPatterns = dateTimeFormatAnnotation.fallbackPatterns,
                                pattern = dateTimeFormatAnnotation.pattern,
                                style = dateTimeFormatAnnotation.style,
                                clazz = retrieveArgumentType(element).retrieveGeneric()
                            )
                    }
                }
        }

        postProcessor.update(model)

        if (model.controllers.isNotEmpty()) {
            stubWriter.writeStubFile(model)
        }

        if (stubAnnotations.isNotEmpty()) {
            messager.printMessage(NOTE, "Finished generating WireMock stubs")
        }

        return true
    }

    private fun firstNotNull(vararg elements: String) = elements.first { it.isNotEmpty() }

    private fun elementsBelongingToAnnotatedClasses(annotatedClasses: List<Element>): (Element) -> Boolean =
        {
            annotatedClasses.contains(it) ||
                    annotatedClasses.contains(it.enclosingElement) ||
                    annotatedClasses.contains(it.enclosingElement.enclosingElement) ||
                    annotatedClasses.contains(it.enclosingElement.enclosingElement.enclosingElement)
        }
}
