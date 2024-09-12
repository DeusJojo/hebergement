package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.CreationException;
import com.afpa.hebergement.exception.DuplicateException;
import com.afpa.hebergement.exception.InternalServerException;
import com.afpa.hebergement.exception.ResourceNotFoundException;
import com.afpa.hebergement.model.dto.DepartmentDTO;
import com.afpa.hebergement.model.entity.Department;
import com.afpa.hebergement.model.mapper.DepartmentMapper;
import com.afpa.hebergement.model.repository.DepartmentRepository;
import com.afpa.hebergement.service.entity_service.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implémentation du service pour gérer les opérations liées aux départements.
 */
@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    private static final String NO_DEPARTMENT_FOUND = "Department does not exist";

    /**
     * Crée un nouveau département à partir du DTO fourni.
     *
     * @param departmentDto DTO représentant les données du département à créer
     * @return DTO représentant le département créé
     * @throws CreationException si le département fourni est invalide
     * @throws DuplicateException si un département avec le même code existe déjà
     * @throws InternalServerException si une erreur interne se produit lors de la création
     */
    @Override
    public DepartmentDTO create(DepartmentDTO departmentDto) {
        if(departmentDto == null) throw new CreationException("Invalid department provided");

        Optional<Department> existingDepartment = departmentRepository.findByDepartmentCode(departmentDto.getDepartmentCode());
        if(existingDepartment.isPresent()) throw new DuplicateException("Department already exists");

        try {
            Department department = DepartmentMapper.mapToDepartment(departmentDto);
            Department savedDepartment = departmentRepository.save(department);
            return DepartmentMapper.mapToDepartmentDto(savedDepartment);
        }catch (DataIntegrityViolationException e){
            throw new CreationException("Unable to create a department due to a data integrity issue.");
        }
        catch (Exception e){
            throw new InternalServerException("An error occurred.");
        }
    }

    /**
     * Récupère un département par son identifiant.
     *
     * @param id Identifiant du département à récupérer
     * @return DTO représentant le département trouvé
     * @throws ResourceNotFoundException si aucun département n'est trouvé avec l'identifiant spécifié
     */
    @Override
    public Optional<DepartmentDTO> getById(Integer id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(NO_DEPARTMENT_FOUND));
        return Optional.of(DepartmentMapper.mapToDepartmentDto(department));
    }

    /**
     * Récupère un département par son code.
     *
     * @param departmentCode Code du département à récupérer
     * @return DTO représentant le département trouvé
     * @throws ResourceNotFoundException si aucun département n'est trouvé avec le code spécifié
     */
    @Override
    public Optional<DepartmentDTO> getByDepartmentCode(String departmentCode) {
        Department department = departmentRepository.findByDepartmentCode(departmentCode)
                .orElseThrow(() ->
                        new ResourceNotFoundException(NO_DEPARTMENT_FOUND));
        return Optional.of(DepartmentMapper.mapToDepartmentDto(department));
    }
}
