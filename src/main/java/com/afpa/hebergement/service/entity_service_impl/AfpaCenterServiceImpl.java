package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.CreationException;
import com.afpa.hebergement.exception.DuplicateException;
import com.afpa.hebergement.exception.InternalServerException;
import com.afpa.hebergement.exception.ResourceNotFoundException;
import com.afpa.hebergement.model.dto.AfpaCenterDTO;
import com.afpa.hebergement.model.entity.AfpaCenter;
import com.afpa.hebergement.model.entity.City;
import com.afpa.hebergement.model.entity.Department;
import com.afpa.hebergement.model.entity.Region;
import com.afpa.hebergement.model.mapper.AfpaCenterMapper;
import com.afpa.hebergement.model.mapper.CityMapper;
import com.afpa.hebergement.model.repository.AfpaCenterRepository;
import com.afpa.hebergement.model.repository.CityRepository;
import com.afpa.hebergement.model.repository.DepartmentRepository;
import com.afpa.hebergement.model.repository.RegionRepository;
import com.afpa.hebergement.service.entity_service.AfpaCenterService;
import com.afpa.hebergement.util.StringUtil;
import com.afpa.hebergement.util.VerifyCityAndDepartmentAndRegionExist;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service pour la gestion des centres AFPA.
 */
@Service
@AllArgsConstructor
public class AfpaCenterServiceImpl implements AfpaCenterService {

    private final AfpaCenterRepository afpaCenterRepository;
    private final CityRepository cityRepository;
    private final DepartmentRepository departmentRepository;
    private final RegionRepository regionRepository;
    private final VerifyCityAndDepartmentAndRegionExist verifyCityAndDepartmentAndRegionExist;
    private static final String AN_ERROR_OCCURRED = "An error occurred.";
    private static final String NO_AFPA_CENTER_FOUND = "Afpa center does not exist";

    /**
     * Crée un nouveau centre AFPA.
     *
     * @param afpaCenterDto Le DTO du centre AFPA
     * @return Le DTO du centre AFPA créé
     * @throws CreationException si le centre AFPA fourni est invalide
     * @throws DuplicateException si un centre avec le même nom existe déjà
     * @throws InternalServerException si une erreur se produit lors de la création
     */
    @Override
    public AfpaCenterDTO create(AfpaCenterDTO afpaCenterDto) {
        if (afpaCenterDto == null) throw new CreationException("Invalid afpa center provided");

//        Vérifie si le centre existe déjà
        AfpaCenter afpaCenter = AfpaCenterMapper.mapToAfpaCenter(afpaCenterDto);
        verifyExistingAfpaCenter(afpaCenter);

        try {
            // Passe les repositories nécessaires pour gérer la ville et autres entités
            City city = verifyCityAndDepartmentAndRegionExist.manageCity(
                    afpaCenter.getIdCity(),
                    cityRepository,
                    departmentRepository,
                    regionRepository
            );
            afpaCenter.setIdCity(city);

            AfpaCenter savedAfpaCenter = afpaCenterRepository.save(afpaCenter);
            return AfpaCenterMapper.mapToAfpaCenterDto(savedAfpaCenter);
        } catch (DataIntegrityViolationException e){
            throw new CreationException("Unable to create an afpa center due to a data integrity issue.");
        }
        catch (Exception e) {
            throw new InternalServerException(AN_ERROR_OCCURRED + e.getMessage());
        }
    }

    /**
     * Récupère tous les centres AFPA.
     *
     * @return Une liste de DTO de centres AFPA
     * @throws InternalServerException si une erreur se produit lors de la récupération
     */
    @Override
    public List<AfpaCenterDTO> getAll() {
        try {
            List<AfpaCenter> afpaCenterList = afpaCenterRepository.findAll();
            return afpaCenterList.stream().map(AfpaCenterMapper::mapToAfpaCenterDto).toList();
        } catch (Exception e) {
            throw new InternalServerException(AN_ERROR_OCCURRED);
        }
    }

    /**
     * Récupère un centre AFPA par son ID.
     *
     * @param id L'ID du centre AFPA
     * @return Un optional contenant le DTO du centre AFPA si trouvé
     * @throws ResourceNotFoundException si le centre AFPA n'est pas trouvé
     */
    @Override
    public Optional<AfpaCenterDTO> getById(Integer id) {
        AfpaCenter afpaCenter = afpaCenterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NO_AFPA_CENTER_FOUND));
        return Optional.of(AfpaCenterMapper.mapToAfpaCenterDto(afpaCenter));
    }

    /**
     * Récupère un centre AFPA par son nom.
     *
     * @param centerName Le nom du centre AFPA
     * @return Un optional contenant le DTO du centre AFPA si trouvé
     * @throws ResourceNotFoundException si le centre AFPA n'est pas trouvé
     */
    @Override
    public Optional<AfpaCenterDTO> getByCenterName(String centerName) {
        AfpaCenter afpaCenter = afpaCenterRepository.findByCenterName(centerName.toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException(NO_AFPA_CENTER_FOUND));
        return Optional.of(AfpaCenterMapper.mapToAfpaCenterDto(afpaCenter));
    }

    /**
     * Récupère un centre AFPA par le nom de la ville.
     *
     * @param cityName Le nom de la ville où se trouve le centre AFPA
     * @return Un optional contenant le DTO du centre AFPA si trouvé
     * @throws ResourceNotFoundException si le centre AFPA n'est pas trouvé
     */
    @Override
    public Optional<AfpaCenterDTO> getByCityName(String cityName) {
        AfpaCenter afpaCenter = afpaCenterRepository.findByIdCity_CityName(cityName.toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException(NO_AFPA_CENTER_FOUND));
        return Optional.of(AfpaCenterMapper.mapToAfpaCenterDto(afpaCenter));
    }

    /**
     * Met à jour un centre AFPA existant.
     *
     * @param id L'ID du centre AFPA à mettre à jour
     * @param afpaCenterDto Les nouvelles données pour le centre AFPA
     * @return Un optional contenant le DTO du centre AFPA mis à jour si réussi
     * @throws ResourceNotFoundException si le centre AFPA n'est pas trouvé
     * @throws InternalServerException si une erreur se produit lors de la mise à jour
     */
    @Override
    public Optional<AfpaCenterDTO> update(Integer id, AfpaCenterDTO afpaCenterDto) {
        AfpaCenter afpaCenter = afpaCenterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NO_AFPA_CENTER_FOUND));
        try {
            afpaCenter.setCenterName(afpaCenterDto.getCenterName().toLowerCase().trim());
            afpaCenter.setAddressCenter(afpaCenterDto.getAddressCenter().toLowerCase().trim());
            afpaCenter.setComplementAddrCenter(afpaCenterDto.getComplementAddrCenter().toLowerCase().trim());
            afpaCenter.setSiren(afpaCenterDto.getSiren().toLowerCase().trim());
            afpaCenter.setTvaNumber(StringUtil.deleteSpace(afpaCenterDto.getTvaNumber().toLowerCase().trim()));
            afpaCenter.setCenterPhoneNum(StringUtil.deleteSpace(afpaCenterDto.getCenterPhoneNum().toLowerCase().trim()));
            afpaCenter.setFaxCenter(StringUtil.deleteSpace(afpaCenterDto.getFaxCenter().toLowerCase().trim()));
            afpaCenter.setCenterManager(afpaCenterDto.getCenterManager().toLowerCase().trim());
            afpaCenter.setCodeCenter(afpaCenterDto.getCodeCenter().toLowerCase().trim());
            afpaCenter.setIdCity(CityMapper.mapToCity(afpaCenterDto.getIdCity()));
            AfpaCenter updatedAfpaCenter = afpaCenterRepository.save(afpaCenter);
            return Optional.of(AfpaCenterMapper.mapToAfpaCenterDto(updatedAfpaCenter));
        } catch (DataIntegrityViolationException e){
            throw new CreationException("Unable to create an afpa center due to a data integrity issue.");
        }
        catch (Exception e) {
            throw new InternalServerException(AN_ERROR_OCCURRED);
        }
    }

    /**
     * Supprime un centre AFPA par son ID.
     *
     * @param id L'ID du centre AFPA à supprimer
     * @throws ResourceNotFoundException si le centre AFPA n'est pas trouvé
     * @throws InternalServerException si une erreur se produit lors de la suppression
     */
    @Override
    public void deleteById(Integer id) {
        AfpaCenter afpaCenter = afpaCenterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NO_AFPA_CENTER_FOUND));
        try {
            afpaCenterRepository.delete(afpaCenter);
        } catch (Exception e) {
            throw new InternalServerException(AN_ERROR_OCCURRED);
        }
    }

    private void verifyExistingAfpaCenter(AfpaCenter afpaCenter){
        Optional<AfpaCenter> existingAfpaCenter = afpaCenterRepository.findByCenterName(afpaCenter.getCenterName());
        if (existingAfpaCenter.isPresent()) throw new DuplicateException("Afpa center already exists");
    }
}
