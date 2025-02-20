package br.com.nlw.events.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.com.nlw.events.DTO.ErrorMessage;
import br.com.nlw.events.DTO.SubscriptionResponseDTO;
import br.com.nlw.events.exception.EventNotFoundException;
import br.com.nlw.events.exception.SubscriptionConflictException;
import br.com.nlw.events.exception.UserIndicadorNotFoundException;
import br.com.nlw.events.model.User;
import br.com.nlw.events.service.SubscriptionService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;


    @PostMapping({"/subscription/{prettyName}", "/subscription/{prettyName}/{userId}"})
    public ResponseEntity<Object> createNewSubscription(@PathVariable String prettyName, @RequestBody User user,  @PathVariable(required = false)  Integer indicadorId) {
        try {
            SubscriptionResponseDTO result = subscriptionService.createNewSubscription(prettyName, user, indicadorId);
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


    @GetMapping("/subscription/{prettyName}/ranking")
    public ResponseEntity<?> generateRankingByEvent(@PathVariable String prettyName) {
        try {
            return ResponseEntity.ok().body(subscriptionService.getCompleteRanking(prettyName).stream().limit(3).toList());
        } catch(EventNotFoundException e ) {
            return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));
        }
    }


    @GetMapping("/subscription/{prettyName}/ranking/{userId}")
    public ResponseEntity<?> generateRaningByEventAndUser(@PathVariable String prettyName, @PathVariable Integer userId){
        try{
            return ResponseEntity.ok(subscriptionService.getCompleteRankingByUser(prettyName, userId));
        }catch(Exception ex){
            return ResponseEntity.status(404).body(new ErrorMessage(ex.getMessage()));
        }
    } 
    
    
    
    
}
