package br.com.nlw.events.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.com.nlw.events.DTO.ErrorMessage;
import br.com.nlw.events.DTO.SubscriptionResponse;
import br.com.nlw.events.exception.EventNotFoundException;
import br.com.nlw.events.exception.SubscriptionConflictException;
import br.com.nlw.events.exception.UserIndicadorNotFoundException;
import br.com.nlw.events.model.User;
import br.com.nlw.events.service.SubscriptionService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;


    @PostMapping("/subscription/{prettyName}")
    public ResponseEntity<Object> createNewSubscription(@PathVariable String prettyName, @RequestBody User user, Integer indicadorId) {
        try {
            SubscriptionResponse result = subscriptionService.createNewSubscription(prettyName, user, indicadorId);
            if(result != null) {
                return ResponseEntity.ok(result);
            }
        } 
        catch(EventNotFoundException ex) {
            return ResponseEntity.status(404).body(new ErrorMessage(ex.getMessage()));
        } 
        catch(SubscriptionConflictException ex){
            return ResponseEntity.status(409).body(new ErrorMessage(ex.getMessage()));
        }
        catch(UserIndicadorNotFoundException ex){
            return ResponseEntity.status(404).body(new ErrorMessage(ex.getMessage()));
        }

        return ResponseEntity.badRequest().build();
        
    }
    
    
}
