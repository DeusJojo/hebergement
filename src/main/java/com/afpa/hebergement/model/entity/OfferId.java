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
public class OfferId implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 8574620134856712384L;

    @NotNull
    @Column(name = "id_service", nullable = false)
    private Integer idService;

    @NotNull
    @Column(name = "id_afpa_center", nullable = false)
    private Integer idAfpaCenter;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        OfferId entity = (OfferId) o;
        return Objects.equals(this.idService, entity.idService) &&
                Objects.equals(this.idAfpaCenter, entity.idAfpaCenter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idService, idAfpaCenter);
    }
}
