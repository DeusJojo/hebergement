package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.CreationException;
import com.afpa.hebergement.exception.InternalServerException;
import com.afpa.hebergement.exception.NoContentException;
import com.afpa.hebergement.exception.ResourceNotFoundException;
import com.afpa.hebergement.model.dto.DepositDTO;
import com.afpa.hebergement.model.dto.FinancierDTO;
import com.afpa.hebergement.model.entity.Deposit;
import com.afpa.hebergement.model.entity.Financier;
import com.afpa.hebergement.model.mapper.DepositMapper;
import com.afpa.hebergement.model.mapper.FinancierMapper;
import com.afpa.hebergement.model.repository.FinancierRepository;
import com.afpa.hebergement.service.entity_service.FinancierService;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FinancierIServiceImpl implements FinancierService {

    FinancierRepository repo;

    @Override
    public FinancierDTO create(FinancierDTO entity) {

        if(entity==null){
            throw new CreationException("Invalid guarantee provided");
        }

        Optional<Financier> financierOptional = repo.findById(entity.getId());
        if (financierOptional.isPresent()) {

            Financier savedFinancier = repo.save(financierOptional.get());
            return FinancierMapper.mapToFinancierDTO(savedFinancier);
        }
        throw new NoContentException("Financier not found");
    }


    @Override
    public List<FinancierDTO> getAll() {
        List<Financier> financierList = repo.findAll();
        if(financierList.isEmpty()){throw new NoContentException("Financier not found");}

        try {
            return financierList.stream().map(FinancierMapper::mapToFinancierDTO).collect(Collectors.toList());
        }catch (Exception e){throw new InternalServerException("Error occured while getting financier list");
        }
    }


    @Override
    public Optional<FinancierDTO> getById(Integer id) {
        Financier findFinancier = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Deposit not found at ID : " + id));
        try {
            FinancierDTO financierDTO = FinancierMapper.mapToFinancierDTO(findFinancier);
            return Optional.of(financierDTO);
        } catch (Exception e) {
            throw new InternalServerException("Error occured while getting deposit at ID : " +id);
        }
    }

    @Override
    public Optional<FinancierDTO> update(Integer integer, FinancierDTO entityDto) {
        try {
            Financier toUpdate = FinancierMapper.mapToFinancierEntity(entityDto);
            toUpdate.setId(integer);
            Financier updatedFinancier = repo.save(toUpdate);

            return Optional.of(FinancierMapper.mapToFinancierDTO(updatedFinancier));
        }catch (Exception e){
            throw new InternalServerException("Error occured while updating deposit at ID : " +integer);
        }
    }

    @Override
    public void deleteById(Integer id) {

        Financier financier = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Financier Not Found"));
        try {
            repo.delete(financier);
        }catch (Exception e){
            throw new InternalServerException("An unexpected error occurred while deleting financier with id");
        }

    }
}
