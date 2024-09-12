package com.afpa.hebergement.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Exception personnalisée pour gérer les doublons.
 * Renvoie un statut HTTP 409(conflict)
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateException extends RuntimeException {


    //constructeur prennant en parametre un message
    public DuplicateException(String message) {

        // Appel du constructeur de RuntimeException (parent) avec le message
        super(message);
    }

}
