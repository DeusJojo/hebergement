package com.afpa.hebergement.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "document_type")
public class DocumentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('document_type_id_document_type_seq')")
    @Column(name = "id_document_type", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "wording_document_type", nullable = false, length = 100)
    private String wordingDocumentType;

    @OneToMany(mappedBy = "idDocumentType")
    private Set<DocumentCenter> documentCenters = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idDocumentType")
    private Set<DocumentUser> documentUsers = new LinkedHashSet<>();

}