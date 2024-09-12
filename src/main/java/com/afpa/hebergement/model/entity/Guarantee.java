package com.afpa.hebergement.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

// Utilisation des annotations Lombok pour générer automatiquement les getters et setters
@Getter
@Setter
// Annotation JPA indiquant que cette classe est une entité
@Entity
// Définition de la table correspondante dans la base de données
@Table(name = "guarantee")
public class Guarantee {

    // Déclaration de l'ID de l'entité, généré automatiquement avec la stratégie d'identité (auto-incrément)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('guarantee_id_guarantee_seq')") // Définit la valeur par défaut pour la colonne
    @Column(name = "id_guarantee", nullable = false) // Mappe la colonne "id_guarantee" à cet attribut
    private Integer id;

    // Attribut montant, ne peut pas être null, avec précision et échelle définies pour les valeurs décimales
    @NotNull
    @Column(name = "amount", nullable = false, precision = 15, scale = 3) // Spécifie le format de la colonne
    private BigDecimal amount;

    // Relation ManyToOne avec l'entité AfpaCenter, chargée de manière paresseuse, ne peut pas être null
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_afpa_center", nullable = false) // Clé étrangère vers la table "afpa_center"
    private AfpaCenter idAfpaCenter;

    // Relation ManyToOne avec l'entité GuaranteeType, chargée de manière paresseuse, ne peut pas être null
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_guarantee_type", nullable = false) // Clé étrangère vers la table "guarantee_type"
    private GuaranteeType idGuaranteeType;

    // Relation OneToMany avec l'entité Deposit, définie avec une LinkedHashSet pour préserver l'ordre d'insertion
    @OneToMany(mappedBy = "idGuarantee") // Indique que la relation est mappée par l'attribut "idGuarantee" dans Deposit
    private Set<Deposit> deposits = new LinkedHashSet<>();

}
