package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.CreationException;
import com.afpa.hebergement.exception.InternalServerException;
import com.afpa.hebergement.exception.ResourceNotFoundException;
import com.afpa.hebergement.model.dto.RentDTO;
import com.afpa.hebergement.model.entity.Rent;
import com.afpa.hebergement.model.mapper.AfpaCenterMapper;
import com.afpa.hebergement.model.mapper.RentMapper;
import com.afpa.hebergement.model.repository.RentRepository;
import com.afpa.hebergement.service.entity_service.RentService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service pour gérer les opérations liées aux loyers.
 */
@Service
@AllArgsConstructor
public class RentServiceImpl implements RentService {

    private final RentRepository rentRepository;

    private static final String AN_ERROR_OCCURRED = "An error occurred.";
    private static final String NO_RENT_FOUND = "Rent does not exist";

    /**
     * Crée un nouveau loyer.
     *
     * @param rentDto L'objet RentDTO contenant les détails du loyer.
     * @return RentDTO créé.
     * @throws CreationException si le RentDTO est invalide.
     * @throws InternalServerException en cas d'erreur interne.
     */
    @Override
    public RentDTO create(RentDTO rentDto) {
        if (rentDto == null) throw new CreationException("Invalid rent provided");

        try {
            Rent rent = RentMapper.mapToRent(rentDto);
            Rent savedRent = rentRepository.save(rent);
            return RentMapper.mapToRentDto(savedRent);
        }catch (DataIntegrityViolationException e){
            throw new CreationException("Unable to create a rent due to a data integrity issue.");
        }
        catch (Exception e) {
            throw new InternalServerException(AN_ERROR_OCCURRED);
        }
    }

    /**
     * Récupère tous les loyers.
     *
     * @return Liste de RentDTO.
     * @throws InternalServerException en cas d'erreur interne.
     */
    @Override
    public List<RentDTO> getAll() {
        try {
            List<Rent> rentList = rentRepository.findAll();
            return rentList.stream().map(RentMapper::mapToRentDto).toList();
        } catch (Exception e) {
            throw new InternalServerException(AN_ERROR_OCCURRED);
        }
    }

    /**
     * Récupère tous les loyers associés à un centre AFPA donné.
     *
     * @param idAfpaCenter L'identifiant du centre AFPA dont on veut récupérer les loyers.
     * @return Liste de RentDTO.
     * @throws InternalServerException Si une erreur se produit lors de la récupération des loyers depuis le repository.
     */
    @Override
    public List<RentDTO> getAllByAfpaCenterId(Integer idAfpaCenter) {
        try {
            List<Rent> rentList = rentRepository.findAllByIdAfpaCenter_Id(idAfpaCenter);
            return rentList.stream().map(RentMapper::mapToRentDto).toList();
        } catch (Exception e) {
            throw new InternalServerException(AN_ERROR_OCCURRED);
        }
    }

    /**
     * Récupère un loyer par son identifiant.
     *
     * @param id L'identifiant du loyer.
     * @return RentDTO correspondant.
     * @throws ResourceNotFoundException si le loyer n'est pas trouvé.
     */
    @Override
    public Optional<RentDTO> getById(Integer id) {
        Rent rent = rentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NO_RENT_FOUND));
        return Optional.of(RentMapper.mapToRentDto(rent));
    }

    /**
     * Met à jour un loyer existant.
     *
     * @param id L'identifiant du loyer à mettre à jour.
     * @param rentDto L'objet RentDTO contenant les nouveaux détails du loyer.
     * @return RentDTO mis à jour.
     * @throws ResourceNotFoundException si le loyer n'est pas trouvé.
     * @throws InternalServerException en cas d'erreur interne.
     */
    @Override
    public Optional<RentDTO> update(Integer id, RentDTO rentDto) {
        Rent rent = rentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NO_RENT_FOUND));
        try {
            rent.setAmount(rentDto.getAmount());
            rent.setFrequency(rentDto.getFrequency());
            rent.setIdAfpaCenter(AfpaCenterMapper.mapToAfpaCenter(rentDto.getIdAfpaCenter()));
            Rent updatedRent = rentRepository.save(rent);
            return Optional.of(RentMapper.mapToRentDto(updatedRent));
        } catch (Exception e) {
            throw new InternalServerException(AN_ERROR_OCCURRED);
        }
    }

    /**
     * Supprime un loyer par son identifiant.
     *
     * @param id L'identifiant du loyer à supprimer.
     * @throws ResourceNotFoundException si le loyer n'est pas trouvé.
     * @throws InternalServerException en cas d'erreur interne.
     */
    @Override
    public void deleteById(Integer id) {
        Rent rent = rentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NO_RENT_FOUND));
        try {
            rentRepository.delete(rent);
        } catch (Exception e) {
            throw new InternalServerException(AN_ERROR_OCCURRED);
        }
    }
}
