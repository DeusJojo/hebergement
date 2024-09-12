package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.CreationException;
import com.afpa.hebergement.exception.InternalServerException;
import com.afpa.hebergement.exception.ResourceNotFoundException;
import com.afpa.hebergement.model.dto.ContactPersonDTO;
import com.afpa.hebergement.model.entity.ContactPerson;
import com.afpa.hebergement.model.mapper.ContactPersonMapper;
import com.afpa.hebergement.model.repository.ContactPersonRepository;
import com.afpa.hebergement.service.entity_service.ContactPersonService;
import com.afpa.hebergement.util.StringUtil;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implémentation de l'interface ContactPersonService fournissant les opérations CRUD pour les entités ContactPersonDto.
 */
@Service
@AllArgsConstructor
public class ContactPersonServiceImpl implements ContactPersonService {

    private final ContactPersonRepository contactPersonRepository;
    private static final String CONTACT_PERSON_DOES_NOT_EXISTS = "Contact person does not exists";
    private static final String AN_ERROR_OCCURRED = "An error occurred.";

    /**
     * Crée une nouvelle entité ContactPersonDto basée sur le ContactPersonDto fourni.
     *
     * @param contactPersonDto Le ContactPersonDto à créer.
     * @return L'objet ContactPersonDto créé.
     * @throws CreationException S'il y a un problème lors de la création de la personne de contact.
     * @throws InternalServerException En cas d'erreur interne du serveur.
     */
    @Override
    public ContactPersonDTO create(ContactPersonDTO contactPersonDto) {
        if(contactPersonDto == null) throw new CreationException("Invalid contact person provided.");

        try {
            ContactPerson contactPerson = ContactPersonMapper.mapToContactPerson(contactPersonDto);
            ContactPerson savedContactPerson = contactPersonRepository.save(contactPerson);
            return ContactPersonMapper.mapToContactPersonDto(savedContactPerson);
        } catch (DataIntegrityViolationException e){
            throw new CreationException("Unable to create contact person due to a data integrity issue.");
        }
        catch (Exception e) {
            throw new InternalServerException(AN_ERROR_OCCURRED);
        }

    }

    /**
     * Récupère une entité ContactPersonDto par son ID.
     *
     * @param id L'ID du ContactPersonDto à récupérer.
     * @return Optionnel contenant l'objet ContactPersonDto s'il est trouvé.
     * @throws ResourceNotFoundException Si aucune personne de contact n'est trouvée avec cet ID.
     */
    @Override
    public Optional<ContactPersonDTO> getById(Integer id) {
        ContactPerson contactPerson = contactPersonRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(CONTACT_PERSON_DOES_NOT_EXISTS));
        return Optional.of(ContactPersonMapper.mapToContactPersonDto(contactPerson));
    }

    /**
     * Met à jour une entité ContactPersonDto identifiée par son ID.
     *
     * @param id L'ID de la ContactPersonDto à mettre à jour.
     * @param contactPersonDto Le ContactPersonDto contenant les nouvelles données.
     * @return L'objet ContactPersonDto mis à jour.
     * @throws ResourceNotFoundException Si la personne de contact n'est pas trouvée.
     * @throws InternalServerException En cas d'erreur interne du serveur.
     */
    @Override
    public Optional<ContactPersonDTO> update(Integer id, ContactPersonDTO contactPersonDto) {
        ContactPerson contactPerson = contactPersonRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(CONTACT_PERSON_DOES_NOT_EXISTS));

        try {
            contactPerson.setFirstname(contactPersonDto.getFirstname().toLowerCase());
            contactPerson.setName(contactPersonDto.getName().toLowerCase());
            contactPerson.setPhoneNumberContact(StringUtil.deleteSpace(contactPersonDto.getPhoneNumberContact()));
            ContactPerson updatedContactPerson = contactPersonRepository.save(contactPerson);
            return Optional.of(ContactPersonMapper.mapToContactPersonDto(updatedContactPerson));
        } catch (Exception e) {
            throw new InternalServerException(AN_ERROR_OCCURRED);
        }
    }

    /**
     * Supprime une entité ContactPersonDto identifiée par son ID.
     *
     * @param id L'ID de la ContactPersonDto à supprimer.
     * @throws ResourceNotFoundException Si la personne de contact n'est pas trouvée.
     * @throws InternalServerException En cas d'erreur interne du serveur.
     */
    @Override
    public void deleteById(Integer id) {
        ContactPerson contactPerson = contactPersonRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(CONTACT_PERSON_DOES_NOT_EXISTS));
        try {
            contactPersonRepository.delete(contactPerson);
        } catch (Exception e) {
            throw new InternalServerException(AN_ERROR_OCCURRED);
        }
    }
}
