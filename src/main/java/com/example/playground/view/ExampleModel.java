package com.example.playground.view;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ExampleModel {

    private String id;
    private String message;
    private LocalDateTime timeStamp;
    private LocalDateTime date;

}
