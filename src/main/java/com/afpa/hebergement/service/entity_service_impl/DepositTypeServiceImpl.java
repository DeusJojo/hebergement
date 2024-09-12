package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.*;
import com.afpa.hebergement.model.dto.DepositTypeDTO;
import com.afpa.hebergement.model.entity.DepositType;
import com.afpa.hebergement.model.mapper.DepositTypeMapper;
import com.afpa.hebergement.model.repository.DepositTypeRepository;
import com.afpa.hebergement.service.entity_service.DepositTypeService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DepositTypeServiceImpl implements DepositTypeService {

    DepositTypeRepository repo;

    @Override
    public DepositTypeDTO create(DepositTypeDTO entityDto) {

        Optional<DepositType> depositType = repo.findByWordingDepositType(entityDto.getWordingDepositType());
        if (depositType.isPresent()) {
            throw new DuplicateException("User already have a deposit for these items");
        }

        DepositType depositTypeEntity = DepositTypeMapper.mapToEntity(entityDto);

        try {
            DepositType savedDeposit = repo.save(depositTypeEntity);
            return DepositTypeMapper.mapToDTO(savedDeposit);
        } catch (DataIntegrityViolationException e) {
            throw new CreationException("Error occured while creating deposit");
        }
    }

    @Override
    public List<DepositTypeDTO> getAll() {
        List<DepositType> deposits = repo.findAll();
        if (deposits.isEmpty()) {
            throw new NoContentException("No deposits found");
        }

        try {
            return deposits.stream().map(DepositTypeMapper::mapToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            throw new InternalServerException("Error occured while getting deposits");
        }
    }

    @Override
    public Optional<DepositTypeDTO> getById(Integer id) {
        DepositType findDepositType = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("DepositType not found at ID : " + id));
        try {
            DepositTypeDTO depositDTO = DepositTypeMapper.mapToDTO(findDepositType);
            return Optional.of(depositDTO);
        } catch (Exception e) {
            throw new InternalServerException("Error occured while getting deposit at ID : " + id);
        }
    }

    @Override
    public Optional<DepositTypeDTO> update(Integer id, DepositTypeDTO entityDto) {
        try {
            DepositType toUpdate = DepositTypeMapper.mapToEntity(entityDto);
            toUpdate.setId(id);
            DepositType saved = repo.save(toUpdate);

            return Optional.of(DepositTypeMapper.mapToDTO(saved));

        } catch (Exception e) {
            throw new InternalServerException("Error occured while updating deposit at id : " + id);
        }
    }

    @Override
    public void deleteById(Integer id) {
        DepositType depo = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("DepositType not found at id : " + id));
        try {
            repo.delete(depo);
        } catch (Exception e) {
            throw new InternalServerException("An unexpected error occured while deleting deposit with id : " + id);
        }
    }

}
