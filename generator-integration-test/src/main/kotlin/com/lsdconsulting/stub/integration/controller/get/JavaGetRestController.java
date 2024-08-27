package com.lsdconsulting.stub.integration.controller.get;

import com.lsdconsulting.stub.integration.model.GreetingResponse;
import io.lsdconsulting.stub.annotation.GenerateWireMockStub;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.Set;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@GenerateWireMockStub
@RestController
@RequestMapping("/getController")
class JavaGetRestController {

    @GetMapping("/resourceWithParamAndAnnotations")
    ResponseEntity<GreetingResponse> resourceWithParamAndAnnotations(
            @RequestParam @Valid @Email @SuppressWarnings("unused") String param) {
        return ResponseEntity.ok().body(new GreetingResponse(randomAlphabetic(10)));
    }

    @GetMapping("/resourceWithZonedDatetimeAndMultiValue")
    GreetingResponse resourceWithZonedDatetimeAndMultiValue(
            @RequestParam @NotBlank @DateTimeFormat(iso = DATE_TIME) @SuppressWarnings("unused") ZonedDateTime param,
            @RequestParam @SuppressWarnings("unused") Set<Integer> multiValue) {
        return new GreetingResponse(randomAlphabetic(10));
    }
}
