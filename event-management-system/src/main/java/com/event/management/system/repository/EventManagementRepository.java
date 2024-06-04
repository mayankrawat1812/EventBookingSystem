package com.event.management.system.repository;

import com.event.management.system.model.Event;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventManagementRepository extends MongoRepository<Event, ObjectId> {

}
