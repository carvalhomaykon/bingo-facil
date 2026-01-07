package com.bingofacil.bingofacil.services.card;

import com.bingofacil.bingofacil.dtos.TemplateCardDTO;
import com.bingofacil.bingofacil.model.card.TemplateCard;
import com.bingofacil.bingofacil.model.project.Project;
import com.bingofacil.bingofacil.repositories.card.TemplateCardRepository;
import com.bingofacil.bingofacil.repositories.project.ProjectRepository;
import com.bingofacil.bingofacil.services.project.ProjectService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TemplateCardService {

    @Autowired
    private TemplateCardRepository templateCardRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public TemplateCard findTemplateCardById(Long id){
        return templateCardRepository.findById(id).orElse(null);
    }

    public TemplateCard findTemplateCardByProjecId(Long projectId){
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new RuntimeException("Project não encontrado.")
        );

        return templateCardRepository.findByProjectId(project.getId());
    }

    public TemplateCard saveTemplateCard(TemplateCardDTO dto){
        Project project = projectRepository.findById(dto.project()).orElseThrow(
                () -> new EntityNotFoundException("Projeto com ID " + dto.project() + " não encontrado.")
        );

        TemplateCard newTemplateCard = new TemplateCard(dto);
        newTemplateCard.setProject(project);

        return templateCardRepository.save(newTemplateCard);
    }

    public TemplateCard editTemplateCard(Long id, TemplateCardDTO dto){
        TemplateCard templateCardEdit = templateCardRepository.findById(id).orElseThrow(
                () -> new RuntimeException("TemplateCard não encontrado")
        );

        templateCardEdit.setLayoutConfig(dto.layoutConfig());
        templateCardEdit.setBackgroundImagePath(dto.backgroundImagePath());

        return templateCardRepository.save(templateCardEdit);
    }

    public void deleteTemplateCard(Long id){
        TemplateCard templateCard = templateCardRepository.findById(id).orElseThrow(
                () -> new RuntimeException("TemplateCard não encontrado")
        );

        templateCardRepository.deleteById(id);
    }

}
