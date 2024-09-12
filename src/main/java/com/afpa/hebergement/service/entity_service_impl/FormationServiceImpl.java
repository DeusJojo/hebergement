package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.CreationException;
import com.afpa.hebergement.exception.DuplicateException;
import com.afpa.hebergement.exception.InternalServerException;
import com.afpa.hebergement.exception.NoContentException;
import com.afpa.hebergement.model.dto.FormationDTO;
import com.afpa.hebergement.model.entity.Formation;
import com.afpa.hebergement.model.mapper.FormationMapper;
import com.afpa.hebergement.model.repository.FormationRepository;
import com.afpa.hebergement.service.entity_service.FormationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FormationServiceImpl implements FormationService {

    FormationRepository formationRepository;

    @Override
    public FormationDTO create(FormationDTO formationDTO) {
        Optional<Formation> optionalFormation = formationRepository.findById(formationDTO.getId());
        if (optionalFormation.isPresent()) {
            throw new DuplicateException("Domaine already exists");
        }

        Formation forma = FormationMapper.mapToFormationEntity(formationDTO);
        try {
            return FormationMapper.mapToFormationDTO(formationRepository.save(forma));
        }catch (Exception e) {
            throw new CreationException("Error occured while creating formation");
        }
    }

    @Override
    public List<FormationDTO> getAll() {
        List<FormationDTO> formations = formationRepository.findAll().stream().map(FormationMapper::mapToFormationDTO).toList();
        if (formations.isEmpty()){
            throw new NoContentException("No formations found");
        }
        try {
            return formations;
        }catch (Exception e) {
            throw new InternalServerException("Error occurred while getting formations");
        }

    }

    @Override
    public Optional<FormationDTO> getById(Integer id) {
        Formation formation = formationRepository.findById(id).orElseThrow(()-> new NoContentException("No formation found at id : " + id));
        try {
            FormationDTO formationDTO = FormationMapper.mapToFormationDTO(formation);
            return Optional.of(formationDTO);
        }catch (Exception e) {
            throw new InternalServerException("Error occurred while getting formation at id : " + id);
        }
    }

    @Override
    public Optional<FormationDTO> update(Integer id, FormationDTO entityDto) {
        try {
            Formation formation = FormationMapper.mapToFormationEntity(entityDto);
            formation.setId(id);
            formationRepository.save(formation);
            return Optional.of(FormationMapper.mapToFormationDTO(formation));
        }catch (Exception e) {
            throw new InternalServerException("Error occurred while updating formation at id : " + id);
        }
    }

    @Override
    public void deleteById(Integer id){
        Formation depo = formationRepository.findById(id).orElseThrow(() -> new NoContentException("No formation found at id : " + id));
        try {
            formationRepository.delete(depo);
        }catch (Exception e) {
            throw new InternalServerException("Error occurred while deleting formation at id : " + id);
        }
    }


}
