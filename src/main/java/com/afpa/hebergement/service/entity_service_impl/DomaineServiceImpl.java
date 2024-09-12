package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.CreationException;
import com.afpa.hebergement.exception.DuplicateException;
import com.afpa.hebergement.exception.InternalServerException;
import com.afpa.hebergement.exception.NoContentException;
import com.afpa.hebergement.model.dto.DomaineDTO;
import com.afpa.hebergement.model.entity.Domaine;
import com.afpa.hebergement.model.mapper.DomaineMapper;
import com.afpa.hebergement.model.repository.DomaineRepository;
import com.afpa.hebergement.service.entity_service.DomaineService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DomaineServiceImpl implements DomaineService {
    DomaineRepository domaineRepository;

    @Override
    public DomaineDTO create(DomaineDTO entityDto) {
        Optional<Domaine> optionalDomaine = domaineRepository.findById(entityDto.getId());
        if (optionalDomaine.isPresent()) {
            throw new DuplicateException("Domaine Already Exists");
        }

        Domaine domaine = DomaineMapper.mapToDomaineEntity(entityDto);
        try {
            Domaine savedDomaine = domaineRepository.save(domaine);
            return DomaineMapper.mapToDomaineDTO(savedDomaine);
        }catch (DataIntegrityViolationException e){
            throw new CreationException("Error occurred while creating domaine");
        }
    }

    @Override
    public List<DomaineDTO> getAll() {
        List<Domaine> domaines = domaineRepository.findAll();
        if (domaines.isEmpty()) {
            throw new NoContentException("Domaine Not Found");
        }
        try {
            return domaines.stream().map(DomaineMapper::mapToDomaineDTO).collect(Collectors.toList());
        }catch (InternalServerException e){
            throw new InternalServerException("Error occurred while getting domaines");
        }
    }

    @Override
    public Optional<DomaineDTO> getById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public Optional<DomaineDTO> update(Integer integer, DomaineDTO entityDto) {
        return DomaineService.super.update(integer, entityDto);
    }

    @Override
    public void deleteById(Integer integer) {
        DomaineService.super.deleteById(integer);
    }
}
