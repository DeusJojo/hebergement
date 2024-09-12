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
public class ParticipateId implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = -6075761416457515116L;
    @NotNull
    @Column(name = "id_session", nullable = false)
    private Integer idSession;

    @NotNull
    @Column(name = "id_user", nullable = false)
    private Integer idUser;

    @NotNull
    @Column(name = "id_financier", nullable = false)
    private Integer idFinancier;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ParticipateId entity = (ParticipateId) o;
        return Objects.equals(this.idUser, entity.idUser) &&
                Objects.equals(this.idSession, entity.idSession) &&
                Objects.equals(this.idFinancier, entity.idFinancier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, idSession, idFinancier);
    }

}