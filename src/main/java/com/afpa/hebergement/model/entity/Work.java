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
@Table(name = "work")
public class Work {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('work_id_work_seq')")
    @Column(name = "id_work", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "start_work", nullable = false)
    private LocalDate startWork;

    @Column(name = "end_work")
    private LocalDate endWork;

    @Size(max = 2000)
    @Column(name = "work_commentary", length = 2000)
    private String workCommentary;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_work_type", nullable = false)
    private WorkType idWorkType;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_room", nullable = false)
    private Room idRoom;

}