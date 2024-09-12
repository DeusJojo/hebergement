package com.afpa.hebergement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


//retourne un statut HTTP 404 (Not Found)
//utilisé pour indiquer que le serveur n'a pas trouvé la ressource demandée (données inexistantes, URL mal formée ou incorrecte, Ressource protégée ou restreinte)
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{

    //constructeur
    public ResourceNotFoundException (String message) {

        // Appel du constructeur de RuntimeException (parent) avec le message
        super(message);

    }

}