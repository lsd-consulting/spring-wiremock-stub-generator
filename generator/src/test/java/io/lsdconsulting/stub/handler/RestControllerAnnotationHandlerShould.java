package io.lsdconsulting.stub.handler;

import io.lsdconsulting.stub.model.ControllerModel;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RestController;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.util.Elements;
import java.lang.annotation.Annotation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class RestControllerAnnotationHandlerShould {

    private final Elements elementUtils = mock(Elements.class);
    private final Element element = mock(Element.class);
    private final Name className = mock(Name.class);
    private final Name packageName = mock(Name.class);
    private final PackageElement packageElement = mock(PackageElement.class);

    private final RestControllerAnnotationHandler underTest = new RestControllerAnnotationHandler(elementUtils);

    private final String controllerBeanName = RandomStringUtils.randomAlphabetic(10);
    private final String annotatedClassName = RandomStringUtils.randomAlphabetic(10);
    private final String packageNameString = RandomStringUtils.randomAlphabetic(10);
    private final String stubPackageName = RandomStringUtils.randomAlphabetic(10);
    private final String stubClassName = RandomStringUtils.randomAlphabetic(10);

    private final ControllerModel model = new ControllerModel();

    @Test
    void handleRestControllerAnnotation() {
        model.setPackageName(stubPackageName);
        model.setStubClassName(stubClassName);

        given(element.getAnnotation(RestController.class)).willReturn(new RestController() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @NotNull
            @Override
            public String value() {
                return controllerBeanName;
            }
        });
        given(element.getSimpleName()).willReturn(className);
        given(className.toString()).willReturn(annotatedClassName);
        given(elementUtils.getPackageOf(element)).willReturn(packageElement);
        given(packageElement.getQualifiedName()).willReturn(packageName);
        given(packageName.toString()).willReturn(packageNameString);

        underTest.handle(element, model);

        assertThat(model.getStubClassName(), is(annotatedClassName + "Stub"));
        assertThat(model.getPackageName(), is(packageNameString));
        assertThat(model.getStubFullyQualifiedName(), is(packageNameString + "." + annotatedClassName + "Stub"));
    }
}
