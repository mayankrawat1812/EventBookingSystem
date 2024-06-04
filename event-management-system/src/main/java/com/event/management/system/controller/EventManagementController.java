package com.event.management.system.controller;

import com.event.management.system.exception.EventNotFoundException;
import com.event.management.system.model.Event;
import com.event.management.system.service.EventManagementService;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EventManagementController {
    @Autowired
    EventManagementService service;
    Logger log = LoggerFactory.getLogger(EventManagementController.class);
    @PostMapping("/add-event")
    public ResponseEntity<String> addNewEvent(@RequestBody Event newEvent){
        log.info("Loading...");
        try{
            Event event=service.add(newEvent);
            return new ResponseEntity<>("New Event is added: "+event.toString(),HttpStatus.CREATED);
        }
        catch(Exception e){
            return new ResponseEntity<>("Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/view-all-event")
    public ResponseEntity<List<Event>> getAllEvents(){
        log.info(("Loading..."));
        try{
            List<Event> listOfEvents=service.getAllEvents();
            return new ResponseEntity<>(listOfEvents,HttpStatus.OK);
        }
        catch (EventNotFoundException e){
            log.warn(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/eventById")
    public ResponseEntity<Event> getEventById(@RequestParam("_id") ObjectId eventId){
        log.info("Loading the event of ID:"+eventId);
        try{
            Event eventById= service.getEventById(eventId);
            return new ResponseEntity<>(eventById,HttpStatus.OK);
        }
        catch (EventNotFoundException e){
            log.warn(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update-event")
    public ResponseEntity<String> updateEventDetails(@RequestParam("eventId") ObjectId eventId,@RequestBody Event event){
        log.info("Updating the event details...");
        try{
            service.updateEvent(eventId,event);
            return new ResponseEntity<>("Event is Updated",HttpStatus.OK);
        }
        catch (EventNotFoundException e){
            log.warn(e.getMessage());
            return new ResponseEntity<>("Event Not Found",HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>("Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/remove-event")
    public ResponseEntity<String> deleteEventById(@RequestParam("eventId") ObjectId eventId){
        log.info("Processing your request....");
        try{
            service.deleteEventById(eventId);
            return new ResponseEntity<>("Event is deleted",HttpStatus.ACCEPTED);
        }
        catch (EventNotFoundException e){
            log.warn(e.getMessage());
            return new ResponseEntity<>("Event Not Found",HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            log.error(e.getMessage());
            return  new ResponseEntity<>("Error Occur",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
