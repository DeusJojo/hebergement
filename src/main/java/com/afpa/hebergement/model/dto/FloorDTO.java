package com.afpa.hebergement.model.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FloorDTO {

    private Integer id;
    private Integer numberFloor;
    private Boolean isForWomen;

    //Many to One
    private AfpaCenterDTO idAfpaCenter;
}
