package com.example.playground.view;

import com.example.playground.dal.mongo.ExampleEntry;
import com.example.playground.dal.mongo.repository.ExampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ExampleService {

    private final ExampleRepository repository;

    public void save(ExampleModel model, String zoneId) {
        repository.save(transformToEntry(model, zoneId));
    }

    public void delete(ExampleModel model, String zoneId) {
        repository.delete(transformToEntry(model, zoneId));
    }

    public Optional<ExampleModel> findById(String id, String zoneId) {
        return repository.findById(id).map(entry -> transformToModel(entry, zoneId));
    }

    public List<ExampleModel> findAll(String zoneId) {
        return repository.findAll().stream()
                .map(entry -> transformToModel(entry, zoneId))
                .collect(Collectors.toList());
    }

    public List<ExampleModel> findAllAfter(LocalDateTime timestampFilter, String zoneId) {
        Instant timestamp = timestampFilter.toInstant(toZoneOffset(zoneId));
        return repository.findAllByTimeStampAfter(timestamp).stream()
                .map(entry -> transformToModel(entry, zoneId))
                .collect(Collectors.toList());
    }

    private ZoneOffset toZoneOffset(String zoneId) {
        return ZoneId.of(zoneId).getRules().getOffset(LocalDateTime.now());
    }

    private ExampleModel transformToModel(final ExampleEntry entry, final String zoneId) {
        return ExampleModel.builder()
                .id(entry.getId())
                .message(entry.getMessage())
                .timeStamp(LocalDateTime.ofInstant(entry.getTimeStamp(), ZoneId.of(zoneId)))
                .date(LocalDateTime.ofInstant(entry.getDate().toInstant(), ZoneId.of(zoneId)))
                .build();
    }

    private ExampleEntry transformToEntry(final ExampleModel model, final String zoneId) {
        Date date = Date.from(model.getDate().toInstant(toZoneOffset(zoneId)));
        return ExampleEntry.builder()
                .id(model.getId())
                .message(model.getMessage())
                .timeStamp(model.getTimeStamp().toInstant(toZoneOffset(zoneId)))
                .date(date)
                .build();
    }
}
