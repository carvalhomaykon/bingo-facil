package com.bingofacil.bingofacil.model.award;

import com.bingofacil.bingofacil.dtos.AwardDTO;
import com.bingofacil.bingofacil.model.project.Project;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="awards")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class Award {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name="project_id")
    private Project project;

    @Enumerated(EnumType.STRING)
    private StyleAward styleAward;

    public Award(AwardDTO data){
        this.name = data.name();
        this.styleAward = data.styleAward();
    }

}
