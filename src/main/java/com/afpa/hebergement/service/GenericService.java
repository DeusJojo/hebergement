package com.afpa.hebergement.service;

import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Optional;

public interface GenericService<T, I> {

    default T create(T entityDto) {return null;}

    default List<T> getAll() {return List.of();}

    //récupération de toutes les données avec pagination, mais pas d'obligation d'implémentation selon les entités
    default Page<T> getAll(int page, int size) {
        throw new UnsupportedOperationException("Pagination not supported");
    }

    // Récupère une entité par son ID, l'entité peut être absente, donc retour d'un Optional
    Optional <T> getById(I id);

    // Met à jour une entité par son ID, l'entité peut être absente, donc retour d'un Optional, pas d'obligation d'implémentation selon les entités
    default Optional <T>  update(I id, T entityDto) {return Optional.empty();}

    default void deleteById(I id) {}

}
