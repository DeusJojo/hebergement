package com.afpa.hebergement.model.payload.response;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Integer id;
    private String number;
    private String email;
    private Integer centerId;
    private List<String> roles;

    public JwtResponse(String accessToken, Integer id, String number, String email,Integer centerId, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.number = number;
        this.email = email;
        this.roles = roles;
        this.centerId = centerId;
    }

}