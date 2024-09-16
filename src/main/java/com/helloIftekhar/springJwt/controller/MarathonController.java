package com.helloIftekhar.springJwt.controller;

import com.helloIftekhar.springJwt.model.MarathonEvent;
import com.helloIftekhar.springJwt.model.MarathonRegistration;
import com.helloIftekhar.springJwt.model.User;
import com.helloIftekhar.springJwt.repository.MarathonEventRepository;
import com.helloIftekhar.springJwt.repository.MarathonRegistrationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/marathon")
public class MarathonController {

    private final MarathonEventRepository eventRepository;
    private final MarathonRegistrationRepository registrationRepository;

    public MarathonController(MarathonEventRepository eventRepository, MarathonRegistrationRepository registrationRepository) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
    }

    @PostMapping("/register/{eventId}")
    public ResponseEntity<String> registerForEvent(@PathVariable Long eventId, @AuthenticationPrincipal User user) {
        MarathonEvent event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        MarathonRegistration registration = new MarathonRegistration();
        registration.setUser(user);
        registration.setEvent(event);
        registration.setRegistrationDate(LocalDateTime.now());

        registrationRepository.save(registration);

        return ResponseEntity.ok("Registration successful.");
    }
}
