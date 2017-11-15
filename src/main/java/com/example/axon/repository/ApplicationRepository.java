package com.example.axon.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.axon.document.Application;

public interface ApplicationRepository extends MongoRepository<Application, String> {

}
