package com.uep.sors.controller;
 
import com.uep.sors.entity.Event;
import com.uep.sors.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
 
@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "${cors.allowed-origins}")
public class EventController {
 
    @Autowired
    private EventService eventService;
 
    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }
 
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return eventService.getEventById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
 
    @PostMapping
    @PreAuthorize("hasAnyRole('PIO', 'EDITOR', 'ADMIN')")
    public Event createEvent(@RequestBody Event event) {
        return eventService.createEvent(event);
    }
 
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('PIO', 'EDITOR', 'ADMIN')")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id,
                                              @RequestBody Event event) {
        return ResponseEntity.ok(eventService.updateEvent(id, event));
    }
 
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('PIO', 'EDITOR', 'ADMIN')")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}