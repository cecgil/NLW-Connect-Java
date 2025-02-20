package br.com.nlw.events.service;


import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.nlw.events.DTO.SubscriptionRankingByUserDTO;
import br.com.nlw.events.DTO.SubscriptionRankingDTO;
import br.com.nlw.events.DTO.SubscriptionResponseDTO;
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

    public SubscriptionResponseDTO createNewSubscription(String eventName, User user, Integer indicadorId) {
        Subscription newSubscription = new Subscription();

        Event eventData = Optional.ofNullable(eventRepository.findByPrettyName(eventName)).orElseThrow(() -> {
            throw new EventNotFoundException("O evento" + eventName + "não existe");
        });
        newSubscription.setEvent(eventData);


        User userSubscriber = userRepository.findByEmail(user.getEmail());
        if(userSubscriber == null) {
            userSubscriber = userRepository.save(user);
        }

        newSubscription.setSubscriber(userSubscriber);

        if(indicadorId != null) {
            User indicador = userRepository.findById(indicadorId).orElseThrow(() -> {
                throw new UserIndicadorNotFoundException("Usuário " +indicadorId+ " indicador não existe");
            });
            newSubscription.setIndication(indicador);
        }

        Optional<Subscription> existingSubscription = Optional.ofNullable(
            subscriptionRepository.findByEventAndSubscriber(eventData, userSubscriber)
        );
        
        if (existingSubscription.isPresent()) {
            throw new SubscriptionConflictException("Já existe inscrição para o usuário " 
                + userSubscriber.getName() + " no evento " + eventData.getTitle());
        }

        subscriptionRepository.save(newSubscription);

        //retorna o DTO da subscription apenas com as informações necessárias
        return new SubscriptionResponseDTO(newSubscription.getSubscriptionNumber(), "http://codecraft.com/subscription/"+newSubscription.getEvent().getPrettyName()+"/"+newSubscription.getSubscriber().getId());
    }


    public List<SubscriptionRankingDTO> getCompleteRanking(String prettyEventName) {
        Event existingEvent = Optional.ofNullable(eventRepository.findByPrettyName(prettyEventName)).orElseThrow(() -> {
            throw new EventNotFoundException("O ranking do evento" + prettyEventName + "não existe");
        });        

        return subscriptionRepository.generateRanking(existingEvent.getEventId());
    }

    public SubscriptionRankingByUserDTO getCompleteRankingByUser(String prettyEventName, Integer userId) {
        List<SubscriptionRankingDTO> ranking = getCompleteRanking(prettyEventName);

        SubscriptionRankingDTO item = ranking.stream().filter(i -> i.userId().equals(userId)).findFirst().orElse(null);
        if(item == null) {
            throw new UserIndicadorNotFoundException("Não há inscrições para este usuário");
        }

        Integer posicao = IntStream.range(0, ranking.size())
                          .filter(pos -> ranking.get(pos).userId().equals(userId))
                          .findFirst().getAsInt();

        return new SubscriptionRankingByUserDTO(item, posicao+1);
 
    }
    
}
