package com.lsdconsulting.stub.integration.controller.post;

import com.lsdconsulting.stub.integration.model.GreetingRequest;
import com.lsdconsulting.stub.integration.model.GreetingResponse;
import io.lsdconsulting.stub.annotation.GenerateWireMockStub;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Email;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@GenerateWireMockStub
@RestController
@RequestMapping("/postController")
class JavaPostRestController {

    @PostMapping("/resourceWithBodyAndAnnotations")
    GreetingResponse resourceWithBodyAndAnnotations(@RequestBody @Valid @Email GreetingRequest greetingRequest) {
        return new GreetingResponse(randomAlphabetic(10));
    }
}
