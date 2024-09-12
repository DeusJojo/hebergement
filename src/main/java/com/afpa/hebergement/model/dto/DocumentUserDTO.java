package com.afpa.hebergement.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class DocumentUserDTO {
    private Integer id;
    private String documentTitle;
    private String documentCode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dateDepoDoc;

    private AppUserDTO idUser;
    private DocumentTypeDTO idDocumentType;
    private AfpaCenterDTO idAfpaCenter;
}
