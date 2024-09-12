package com.afpa.hebergement.model.dto;

import com.afpa.hebergement.model.enum_role.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {

    private Integer id;
    private RoleType wordingRole;

}

