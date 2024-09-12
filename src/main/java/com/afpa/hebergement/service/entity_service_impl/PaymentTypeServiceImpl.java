package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.*;
import com.afpa.hebergement.model.dto.PaymentTypeDTO;
import com.afpa.hebergement.model.entity.PaymentType;
import com.afpa.hebergement.model.mapper.PaymentTypeMapper;
import com.afpa.hebergement.model.repository.PaymentTypeRepository;
import com.afpa.hebergement.service.entity_service.PaymentTypeService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PaymentTypeServiceImpl implements PaymentTypeService {
    private final PaymentTypeRepository paymentTypeRepository;

    @Override
    public PaymentTypeDTO create(PaymentTypeDTO paymentTypeDto) {
        // Vérifier si le type de paiement existe déjà
        Optional<PaymentType> existingPaymentType = paymentTypeRepository.findByWordingPaymentType(paymentTypeDto.getWordingPaymentType());

        if (existingPaymentType.isPresent()) {
            // Lancer une exception si le type de paiement existe déjà
            throw new DuplicateException("Payment type already exists");
        }

        // Mappe le DTO en entité PaymentType
        PaymentType paymentType = PaymentTypeMapper.mapToPaymentType(paymentTypeDto);

        try {
            // Sauvegarde l'entité PaymentType dans le repository
            PaymentType savedPaymentType = paymentTypeRepository.save(paymentType);
            // Mappe l'entité PaymentType sauvegardée en DTO et la retourne
            return PaymentTypeMapper.mapToPaymentTypeDTO(savedPaymentType);

        } catch (DataIntegrityViolationException e) {
            // Capturer une exception de violation d'intégrité des données
            throw new CreationException("Error occurred while creating payment type");
        }
    }

    @Override
    public List<PaymentTypeDTO> getAll() {
        // Récupère toutes les entités PaymentType du repository
        List<PaymentType> paymentType = paymentTypeRepository.findAll();

        // Vérifie si la liste est vide
        if (paymentType.isEmpty()) {
            // Lance une ResourceNotFoundException si aucune entité n'est trouvée
            throw new NoContentException("No work types found");
        }

        try {
            // Mappe les entités PaymentType en DTO et retourne la liste
            return paymentType.stream()
                    .map(PaymentTypeMapper::mapToPaymentTypeDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while getting all payment types");
        }
    }

    @Override
    public Optional<PaymentTypeDTO> getById(Integer id) {
        // Cherche une entité PaymentType par son identifiant
        PaymentType findPaymentType = paymentTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment type not found with ID: " + id));

        try {
            // Convertit l'entité PaymentType en PaymentTypeDTO par le PaymentTypeMapper
            PaymentTypeDTO paymentTypeDTO = PaymentTypeMapper.mapToPaymentTypeDTO(findPaymentType);

            // Retourne le PaymentTypeDTO encapsulé dans un Optional
            return Optional.of(paymentTypeDTO);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while getting payment type by ID");
        }
    }

    @Override
    public Optional<PaymentTypeDTO> update(Integer id, PaymentTypeDTO paymentTypeDto) {
        // Cherche l'entité PaymentType à mettre à jour par son identifiant
        PaymentType paymentTypeToUpdate = paymentTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work type ID not found: " + id));

        try {
            // Met à jour le champ wordingPaymentType de l'entité PaymentType avec la valeur du DTO
            paymentTypeToUpdate.setWordingPaymentType(paymentTypeDto.getWordingPaymentType());

            // Sauvegarde l'entité PaymentType mise à jour dans le repository
            PaymentType updatedPaymentType = paymentTypeRepository.save(paymentTypeToUpdate);

            // Mappe l'entité PaymentType mise à jour en DTO et la retourne encapsulée dans un Optional
            return Optional.of(PaymentTypeMapper.mapToPaymentTypeDTO(updatedPaymentType));

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while updating Payment type");
        }
    }

    @Override
    public void deleteById(Integer id) {
         // Tente de trouver l'entité PaymentType par son identifiant
        PaymentType paymentType = paymentTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment type not found with id: " + id));
        try {
            // Supprime l'entité PaymentType trouvée
            paymentTypeRepository.delete(paymentType);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("An unexpected error occurred while deleting payment type with id: " + id);
        }
    }
}
