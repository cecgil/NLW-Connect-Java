package br.com.nlw.events.service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.nlw.events.DTO.SubscriptionResponse;
import br.com.nlw.events.exception.EventNotFoundException;
import br.com.nlw.events.exception.SubscriptionConflictException;
import br.com.nlw.events.exception.UserIndicadorNotFoundException;
import br.com.nlw.events.model.Event;
import br.com.nlw.events.model.Subscription;
import br.com.nlw.events.model.User;
import br.com.nlw.events.repository.EventRepository;
import br.com.nlw.events.repository.SubscriptionRepository;
import br.com.nlw.events.repository.UserRepository;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    public SubscriptionResponse createNewSubscription(String eventName, User user, Integer indicadorId) {
        Subscription newSubscription = new Subscription();

        Event eventData = Optional.ofNullable(eventRepository.findByPrettyName(eventName)).orElseThrow(() -> {
            throw new EventNotFoundException("O evento" + eventName + "não existe");
        });
        newSubscription.setEvent(eventData);


        User userSubscriber = userRepository.findByEmail(user.getEmail());
        if(userSubscriber == null) {
            userRepository.save(userSubscriber);
        }

        newSubscription.setSubscriber(userSubscriber);

        if(indicadorId != null) {
            User indicador = userRepository.findById(indicadorId).orElseThrow(() -> {
                throw new UserIndicadorNotFoundException("Usuário " +indicadorId+ " indicador não existe");
            });
            newSubscription.setIndication(indicador);
        }

        //Checa se o usuário já está cadastrado para aquela evento
        Optional.of(subscriptionRepository.findByEventAndSubscriber(eventData, userSubscriber)).ifPresent(sub -> {
            throw new SubscriptionConflictException("Já existe incrição para o usuário " + userSubscriber.getName() + " no evento " + eventData.getTitle());
        });

        subscriptionRepository.save(newSubscription);

        //retorna o DTO da subscription apenas com as informações necessárias
        return new SubscriptionResponse(newSubscription.getSubscriptionNumber(), "http://codecraft.com/subscription/"+newSubscription.getEvent().getPrettyName()+"/"+newSubscription.getSubscriber().getId());
    }
    
}
