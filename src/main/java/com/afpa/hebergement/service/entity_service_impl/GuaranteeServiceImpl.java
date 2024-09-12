package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.CreationException;
import com.afpa.hebergement.exception.InternalServerException;
import com.afpa.hebergement.exception.ResourceNotFoundException;
import com.afpa.hebergement.model.dto.GuaranteeDTO;
import com.afpa.hebergement.model.entity.Guarantee;
import com.afpa.hebergement.model.mapper.GuaranteeMapper;
import com.afpa.hebergement.model.mapper.GuaranteeTypeMapper;
import com.afpa.hebergement.model.mapper.AfpaCenterMapper;
import com.afpa.hebergement.model.repository.GuaranteeRepository;
import com.afpa.hebergement.service.entity_service.GuaranteeService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service pour gérer les garanties.
 * Ce service implémente les méthodes génériques de GenericService et les méthodes spécifiques de GuaranteeService.
 */
@Service
@AllArgsConstructor
public class GuaranteeServiceImpl implements GuaranteeService {

    private final GuaranteeRepository guaranteeRepository;

    private static final String AN_ERROR_OCCURRED = "An error occurred.";


    /**
     * Crée une nouvelle garantie à partir du DTO fourni.
     *
     * @param guaranteeDto Le DTO de la garantie à créer
     * @return Le DTO de la garantie créée
     * @throws CreationException Si une erreur survient lors de la création de la garantie
     */
    @Override
    public GuaranteeDTO create(GuaranteeDTO guaranteeDto) {
        if(guaranteeDto == null) throw new CreationException("Invalid guarantee provided");

        try {
            Guarantee guarantee = GuaranteeMapper.mapToGuarantee(guaranteeDto);
            Guarantee savedGuarantee = guaranteeRepository.save(guarantee);
            return GuaranteeMapper.mapToGuaranteeDto(savedGuarantee);
        } catch (DataIntegrityViolationException e){
            throw new CreationException("Unable to create a guarantee due to a data integrity issue.");
        } catch (Exception e) {
            throw new InternalServerException(AN_ERROR_OCCURRED);
        }
    }

    /**
     * Récupère toutes les garanties.
     *
     * @return La liste des DTO de toutes les garanties
     * @throws InternalServerException Si une erreur survient lors de la récupération des garanties
     */
    @Override
    public List<GuaranteeDTO> getAll() {
        try {
            List<Guarantee> guaranteeList = guaranteeRepository.findAll();
            return guaranteeList.stream().map(GuaranteeMapper::mapToGuaranteeDto).toList();
        } catch (Exception e) {
            throw new InternalServerException(AN_ERROR_OCCURRED);
        }
    }

    /**
     * Récupère toutes les garanties d'un centre afpa.
     *
     * @return La liste des DTO de toutes les garanties
     * @throws InternalServerException Si une erreur survient lors de la récupération des garanties
     */
    @Override
    public List<GuaranteeDTO> getAllByAfpaCenterId(Integer idAfpaCenter) {
        try {
            List<Guarantee> guaranteeList = guaranteeRepository.findAllByIdAfpaCenter_IdOrderByIdAsc(idAfpaCenter);
            return guaranteeList.stream().map(GuaranteeMapper::mapToGuaranteeDto).toList();
        } catch (Exception e) {
            throw new InternalServerException(AN_ERROR_OCCURRED);
        }
    }

    /**
     * Récupère une garantie par son ID.
     *
     * @param id L'ID de la garantie à récupérer
     * @return Le DTO de la garantie correspondant à l'ID
     * @throws ResourceNotFoundException Si la garantie n'existe pas
     */
    @Override
    public Optional<GuaranteeDTO> getById(Integer id) {
        Guarantee guarantee = guaranteeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guarantee does not exist"));
        return Optional.of(GuaranteeMapper.mapToGuaranteeDto(guarantee));
    }

    /**
     * Récupère une garantie par le type de garantie.
     *
     * @param idGuaranteeType Le type de garantie pour lequel récupérer le DTO
     * @return Le DTO de la garantie correspondant au type de garantie
     * @throws ResourceNotFoundException Si aucune garantie avec ce type n'est trouvée
     */
    @Override
    public Optional<GuaranteeDTO> getByIdGuaranteeTypeAndAfpaCenter(Integer idGuaranteeType, Integer idAfpaCenter) {
        Guarantee guarantee = guaranteeRepository.findByIdGuaranteeType_IdAndIdAfpaCenter_Id(idGuaranteeType, idAfpaCenter)
                .orElseThrow(() -> new ResourceNotFoundException("Guarantee does not exist"));
        return Optional.of(GuaranteeMapper.mapToGuaranteeDto(guarantee));
    }

    /**
     * Met à jour une garantie existante.
     *
     * @param id L'ID de la garantie à mettre à jour
     * @param guaranteeDto Les nouvelles données pour la garantie
     * @return Le DTO de la garantie mise à jour
     * @throws ResourceNotFoundException Si la garantie à mettre à jour n'existe pas
     * @throws InternalServerException Si une erreur survient lors de la mise à jour de la garantie
     */
    @Override
    public Optional<GuaranteeDTO> update(Integer id, GuaranteeDTO guaranteeDto) {
        Guarantee guarantee = guaranteeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guarantee person does not exists"));

        try {
            guarantee.setAmount(guaranteeDto.getAmount());
            guarantee.setIdGuaranteeType(GuaranteeTypeMapper.mapToGuaranteeType(guaranteeDto.getIdGuaranteeType()));
            guarantee.setIdAfpaCenter(AfpaCenterMapper.mapToAfpaCenter(guaranteeDto.getIdAfpaCenter()));
            Guarantee updatedGuarantee = guaranteeRepository.save(guarantee);
            return Optional.of(GuaranteeMapper.mapToGuaranteeDto(updatedGuarantee));
        } catch (Exception e) {
            throw new InternalServerException(AN_ERROR_OCCURRED);
        }
    }

    /**
     * Supprime une garantie par son ID.
     *
     * @param id L'ID de la garantie à supprimer
     * @throws ResourceNotFoundException Si la garantie à supprimer n'existe pas
     * @throws InternalServerException Si une erreur survient lors de la suppression de la garantie
     */
    @Override
    public void deleteById(Integer id) {
        Guarantee guarantee = guaranteeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guarantee does not exists"));
        try {
            guaranteeRepository.delete(guarantee);
        } catch (Exception e) {
            throw new InternalServerException(AN_ERROR_OCCURRED);
        }
    }
}
