package com.example.playground.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exapmle/rest")
public class ExampleController {

    @RequestMapping("/resource")
    public String getResource() {
        return "example";
    }

}
