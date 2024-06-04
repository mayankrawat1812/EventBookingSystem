package com.event.management.system.service;

import com.event.management.system.exception.EventNotFoundException;
import com.event.management.system.model.Event;
import com.event.management.system.repository.EventManagementRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventManagementServiceImpl implements EventManagementService{
    @Autowired
    EventManagementRepository eventManagementRepository;
    @Override
    public Event add(Event event) {
       // event.setEventId(sequenceGeneratorService.generateSequence(Event.SEQUENCE_NAME));
        return eventManagementRepository.save(event);
    }

    @Override
    public List<Event> getAllEvents() {
        List<Event> listOfEvents=eventManagementRepository.findAll();
        if(listOfEvents.isEmpty()) {
            throw new EventNotFoundException("No Events is found");
        }
        return listOfEvents;
    }

    @Override
    public Event getEventById(ObjectId eventId) {
        Optional<Event> eventById= eventManagementRepository.findById(eventId);
        if(eventById.isEmpty()){
            throw new EventNotFoundException("Event not found");
        }
        return eventById.get();
    }

    @Override
    public Event updateEvent(ObjectId eventId, Event event){
        Optional<Event> existingEventById=eventManagementRepository.findById(eventId);
        if(existingEventById.isPresent()){
            Event existingEvent=existingEventById.get();
            existingEvent.setEventTitle(event.getEventTitle());
            existingEvent.setArtistName(event.getArtistName());
            existingEvent.setEventDate(event.getEventDate());
            existingEvent.setTimeDuration(event.getTimeDuration());
            existingEvent.setPrice(event.getPrice());
            eventManagementRepository.save(existingEvent);
            return existingEvent;
        }
        else{
            throw new EventNotFoundException("Event Not Found");
        }
    }

    @Override
    public void deleteEventById(ObjectId eventId){
        eventManagementRepository.deleteById(eventId);
    }
}
