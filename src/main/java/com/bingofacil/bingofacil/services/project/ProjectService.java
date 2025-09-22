package com.bingofacil.bingofacil.services.project;

import com.bingofacil.bingofacil.dtos.CardDTO;
import com.bingofacil.bingofacil.dtos.ProjectDTO;
import com.bingofacil.bingofacil.model.card.Card;
import com.bingofacil.bingofacil.model.project.Project;
import com.bingofacil.bingofacil.model.user.User;
import com.bingofacil.bingofacil.repositories.project.ProjectRepository;
import com.bingofacil.bingofacil.repositories.user.UserRepository;
import com.bingofacil.bingofacil.services.card.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardService cardService;

    public Project createProject(ProjectDTO project){
        Project newProject = new Project(project);

        User organizer = userRepository.findById(project.organizer())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        newProject.setOrganizer(organizer);

        return projectRepository.save(newProject);
    }

    public void removeProject(Long id){
        projectRepository.deleteById(id);
    }

    public Project findProjectById(Long id){
        return projectRepository.findById(id).orElse(null);
    }

    public List<Project> findAllProjects(){
        return projectRepository.findAll();
    }

    public Project editProject(Long id, ProjectDTO dto){
        Project project = this.projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));

        project.setName(dto.name());
        project.setAmountAwards(dto.amountAwards());
        project.setDateAndTime(dto.dateAndTime());
        project.setDescription(dto.description());
        project.setValue(dto.value());
        project.setStatus(dto.status());

        return this.projectRepository.save(project);

    }

    public List<Card> generateCard(int amount, CardDTO cardRequest){
        List<Card> cards = new ArrayList<>();

        for (int i = 0; i < amount; i++){
            Card card = cardService.createCard(cardRequest);
            cards.add(card);
        }

        return cards;
    }

    //public void startBingo(){}

}
