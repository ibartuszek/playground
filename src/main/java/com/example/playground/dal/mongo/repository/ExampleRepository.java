package com.example.playground.dal.mongo.repository;


import com.example.playground.dal.mongo.ExampleEntry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExampleRepository extends MongoRepository<ExampleEntry, String> {
}
