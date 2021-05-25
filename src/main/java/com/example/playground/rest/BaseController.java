package com.example.playground.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@Tag(name = "BaseController", description = "to give back example response as plain text")
@RestController
@RequestMapping("/playground/rest")
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

    @Operation(summary = "Post a custom object with lombok annotations which was mapped by Spring automatically")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "")
    })
    @PostMapping("/resource/custom-object")
    public CustomObject createCustomObject(
            @Parameter(description = "Given object", required = true)
            @RequestBody CustomObject object) {
        log.info("Posted object: " + object);
        return object;
    }

}
