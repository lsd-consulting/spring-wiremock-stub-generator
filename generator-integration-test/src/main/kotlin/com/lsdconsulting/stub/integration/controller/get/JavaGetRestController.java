package com.lsdconsulting.stub.integration.controller.get;

import com.lsdconsulting.stub.integration.model.GreetingResponse;
import io.lsdconsulting.stub.annotation.GenerateWireMockStub;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Email;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@GenerateWireMockStub
@RestController
@RequestMapping("/getController")
class JavaGetRestController {

    @GetMapping("/resourceWithParamAndAnnotations")
    GreetingResponse resourceWithParamAndAnnotations(@RequestParam @Valid @Email String param) {
        return new GreetingResponse(randomAlphabetic(10));
    }
}
