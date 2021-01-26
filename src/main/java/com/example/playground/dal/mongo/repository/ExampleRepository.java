package com.example.playground.dal.mongo.repository;


import com.example.playground.dal.mongo.ExampleEntry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Repository
public interface ExampleRepository extends MongoRepository<ExampleEntry, String> {

    List<ExampleEntry> findAllByTimeStampBefore(Instant date);

    List<ExampleEntry> findAllByTimeStampAfter(Instant date);

    List<ExampleEntry> findAllByDateBefore(Date date);

}
