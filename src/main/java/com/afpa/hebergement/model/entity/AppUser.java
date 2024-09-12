package com.afpa.hebergement.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "app_user")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('app_user_id_user_seq')")
    @Column(name = "id_user", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "beneficiary_number", nullable = false, length = 50)
    private String beneficiaryNumber;

    @Size(max = 150)
    @NotNull
    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Size(max = 150)
    @NotNull
    @Column(name = "firstname", nullable = false, length = 150)
    private String firstname;

    @Size(max = 255)
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Size(min = 8, max = 150)
    @NotNull
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")
    @Column(name = "password", nullable = false, length = 150)
    private String password;

    @NotNull
    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;

    @Size(max = 5)
    @NotNull
    @Column(name = "birthplace", nullable = false, length = 5)
    private String birthplace;

    @Size(max = 20)
    @NotNull
    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @NotNull
    @Column(name = "is_blacklisted", nullable = false)
    private Boolean isBlacklisted = false;

    @NotNull
    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;

    @Size(max = 50)
    @Column(name = "ip_registration", length = 50)
    private String ipRegistration;

    @Size(max = 255)
    @Column(name = "tracker_registration")
    private String trackerRegistration;

    @Size(max = 255)
    @NotNull
    @Column(name = "address_user", nullable = false)
    private String addressUser;

    @Size(max = 255)
    @Column(name = "complement_user_address")
    private String complementUserAddress;

    @Size(max = 100)
    @Column(name = "reset_password_token", length = 100)
    private String resetPasswordToken;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_role", nullable = false)
    private Role idRole;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_civility", nullable = false)
    private Civility idCivility;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_city", nullable = false)
    private City idCity;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_afpa_center", nullable = false)
    private AfpaCenter idAfpaCenter;

    @OneToMany(mappedBy = "idUser")
    private Set<Appointment> appointments = new LinkedHashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_contact_person")
    private ContactPerson idContactPerson;

    @OneToMany(mappedBy = "idUser")
    private Set<Deposit> deposits = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idUser")
    private Set<DocumentUser> documentUsers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idUser")
    private Set<Intendant> intendants = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idUser")
    private Set<LeaseContract> leaseContracts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idUser")
    private Set<Message> messages = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idUser")
    private Set<Participate> participates = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(
            name = "see",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_alert")
    )
    private Set<Alert> alerts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idUser")
    private Set<Warning> warnings = new LinkedHashSet<>();

    public AppUser(
            String firstname,
            String name,
            LocalDate birthdate,
            String birthplace,
            String number,
            String email,
            String encode
    ) {
        this.firstname = firstname;
        this.name = name;
        this.birthdate = birthdate;
        this.birthplace = birthplace;
        this.beneficiaryNumber = number;
        this.email = email;
        this.password = encode;
    }

}
