package com.example.playground.rest;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Builder
@JsonPOJOBuilder(withPrefix = "")
public final class CustomObject {

    @Schema(example = "42")
    private final long id;
    @Schema(example = "My very uniq custom message")
    private final String message;
    @Schema(example = "2020-08-14T10:00:00.000Z")
    private final ZonedDateTime time;
    private final List<Detail> details;

    @Data
    @Builder
    @JsonPOJOBuilder(withPrefix = "")
    public static class Detail {

        @Schema(example = "1")
        private final long id;
        @Schema(example = "Custom detailed description 1")
        private final String description;

    }

}
