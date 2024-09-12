package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.CreationException;
import com.afpa.hebergement.exception.DuplicateException;
import com.afpa.hebergement.exception.InternalServerException;
import com.afpa.hebergement.exception.ResourceNotFoundException;
import com.afpa.hebergement.model.dto.CityDTO;
import com.afpa.hebergement.model.entity.City;
import com.afpa.hebergement.model.mapper.CityMapper;
import com.afpa.hebergement.model.repository.CityRepository;
import com.afpa.hebergement.service.entity_service.CityService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * Implémentation du service pour la gestion des villes.
 * Cette classe implémente les interfaces GenericService et CityService.
 */
@Service
@AllArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    private static final String NO_CITY_FOUND = "City does not exist";

    /**
     * Crée une nouvelle ville.
     * @param cityDto L'objet CityDTO contenant les détails de la ville à créer.
     * @return L'objet CityDTO créé.
     * @throws CreationException Si l'objet cityDto est null.
     * @throws DuplicateException Si une ville avec le même code INSEE existe déjà.
     * @throws InternalServerException Si une erreur interne survient lors de la création.
     */
    @Override
    public CityDTO create(CityDTO cityDto) {
        if (cityDto == null) throw new CreationException("Invalid city provided");

        Optional<City> existingCity = cityRepository.findByInseeCodeAndPostcode(cityDto.getInseeCode(), cityDto.getPostcode());
        if (existingCity.isPresent()) throw new DuplicateException("City already exists");

        try {
            City city = CityMapper.mapToCity(cityDto);
            City savedCity = cityRepository.save(city);
            return CityMapper.mapToCityDto(savedCity);
        } catch (DataIntegrityViolationException e){
            throw new CreationException("Unable to create a city due to a data integrity issue.");
        }
        catch (Exception e) {
            throw new InternalServerException("An error occurred.");
        }
    }

    /**
     * Récupère une ville par son identifiant.
     * @param id L'identifiant de la ville à récupérer.
     * @return Un Optional contenant l'objet CityDTO s'il est trouvé.
     * @throws ResourceNotFoundException Si la ville avec l'identifiant spécifié n'existe pas.
     */
    @Override
    public Optional<CityDTO> getById(Integer id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NO_CITY_FOUND));
        return Optional.of(CityMapper.mapToCityDto(city));
    }

    /**
     * Récupère une ville par son code INSEE.
     * @param inseeCode Le code INSEE de la ville à récupérer.
     * @return Un Optional contenant l'objet CityDTO s'il est trouvé.
     * @throws ResourceNotFoundException Si la ville avec le code INSEE spécifié n'existe pas.
     */
    @Override
    public Optional<CityDTO> getByInseeCodeAndPostCode(String inseeCode, String cityName) {
        City city = cityRepository.findByInseeCodeAndPostcode(inseeCode, cityName)
                .orElseThrow(() -> new ResourceNotFoundException(NO_CITY_FOUND));
        return Optional.of(CityMapper.mapToCityDto(city));
    }
}
