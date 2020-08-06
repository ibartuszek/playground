package com.example.playground.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/example/rest")
@Slf4j
public class BaseController {

    @Operation(summary = "Get a hello message with the given message the date.")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "E.g.: \"hello user (2020-07-10T16:30:00Z)\"")
    })
    @GetMapping("/resource")
    public String getBaseResource(
            @Parameter(description = "Given date", required = true, example = "2020-07-10T16:30:00Z")
            @RequestParam Instant date,
            @Parameter(description = "Given message", required = true, example = "message")
            @RequestParam String message) {
        log.info("test: " + message);
        return "message: "+ message + " (" + date + ")";
    }

}
