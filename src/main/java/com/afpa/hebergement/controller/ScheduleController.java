package com.afpa.hebergement.controller;

import com.afpa.hebergement.model.dto.ScheduleDTO;
import com.afpa.hebergement.service.entity_service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Tag(name = "Schedule", description = "Endpoints to manage schedules")
@RestController
@RequestMapping("/api/schedules")
@AllArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;


    // Endpoint pour créer un nouveau schedule
    @PostMapping("/create")
    @Operation(summary = "Create new schedule")
    public ResponseEntity<ScheduleDTO> createSchedule(@Valid @RequestBody ScheduleDTO scheduleDto) {
        // Appel du service pour créer un nouveau schedule à partir du DTO
        ScheduleDTO createdSchedule = scheduleService.create(scheduleDto);
        // Retourne le schedule créé avec un statut HTTP 201 (Created)
        return new ResponseEntity<>(createdSchedule, HttpStatus.CREATED);
    }


    // Endpoint pour créer un nouveau schedule
    @PostMapping("/create/{idIntendant}/{idDay}")
    @Operation(summary = "Create new schedule by intendant and days")
    public ResponseEntity<ScheduleDTO> createSchedule(@PathVariable("idIntendant") Integer intendantId,
                                                      @PathVariable("idDay") Integer idDay,
                                                      @Valid @RequestBody ScheduleDTO scheduleDto) {
        // Appel du service pour créer un nouveau schedule à partir du DTO, de l'ID de l'intendant et de l'id jour
        ScheduleDTO createdSchedule = scheduleService.create(scheduleDto, intendantId,idDay);
        // Retourne le schedule créé avec un statut HTTP 201 (Created)
        return new ResponseEntity<>(createdSchedule, HttpStatus.CREATED);
    }


    // Endpoint pour récupérer un schedule par son ID
    @GetMapping("/{id}")
    @Operation(summary = "Get schedule by ID")
    public ResponseEntity<ScheduleDTO> getScheduleById(@PathVariable("id") Integer idSchedule) {
        // Appel du service pour obtenir un schedule par son ID
        Optional<ScheduleDTO> schedule = scheduleService.getById(idSchedule);

        // Retourne le schedule avec un statut HTTP 200 (OK) s'il est présent
        return schedule.map(ResponseEntity::ok)
                // Retourne une réponse avec un statut HTTP 404 (Not Found) si le schedule n'est pas trouvé
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Endpoint pour récupérer le(s) schedule(s) par l'ID de l'intendant
    @GetMapping("/intendant/{idIntendant}")
    @Operation(summary = "Get schedule by Intendant ID")
    public ResponseEntity<List<ScheduleDTO>> getScheduleByIdIntendant(@PathVariable("idIntendant") Integer idIntendant) {
        // Appel du service pour obtenir un schedule par l'ID de l'intendant
        List<ScheduleDTO> schedules = scheduleService.getByIdIntendant(idIntendant);
        // Retourne la liste des schedules avec un statut HTTP 200 (OK)
        return ResponseEntity.ok(schedules);
    }


    // Endpoint pour récupérer un schedule par son ID et celui de l'intendant
    @GetMapping("/{idSchedule}/intendant/{idIntendant}")
    @Operation(summary = "Get schedule by Schedule ID and Intendant ID")
    public ResponseEntity<ScheduleDTO> getScheduleByIdAndIntendantId(@PathVariable("idSchedule") Integer idSchedule,
                                                                     @PathVariable("idIntendant") Integer idIntendant) {

        // Appel du service pour obtenir un schedule par son ID et l'ID de l'intendant
        Optional<ScheduleDTO> schedule = scheduleService.getByIdScheduleAndIdIntendant(idSchedule, idIntendant);

        // Retourne le schedule avec un statut HTTP 200 (OK) si présent, sinon retourne HTTP 404 (Not Found)
        return schedule.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Endpoint pour mettre à jour un schedule par son ID
    @PutMapping("/update/{id}")
    @Operation(summary = "Update schedule by ID")
    public ResponseEntity<ScheduleDTO> updateSchedule(@PathVariable("id") Integer idSchedule, @Valid @RequestBody ScheduleDTO scheduleDto) {
        // Appel du service pour mettre à jour le schedule existant avec les nouvelles données
        Optional<ScheduleDTO> updatedSchedule = scheduleService.update(idSchedule, scheduleDto);

        // Retourne le schedule mis à jour avec un statut HTTP 200 (OK) s'il est présent
        return updatedSchedule.map(ResponseEntity::ok)
                // Retourne une réponse avec un statut HTTP 404 (Not Found) si le schedule n'est pas trouvé
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Endpoint pour mettre à jour un schedule par son ID et l'ID de l'intendant
    @PutMapping("/update/{id}/intendant/{idIntendant}")
    @Operation(summary = "Update schedule by ID and Intendant ID")
    public ResponseEntity<ScheduleDTO> updateSchedule(@PathVariable("id") Integer idSchedule,
                                                      @PathVariable("idIntendant") Integer idIntendant,
                                                      @Valid @RequestBody ScheduleDTO scheduleDto) {
        // Appel du service pour mettre à jour le schedule existant avec les nouvelles données
        Optional<ScheduleDTO> updatedSchedule = scheduleService.updateByIdScheduleAndIdIntendant(idSchedule, idIntendant, scheduleDto);

        // Retourne le schedule mis à jour avec un statut HTTP 200 (OK) s'il est présent
        return updatedSchedule.map(ResponseEntity::ok)
                // Retourne une réponse avec un statut HTTP 404 (Not Found) si le schedule n'est pas trouvé
                .orElseGet(() -> ResponseEntity.notFound().build());
    }



    // Endpoint pour supprimer un schedule par son ID
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete schedule by ID")
    public ResponseEntity<HttpStatus> deleteSchedule(@PathVariable("id") Integer idSchedule) {
        // Appel du service pour supprimer le schedule par son ID
        scheduleService.deleteById(idSchedule);
        // Retourne un statut HTTP 204 (No Content) après suppression
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    // Endpoint pour supprimer un schedule par id_Intendant
    @DeleteMapping("/delete/intendant/{idIntendant}")
    @Operation(summary = "Delete schedule by ID intendant")
    public ResponseEntity<HttpStatus> deleteByIdIntendant(@PathVariable("idIntendant") Integer idIntendant) {
        // Appel du service pour supprimer le schedule par id_Intendant
        scheduleService.deleteByIdIntendant(idIntendant);
        // Retourne un statut HTTP 204 (No Content) après suppression
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}

