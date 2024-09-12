package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.CreationException;
import com.afpa.hebergement.exception.InternalServerException;
import com.afpa.hebergement.exception.ResourceNotFoundException;
import com.afpa.hebergement.model.dto.AppServiceDTO;
import com.afpa.hebergement.model.entity.AppService;
import com.afpa.hebergement.model.mapper.AppServiceMapper;
import com.afpa.hebergement.model.repository.AppServiceRepository;
import com.afpa.hebergement.service.entity_service.AppServiceService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service pour gérer les opérations liées aux services
 * */
@Service
@AllArgsConstructor
public class AppServiceServiceImpl implements AppServiceService {

    private final AppServiceRepository appServiceRepository;
    private static final String AN_ERROR_OCCURRED = "An error occurred.";
    private static final String NO_SERVICE_FOUND = "Service does not exist";

    /**
     * Créer un service
     *
     * @param appServiceDto L'objet AppServiceDTO contenant les détails du service.
     * @return AppServiceDTO créé.
     * @throws CreationException si le AppServiceDTO est invalide.
     * @throws InternalServerException en cas d'erreur interne.
     * */
    @Override
    public AppServiceDTO create(AppServiceDTO appServiceDto) {
        if (appServiceDto == null) throw new CreationException("Invalid service provided");

        try {
            AppService appService = AppServiceMapper.mapToService(appServiceDto);
            AppService savedAppService = appServiceRepository.save(appService);
            return AppServiceMapper.mapToServiceDto(savedAppService);
        } catch (DataIntegrityViolationException e) {
            throw new CreationException("Unable to create a service due to data integrity issue. " + e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException(AN_ERROR_OCCURRED + e.getMessage());
        }
    }

    /**
     * Récupère tous les services associés à un centre AFPA donné.
     *
     * @param idAfpaCenter L'identifiant du centre AFPA dont on veut récupérer les services.
     * @return Liste de AppServiceDTO.
     * @throws InternalServerException Si une erreur se produit lors de la récupération des services depuis le repository.
     */
    @Override
    public List<AppServiceDTO> getAllByAfpaCenterId(Integer idAfpaCenter) {
        try {
            List<AppService> appServiceList = appServiceRepository.findAllByAfpaCenters_Id(idAfpaCenter);
            return appServiceList.stream().map(AppServiceMapper::mapToServiceDto).toList();
        } catch (Exception e) {
            throw new InternalServerException(AN_ERROR_OCCURRED);
        }
    }

    /**
     * Récupère un service par son identifiant.
     *
     * @param id L'identifiant du service.
     * @return AppServiceDTO correspondant.
     * @throws ResourceNotFoundException si le service n'est pas trouvé.
     */
    @Override
    public Optional<AppServiceDTO> getById(Integer id) {
        AppService appService = appServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NO_SERVICE_FOUND));
        return Optional.of(AppServiceMapper.mapToServiceDto(appService));
    }

    /**
     * Met à jour un service existant.
     *
     * @param id L'identifiant du service à mettre à jour.
     * @param appServiceDto L'objet AppServiceDTO contenant les nouveaux détails du service.
     * @return AppServiceDTO mis à jour.
     * @throws ResourceNotFoundException si le service n'est pas trouvé.
     * @throws InternalServerException en cas d'erreur interne.
     */
    @Override
    public Optional<AppServiceDTO> update(Integer id, AppServiceDTO appServiceDto) {
        AppService appService = appServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NO_SERVICE_FOUND));
        try {
            appService.setWordingService(appServiceDto.getWordingService());
            AppService savedAppService = appServiceRepository.save(appService);
            return Optional.of(AppServiceMapper.mapToServiceDto(savedAppService));
        } catch (Exception e){
            throw new InternalServerException(AN_ERROR_OCCURRED);
        }
    }

    /**
     * Supprime un service par son identifiant.
     *
     * @param id L'identifiant du service à supprimer.
     * @throws ResourceNotFoundException si le service n'est pas trouvé.
     * @throws InternalServerException en cas d'erreur interne.
     */
    @Override
    public void deleteById(Integer id) {
        AppService appService = appServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NO_SERVICE_FOUND));
        try {
            appServiceRepository.delete(appService);
        } catch (Exception e){
            throw new InternalServerException(AN_ERROR_OCCURRED);
        }
    }
}
