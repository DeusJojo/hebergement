package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.*;
import com.afpa.hebergement.model.dto.AppointmentDTO;
import com.afpa.hebergement.model.entity.*;
import com.afpa.hebergement.model.mapper.AppointmentMapper;
import com.afpa.hebergement.model.repository.AfpaCenterRepository;
import com.afpa.hebergement.model.repository.AppointmentRepository;
import com.afpa.hebergement.model.repository.IntendantRepository;
import com.afpa.hebergement.model.repository.AppUserRepository;
import com.afpa.hebergement.service.entity_service.AppointmentService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {


    private final AppointmentRepository appointmentRepository;
    private final IntendantRepository intendantRepository;
    private final AppUserRepository appUserRepository;
    private final AfpaCenterRepository afpaCenterRepository;
    public static final String NO_APPOINTMENTS_FOUND = "No appointments found";


    // dans swagger le format appointment : "dateAppointment" : "30/08/2024 07:58"
    @Override
    public AppointmentDTO create(AppointmentDTO appointmentDto) {

        // Recherche de l'ID de l'intendant dans le repository
        Intendant idIntendant = intendantRepository.findById(appointmentDto.getIdIntendant().getId()).orElse(null);
        if (idIntendant == null) {
            throw new ResourceNotFoundException("Intendant not found with id: " + appointmentDto.getIdIntendant().getId());
        }

        // Recherche de l'ID de l'utilisateur dans le repository
        AppUser idUser = appUserRepository.findById(appointmentDto.getIdUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + appointmentDto.getIdUser().getId()));


        // Vérifiez si l'ID de l'intendant et l'ID de l'utilisateur sont les mêmes (ne doivent pas être les memes !)
        if (idIntendant.getIdUser().getId().equals(idUser.getId())) {
            throw new IllegalArgumentException("Intendant ID and User ID cannot be the same");
        }

        //vérifier si la date de rendez-vous n'est pas postérieure à la date du jour
        if (appointmentDto.getDateAppointment().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot create an appointment for a past date");
        }

        // Vérifie si le rendez-vous existe déjà pour cette date/heure et cet utilisateur et l'intendant
        Optional<Appointment> existingAppointment = appointmentRepository.findByDateAppointmentAndIdIntendantAndIdUser(
                appointmentDto.getDateAppointment(),
                idIntendant,
                idUser
        );

        if (existingAppointment.isPresent()) {
            // Lancer une exception si le rendez-vous existe déjà
            throw new DuplicateException("Appointment already exists for the given date and user");
        }


        // Mappe le DTO en entité Appointment
        Appointment appointment = AppointmentMapper.mapToAppointmentEntity(appointmentDto);

        try {

            // Sauvegarde l'entité Appointment dans le repository
            Appointment savedAppointment = appointmentRepository.save(appointment);

            // Mappe l'entité Appointment sauvegardée en DTO et la retourne
            return AppointmentMapper.mapToAppointmentDTO(savedAppointment);

        } catch (DataIntegrityViolationException e) {
            // Capturer une exception de violation d'intégrité des données
            throw new CreationException("Error occurred while creating appointment");
        }
    }


    @Override
    public List<AppointmentDTO> getAllAppointmentsByAfpaCenter(Integer idAfpaCenter) {

        //recherche de l'id du centre afpa dans le repository
        AfpaCenter afpaCenter = afpaCenterRepository.findById(idAfpaCenter)
                .orElseThrow(() -> new ResourceNotFoundException("Afpa Center ID not found: " + idAfpaCenter));

        // Cherche les entités appointments par id afpa center
        List<Appointment> appointmentsList = appointmentRepository.findByIdUser_IdAfpaCenter_Id(afpaCenter.getId());

        // Vérifie si la liste est vide
        if (appointmentsList.isEmpty()) {
            // Lance une ResourceNotFoundException si aucune entité n'est trouvée
            throw new NoContentException(NO_APPOINTMENTS_FOUND);
        }

        try {
            // Mappe les entités Room en DTO et retourne la liste
            return appointmentsList.stream()
                    .map(AppointmentMapper::mapToAppointmentDTO)
                    .toList();

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred to recover appointment with ID: " + idAfpaCenter);
        }

    }


    @Override
    public Page<AppointmentDTO> getAllAppointmentsByAfpaCenter(Integer idAfpaCenter, Integer page, Integer size) {

        // Recherche de l'id du centre Afpa dans le repository
        AfpaCenter afpaCenter = afpaCenterRepository.findById(idAfpaCenter)
                .orElseThrow(() -> new ResourceNotFoundException("Afpa Center ID not found: " + idAfpaCenter));

        // Crée un objet Pageable avec le numéro de page, la taille de la page, et le tri par ID croissant
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        // Cherche les entités appointments par id Afpa center avec pagination
        Page<Appointment> appointmentsPage = appointmentRepository.findByIdUser_IdAfpaCenter_Id(afpaCenter.getId(), pageable);

        // Vérifie si la liste est vide
        if (appointmentsPage.isEmpty()) {
            // Lance une NoContentException si aucune entité n'est trouvée
            throw new NoContentException(NO_APPOINTMENTS_FOUND);
        }

        try {
            // Mappe les entités Appointment en DTO et retourne la page
            return appointmentsPage.map(AppointmentMapper::mapToAppointmentDTO);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred to recover appointments for Afpa Center ID: " + idAfpaCenter);
        }
    }


    @Override
    public Optional<AppointmentDTO> getById(Integer id) {

        // Cherche une entité Appointment par son identifiant
        Appointment findAppointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));

        try {
            // Convertit l'entité Appointment en AppointmentDTO par le AppointmentMapper
            AppointmentDTO appointmentDTO = AppointmentMapper.mapToAppointmentDTO(findAppointment);

            // Retourne le AppointmentDTO encapsulé dans un Optional
            return Optional.of(appointmentDTO);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred to recover appointment with ID: " + id);
        }

    }


    @Override
    public List<AppointmentDTO> getAppointmentsByIntendant(Integer idIntendant) {

        // Recherche de l'intendant dans le repository
        Intendant intendant = intendantRepository.findById(idIntendant)
                .orElseThrow(() -> new ResourceNotFoundException("Intendant ID not found: " + idIntendant));

        // Cherche les entités appointments par id intendant
        List<Appointment> appointmentsList = appointmentRepository.findByIdIntendant_Id(intendant.getId());

        // Vérifie si la liste est vide
        if (appointmentsList.isEmpty()) {
            // Lance une ResourceNotFoundException si aucune entité n'est trouvée
            throw new NoContentException(NO_APPOINTMENTS_FOUND);
        }

        try {
            // Mappe les entités Appointment en DTO et retourne la liste
            return appointmentsList.stream()
                    .map(AppointmentMapper::mapToAppointmentDTO)
                    .toList();

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred to recover appointments for Intendant ID: " + idIntendant);
        }
    }


    @Override
    public List<AppointmentDTO> getAppointmentsByUser(Integer idUser) {

        // Recherche de l'utilisateur dans le repository
        AppUser user = appUserRepository.findById(idUser)
                .orElseThrow(() -> new ResourceNotFoundException("User ID not found: " + idUser));

        // Cherche les entités appointments par id utilisateur
        List<Appointment> appointmentsList = appointmentRepository.findByIdUser_Id(user.getId());

        // Vérifie si la liste est vide
        if (appointmentsList.isEmpty()) {
            // Lance une ResourceNotFoundException si aucune entité n'est trouvée
            throw new NoContentException(NO_APPOINTMENTS_FOUND);
        }

        try {
            // Mappe les entités Appointment en DTO et retourne la liste
            return appointmentsList.stream()
                    .map(AppointmentMapper::mapToAppointmentDTO)
                    .toList();

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred to recover appointments for User ID: " + idUser);
        }
    }


    // rechercher dans swagger avec ce format : dd/MM/yyyy
    @Override
    public List<AppointmentDTO> getAppointmentsByDate(String date) {

        // Définir le format du paramètre de date attendu dans l'URL (format : "dd/MM/yyyy")
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        // Convertir la chaîne de caractères en objet LocalDate, vérifie que la chaîne de caractères date correspond au format spécifié par formatter
        LocalDate dateAppointment = LocalDate.parse(date, formatter);

        // Convertir la date en début et fin de la journée
        LocalDateTime startDate = dateAppointment.atStartOfDay(); // 00:00:00
        LocalDateTime endDate = dateAppointment.plusDays(1).atStartOfDay().minusNanos(1); // 23:59:59.999999999

        // Cherche les entités appointments dans la plage de dates
        List<Appointment> appointmentsList = appointmentRepository.findByDateAppointmentBetween(startDate, endDate);

        // Vérifie si la liste est vide
        if (appointmentsList.isEmpty()) {
            // Lance une ResourceNotFoundException si aucune entité n'est trouvée
            throw new NoContentException(NO_APPOINTMENTS_FOUND);
        }

        try {
            // Mappe les entités Appointment en DTO et retourne la liste
            return appointmentsList.stream()
                    .map(AppointmentMapper::mapToAppointmentDTO)
                    .toList();

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred to recover appointments for date: " + dateAppointment);
        }
    }


    // dans swagger le format appointment : "dateAppointment" : "30-08-2024 07:58"
    @Override
    public Optional<AppointmentDTO> update(Integer id, AppointmentDTO appointmentDto) {

        // Cherche l'entité Appointment à mettre à jour par son identifiant
        Appointment appointmentToUpdate = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment ID not found: " + id));


        //vérifier si la date de rendez-vous n'est pas postérieure à la date du jour
        if (appointmentDto.getDateAppointment().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot update an appointment for a past date");
        }

        // Met à jour les champs date de rendez-vous et raison de l'entité Appointment avec les valeurs du DTO
        appointmentToUpdate.setDateAppointment(appointmentDto.getDateAppointment());

        if (appointmentDto.getReason() != null && !appointmentDto.getReason().isBlank()) {
            appointmentToUpdate.setReason(appointmentDto.getReason());
        }

        try {
            // Sauvegarde l'entité Appointment mise à jour dans le repository
            Appointment updatedAppointment = appointmentRepository.save(appointmentToUpdate);

            // Mappe l'entité Appointment mise à jour en DTO et la retourne (encapsulée) dans un Optional
            return Optional.of(AppointmentMapper.mapToAppointmentDTO(updatedAppointment));

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while updating the appointment with ID: " + id);
        }
    }


    @Override
    public void deleteById(Integer id) {

        // Recherche de l'entité Appointment par son identifiant
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));

        try {
            // Supprime l'entité Appointment trouvée
            appointmentRepository.delete(appointment);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("An unexpected error occurred while deleting the appointment with id: " + id);
        }
    }

}
