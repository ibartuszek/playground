package com.example.playground.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/example/rest")
@Slf4j
public class ExampleController {

    @RequestMapping("/resource")
    public String getResource(@RequestParam Instant date, @RequestParam String test) {
        log.info("test: " + test);
        return "hello (" + date + ")";
    }

}
