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

The JAR file can then be published as an artifact:

```publishing {
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

## Examples:
For Java and Kotlin examples please check out the following project:

https://github.com/lsd-consulting/spring-wiremock-stub-generator-example

## TODO:
- add support for path arrays in mappings (currently we use only the first value in the array)
- add support for @RequestMapping on methods
- add a diagram and screenshots to README
- fix Jacoco/Codecov
