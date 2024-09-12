package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.CreationException;
import com.afpa.hebergement.exception.InternalServerException;
import com.afpa.hebergement.exception.ResourceNotFoundException;
import com.afpa.hebergement.model.dto.GuaranteeTypeDTO;
import com.afpa.hebergement.model.entity.GuaranteeType;
import com.afpa.hebergement.model.mapper.GuaranteeTypeMapper;
import com.afpa.hebergement.model.repository.GuaranteeTypeRepository;
import com.afpa.hebergement.service.entity_service.GuaranteeTypeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service pour gérer les types de garantie.
 */
@Service
@AllArgsConstructor
public class GuaranteeTypeServiceImpl implements GuaranteeTypeService {

    private final GuaranteeTypeRepository guaranteeTypeRepository;

    private static final String AN_ERROR_OCCURRED = "An error occurred.";
    private static final String NO_GUARANTEE_TYPE_FOUND = "Guarantee type does not exist";

    /**
     * Crée un nouveau type de garantie.
     *
     * @param guaranteeTypeDto Le DTO du type de garantie à créer
     * @return Le DTO du type de garantie créé
     * @throws CreationException si les données fournies sont invalides ou en cas de violation d'intégrité des données
     * @throws InternalServerException en cas d'erreur interne du serveur
     */
    @Override
    public GuaranteeTypeDTO create(GuaranteeTypeDTO guaranteeTypeDto) {
        if (guaranteeTypeDto == null) throw new CreationException("Invalid guarantee type provided");

        try {
            GuaranteeType guaranteeType = GuaranteeTypeMapper.mapToGuaranteeType(guaranteeTypeDto);
            GuaranteeType savedGuaranteeType = guaranteeTypeRepository.save(guaranteeType);
            return GuaranteeTypeMapper.mapToGuaranteeTypeDTO(savedGuaranteeType);
        } catch (DataIntegrityViolationException e){
            throw new CreationException("Unable to create a guarantee type due to a data integrity issue.");
        } catch (Exception e) {
            throw new InternalServerException(AN_ERROR_OCCURRED);
        }
    }

    /**
     * Récupère tous les types de garantie.
     *
     * @return Une liste de DTO de types de garantie
     * @throws InternalServerException en cas d'erreur interne du serveur
     */
    @Override
    public List<GuaranteeTypeDTO> getAll() {
        try {
            List<GuaranteeType> guaranteeTypesList = guaranteeTypeRepository.findAll();
            return guaranteeTypesList.stream().map(GuaranteeTypeMapper::mapToGuaranteeTypeDTO).toList();
        } catch (Exception e){
            throw new InternalServerException(AN_ERROR_OCCURRED);
        }
    }

    /**
     * Récupère un type de garantie par son ID.
     *
     * @param id L'ID du type de garantie
     * @return Un Optional contenant le DTO du type de garantie s'il existe
     * @throws EntityNotFoundException si le type de garantie n'existe pas
     */
    @Override
    public Optional<GuaranteeTypeDTO> getById(Integer id) {
        GuaranteeType guaranteeTypeOptional = guaranteeTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NO_GUARANTEE_TYPE_FOUND));
        return Optional.of(GuaranteeTypeMapper.mapToGuaranteeTypeDTO(guaranteeTypeOptional));
    }

    /**
     * Met à jour un type de garantie.
     *
     * @param id L'ID du type de garantie à mettre à jour
     * @param guaranteeTypeDto Les nouvelles données pour le type de garantie
     * @return Un Optional contenant le DTO du type de garantie mis à jour s'il existe
     * @throws EntityNotFoundException si le type de garantie n'existe pas
     * @throws InternalServerException en cas d'erreur interne du serveur
     */
    @Override
    public Optional<GuaranteeTypeDTO> update(Integer id, GuaranteeTypeDTO guaranteeTypeDto) {
        GuaranteeType guaranteeType = guaranteeTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NO_GUARANTEE_TYPE_FOUND));
        try {
            guaranteeType.setWordingGuaranteeType(guaranteeTypeDto.getWordingGuaranteeType());
            GuaranteeType updatedGuaranteeType = guaranteeTypeRepository.save(guaranteeType);
            return Optional.of(GuaranteeTypeMapper.mapToGuaranteeTypeDTO(updatedGuaranteeType));
        } catch (Exception e){
            throw new InternalServerException(AN_ERROR_OCCURRED);
        }
    }

    /**
     * Supprime un type de garantie par son ID.
     *
     * @param id L'ID du type de garantie à supprimer
     * @throws ResourceNotFoundException si le type de garantie n'existe pas
     * @throws InternalServerException en cas d'erreur interne du serveur
     */
    @Override
    public void deleteById(Integer id) {
        GuaranteeType guaranteeType = guaranteeTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NO_GUARANTEE_TYPE_FOUND));
        try {
            guaranteeTypeRepository.delete(guaranteeType);
        } catch (Exception e){
            throw new InternalServerException(AN_ERROR_OCCURRED);
        }
    }
}