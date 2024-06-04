package com.event.management.system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@Document(collection = "events")
public class Event {
    @Id
    private ObjectId eventId;

    private String eventTitle;
    private String artistName;
    private LocalDate eventDate;
    private String timeDuration;
    private double price;
}
