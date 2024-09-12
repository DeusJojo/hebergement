package com.afpa.hebergement.controller;




import com.afpa.hebergement.model.dto.PaymentTypeDTO;
import com.afpa.hebergement.service.entity_service.PaymentTypeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/paymentType")
@AllArgsConstructor
public class PaymentTypeController {
    private final PaymentTypeService paymentTypeService;

    @PostMapping("/create")
    public ResponseEntity<PaymentTypeDTO> createPaymentType(@RequestBody PaymentTypeDTO paymentTypeDto){
        PaymentTypeDTO savedPaymentType = paymentTypeService.create(paymentTypeDto);
        return new ResponseEntity<>(savedPaymentType, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<PaymentTypeDTO>> getAll() {
        List<PaymentTypeDTO> paymentTypeDtoList = paymentTypeService.getAll();
        return ResponseEntity.ok(paymentTypeDtoList);
    }
    @GetMapping("/{id}")
    public ResponseEntity<PaymentTypeDTO> getById(@PathVariable("id")Integer id){
        Optional<PaymentTypeDTO> paymentTypeDTO = paymentTypeService.getById(id);
        return paymentTypeDTO.map(paymentTypeDto -> new ResponseEntity<>(paymentTypeDto,HttpStatus.OK))
                .orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<PaymentTypeDTO> update(@PathVariable("id") Integer idPaymentType,
                                                   @RequestBody PaymentTypeDTO paymentTypeDto) {
        Optional<PaymentTypeDTO> updatedPaymentTypeDto = paymentTypeService.update(idPaymentType, paymentTypeDto);
        return updatedPaymentTypeDto.map(paymentType -> new ResponseEntity<>(paymentType,HttpStatus.OK))
                .orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable("id") Integer id) {
        paymentTypeService.deleteById(id);
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }

}
