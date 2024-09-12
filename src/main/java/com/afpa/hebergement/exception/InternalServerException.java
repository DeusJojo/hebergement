package com.afpa.hebergement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception personnalisée pour gérer les erreurs aléatoires du serveur.
 * Renvoie un statut HTTP 500(error server)
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerException extends RuntimeException {

    //constructeur prenant en paramètre un message
    public InternalServerException(String message) {

        // Appel du constructeur de RuntimeException (parent) avec le message
        super(message);
    }

}
