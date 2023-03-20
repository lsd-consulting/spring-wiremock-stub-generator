[![semantic-release](https://img.shields.io/badge/semantic-release-e10079.svg?logo=semantic-release)](https://github.com/semantic-release/semantic-release)

# spring-wiremock-stub-generator
![GitHub](https://img.shields.io/github/license/lsd-consulting/spring-wiremock-stub-generator)
![Codecov](https://img.shields.io/codecov/c/github/lsd-consulting/spring-wiremock-stub-generator)

[![CI](https://github.com/lsd-consulting/spring-wiremock-stub-generator/actions/workflows/ci.yml/badge.svg)](https://github.com/lsd-consulting/spring-wiremock-stub-generator/actions/workflows/ci.yml)
[![Nightly Build](https://github.com/lsd-consulting/spring-wiremock-stub-generator/actions/workflows/nightly.yml/badge.svg)](https://github.com/lsd-consulting/spring-wiremock-stub-generator/actions/workflows/nightly.yml)
[![GitHub release](https://img.shields.io/github/release/lsd-consulting/spring-wiremock-stub-generator)](https://github.com/lsd-consulting/spring-wiremock-stub-generator/releases)
![Maven Central](https://img.shields.io/maven-central/v/io.github.lsd-consulting/spring-wiremock-stub-generator)

## About
This is a tool for generating Wiremock stubs from Spring @Controller & @RestController annotated classes

![WireMock stub generation diagram](https://github.com/lsd-consulting/spring-wiremock-stub-generator/blob/main/docs/diagram.png?raw=true)

## Applicability
The WireMock generator has the following constraints and limitations:
- the request and response classes are shared between the producer and the consumers of the API
- currently, the generator supports only JSON based communication

## How to use it
Add the following dependencies to a Java project:
```groovy
    implementation 'io.github.lsd-consulting:spring-wiremock-stub-generator:x.x.x'
    annotationProcessor 'io.github.lsd-consulting:spring-wiremock-stub-generator:x.x.x'
    compileOnly 'com.github.tomakehurst:wiremock:2.27.2'
```

or these for a Kotlin project:
```groovy
    kapt 'io.github.lsd-consulting:spring-wiremock-stub-generator:x.x.x'
    annotationProcessor 'io.github.lsd-consulting:spring-wiremock-stub-generator:x.x.x'
    compileOnly 'com.github.tomakehurst:wiremock:2.27.2'
```

The above will set up the annotation processor which will analyse the source code and generate the Java WireMock stubs.

![Generated Java WireMock stub](https://github.com/lsd-consulting/spring-wiremock-stub-generator/blob/main/docs/generated_wiremock_stub.png?raw=true)

To compile the subs add the following:

```groovy
task compileStubs(type: JavaCompile) {
    JavaCompile compileJava = project.getTasksByName("compileJava", true).toArray()[0]
    classpath = compileJava.classpath
    source = project.getLayout().getBuildDirectory().dir("generated-stub-sources")
    def stubsClassesDir = file("${project.getBuildDir()}/generated-stub-classes")
    destinationDir(stubsClassesDir)
    compileJava.finalizedBy(compileStubs)
}
```

The result would be a class file(s) like this:
![Compiled Java WireMock stub](https://github.com/lsd-consulting/spring-wiremock-stub-generator/blob/main/docs/compiled_wiremock_stub.png?raw=true)

And to build a JAR file with the stubs:

```groovy
task stubsJar(type: Jar) {
    JavaCompile compileJavaStubs = project.getTasksByName("compileStubs", true).toArray()[0]
    setDescription('Java Wiremock stubs JAR')
    setGroup("Verification")
    archiveBaseName.convention(project.provider(project::getName))
    archiveClassifier.convention("wiremock-stubs")
    from(compileJavaStubs.getDestinationDirectory())
    dependsOn(compileJavaStubs)
    compileJavaStubs.finalizedBy(stubsJar)
    project.artifacts(artifactHandler -> artifactHandler.add("archives", stubsJar))
}
```

![WireMock stub jar](https://github.com/lsd-consulting/spring-wiremock-stub-generator/blob/main/docs/wiremock_stub_jar.png?raw=true)

The JAR file can then be published as an artifact:

```groovy
publishing {
    publications {
        mavenJava(MavenPublication) {
            from components.java

            artifact stubsJar
            artifactId "${rootProject.name}-${project.name}"
        }
    }
}
```

The last step is to import it as a dependency in another project and use it.
```groovy
testImplementation('group:artifact:+:wiremock-stubs') {
        exclude group: "*", module: "*"
    }
```
NOTE: The exclusion is necessary if the published artifact imports transitively all the producer's dependencies.


## Examples:
For Java and Kotlin examples please check out the following project:

https://github.com/lsd-consulting/spring-wiremock-stub-generator-example

## Multi-value request parameters

### The problem
According to the accepted answer [here](https://stackoverflow.com/questions/24059773/correct-way-to-pass-multiple-values-for-same-parameter-name-in-get-request) 
and [this Wiki page](https://en.wikipedia.org/wiki/Query_string) :
```text
While there is no definitive standard, most web frameworks allow multiple values to be associated with a single field (e.g. field1=value1&field1=value2&field2=value3).
```

However, according to [this thread](https://github.com/wiremock/wiremock/issues/398), WireMock doesn't support duplicate query names.

Since Spring MVC does provide handling of duplicate query params out of the box, eg.
```kotlin
    @GetMapping("/resourceWithParamSet")
    fun resourceWithParamSet(@RequestParam paramSet: Set<String>) {
        ...
    }
```

there is no easy way to set up a WireMock stub for the above, or similar, examples.

### The implemented solution
The library handles queries like this in a special way.

As soon as a multi-value query parameter is detected, the WireMock stub is switched from `urlPathEqualTo` matcher, to the `urlEqualTo` one.
The implication is that the ordering of the request parameters becomes significant.

Therefore, it is important to use ordered collections when sending multi-value parameters in tests to make the queries deterministic.

## Optional request parameters
Optional request parameters is another feature of the HTTP protocol that is not yet supported by WireMock (see [here](https://groups.google.com/g/wiremock-user/c/WKMkb_LhJTU)).

To handle the following SpringMVC definition:
```kotlin
    @GetMapping("/resourceWithOptionalBooleanRequestParam")
    fun resourceWithOptionalBooleanRequestParam(@RequestParam(required = false) param: Boolean) {
        ...
    }
```
the library introduces login into the generated stub.

If a parameter is optional and the value passed in is a `null`, the generated stub will not add the query param matcher to the Wiremock stub.

This means that the following call to the generated stub:
```kotlin
underTest.resourceWithOptionalBooleanRequestParam(response, null)
```

will generate a WireMock matcher to match the following GET request:
```
/resourceWithOptionalBooleanRequestParam
```

But if a value is passed in that call, eg:
```kotlin
underTest.resourceWithOptionalBooleanRequestParam(response, "value")
```

then WireMock will expect the following request:
```
/resourceWithOptionalBooleanRequestParam?param=value
```

## Handling @DateTimeFormat
If the request parameter or path variable is a date, it needs to be annotated with `@DateTimeFormat`, eg:

```kotlin
@GetMapping("/resource") 
fun resource(@RequestParam @DateTimeFormat(iso = DATE_TIME) timestamp: ZonedDateTime)
```

For the generated stub object to be able to handle such values, it needs an `AnnotationFormatterFactory`, eg. `Jsr310DateTimeFormatAnnotationFormatterFactory`.

A special constructor needs to be used when creating an instance of the stub:
```kotlin
RestControllerStub(ObjectMapper(), Jsr310DateTimeFormatAnnotationFormatterFactory())
```
