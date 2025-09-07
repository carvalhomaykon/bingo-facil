package com.bingofacil.bingofacil.model.project;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name="projects")
@Table(name="projects")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer amountAwards;
    private LocalDateTime dateAndTime;
    private String descrition;
    private BigDecimal value;

    private StatusProject status;

}
