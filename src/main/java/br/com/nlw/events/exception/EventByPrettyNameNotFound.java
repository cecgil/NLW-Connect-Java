package br.com.nlw.events.exception;

public class EventByPrettyNameNotFound extends RuntimeException {

    public EventByPrettyNameNotFound() {
        super("Evento não encontrado");
    }
    
    
}
