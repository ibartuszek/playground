package com.example.playground.rest.kafka;

import com.example.playground.service.kafka.CustomMessage;
import com.example.playground.service.kafka.KafkaMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "KafkaMessageProducerController", description = "To send and get messages from kafka")
@RestController
@RequestMapping("/playground/kafka")
@ConditionalOnProperty(value = "kafka.enabled", havingValue = "true")
@RequiredArgsConstructor
public class KafkaMessageController {

    private final KafkaMessageService messageService;

    @Operation(summary = "Submit message into the predefined kafka topic.")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "CustomMessage={} is scheduled to send kafka.")
    })
    @PostMapping("/send")
    public String send(
            @Parameter(description = "Message", required = true, example = "My message")
            @RequestParam String message) {
        return messageService.sendMessage(message);
    }

    @Operation(summary = "Get messages from kafka.")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "Messages in json format")
    })
    @GetMapping("/messages")
    public List<CustomMessage> getMessages() {
        return messageService.getMessages();
    }

}
