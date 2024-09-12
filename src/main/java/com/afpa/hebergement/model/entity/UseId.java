package com.afpa.hebergement.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class UseId implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 3745812598553872584L;
    @NotNull
    @Column(name = "id_bill", nullable = false)
    private Integer idBill;

    @NotNull
    @Column(name = "id_payment_type", nullable = false)
    private Integer idPaymentType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UseId entity = (UseId) o;
        return Objects.equals(this.idPaymentType, entity.idPaymentType) &&
                Objects.equals(this.idBill, entity.idBill);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPaymentType, idBill);
    }

}