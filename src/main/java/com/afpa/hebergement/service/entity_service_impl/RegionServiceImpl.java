package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.*;
import com.afpa.hebergement.model.dto.RegionDTO;
import com.afpa.hebergement.model.entity.Region;
import com.afpa.hebergement.model.mapper.RegionMapper;
import com.afpa.hebergement.model.repository.RegionRepository;
import com.afpa.hebergement.service.entity_service.RegionService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * Implémentation du service pour gérer les opérations sur les régions.
 */
@Service
@AllArgsConstructor
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    private static final String NO_REGION_FOUND = "Region does not exist";

    /**
     * Crée une nouvelle région.
     *
     * @param regionDto Les informations de la région à créer.
     * @return La région créée.
     * @throws CreationException Si les données de la région sont invalides.
     * @throws DuplicateException Si une région avec le même code existe déjà.
     * @throws InternalServerException En cas d'erreur interne du serveur.
     */
    @Override
    public RegionDTO create(RegionDTO regionDto) {

        if(regionDto == null) throw new CreationException("Invalid region provided.");

//        Vérifie si region existe déjà en bdd
        Optional<Region> existingRegion = regionRepository.findByRegionName(regionDto.getRegionName());
        if(existingRegion.isPresent()) {
            throw new DuplicateException("Region already exists.");
        }

        try {
            Region region = RegionMapper.mapToRegion(regionDto);
            Region savedRegion = regionRepository.save(region);
            return RegionMapper.mapToRegionDto(savedRegion);
        }catch (DataIntegrityViolationException e){
            throw new CreationException("Unable to create a region due to a data integrity issue.");
        }
        catch (Exception e){
            throw new InternalServerException("An error occurred.");
        }
    }

    /**
     * Récupère une région par son identifiant.
     *
     * @param id L'identifiant de la région à récupérer.
     * @return Un Optional contenant la région si trouvée.
     * @throws ResourceNotFoundException Si aucune région n'est trouvée avec cet identifiant.
     */
    @Override
    public Optional<RegionDTO> getById(Integer id) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(NO_REGION_FOUND));
        return Optional.of(RegionMapper.mapToRegionDto(region));
    }

    /**
     * Récupère une région par son code de région.
     *
     * @param regionName Le code de la région à récupérer.
     * @return Un Optional contenant la région si trouvée.
     * @throws ResourceNotFoundException Si aucune région n'est trouvée avec ce code.
     */
    @Override
    public Optional<RegionDTO> getByRegionName(String regionName) {
        Region region = regionRepository.findByRegionName(regionName.toLowerCase())
                .orElseThrow(() ->
                        new ResourceNotFoundException(NO_REGION_FOUND));
        return Optional.of(RegionMapper.mapToRegionDto(region));
    }

    /**
     * Supprime une région par son identifiant.
     *
     * @param id L'identifiant de la région à supprimer.
     * @throws ResourceNotFoundException Si aucune région n'est trouvée avec cet identifiant.
     * @throws InternalServerException En cas d'erreur interne du serveur.
     */
    @Override
    public void deleteById(Integer id) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(NO_REGION_FOUND));
        try {
            regionRepository.delete(region);
        } catch (Exception e) {
            throw new InternalServerException("An error occured.");
        }
    }
}
