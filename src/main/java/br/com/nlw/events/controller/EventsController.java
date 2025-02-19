package br.com.nlw.events.controller;

import org.springframework.web.bind.annotation.RestController;

import br.com.nlw.events.exception.EventByPrettyNameNotFound;
import br.com.nlw.events.model.Event;
import br.com.nlw.events.service.EventService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
public class EventsController {

    @Autowired
    private EventService eventService;


    @PostMapping("/events")
    public Event addNewEvent(@RequestBody Event entity) {
        return this.eventService.addNewEvent(entity); 
    }

    @GetMapping("/events")
    public List<Event> getAllEvents() {
        return this.eventService.getAllEvents();
    }

    @GetMapping("/events/{prettyName}")
    public ResponseEntity<Event> getEventByPrettyName(@PathVariable String prettyName) {
        Event event =  eventService.getByPrettyName(prettyName);
        if(event == null) {
            throw new EventByPrettyNameNotFound();
        }
        return ResponseEntity.ok().body(event);
    }
    
    
    
}
