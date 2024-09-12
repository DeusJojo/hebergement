package com.afpa.hebergement.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CityDTO {

    private Integer id;
    private String cityName;
    private String inseeCode;
    private String postcode;
    private DepartmentDTO idDepartment;
}
