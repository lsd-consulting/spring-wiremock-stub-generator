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

@SupportedSourceVersion(SourceVersion.RELEASE_17)
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
        DateTimeFormat::class.java.canonicalName,
        RequestHeader::class.java.canonicalName,
    )

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.RELEASE_17
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
                    element.getAnnotation(RestController::class.java)?.let {
                        val controllerModel = model.getControllerModel(element.toString())
                        restControllerAnnotationHandler.handle(element, controllerModel)
                    }
                    element.getAnnotation(GetMapping::class.java)?.let {
                        methodMappingAnnotationHandler.handle(
                            element = element,
                            model = model,
                            path = element.getAnnotation(GetMapping::class.java).path,
                            value = element.getAnnotation(GetMapping::class.java).value,
                            httpMethod = GET,
                            responseType = element.asType().toString().retrieveResponseType()?.removeResponseEntity()
                        )
                    }
                    element.getAnnotation(PostMapping::class.java)?.let {
                        methodMappingAnnotationHandler.handle(
                            element = element,
                            model = model,
                            path = element.getAnnotation(PostMapping::class.java).path,
                            value = element.getAnnotation(PostMapping::class.java).value,
                            httpMethod = POST,
                            responseType = element.asType().toString().retrieveResponseType()?.removeResponseEntity()
                        )
                    }
                    element.getAnnotation(PutMapping::class.java)?.let {
                        methodMappingAnnotationHandler.handle(
                            element = element,
                            model = model,
                            path = element.getAnnotation(PutMapping::class.java).path,
                            value = element.getAnnotation(PutMapping::class.java).value,
                            httpMethod = PUT
                        )
                    }
                    element.getAnnotation(DeleteMapping::class.java)?.let {
                        methodMappingAnnotationHandler.handle(
                            element = element,
                            model = model,
                            path = element.getAnnotation(DeleteMapping::class.java).path,
                            value = element.getAnnotation(DeleteMapping::class.java).value,
                            httpMethod = DELETE
                        )
                    }
                    element.getAnnotation(RequestMapping::class.java)?.let {
                        val path: Array<String> = element.getAnnotation(RequestMapping::class.java).path
                        val value: Array<String> = element.getAnnotation(RequestMapping::class.java).value
                        val controllerModel = model.getControllerModel(element.toString())
                        if (path.isNotEmpty()) {
                            controllerModel.rootResource = path[0]
                        } else if (value.isNotEmpty()) {
                            controllerModel.rootResource = value[0]
                        }
                    }
                    element.getAnnotation(ResponseStatus::class.java)?.let {
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
                    element.getAnnotation(RequestParam::class.java)?.let {
                        val requestParam = element.getAnnotation(RequestParam::class.java)
                        val argumentName = firstNotNull(requestParam.name, requestParam.value, element.simpleName.toString())
                        val methodName = element.enclosingElement.toString()
                        val argumentType = element.retrieveArgumentType().replacePrimitive()
                        val controllerModel = model.getControllerModel(element.enclosingElement.enclosingElement.toString())
                        controllerModel.getResourceModel(methodName).getRequestParamModel(argumentName).type = argumentType
                        controllerModel.getResourceModel(methodName).getRequestParamModel(argumentName).name = argumentName
                        controllerModel.getResourceModel(methodName).getRequestParamModel(argumentName).optional = !requestParam.required

                        if ("java.util.Set<(.*)>|java.util.List<(.*)>|java.lang.String\\[]".toRegex().containsMatchIn(argumentType)) {
                            controllerModel.getResourceModel(methodName).hasMultiValueRequestParams = true
                            controllerModel.getResourceModel(methodName).getRequestParamModel(argumentName).iterable = true
                        }
                    }
                    element.getAnnotation(PathVariable::class.java)?.let {
                        val pathVariable = element.getAnnotation(PathVariable::class.java)
                        val argumentName = firstNotNull(pathVariable.name, pathVariable.value, element.simpleName.toString())
                        val methodName = element.enclosingElement.toString()
                        val argumentType = element.retrieveArgumentType()
                        val controllerModel = model.getControllerModel(element.enclosingElement.enclosingElement.toString())
                        controllerModel.getResourceModel(methodName).urlHasPathVariable = true
                        controllerModel.getResourceModel(methodName).getPathVariableModel(argumentName).type = argumentType
                        controllerModel.getResourceModel(methodName).getPathVariableModel(argumentName).name = argumentName
                    }
                    element.getAnnotation(RequestBody::class.java)?.let {
                        val methodName = element.enclosingElement.toString()
                        val argumentName = element.simpleName.toString()
                        val argumentType = element.retrieveArgumentType()
                        val controllerModel = model.getControllerModel(element.enclosingElement.enclosingElement.toString())
                        val requestBody = ArgumentModel(type = argumentType, name = argumentName)
                        controllerModel.getResourceModel(methodName).requestBody = requestBody
                    }
                    element.getAnnotation(DateTimeFormat::class.java)?.let {
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
                                clazz = element.retrieveArgumentType().retrieveGeneric()
                            )
                    }
                    element.getAnnotation(RequestHeader::class.java)?.let {
                        val requestHeader = element.getAnnotation(RequestHeader::class.java)
                        val argumentName = firstNotNull(requestHeader.name, requestHeader.value, element.simpleName.toString())
                        val methodName = element.enclosingElement.toString()
                        val argumentType = element.retrieveArgumentType()
                        val controllerModel = model.getControllerModel(element.enclosingElement.enclosingElement.toString())
                        controllerModel.getResourceModel(methodName).getRequestHeaderModel(argumentName).type = argumentType
                        controllerModel.getResourceModel(methodName).getRequestHeaderModel(argumentName).name = argumentName
                        controllerModel.getResourceModel(methodName).getRequestHeaderModel(argumentName).optional = !requestHeader.required
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
            annotatedClasses.contains(it) or
                    annotatedClasses.contains(it.enclosingElement) or
                    annotatedClasses.contains(it.enclosingElement.enclosingElement) or
                    annotatedClasses.contains(it.enclosingElement.enclosingElement.enclosingElement)
        }
}
