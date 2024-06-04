package com.event.management.system;

import com.event.management.system.controller.EventManagementController;
import com.event.management.system.exception.EventNotFoundException;
import com.event.management.system.model.Event;
import com.event.management.system.repository.EventManagementRepository;
import com.event.management.system.service.EventManagementService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class EventManagementControllerTest {
    @Mock
    private EventManagementService service;
    @InjectMocks
    private EventManagementController eventManagementController;
    @Mock
    private EventManagementRepository repo;

    private Event sample1,sample2;
    private ObjectId eventId;

    @BeforeEach
    void setUp(){
        ObjectId eventId1=new ObjectId();
        eventId=eventId1;
        sample1=new Event(eventId1,"Song","Badshah", LocalDate.of(2024,6,6),"2 Days",2000.0);
        ObjectId eventId2=new ObjectId();
        sample2 = new Event(eventId2, "Song", "Arijit Singh", LocalDate.of(2024, 6, 8), "2 Days", 3000.0);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testControllerAddEvent(){
        when(service.add(any(Event.class))).thenReturn(sample1);
        ResponseEntity<String> result=eventManagementController.addNewEvent(sample1);
        verify(service,times(1)).add(any(Event.class));
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("New Event is added: "+sample1.toString(), result.getBody());
    }

    @Test
    void testControllerAddEvent_InternalServerError(){
       when(service.add(any(Event.class))).thenThrow(new RuntimeException());
        ResponseEntity<String> result=eventManagementController.addNewEvent(sample1);
        verify(service,times(1)).add(any(Event.class));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals("Internal Server Error", result.getBody());
    }

    @Test
    void testControllerGetAllEvents(){
        List<Event> eventList=new ArrayList<>();
        eventList.add(sample1);
        eventList.add(sample2);
        when(service.getAllEvents()).thenReturn(eventList);
        ResponseEntity<List<Event>> response=eventManagementController.getAllEvents();

        verify(service,times(1)).getAllEvents();
        assertEquals(eventList.size(),response.getBody().size());
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    void testControllerGetAllEvents_EVENT_NOT_FOUND(){
        when(service.getAllEvents()).thenThrow(new EventNotFoundException("Event Not Found"));
        ResponseEntity<List<Event>> response=eventManagementController.getAllEvents();
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void  testControllerGetAllEvents_INTERNAL_SERVER_ERROR(){
        when(service.getAllEvents()).thenThrow(new RuntimeException());
        ResponseEntity<List<Event>> response=eventManagementController.getAllEvents();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testControllerGetEventById(){
        when(service.getEventById(any(ObjectId.class))).thenReturn(sample1);
        ResponseEntity<Event> response=eventManagementController.getEventById(eventId);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testControllerGetEventById_EVENT_NOT_FOUND(){
        when(service.getEventById(any(ObjectId.class))).thenThrow(new EventNotFoundException("Event Not Found"));
        ResponseEntity<Event> response=eventManagementController.getEventById(eventId);
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testControllerGetEventById_INTERNAL_SERVER_ERROR(){
        when(service.getEventById(any(ObjectId.class))).thenThrow(new RuntimeException());
        ResponseEntity<Event> response=eventManagementController.getEventById(eventId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testControllerUpdateEventDetails(){
        Event newEvent=new Event(eventId,"Rap Song","Badshah", LocalDate.of(2024,6,9),"1 Days",1000.0);
        when(service.updateEvent(any(ObjectId.class),any(Event.class))).thenReturn(newEvent);
       // when(repo.findById(any(ObjectId.class))).thenReturn(Optional.of(sample1));
        ResponseEntity<String> response=eventManagementController.updateEventDetails(eventId,newEvent);
        assertEquals("Event is Updated",response.getBody());
        assertEquals(HttpStatus.OK,response.getStatusCode());
        verify(service,times(1)).updateEvent(any(ObjectId.class),any(Event.class));
        /*verify(repo,times(1)).findById(any(ObjectId.class));
        verify(repo,times(1)).save(any(Event.class));*/
    }

    @Test
    void testControllerUpdateEventDetails_EVENT_NOT_FOUND(){
        Event newEvent=new Event(eventId,"Rap Song","Badshah", LocalDate.of(2024,6,9),"1 Days",1000.0);
        when(service.updateEvent(any(ObjectId.class),any(Event.class))).thenThrow(new EventNotFoundException("Event Not Found"));
        ResponseEntity<String> response=eventManagementController.updateEventDetails(eventId,newEvent);
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        assertEquals("Event Not Found",response.getBody());
    }

    @Test
    void testControllerUpdateEventDetails_INTERNAL_SERVER_ERROR(){
        Event newEvent=new Event(eventId,"Rap Song","Badshah", LocalDate.of(2024,6,9),"1 Days",1000.0);
        when(service.updateEvent(any(ObjectId.class),any(Event.class))).thenThrow(new RuntimeException());
        ResponseEntity<String> response=eventManagementController.updateEventDetails(eventId,newEvent);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
        assertEquals("Internal Server Error",response.getBody());
    }

    @Test
     void testControllerDeleteEventById(){
        doNothing().when(service).deleteEventById(any(ObjectId.class));
        ResponseEntity<String> response=eventManagementController.deleteEventById(eventId);
        assertEquals("Event is deleted",response.getBody());
        assertEquals(HttpStatus.ACCEPTED,response.getStatusCode());
    }

    @Test
    void testControllerDeleteEventById_EVENT_NOT_FOUND(){
        doThrow(new EventNotFoundException("Event Not Found")).when(service).deleteEventById(any(ObjectId.class));
        ResponseEntity<String> response=eventManagementController.deleteEventById(eventId);
        assertEquals("Event Not Found",response.getBody());
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    void testControllerDeleteEventById_INTERNAL_SERVER_ERROR(){
        doThrow(new RuntimeException()).when(service).deleteEventById(any(ObjectId.class));
        ResponseEntity<String> response=eventManagementController.deleteEventById(eventId);
        assertEquals("Error Occur",response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

}



