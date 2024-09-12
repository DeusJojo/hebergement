package com.afpa.hebergement.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class DepartmentDTO {

    private Integer id;
    private String departmentName;
    private String departmentCode;
    private RegionDTO idRegion;
}
