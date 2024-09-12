package com.afpa.hebergement.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDTO {

    private Integer id;
    private Time startMorning;
    private Time endMorning;
    private Time startAfternoon;
    private Time endAfternoon;

    //relation Many to One
    private IntendantDTO idIntendant;
    private DayDTO idDay;

}
