package com.bingofacil.bingofacil.services.project;

import com.bingofacil.bingofacil.dtos.ProjectDTO;
import com.bingofacil.bingofacil.model.award.Award;
import com.bingofacil.bingofacil.model.project.Project;
import com.bingofacil.bingofacil.model.user.User;
import com.bingofacil.bingofacil.repositories.project.ProjectRepository;
import com.bingofacil.bingofacil.repositories.user.UserRepository;
import com.bingofacil.bingofacil.services.award.AwardService;
import com.bingofacil.bingofacil.services.card.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardService cardService;

    @Autowired
    private AwardService awardService;

    public Project createProject(ProjectDTO project, Principal principal){
        String emailLogado = principal.getName();

        User organizer = userRepository.findByEmail(emailLogado).orElseThrow(
                () -> new RuntimeException("Email não encontrado")
        );

        Project newProject = new Project(project);

        newProject.setOrganizer(organizer);

        return projectRepository.save(newProject);
    }

    public void removeProject(Long id){
        List<Award> awardsProject = awardService.findByProjectId(id);

        for (Award award : awardsProject) {
            awardService.removeAward(award.getId());
        }

        projectRepository.deleteById(id);
    }

    public Project findProjectById(Long id){
        return projectRepository.findById(id).orElse(null);
    }

    public List<Project> findAllProjects(Principal principal){
        String emailLogado = principal.getName();

        User user = userRepository.findByEmail(emailLogado).orElseThrow(
                () -> new RuntimeException("Email não encontrado.")
        );

        return projectRepository.findAllByOrganizerId(user.getId());
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

    //public void startBingo(){}

}
