package com.example.playground.rest.mongo;

import com.example.playground.dal.mongo.ExampleEntry;
import com.example.playground.dal.mongo.repository.ExampleRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ExampleEntryController", description = "to give an endpoint of CRUD operations of ExampleEntry")
@RestController
@RequestMapping("/playground/mongo/example-entry")
@Slf4j
@RequiredArgsConstructor
public class ExampleEntryController {

    private final ExampleRepository exampleRepository;

    @Operation(summary = "Get an example entry by id")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "")
    })
    @GetMapping("/{id}")
    public ExampleEntry getById(
            @Parameter(description = "Given id", required = true, example = "5f759df0192cf82efd481c64")
            @PathVariable String id) {
        return exampleRepository.findById(id)
                .orElse(null);
    }

    @Operation(summary = "Get all entries")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "")
    })
    @GetMapping("/all")
    public List<ExampleEntry> getAll() {
        return exampleRepository.findAll();
    }

    @Operation(summary = "Create an entry")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "")
    })
    @PostMapping
    public ExampleEntry create(
            @Parameter(description = "Entry to create", required = true)
            @RequestBody ExampleEntry entryToUpdate) {
        return exampleRepository.save(entryToUpdate);
    }

    @Operation(summary = "Update an entry")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "")
    })
    @PutMapping
    public ExampleEntry update(
            @Parameter(description = "Entry to update", required = true)
            @RequestBody ExampleEntry entryToUpdate) {
        return exampleRepository.save(entryToUpdate);
    }

    @Operation(summary = "Delete an entry")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "")
    })
    @DeleteMapping("/{id}")
    public void deleteById(
            @Parameter(description = "Given id", required = true, example = "5f759df0192cf82efd481c64")
            @PathVariable String id) {
        exampleRepository.deleteById(id);
    }

}
