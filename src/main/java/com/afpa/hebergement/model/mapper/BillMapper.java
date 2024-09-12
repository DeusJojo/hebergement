package com.afpa.hebergement.model.mapper;

import com.afpa.hebergement.model.dto.BillDTO;
import com.afpa.hebergement.model.dto.BillFormDTO;
import com.afpa.hebergement.model.entity.Bill;
import com.afpa.hebergement.model.entity.LeaseContract;
import com.afpa.hebergement.model.entity.PaymentType;

import java.util.stream.Collectors;

public class BillMapper {

    // Constructeur privé pour empêcher l'instanciation de cette classe
    private BillMapper() {
    }
    //Méthode pour mapper une entité Bill vers un DTO BillDTO.
    public static BillDTO mapToBillDTO(Bill bill){
        // Crée un objet BillDTO
        BillDTO billDTO = new BillDTO();
        // Affecte les champs de l'entité Bill au DTO BillDTO
        billDTO.setId(bill.getId());
        billDTO.setBillNumber(bill.getBillNumber());
        billDTO.setTotalAmount(bill.getTotalAmount());
        billDTO.setBillDate(bill.getBillDate());
        billDTO.setIsPayed(bill.getIsPayed());
        billDTO.setPaymentDateBill(bill.getPaymentDateBill());

        // Mappage de Many to One
        //billDTO.setIdLease(LeaseContractMapper.mapToLeaseContractDTO(bill.getIdLease()));

        //Mappage du many to many
        if (bill.getPaymentTypes()!=null && !bill.getPaymentTypes().isEmpty()){
            billDTO.setPaymentTypes(bill.getPaymentTypes().stream()
                    .map(PaymentTypeMapper::mapToPaymentTypeDTO)
                    .collect(Collectors.toSet()));
        }
        // Retourne le DTO BillDTO
        return billDTO;
    }

    //Méthode pour mapper un DTO BillDTO vers une entité Bill.
    public static Bill mapToBill(BillDTO billDTO){
        //Creé un objet Bill
        Bill bill = new Bill();

        // Affecte les champs du DTO BillDTO à l'entité Bill
        bill.setId(billDTO.getId());
        bill.setBillNumber(billDTO.getBillNumber());
        bill.setTotalAmount(billDTO.getTotalAmount());
        bill.setBillDate(billDTO.getBillDate());
        bill.setIsPayed(billDTO.getIsPayed());
        bill.setPaymentDateBill(billDTO.getPaymentDateBill());
        // Mappage de Many to One
        //bill.setIdLease(LeaseContractMapper.mapToLeaseContract(billDTO.getIdLease()));

        // Mappage de Many to Many
        if (billDTO.getPaymentTypes()!= null && !billDTO.getPaymentTypes().isEmpty()){
            bill.setPaymentTypes(billDTO.getPaymentTypes().stream().map(PaymentTypeMapper::mapToPaymentType)
                    .collect(Collectors.toSet()));
        }
        // Retourne l'entité Bill
        return bill;

    }

    public static Bill mapBillFormDtoToBill(BillFormDTO formDTO, LeaseContract leaseContract, PaymentType paymentType){
        Bill bill = new Bill();

        bill.setBillNumber(formDTO.getBillNumber());
        bill.setTotalAmount(formDTO.getTotalAmount());
        bill.setBillDate(formDTO.getBillDate());
        bill.setIsPayed(true);
        bill.setPaymentDateBill(formDTO.getPaymentDateBill());

        bill.setIdLease(leaseContract);


        if (formDTO.getPaymentTypes() != null && !formDTO.getPaymentTypes().isEmpty()) {
            bill.setPaymentTypes(formDTO.getPaymentTypes().stream()
                    .map(PaymentTypeMapper::mapToPaymentType)
                    .collect(Collectors.toSet()));
        }

        return bill;

    }
}
