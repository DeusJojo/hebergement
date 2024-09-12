package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.*;

import com.afpa.hebergement.model.dto.ScheduleDTO;
import com.afpa.hebergement.model.entity.Day;
import com.afpa.hebergement.model.entity.Intendant;
import com.afpa.hebergement.model.entity.Schedule;
import com.afpa.hebergement.model.mapper.ScheduleMapper;
import com.afpa.hebergement.model.repository.DayRepository;
import com.afpa.hebergement.model.repository.IntendantRepository;
import com.afpa.hebergement.model.repository.ScheduleRepository;
import com.afpa.hebergement.service.entity_service.ScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@AllArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {


    public static final String INTENDANT_ID_NOT_FOUND = "Intendant ID not found";
    public static final String DAY_ID_NOT_FOUND = "Day ID not found: ";
    private final ScheduleRepository scheduleRepository;
    private final DayRepository dayRepository;
    private final IntendantRepository intendantRepository;


    @Override
    public ScheduleDTO create(ScheduleDTO scheduleDto) {

        // Vérifier si l'ID de l'intendant existe
        Intendant intendant = intendantRepository.findById(scheduleDto.getIdIntendant().getId()).orElse(null);
        if (intendant == null) {
            throw new ResourceNotFoundException(INTENDANT_ID_NOT_FOUND);
        }

        Day day = dayRepository.findById(scheduleDto.getIdDay().getId()).orElse(null);
        if (day == null) {
            throw new ResourceNotFoundException(DAY_ID_NOT_FOUND);
        }


        // Vérifier si un schedule existe déjà pour l'intendant, meme jour et meme horaire
        Optional<Schedule> existSchedule = scheduleRepository.findByIdIntendant_IdAndIdDay_Id(
                intendant.getId(),
                day.getId()
        );

        if (existSchedule.isPresent()) {
            throw new DuplicateException("Schedule with the same day already exists for this intendant.");
        }

        // Mappe le DTO en entité Schedule
        Schedule schedule = ScheduleMapper.mapToScheduleEntity(scheduleDto);

        try {

            // Sauvegarde l'entité Schedule dans le repository
            Schedule savedSchedule = scheduleRepository.save(schedule);

            // Mappe l'entité Schedule sauvegardée en DTO et la retourne
            return ScheduleMapper.mapToScheduleDTO(savedSchedule);

        } catch (DataIntegrityViolationException e) {
            // Capturer une exception de violation d'intégrité des données
            throw new CreationException("Error occurred while creating schedule");
        }
    }


    @Override
    public ScheduleDTO create(ScheduleDTO scheduleDto, Integer idIntendant, Integer idDay) {

        // Vérifier si l'ID de l'intendant existe
        Intendant intendant = intendantRepository.findById(idIntendant)
                .orElseThrow(() -> new ResourceNotFoundException(INTENDANT_ID_NOT_FOUND));

        Day day = dayRepository.findById(idDay)
                .orElseThrow(() -> new ResourceNotFoundException(DAY_ID_NOT_FOUND + idDay));


        // Vérifier si un schedule existe déjà pour l'intendant, meme jour et meme horaire
        Optional<Schedule> existSchedule = scheduleRepository.findByIdIntendant_IdAndIdDay_Id(
                intendant.getId(),
                day.getId()
        );

        if (existSchedule.isPresent()) {
            throw new DuplicateException("Schedule with the same day already exists for this intendant.");
        }

        // Mappe le DTO en entité Schedule
        Schedule schedule = ScheduleMapper.mapToScheduleEntity(scheduleDto);

        // Assigner l'intendant à l'entité Schedule
        schedule.setIdIntendant(intendant);

        //Assigner le jour à l'entité Schedule
        schedule.setIdDay(day);

        try {
            // Sauvegarde l'entité Schedule dans le repository
            Schedule savedSchedule = scheduleRepository.save(schedule);

            // Mappe l'entité Schedule sauvegardée en DTO et la retourne
            return ScheduleMapper.mapToScheduleDTO(savedSchedule);

        } catch (DataIntegrityViolationException e) {
            // Capturer une exception de violation d'intégrité des données
            throw new CreationException("Error occurred while creating schedule");
        }
    }


    @Override
    public Optional<ScheduleDTO> getById(Integer id) {

        // Cherche une entité Schedule par son identifiant
        Schedule findSchedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with ID: " + id));

        try {
            // Convertit l'entité Schedule en ScheduleDTO par le ScheduleMapper
            ScheduleDTO scheduleDTO = ScheduleMapper.mapToScheduleDTO(findSchedule);

            // Retourne le ScheduleDTO encapsulé dans un Optional
            return Optional.of(scheduleDTO);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while getting Schedule by ID");
        }
    }


    public List<ScheduleDTO> getByIdIntendant(Integer idIntendant) {

        //recherche de l'id intendant dans le repository
        Intendant intendantID = intendantRepository.findById(idIntendant)
                .orElseThrow(() -> new ResourceNotFoundException("Intendant ID not found: " + idIntendant));

        // Cherche les entités Schedule par id_intendant
        List<Schedule> schedulesList = scheduleRepository.findByIdIntendant_Id(intendantID.getId());

        // Vérifie si la liste est vide
        if (schedulesList.isEmpty()) {
            // Lance une ResourceNotFoundException si aucune entité n'est trouvée
            throw new NoContentException("No schedules found");
        }

        try {
            // Mappe les entités Schedule en DTO et retourne la liste
            return schedulesList.stream()
                    .map(ScheduleMapper::mapToScheduleDTO)
                    .toList();

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while getting schedule by ID");
        }
    }


    public Optional<ScheduleDTO> getByIdScheduleAndIdIntendant(Integer idSchedule, Integer idIntendant) {

        //recherche de l'id intendant dans le repository
        Intendant existingIntendant = intendantRepository.findById(idIntendant)
                .orElseThrow(() -> new ResourceNotFoundException(INTENDANT_ID_NOT_FOUND));

        // Cherche une entité Schedule par son identifiant et id_intendant
        Schedule findSchedule = scheduleRepository.findByIdAndIdIntendant_Id(idSchedule, existingIntendant.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));

        try {
            // Convertit l'entité Schedule en ScheduleDTO par le ScheduleMapper
            ScheduleDTO scheduleDTO = ScheduleMapper.mapToScheduleDTO(findSchedule);

            // Retourne le ScheduleDTO encapsulé dans un Optional
            return Optional.of(scheduleDTO);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while getting schedule by ID");
        }
    }


    @Override
    public Optional<ScheduleDTO> update(Integer id, ScheduleDTO scheduleDto) {

        // Cherche l'entité Schedule à mettre à jour par son identifiant
        Schedule updateSchedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule ID not found: " + id));

        // Met à jour les champs de l'entité Schedule avec les valeurs du DTO
        updateSchedule.setStartMorning(scheduleDto.getStartMorning());
        updateSchedule.setEndMorning(scheduleDto.getEndMorning());
        updateSchedule.setStartAfternoon(scheduleDto.getStartAfternoon());
        updateSchedule.setEndAfternoon(scheduleDto.getEndAfternoon());


        try {

            // Sauvegarde l'entité Schedule mise à jour dans le repository
            Schedule updatedSchedule = scheduleRepository.save(updateSchedule);

            // Mappe l'entité Schedule mise à jour en DTO et la retourne (encapsulée) dans un Optional
            return Optional.of(ScheduleMapper.mapToScheduleDTO(updatedSchedule));

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while updating schedule");
        }
    }


    public Optional<ScheduleDTO> updateByIdScheduleAndIdIntendant(Integer idSchedule, Integer idIntendant, ScheduleDTO scheduleDto) {

        //recherche de l'id de l'intendant dans le repository
        Intendant intendantId = intendantRepository.findById(idIntendant)
                .orElseThrow(() -> new ResourceNotFoundException(INTENDANT_ID_NOT_FOUND));


        // Cherche l'entité Schedule à mettre à jour par son identifiant et celui de l'intendant
        Schedule scheduleToUpdate = scheduleRepository.findByIdAndIdIntendant_Id(idSchedule, intendantId.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));


        // Met à jour les champs de l'entité Schedule avec les valeurs du DTO
        scheduleToUpdate.setStartMorning(scheduleDto.getStartMorning());
        scheduleToUpdate.setEndMorning(scheduleDto.getEndMorning());
        scheduleToUpdate.setStartAfternoon(scheduleDto.getStartAfternoon());
        scheduleToUpdate.setEndAfternoon(scheduleDto.getEndAfternoon());

        try {

            // Sauvegarde l'entité Schedule mise à jour dans le repository
            Schedule updatedSchedule = scheduleRepository.save(scheduleToUpdate);

            // Mappe l'entité Schedule mise à jour en DTO et la retourne (encapsulée) dans un Optional
            return Optional.of(ScheduleMapper.mapToScheduleDTO(updatedSchedule));

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while updating schedule");
        }

    }


    @Override
    public void deleteById(Integer id) {

        // Tente de trouver l'entité Schedule par son identifiant
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id: " + id));
        try {
            // Supprime l'entité Schedule trouvée
            scheduleRepository.delete(schedule);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("An unexpected error occurred while deleting schedule with id: " + id);
        }
    }


    public void deleteByIdIntendant(Integer idIntendant) {

        //recherche de l'id de l'intendant dans le repository
        Intendant intendant = intendantRepository.findById(idIntendant)
                .orElseThrow(() -> new ResourceNotFoundException(INTENDANT_ID_NOT_FOUND));

        // Tente de trouver l'/les entité(s) schedule(s) par l'id intendant
        List<Schedule> schedulesToDelete = scheduleRepository.findByIdIntendant_Id(intendant.getId());

        // Vérifie si la liste est vide
        if (schedulesToDelete.isEmpty()) {
            // Lance une ResourceNotFoundException si aucune entité n'est trouvée
            throw new NoContentException("No schedules found");
        }

        try {
            // Supprime l'/les entité(s) Schedule trouvée(s)
            scheduleRepository.deleteByIdIntendant_Id(intendant.getId());

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("An unexpected error occurred while deleting schedule with id: " + idIntendant);
        }

    }


}
