package com.afpa.hebergement.model.repository;

import com.afpa.hebergement.model.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    // Méthode pour trouver une réservation par date de début, date de fin et ID de la chambre

    /* findByStartDateAndEndDateAndIdRoom : Cette méthode utilise les noms de colonnes (startDate, endDate, et idRoom.id)
    pour générer une requête JPQL qui cherche une réservation avec les dates et la chambre spécifiées.*/

    //JPQL (Java Persistence Query Langage) est un langage de requête orienté objet qui fonctionne sur les objets et leurs relations, plutôt que sur les tables de la base de données.
    //Optional<Reservation> findByStartDateAndEndDateAndIdRoom_Id(LocalDate startDate, LocalDate endDate, Integer idRoom);


    //rechercher une chambre présente dans la table reservation
    List<Reservation> findByIdRoom_Id(Integer roomId);

    //rechercher les reservations d'un centre afpa
    List<Reservation> findByIdRoom_IdFloor_IdAfpaCenter_Id(Integer idAfpaCenter);

    //vérifier si une réservation existe déjà si les dates ne se chevauchent pas
    @Query("SELECT r FROM Reservation r WHERE r.idRoom.id = :roomId AND (r.startDate < :endDate AND r.endDate > :startDate)")
    List<Reservation> findOverlappingReservations(
            @Param("roomId") Integer roomId, //:roomId
            @Param("startDate") LocalDate startDate, //:startDate
            @Param("endDate") LocalDate endDate //:endDate
    );

    //vérifier si une réservation existe déjà si les dates ne se chevauchent pas, mais en excluant la reservation que l'on veut mettre à jour
    @Query("SELECT r FROM Reservation r WHERE r.idRoom.id = :roomId AND (r.startDate < :endDate AND r.endDate > :startDate) AND r.id != :reservationId")
    List<Reservation> findOverlappingReservationsExcludingCurrent(
            @Param("roomId") Integer roomId, //:roomId
            @Param("startDate") LocalDate startDate,  //:startDate
            @Param("endDate") LocalDate endDate, //:endDate
            @Param("reservationId") Integer reservationId //:reservationId
    );


}
