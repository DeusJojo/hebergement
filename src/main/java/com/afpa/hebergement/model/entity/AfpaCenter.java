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

// Utilisation des annotations Lombok pour générer automatiquement les getters, setters,
// et les constructeurs par défaut et avec tous les arguments
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
// Spécifie que cette classe correspond à la table "afpa_center" dans la base de données
@Table(name = "afpa_center")
public class AfpaCenter {

    // Identifiant unique de l'entité, généré automatiquement avec la stratégie d'identité
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('afpa_center_id_afpa_center_seq')") // Valeur par défaut définie pour la colonne
    @Column(name = "id_afpa_center", nullable = false) // Mappe la colonne "id_afpa_center" à cet attribut
    private Integer id;

    // Nom du centre, ne peut pas être null, avec une taille maximale de 255 caractères
    @Size(max = 255)
    @NotNull
    @Column(name = "center_name", nullable = false)
    private String centerName;

    // Adresse du centre, ne peut pas être null, avec une taille maximale de 255 caractères
    @Size(max = 255)
    @NotNull
    @Column(name = "address_center", nullable = false)
    private String addressCenter;

    // Complément d'adresse du centre, avec une taille maximale de 255 caractères
    @Size(max = 255)
    @Column(name = "complement_addr_center")
    private String complementAddrCenter;

    // Numéro de SIREN, doit contenir exactement 9 chiffres et ne peut pas être null
    @Size(max = 25)
    @NotNull
    @Column(name = "siren", nullable = false, length = 25)
    @Pattern(regexp = "^\\d{9}$", message = "Numéro de SIREN invalide.") // Validation avec une expression régulière
    private String siren;

    // Numéro de TVA français, doit suivre le format "FRxx999999999" et ne peut pas être null
    @Size(max = 13)
    @NotNull
    @Column(name = "tva_number", nullable = false, length = 13)
    @Pattern(regexp = "^FR\\d{2}\\d{9}$", message = "Invalid French VAT number format") // Validation du format
    private String tvaNumber;

    // Numéro de téléphone du centre, ne peut pas être null et accepte les chiffres et certains caractères spéciaux
    @Size(max = 20)
    @NotNull
    @Column(name = "center_phone_num", nullable = false, length = 20)
    private String centerPhoneNum;

    // Numéro de fax du centre, avec une taille maximale de 40 caractères
    @Size(max = 40)
    @Column(name = "fax_center", length = 40)
    private String faxCenter;

    // Nom du responsable du centre, ne peut pas être null et a une taille maximale de 255 caractères
    @Size(max = 255)
    @NotNull
    @Column(name = "center_manager", nullable = false)
    private String centerManager;

    // Code du centre, ne peut pas être null et a une taille maximale de 6 caractères
    @Size(max = 6)
    @NotNull
    @Column(name = "code_center", nullable = false, length = 6)
    private String codeCenter;

    // Relation ManyToOne avec l'entité City, chargée de manière paresseuse, ne peut pas être null
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_city", nullable = false) // Clé étrangère vers la table "city"
    private City idCity;

    // Relations OneToMany avec diverses entités liées à AfpaCenter
    @OneToMany(mappedBy = "idAfpaCenter") // Relie les alertes au centre AFPA
    private Set<Alert> alerts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idAfpaCenter") // Relie les utilisateurs de l'application au centre AFPA
    private Set<AppUser> appUsers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idAfpaCenter") // Relie les documents du centre au centre AFPA
    private Set<DocumentCenter> documentCenters = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idAfpaCenter") // Relie les documents des utilisateurs au centre AFPA
    private Set<DocumentUser> documentUsers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idAfpaCenter") // Relie les garanties au centre AFPA
    private Set<Guarantee> guarantees = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idAfpaCenter") // Relie les locations au centre AFPA
    private Set<Rent> rents = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idAfpaCenter") // Relie les étages au centre AFPA
    private Set<Floor> floors = new LinkedHashSet<>();

    // Relation ManyToMany avec les services de l'application, liés via l'entité AppService
    @ManyToMany(mappedBy = "afpaCenters")
    private Set<AppService> appServices = new LinkedHashSet<>();

}
