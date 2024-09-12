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
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('message_id_message_seq')")
    @Column(name = "id_message", nullable = false)
    private Integer id;

    @Size(max = 2000)
    @NotNull
    @Column(name = "content", nullable = false, length = 2000)
    private String content;

    @NotNull
    @Column(name = "message_date", nullable = false)
    private LocalDate messageDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_intendant", nullable = false)
    private Intendant idIntendant;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_user", nullable = false)
    private AppUser idUser;

}