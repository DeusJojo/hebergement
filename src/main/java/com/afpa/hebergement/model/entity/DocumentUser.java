package com.afpa.hebergement.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "document_user")
public class DocumentUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('document_user_id_document_seq')")
    @Column(name = "id_document", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "document_title", nullable = false, length = 50)
    private String documentTitle;

    @Size(max = 8)
    @NotNull
    @Column(name = "document_code", nullable = false)
    private String documentCode;

    @NotNull
    @Column(name = "date_depo_doc", nullable = false)
    private LocalDate dateDepoDoc;

    @Size(max = 2000)
    @Column(name = "doc_commentary", length = 2000)
    private String docCommentary;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_user", nullable = false)
    private AppUser idUser;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_document_type", nullable = false)
    private DocumentType idDocumentType;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_afpa_center", nullable = false)
    private AfpaCenter idAfpaCenter;

}