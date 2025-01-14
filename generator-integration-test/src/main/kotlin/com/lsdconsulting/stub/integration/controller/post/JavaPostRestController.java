package com.lsdconsulting.stub.integration.controller.post;

import com.lsdconsulting.stub.integration.model.GreetingRequest;
import com.lsdconsulting.stub.integration.model.GreetingResponse;
import io.lsdconsulting.stub.annotation.GenerateWireMockStub;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.web.bind.annotation.*;

import static org.apache.commons.lang3.RandomStringUtils.secure;

@GenerateWireMockStub
@RestController
@RequestMapping("/postController")
class JavaPostRestController {

    @PostMapping("/resourceWithBodyAndAnnotations")
    GreetingResponse resourceWithBodyAndAnnotations(
            @RequestBody @Valid @Email @SuppressWarnings("unused") GreetingRequest greetingRequest) {
        return new GreetingResponse(secure().nextAlphanumeric(10));
    }

    @PostMapping("/resourceWithBodyAndAnnotationsOnPathVariables/{param}")
    GreetingResponse resourceWithBodyAndAnnotationsOnPathVariables(
            @RequestBody @SuppressWarnings("unused") GreetingRequest greetingRequest,
            @PathVariable @Valid @Email @SuppressWarnings("unused") String param) {
        return new GreetingResponse(secure().nextAlphanumeric(10));
    }
}
