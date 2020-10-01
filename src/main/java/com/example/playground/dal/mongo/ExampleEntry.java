package com.example.playground.dal.mongo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "exampleCollection")
@Data
@Builder
public class ExampleEntry {

    @Id
    @Schema(example = "5f759df0192cf82efd481c64")
    @EqualsAndHashCode.Exclude
    private String id;

    @Schema(example = "My very uniq custom message")
    private String message;

}
