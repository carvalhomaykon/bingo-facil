package com.bingofacil.bingofacil.services.project;

import com.bingofacil.bingofacil.dtos.project.ProjectDTO;
import com.bingofacil.bingofacil.model.project.Project;
import com.bingofacil.bingofacil.model.user.User;
import com.bingofacil.bingofacil.repositories.project.ProjectRepository;
import com.bingofacil.bingofacil.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

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

    //public List<Card> generateCard(int amount){}

    //public void startBingo(){}

}
