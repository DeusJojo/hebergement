package com.afpa.hebergement.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception personnalisée pour gérer l'absence de données dans la database.
 * Renvoie un statut HTTP 204(NO CONTENT)
 */
@ResponseStatus(HttpStatus.NO_CONTENT)
public class NoContentException extends RuntimeException {


    //constructeur prennant en parametre un message
    public NoContentException(String message) {

        // Appel du constructeur de RuntimeException (parent) avec le message
        super(message);
    }

}