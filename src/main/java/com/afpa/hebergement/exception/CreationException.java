package com.afpa.hebergement.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Exception personnalisée pour gérer les données incorrectes ou incomplètes, Tentative de création d'un duplicata, Violations de contraintes de validation.
 * Renvoie un statut HTTP 422(Unprocessable Entity)
 */
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class CreationException extends RuntimeException {


    //constructeur prennant en parametre un message
    public CreationException(String message) {

        // Appel du constructeur de RuntimeException (parent) avec le message
        super(message);
    }

}

