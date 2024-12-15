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

## Articles
DZone: [WireMock: The Ridiculously Easy Way (For Spring Microservices)](https://dzone.com/articles/wiremock-the-ridiculously-easy-way)

## Applicability
The WireMock generator has the following constraints and limitations:
- the request and response classes are shared between the producer and the consumers of the API
- currently, the generator supports only JSON based communication

## How to use it
Add the following dependencies to a Java project:
```groovy
    annotationProcessor 'io.github.lsd-consulting:spring-wiremock-stub-generator:x.x.x'
    compileOnly 'io.github.lsd-consulting:spring-wiremock-stub-generator:x.x.x'
    compileOnly 'org.wiremock:wiremock:3.5.2'
```

or these for a Kotlin project:
```groovy
    kapt 'io.github.lsd-consulting:spring-wiremock-stub-generator:x.x.x'
    compileOnly 'io.github.lsd-consulting:spring-wiremock-stub-generator:x.x.x'
    compileOnly 'org.wiremock:wiremock:3.5.2'
```

The above will set up the annotation processor which will analyse the source code and generate the Java WireMock stubs.

![Generated Java WireMock stub](https://github.com/lsd-consulting/spring-wiremock-stub-generator/blob/main/docs/generated_wiremock_stub.png?raw=true)

To compile the stubs add the following:

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
This library follows WireMock's approach to handling multiple-value query parameters.

This means multiple values should be passed like this:
```
field1=value1&field1=value2&field2=value3
```

A stub handling the above will be generated from a mapping like this:
```kotlin
    @GetMapping("/resourceWithParamSet")
    fun resourceWithParamSet(@RequestParam field1: Set<String>, @RequestParam field2: String) {
        ...
    }
```

## Optional multi-value request parameters

### The problem

While WireMock supports multiple-value query parameters as well as optional query parameters, it doesn't yet support
optional multiple-value query parameters. There is no easy way to set up a WireMock stub for the following mapping:.
```kotlin
    @GetMapping("/resourceWithParamSet")
    fun resourceWithParamSet(@RequestParam(required = false) field1: Set<String>) {
        ...
    }
```

### The implemented solution
The library handles queries like this in a special way.

As soon as an optional multi-value query parameter is detected, the WireMock stub is switched from `urlPathEqualTo` matcher, to the `urlEqualTo` one.
The implication is that the ordering of the request parameters becomes significant.

Therefore, it is important to use ordered collections when sending multi-value parameters in tests to make the queries deterministic.

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

## Contributing
We welcome bug fixes and new features in the form of pull requests. If you'd like to contribute, please be mindful of the following guidelines:

- Start with raising an issue
- All changes should include suitable tests, whether to demonstrate the bug or exercise and document the new feature.
- Please make one change per pull request.
- Use [conventional commits](https://www.conventionalcommits.org/)
- If the new feature is significantly large/complex/breaks existing behaviour, please first post a summary on the issue to generate a discussion. This will avoid significant amounts of coding time spent on changes that ultimately get rejected.
- Try to avoid reformats of files that change the indentation, spaces to tabs etc., as this makes reviewing diffs much more difficult.

### Preferred approach 
Probably the best way to add functionality to this project is through tests.

[Here](https://github.com/lsd-consulting/spring-wiremock-stub-generator/commit/be46ab8fa9cc5a2d448c370111ee7236ec2d66ca) is an example commit of adding support for Kotlin `vararg`.

That commit doesn't show how the change actually came about, but here are the steps that were taken in the following order: 
1. Add a new resource to a controller in the integration tests
2. Build the project so that the stubs are updated
3. Add a failing integration test
4. Make a change in the library that makes the test pass

In the majority of cases following the above steps should suffice.
