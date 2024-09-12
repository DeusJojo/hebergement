package com.afpa.hebergement.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class DocumentCenterDTO {
    private Integer id;
    private String documentName;
    private String documentCode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dateDepoDoc;

    private String docCommentary;
    private AfpaCenterDTO idAfpaCenter;
    private DocumentTypeDTO idDocumentType;
}
