package com.kafkab2c.KafkaB2C.Database;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository interface for accessing and manipulating {@link RequestRecord} documents in MongoDB.
 *
 * Extends {@link MongoRepository} to provide CRUD operations and query methods.
 */
public interface RequestRecordRepo extends MongoRepository<RequestRecord, String> {
    // No additional methods are defined here; the repository inherits standard CRUD operations
    // from MongoRepository such as save, findById, findAll, deleteById, etc.
}
