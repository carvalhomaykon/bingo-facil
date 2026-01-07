package com.bingofacil.bingofacil.model.card;

import com.bingofacil.bingofacil.dtos.TemplateCardDTO;
import com.bingofacil.bingofacil.model.project.Project;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "template_card")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
public class TemplateCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="project_id")
    private Project project;

    private String backgroundImagePath;

    @Lob
    private String layoutConfig;

    public TemplateCard (TemplateCardDTO dto){
        this.backgroundImagePath = dto.backgroundImagePath();
        this.layoutConfig = dto.layoutConfig();
    }

}
