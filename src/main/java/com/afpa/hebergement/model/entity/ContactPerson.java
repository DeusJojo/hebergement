package com.afpa.hebergement.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contact_person")
public class ContactPerson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('contact_person_id_contact_person_seq')")
    @Column(name = "id_contact_person", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 255)
    @NotNull
    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Size(max = 20)
    @NotNull
    @Column(name = "phone_number_contact", nullable = false, length = 20)
    // N'accepte que les caractères spéciaux et les chiffres
    @Pattern(regexp = "^\\+?[0-9 ()-]{7,25}$", message = "Invalid phone number format")
    private String phoneNumberContact;

    @OneToMany(mappedBy = "idContactPerson")
    private Set<AppUser> appUsers = new LinkedHashSet<>();
}