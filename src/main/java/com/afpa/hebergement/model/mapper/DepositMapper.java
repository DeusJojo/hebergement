package com.afpa.hebergement.model.mapper;

import com.afpa.hebergement.model.dto.DepositDTO;
import com.afpa.hebergement.model.entity.Deposit;


public class DepositMapper {

private DepositMapper() {}


    public static Deposit mapToEntity(DepositDTO depositDTO) {
        Deposit deposit = new Deposit();
        deposit.setId(depositDTO.getId());
        deposit.setDepositDate(depositDTO.getDepositeDate());
        deposit.setBackDepoDate(depositDTO.getBackDepositeDate());
        deposit.setIdGuarantee(depositDTO.getIdGuarantee());
        deposit.setIdUser(depositDTO.getIdUser());
        deposit.setIdDepositType(depositDTO.getDepositType());
        deposit.setRooms(depositDTO.getRooms());
        return deposit;
    }


    public static DepositDTO mapToDTO(Deposit deposit) {
    DepositDTO depositDTO = new DepositDTO();
    depositDTO.setId(deposit.getId());
    depositDTO.setDepositeDate(deposit.getDepositDate());
    depositDTO.setBackDepositeDate(deposit.getBackDepoDate());
    depositDTO.setIdGuarantee(deposit.getIdGuarantee());
    depositDTO.setIdUser(deposit.getIdUser());
    depositDTO.setDepositType(deposit.getIdDepositType());
    depositDTO.setRooms(deposit.getRooms());
    return depositDTO;
    }

}
