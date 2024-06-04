package com.event.management.system;

import com.event.management.system.exception.EventNotFoundException;
import com.event.management.system.model.Event;
import com.event.management.system.repository.EventManagementRepository;
import com.event.management.system.service.EventManagementService;
import com.event.management.system.service.EventManagementServiceImpl;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
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
import static org.mockito.Mockito.*;

@SpringBootTest
class EventManagementSystemApplicationTests {

	@Test
	void contextLoads() {
	}

}
