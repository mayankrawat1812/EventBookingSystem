package com.event.management.system;

import com.event.management.system.exception.EventNotFoundException;
import com.event.management.system.model.Event;
import com.event.management.system.repository.EventManagementRepository;
import com.event.management.system.service.EventManagementServiceImpl;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
public class EventManagementServiceTest {
    @Mock
    private EventManagementRepository repo;
    @InjectMocks
    private EventManagementServiceImpl service;
    private Event sample1,sample2;
    private ObjectId eventId;
    @BeforeEach
    void setUp(){

        ObjectId eventId1=new ObjectId();
        eventId=eventId1;
        sample1=new Event(eventId1,"Song","Badshah", LocalDate.of(2024,6,6),"2 Days",2000.0);
        ObjectId eventId2=new ObjectId();
        sample2=new Event(eventId2,"Song","Arijit Singh", LocalDate.of(2024,6,8),"2 Days",3000.0);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void eventAdded(){
        service.add(sample1);
        when(repo.save(any(Event.class))).thenReturn(sample1);
        verify(repo,times(1)).save(any(Event.class));
    }

    @Test
    void getAllEvents(){
        List<Event> sampleEvents=new ArrayList<>();
        sampleEvents.add(sample1);
        sampleEvents.add(sample2);
        when(repo.findAll()).thenReturn(sampleEvents);
        List<Event> eventList=service.getAllEvents();
        assertNotNull(eventList);
        assertEquals(sampleEvents.size(),eventList.size());
        verify(repo,times(1)).findAll();
    }
    @Test
    void testGetAllEvents_EmptyList() {
        when(repo.findAll()).thenReturn(Collections.emptyList());
        assertThrows(EventNotFoundException.class, () -> service.getAllEvents());
    }

    @Test
    void getEventById(){
        when(repo.findById(any(ObjectId.class))).thenReturn(Optional.of(sample1));
        Event eventById=service.getEventById(eventId);
        assertEquals(eventById.getEventId(),sample1.getEventId());
        assertEquals(eventById.getEventTitle(),sample1.getEventTitle());
        assertEquals(eventById.getEventDate(),sample1.getEventDate());
        assertEquals(eventById.getArtistName(),sample1.getArtistName());
        assertEquals(eventById.getTimeDuration(),sample1.getTimeDuration());
        assertEquals((eventById.getPrice()),sample1.getPrice());
        verify(repo,times(1)).findById(eventId);
    }

    @Test
    void testGetEventByIdThrowsException() {
        ObjectId eventIdNotAvailable =new ObjectId();
        when(repo.findById(any(ObjectId.class))).thenReturn(Optional.empty());
        assertThrows(EventNotFoundException.class, () -> {
            service.getEventById(eventIdNotAvailable);
        });
    }

    @Test
    public void testDeleteService() {
        doNothing().when(repo).deleteById(any(ObjectId.class));
        service.deleteEventById(eventId);
        verify(repo, times(1)).deleteById(eventId);
    }

    @Test
    void testUpdateEventDetails(){
        Event newEvent=new Event(eventId,"Rap Song","Badshah", LocalDate.of(2024,6,9),"1 Days",1000.0);
        when(repo.findById(any(ObjectId.class))).thenReturn(Optional.of(sample1));
        when(repo.save(any(Event.class))).thenReturn(newEvent);
        Event updatedEvent=service.updateEvent(eventId,newEvent);
        assertEquals(updatedEvent.getEventId(),newEvent.getEventId());
        assertEquals(updatedEvent.getEventTitle(),newEvent.getEventTitle());
        assertEquals(updatedEvent.getArtistName(),newEvent.getArtistName());
        assertEquals(updatedEvent.getEventDate(),newEvent.getEventDate());
        assertEquals(updatedEvent.getTimeDuration(),newEvent.getTimeDuration());
        assertEquals(updatedEvent.getPrice(),newEvent.getPrice());
        verify(repo,times(1)).findById(eventId);
        verify(repo,times(1)).save(any(Event.class));
    }

    @Test
    public void testUpdateEventNotFound() {
        ObjectId id=new ObjectId();
        Event newEvent=new Event(id,"Rap Song","Badshah", LocalDate.of(2024,6,9),"1 Days",1000.0);
        when(repo.findById(any(ObjectId.class))).thenReturn(Optional.empty());

        Exception exception = assertThrows(EventNotFoundException.class, () -> {
            service.updateEvent(id, newEvent);
        });
        String expectedMessage = "Event Not Found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        verify(repo,times(1)).findById(id);
        verify(repo,times(0)).save(any(Event.class));
    }

}
