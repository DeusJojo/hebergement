package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.*;
import com.afpa.hebergement.model.dto.DepositDTO;
import com.afpa.hebergement.model.entity.Deposit;
import com.afpa.hebergement.model.mapper.DepositMapper;
import com.afpa.hebergement.model.repository.DepositRepository;
import com.afpa.hebergement.service.entity_service.DepositService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DepositServiceImpl implements DepositService {

    DepositRepository repo;


    @Override
    public DepositDTO create(DepositDTO entityDto) {

        Optional<Deposit> optionalDeposit = repo.findByIdUserAndIdGuarantee_idGuaranteeType(
                entityDto.getIdUser(),
                entityDto.getIdGuarantee().getIdGuaranteeType());

        if (optionalDeposit.isPresent() && optionalDeposit.map(Deposit::getBackDepoDate).isEmpty()) {
            throw new DuplicateException("User already have a deposit for these items");
        }

        Deposit deposit = DepositMapper.mapToEntity(entityDto);

        try {
            Deposit savedDeposit = repo.save(deposit);
            return DepositMapper.mapToDTO(savedDeposit);
        } catch (DataIntegrityViolationException e) {
            throw new CreationException("Error occured while creating deposit");
        }
    }

    @Override
    public List<DepositDTO> getAll() {
        List<Deposit> deposits = repo.findAll();
        if (deposits.isEmpty()) {
            throw new NoContentException("No deposits found");
        }

        try {
            return deposits.stream().map(DepositMapper::mapToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            throw new InternalServerException("Error occured while getting deposits");
        }
    }

    @Override
    public Optional<DepositDTO> getById(Integer id) {
        Deposit findDeposit = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Deposit not found at ID : " + id));
        try {
            DepositDTO depositDTO = DepositMapper.mapToDTO(findDeposit);
            return Optional.of(depositDTO);
        } catch (Exception e) {
            throw new InternalServerException("Error occured while getting deposit at ID : " + id);
        }
    }

    @Override
    public Optional<DepositDTO> update(Integer id, DepositDTO entityDto) {
        try {
            Deposit toUpdate = DepositMapper.mapToEntity(entityDto);
            toUpdate.setId(id);
            Deposit saved = repo.save(toUpdate);

            return Optional.of(DepositMapper.mapToDTO(saved));
        } catch (Exception e) {
            throw new InternalServerException("Error occured while updating deposit at id : " + id);
        }
    }

    @Override
    public void deleteById(Integer id) {
        Deposit depo = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Deposit not found at id : " + id));
        try {
            repo.delete(depo);
        } catch (Exception e) {
            throw new InternalServerException("An unexpected error occured while deleting deposit with id : " + id);
        }
    }

}
