package com.afpa.hebergement.model.mapper;

import com.afpa.hebergement.exception.NoContentException;
import com.afpa.hebergement.model.dto.SessionFormationDTO;
import com.afpa.hebergement.model.entity.SessionFormation;
import com.afpa.hebergement.model.repository.AppUserRepository;
import com.afpa.hebergement.model.repository.ParticipateRepository;


import java.util.stream.Collectors;

public class SessionFormationMapper {

    private SessionFormationMapper() {}

    static AppUserRepository appUserRepository;
    static ParticipateRepository participateRepository;

    public static SessionFormation mapToSessionFormationEntity(SessionFormationDTO sessionFormationDTO) {
        SessionFormation sessionFormation = new SessionFormation();
        sessionFormation.setId(sessionFormationDTO.getId());
        sessionFormation.setStartDateSession(sessionFormationDTO.getStartDateSession());
        sessionFormation.setEndDateSession(sessionFormationDTO.getEndDateSession());
        sessionFormation.setOfferNumber(sessionFormationDTO.getOfferNumber());
        sessionFormation.setIdFormation(FormationMapper.mapToFormationEntity(sessionFormationDTO.getFormation()));
        sessionFormation.setParticipates(participateRepository.findAllByIdSession(sessionFormation));
        return sessionFormation;
    }

    public static SessionFormationDTO mapToSessionFormationDTO(SessionFormation sessionFormation) {
        SessionFormationDTO sessionFormationDTO = new SessionFormationDTO();
        sessionFormationDTO.setId(sessionFormation.getId());
        sessionFormationDTO.setStartDateSession(sessionFormation.getStartDateSession());
        sessionFormationDTO.setEndDateSession(sessionFormation.getEndDateSession());
        sessionFormationDTO.setOfferNumber(sessionFormation.getOfferNumber());
        sessionFormationDTO.setFormation(sessionFormationDTO.getFormation());
        sessionFormationDTO.setUsers(appUserRepository.findAllByParticipates_IdSession(sessionFormation).orElseThrow(() -> new NoContentException("No users found for this session")).stream().map(AppUserMapper::mapToAppUserDTO).collect(Collectors.toSet()));
        return sessionFormationDTO;
    }

}
