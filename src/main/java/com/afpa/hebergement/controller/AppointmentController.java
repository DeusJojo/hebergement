package com.afpa.hebergement.controller;

import com.afpa.hebergement.model.dto.AppointmentDTO;
import com.afpa.hebergement.service.entity_service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Tag(name = "Appointment", description = "Endpoints to manage appointments")
@RestController
@RequestMapping("/api/appointments")
@AllArgsConstructor
public class AppointmentController {


    private final AppointmentService appointmentService;


    // Endpoint pour créer un nouvel rendez-vous
    @PostMapping("/create")
    @Operation(summary = "Create new appointment")
    public ResponseEntity<AppointmentDTO> createAppointment(@Valid @RequestBody AppointmentDTO appointmentDto) {
        // Appel du service pour créer un nouveau rendez-vous à partir du DTO
        AppointmentDTO createdAppointment = appointmentService.create(appointmentDto);
        // Retourne le rendez-vous créé avec un statut HTTP 201 (Created)
        return new ResponseEntity<>(createdAppointment, HttpStatus.CREATED);
    }


    // Endpoint pour récupérer tous les rendez-vous par centre Afpa
    @GetMapping("/afpa-center/{idAfpaCenter}")
    @Operation(summary = "Get all appointments by Afpa Center")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByAfpaCenter(@PathVariable("idAfpaCenter") Integer idAfpaCenter) {
        // Appel du service pour obtenir tous les rendez-vous par centre afpa
        List<AppointmentDTO> appointments = appointmentService.getAllAppointmentsByAfpaCenter(idAfpaCenter);
        // Retourne la liste des rendez-vous avec un statut HTTP 200 (OK)
        return ResponseEntity.ok(appointments);

    }


    // Endpoint pour récupérer un rendez-vous par son ID
    @GetMapping("/{id}")
    @Operation(summary = "Get appointment by ID")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable("id") Integer idAppointment) {
        // Appel du service pour obtenir un rendez-vous par son ID
        Optional<AppointmentDTO> appointment = appointmentService.getById(idAppointment);

        // Retourne le rendez-vous avec un statut HTTP 200 (OK) s'il est présent
        return appointment.map(ResponseEntity::ok)
                // Retourne une réponse avec un statut HTTP 404 (Not Found) si le rendez-vous n'est pas trouvé
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Endpoint pour récupérer tous les rendez-vous par centre Afpa avec pagination
    @GetMapping("/afpa-center/{idAfpaCenter}/page")
    @Operation(summary = "Get appointments by Afpa Center with pagination")
    public ResponseEntity<Page<AppointmentDTO>> getAppointmentsByAfpaCenterPage(
            @PathVariable("idAfpaCenter") Integer idAfpaCenter,
            // Définit un paramètre de requête "page" avec une valeur par défaut de 0 (1ère page)
            @RequestParam(defaultValue = "0") int page,
            // Définit un paramètre de requête "size" avec une valeur par défaut de 10 (10 éléments par page)
            @RequestParam(defaultValue = "10") int size) {

        // Appel du service pour obtenir les rendez-vous par centre Afpa avec pagination
        Page<AppointmentDTO> appointmentsPage = appointmentService.getAllAppointmentsByAfpaCenter(idAfpaCenter, page, size);
        // Retourne la page des rendez-vous avec un statut HTTP 200 (OK)
        return ResponseEntity.ok(appointmentsPage);
    }


    // Endpoint pour récupérer les rendez-vous par intendant
    @GetMapping("/intendant/{idIntendant}")
    @Operation(summary = "Get appointments by Intendant ID")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByIntendant(@PathVariable("idIntendant") Integer idIntendant) {
        // Appel du service pour obtenir les rendez-vous par intendant
        List<AppointmentDTO> appointments = appointmentService.getAppointmentsByIntendant(idIntendant);
        // Retourne la liste des rendez-vous avec un statut HTTP 200 (OK)
        return ResponseEntity.ok(appointments);
    }

    // Endpoint pour récupérer les rendez-vous par utilisateur
    @GetMapping("/user/{idUser}")
    @Operation(summary = "Get appointments by User ID")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByUser(@PathVariable("idUser") Integer idUser) {
        // Appel du service pour obtenir les rendez-vous par utilisateur
        List<AppointmentDTO> appointments = appointmentService.getAppointmentsByUser(idUser);
        // Retourne la liste des rendez-vous avec un statut HTTP 200 (OK)
        return ResponseEntity.ok(appointments);
    }


    // Endpoint pour récupérer les rendez-vous par date
    @GetMapping("/date")
    @Operation(summary = "Get appointments by Date")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByDate(@RequestParam("date") String dateAppointment) {
        // Appel du service pour obtenir les rendez-vous par date
        List<AppointmentDTO> appointments = appointmentService.getAppointmentsByDate(dateAppointment);
        // Retourne la liste des rendez-vous avec un statut HTTP 200 (OK)
        return ResponseEntity.ok(appointments);
    }


    // Endpoint pour mettre à jour un rendez-vous par son ID
    @PutMapping("/update/{id}")
    @Operation(summary = "Update appointment by ID")
    public ResponseEntity<AppointmentDTO> updateAppointment(@PathVariable("id") Integer idAppointment, @Valid @RequestBody AppointmentDTO appointmentDto) {
        // Appel du service pour mettre à jour le rendez-vous existant avec les nouvelles données
        Optional<AppointmentDTO> updatedAppointment = appointmentService.update(idAppointment, appointmentDto);

        // Retourne le rendez-vous mis à jour avec un statut HTTP 200 (OK) s'il est présent
        return updatedAppointment.map(ResponseEntity::ok)
                // Retourne une réponse avec un statut HTTP 404 (Not Found) si le rendez-vous n'est pas trouvé
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Endpoint pour supprimer un rendez-vous par son ID
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete appointment by ID")
    public ResponseEntity<HttpStatus> deleteAppointment(@PathVariable("id") Integer idAppointment) {
        // Appel du service pour supprimer le rendez-vous par son ID
        appointmentService.deleteById(idAppointment);
        // Retourne un statut HTTP 204 (No Content) après la suppression
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
