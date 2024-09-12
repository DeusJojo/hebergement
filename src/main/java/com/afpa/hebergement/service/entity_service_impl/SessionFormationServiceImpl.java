package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.CreationException;
import com.afpa.hebergement.exception.DuplicateException;
import com.afpa.hebergement.exception.InternalServerException;
import com.afpa.hebergement.exception.NoContentException;
import com.afpa.hebergement.model.dto.SessionFormationDTO;
import com.afpa.hebergement.model.entity.SessionFormation;
import com.afpa.hebergement.model.mapper.SessionFormationMapper;
import com.afpa.hebergement.model.repository.SessionFormationRepository;
import com.afpa.hebergement.service.entity_service.SessionFormationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SessionFormationServiceImpl implements SessionFormationService {

    SessionFormationRepository sessionFormationRepository;

    @Override
    public SessionFormationDTO create(SessionFormationDTO sessionFormationDTO) {
        Optional<SessionFormation> optionalSessionFormation = sessionFormationRepository.findById(sessionFormationDTO.getId());
        if (optionalSessionFormation.isPresent()) {
            throw new DuplicateException("Domaine already exists");
        }

        SessionFormation forma = SessionFormationMapper.mapToSessionFormationEntity(sessionFormationDTO);
        try {
            return SessionFormationMapper.mapToSessionFormationDTO(sessionFormationRepository.save(forma));
        }catch (Exception e) {
            throw new CreationException("Error occured while creating sessionFormation");
        }
    }
    

    @Override
    public List<SessionFormationDTO> getAll() {
        List<SessionFormationDTO> sessionFormations = sessionFormationRepository.findAll().stream().map(SessionFormationMapper::mapToSessionFormationDTO).toList();
        if (sessionFormations.isEmpty()){
            throw new NoContentException("No sessionFormations found");
        }
        try {
            return sessionFormations;
        }catch (Exception e) {
            throw new InternalServerException("Error occurred while getting sessionFormations");
        }

    }

    @Override
    public Optional<SessionFormationDTO> getById(Integer id) {
        SessionFormation sessionFormation = sessionFormationRepository.findById(id).orElseThrow(()-> new NoContentException("No sessionFormation found at id : " + id));
        try {
            SessionFormationDTO sessionFormationDTO = SessionFormationMapper.mapToSessionFormationDTO(sessionFormation);
            return Optional.of(sessionFormationDTO);
        }catch (Exception e) {
            throw new InternalServerException("Error occurred while getting sessionFormation at id : " + id);
        }
    }

    @Override
    public Optional<SessionFormationDTO> update(Integer id, SessionFormationDTO entityDto) {
        try {
            SessionFormation sessionFormation = SessionFormationMapper.mapToSessionFormationEntity(entityDto);
            sessionFormation.setId(id);
            sessionFormationRepository.save(sessionFormation);
            return Optional.of(SessionFormationMapper.mapToSessionFormationDTO(sessionFormation));
        }catch (Exception e) {
            throw new InternalServerException("Error occurred while updating sessionFormation at id : " + id);
        }
    }

    @Override
    public void deleteById(Integer id){
        SessionFormation depo = sessionFormationRepository.findById(id).orElseThrow(() -> new NoContentException("No sessionFormation found at id : " + id));
        try {
            sessionFormationRepository.delete(depo);
        }catch (Exception e) {
            throw new InternalServerException("Error occurred while deleting sessionFormation at id : " + id);
        }
    }


}
