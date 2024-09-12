package com.afpa.hebergement.model.mapper;

import com.afpa.hebergement.model.dto.PaymentTypeDTO;
import com.afpa.hebergement.model.entity.PaymentType;

public class PaymentTypeMapper {
    // Constructeur privé pour empêcher l'instanciation de cette classe
    private PaymentTypeMapper(){
    }
    // Méthode pour mapper une entité PaymentType vers un DTO PaymentTypeDTO
    public static PaymentTypeDTO mapToPaymentTypeDTO(PaymentType paymentType) {

        // Crée un objet PaymentTypeDTO
        PaymentTypeDTO paymentTypeDTO = new PaymentTypeDTO();

        // Affecte les champs de l'entité PaymentType au DTO PaymentTypeDTO
        paymentTypeDTO.setId(paymentType.getId());
        paymentTypeDTO.setWordingPaymentType(paymentType.getWordingPaymentType());

        // Retourne le DTO PaymentTypeDTO
        return paymentTypeDTO;
    }

    // Méthode pour mapper un DTO PaymentTypeDTO vers une entité PaymentType
    public static PaymentType mapToPaymentType(PaymentTypeDTO paymentTypeDto) {

        // Crée un objet PaymentType
        PaymentType paymentType = new PaymentType();

        // Affecte les champs de l'entité PaymentTypeDTO à l'entité PaymentType
        paymentType.setId(paymentTypeDto.getId());
        paymentType.setWordingPaymentType(paymentTypeDto.getWordingPaymentType());

        // Retourne l'entité PaymentType
        return paymentType;
    }


}
