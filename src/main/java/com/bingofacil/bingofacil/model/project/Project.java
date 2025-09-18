package com.bingofacil.bingofacil.model.project;

import com.bingofacil.bingofacil.dtos.ProjectDTO;
import com.bingofacil.bingofacil.model.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
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
    private String name;
    private Integer amountAwards;
    private LocalDateTime dateAndTime;
    private String description;
    @Column(name = "valor")
    private BigDecimal value;

    @Enumerated(EnumType.STRING)
    private StatusProject status;

    @ManyToOne
    @JoinColumn(name="organizer_id")
    private User organizer;

    public Project(ProjectDTO data){
        this.amountAwards = data.amountAwards();
        this.name = data.name();
        this.dateAndTime = data.dateAndTime();
        this.description = data.description();
        this.value = data.value();
        this.status = data.status();
    }
}
