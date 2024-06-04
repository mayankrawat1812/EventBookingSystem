package com.event.management.system.service;

import com.event.management.system.model.Event;
import org.bson.types.ObjectId;

import java.util.List;

public interface EventManagementService {
    public Event add(Event event);

    public List<Event> getAllEvents();

    public Event getEventById(ObjectId eventId);

    public Event updateEvent(ObjectId eventId,Event event);

    void deleteEventById(ObjectId eventId);
}
