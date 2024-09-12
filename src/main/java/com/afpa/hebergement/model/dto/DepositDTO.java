package com.afpa.hebergement.model.dto;

import com.afpa.hebergement.model.entity.AppUser;
import com.afpa.hebergement.model.entity.DepositType;
import com.afpa.hebergement.model.entity.Guarantee;
import com.afpa.hebergement.model.entity.Room;
import lombok.Data;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class DepositDTO {

    private int id;
    private LocalDate depositeDate;
    private LocalDate backDepositeDate;
    private Guarantee idGuarantee;
    private AppUser idUser;
    private DepositType depositType;
    private Set<Room> rooms = new LinkedHashSet<>();

}
