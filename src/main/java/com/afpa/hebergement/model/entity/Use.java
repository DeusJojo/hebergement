package com.afpa.hebergement.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "use")
public class Use {
    @EmbeddedId
    private UseId id;

    @MapsId("idBill")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_bill", nullable = false)
    private Bill idBill;

    @MapsId("idPaymentType")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_payment_type", nullable = false)
    private PaymentType idPaymentType;

}