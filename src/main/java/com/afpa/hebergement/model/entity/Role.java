package com.afpa.hebergement.model.entity;

import com.afpa.hebergement.model.enum_role.RoleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('role_id_role_seq')")
    @Column(name = "id_role", nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "wording_role", nullable = false, length = 50, unique = true)
    private RoleType wordingRole;

    @OneToMany(mappedBy = "idRole")
    private Set<AppUser> appUsers = new LinkedHashSet<>();

}