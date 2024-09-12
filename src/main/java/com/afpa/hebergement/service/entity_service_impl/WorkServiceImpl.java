package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.*;
import com.afpa.hebergement.model.dto.WorkDTO;
import com.afpa.hebergement.model.entity.AfpaCenter;
import com.afpa.hebergement.model.entity.Room;
import com.afpa.hebergement.model.entity.Work;
import com.afpa.hebergement.model.entity.WorkType;
import com.afpa.hebergement.model.mapper.WorkMapper;
import com.afpa.hebergement.model.repository.AfpaCenterRepository;
import com.afpa.hebergement.model.repository.RoomRepository;
import com.afpa.hebergement.model.repository.WorkRepository;
import com.afpa.hebergement.model.repository.WorkTypeRepository;
import com.afpa.hebergement.service.entity_service.WorkService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class WorkServiceImpl implements WorkService {

    public static final String NO_WORKS_FOUND = "No works found";
    public static final String ERROR_WORK_DATE = "Error on reservation dates ";
    public static final String BE_EARLIER_THAN_TODAY_S_DATE = "Dates must not be earlier than today's date";

    private final WorkRepository workRepository;
    private final WorkTypeRepository workTypeRepository;
    private final RoomRepository roomRepository;
    private final AfpaCenterRepository afpaCenterRepository;


    @Override
    public WorkDTO create(WorkDTO workDto) {


        // Vérifier que la date de fin est présente, car elle peut être null
        // si end_work n'est pas null
        if (workDto.getEndWork() != null) {

            // Vérifier que la date de début est antérieure à la date de fin
            if (workDto.getStartWork().isAfter(workDto.getEndWork())) {
                throw new IllegalArgumentException(ERROR_WORK_DATE);
            }

            // Vérifier que la date de début et la date de fin ne sont pas dans le passé
            if (workDto.getStartWork().isBefore(LocalDate.now()) || workDto.getEndWork().isBefore(LocalDate.now())) {
                throw new IllegalArgumentException(BE_EARLIER_THAN_TODAY_S_DATE);
            }

        } else {
            // si end_work est null, vérifier que la date de début n'est pas dans le passé
            if (workDto.getStartWork().isBefore(LocalDate.now())) {
                throw new IllegalArgumentException(BE_EARLIER_THAN_TODAY_S_DATE);
            }
        }

        //recherche de l'id de la chambre dans le repository
        WorkType idWorkType = workTypeRepository.findById(workDto.getIdWorkType().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Work type not found with ID: " + workDto.getIdWorkType().getId()));

        //recherche de l'id de la chambre dans le repository
        Room idRoom = roomRepository.findById(workDto.getIdRoom().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + workDto.getIdRoom().getId()));

        // Vérifier si un travail existe déjà pour les mêmes dates, le même type et la même chambre
        Optional<Work> existingWork = workRepository.findByStartWorkAndEndWorkAndIdWorkTypeAndIdRoom(
                workDto.getStartWork(),
                workDto.getEndWork(),
                idWorkType,
                idRoom
        );

        // Vérifier si une tâche en conflit a été trouvée
        if (existingWork.isPresent()) {
            throw new DuplicateException("Work already exists for the given dates, work type, and room");
        }

        try {
            // Mappe le DTO en entité Work
            Work work = WorkMapper.mapToWorkEntity(workDto);

            // Sauvegarde l'entité Work dans le repository
            Work savedWork = workRepository.save(work);

            // Mappe l'entité Work sauvegardée en DTO et la retourne
            return WorkMapper.mapToWorkDTO(savedWork);

        } catch (DataIntegrityViolationException e) {
            // Capture toute exception de violation d'intégrité des données
            throw new CreationException("Error occurred while creating work");
        }
    }


    @Override
    public List<WorkDTO> getAll() {

        // Récupère toutes les entités Work du repository
        List<Work> works = workRepository.findAll();

        // Vérifie si la liste est vide
        if (works.isEmpty()) {
            // Lance une ResourceNotFoundException si aucune entité n'est trouvée
            throw new NoContentException(NO_WORKS_FOUND);
        }

        try {
            // Mappe les entités Work en DTO et retourne la liste
            return works.stream()
                    .map(WorkMapper::mapToWorkDTO)
                    .toList();

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while getting all works");
        }
    }


    @Override
    public Page<WorkDTO> getAll(int page, int size) {

        // Crée un objet Pageable avec le numéro de page, la taille de la page, et le tri par ID croissant.
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        // Récupère toutes les entités Work du repository
        Page<Work> workPage = workRepository.findAll(pageable);

        // Vérifie si la liste est vide
        if (workPage.isEmpty()) {
            // Lance une ResourceNotFoundException si aucune entité n'est trouvée
            throw new NoContentException(NO_WORKS_FOUND);
        }

        try {
            // Mappe les entités Work en DTO et retourne la liste
            return workPage.map(WorkMapper::mapToWorkDTO);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while getting page of works");
        }
    }


    @Override
    public Optional<WorkDTO> getById(Integer id) {

        // Cherche une entité Work par son identifiant
        Work findWork = workRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work not found with ID: " + id));

        try {
            // Convertit l'entité Work en WorkDTO par le WorkMapper
            WorkDTO workDTO = WorkMapper.mapToWorkDTO(findWork);

            // Retourne le WorkDTO encapsulé dans un Optional
            return Optional.of(workDTO);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred to recover work with ID: " + id);
        }
    }


    public List<WorkDTO> getWorksByAfpaCenter(Integer idAfpaCenter) {

        //recherche si le centre afpa existe
        AfpaCenter afpaCenter = afpaCenterRepository.findById(idAfpaCenter)
                .orElseThrow(() -> new ResourceNotFoundException("Afpa Center ID not found: " + idAfpaCenter));

        //récupération des travaux d'un centre afpa
        List<Work> worksList = workRepository.findByIdRoom_IdFloor_IdAfpaCenter_Id(afpaCenter.getId());

        // Vérifie si la liste est vide
        if (worksList.isEmpty()) {
            // Lance une ResourceNotFoundException si aucune entité n'est trouvée
            throw new NoContentException(NO_WORKS_FOUND);
        }

        try {
            // Mappe les entités Work en DTO et retourne la liste
            return worksList.stream()
                    .map(WorkMapper::mapToWorkDTO)
                    .toList();

        } catch (Exception e) {
            throw new InternalServerException("Error occurred while getting works by Afpa Center ID");
        }
    }


    @Override
    public Optional<WorkDTO> update(Integer id, WorkDTO workDto) {


        // Vérifier que la date de fin est présente, car elle peut être null
        // si end_work n'est pas null
        if (workDto.getEndWork() != null) {
            // Vérifier que la date de début est antérieure à la date de fin
            if (workDto.getStartWork().isAfter(workDto.getEndWork())) {
                throw new IllegalArgumentException(ERROR_WORK_DATE);
            }

            // Vérifier que la date de début et la date de fin ne sont pas dans le passé
            if (workDto.getStartWork().isBefore(LocalDate.now()) || workDto.getEndWork().isBefore(LocalDate.now())) {
                throw new IllegalArgumentException(BE_EARLIER_THAN_TODAY_S_DATE);
            }

        } else {
            // si end_work est null, vérifier que la date de début n'est pas dans le passé
            if (workDto.getStartWork().isBefore(LocalDate.now())) {
                throw new IllegalArgumentException(BE_EARLIER_THAN_TODAY_S_DATE);
            }
        }

        // Cherche l'entité Work à mettre à jour par son identifiant
        Work workToUpdate = workRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work not found with id: " + id));

        // Met à jour les champs startWork, endWork et workCommentary de l'entité Work avec les valeurs du DTO
        workToUpdate.setStartWork(workDto.getStartWork());
        workToUpdate.setEndWork(workDto.getEndWork());

        //attribut pouvant être null
        if (workDto.getWorkCommentary() != null && !workDto.getWorkCommentary().isBlank()) {
            workToUpdate.setWorkCommentary(workDto.getWorkCommentary());
        }

        // Recherche de l'id du type de travail dans le repository
        WorkType workType = workTypeRepository.findById(workDto.getIdWorkType().getId())
                .orElseThrow(() -> new ResourceNotFoundException("WorkType not found with id: " + workDto.getIdWorkType().getId()));

        // Mise à jour du type de travail
        if (workDto.getIdWorkType() != null) {

            // Mettre à jour le type de travail
            workToUpdate.setIdWorkType(workType);

        } else {
            throw new IllegalArgumentException("id work type cannot be null");
        }

        try {
            // Sauvegarde l'entité Work mise à jour dans le repository
            Work updatedWork = workRepository.save(workToUpdate);

            // Mappe l'entité Work mise à jour en DTO et la retourne (encapsulée) dans un Optional
            return Optional.of(WorkMapper.mapToWorkDTO(updatedWork));

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while updating the work with ID: " + id);
        }
    }


    @Override
    public void deleteById(Integer id) {

        // Tente de trouver l'entité Work par son identifiant
        Work work = workRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work not found with id: " + id));

        try {
            // Supprime l'entité Work trouvée
            workRepository.delete(work);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("An unexpected error occurred while deleting the work with id: " + id);
        }
    }

}

