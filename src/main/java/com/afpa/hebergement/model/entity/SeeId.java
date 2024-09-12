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
public class SeeId implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = -8880763299962907788L;

    @NotNull
    @Column(name = "id_user", nullable = false)
    private Integer idUser;

    @NotNull
    @Column(name = "id_alert", nullable = false)
    private Integer idAlert;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SeeId entity = (SeeId) o;
        return Objects.equals(this.idUser, entity.idUser) &&
                Objects.equals(this.idAlert, entity.idAlert);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, idAlert);
    }

}