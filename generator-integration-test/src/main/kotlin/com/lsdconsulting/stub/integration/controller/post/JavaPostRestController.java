package com.lsdconsulting.stub.integration.controller.post;

import com.lsdconsulting.stub.integration.model.GreetingRequest;
import com.lsdconsulting.stub.integration.model.GreetingResponse;
import io.lsdconsulting.stub.annotation.GenerateWireMockStub;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@GenerateWireMockStub
@RestController
@RequestMapping("/postController")
class JavaPostRestController {

    @PostMapping("/resourceWithBodyAndAnnotations")
    GreetingResponse resourceWithBodyAndAnnotations(
            @RequestBody @Valid @Email @SuppressWarnings("unused") GreetingRequest greetingRequest) {
        return new GreetingResponse(randomAlphabetic(10));
    }

    @PostMapping("/resourceWithBodyAndAnnotationsOnPathVariables/{param}")
    GreetingResponse resourceWithBodyAndAnnotationsOnPathVariables(
            @RequestBody @SuppressWarnings("unused") GreetingRequest greetingRequest,
            @PathVariable @Valid @Email @SuppressWarnings("unused") String param) {
        return new GreetingResponse(randomAlphabetic(10));
    }
}
