package com.bingofacil.bingofacil.services.project;

import com.bingofacil.bingofacil.dtos.project.ProjectDTO;
import com.bingofacil.bingofacil.model.project.Project;
import com.bingofacil.bingofacil.repositories.project.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public void saveProject(Project project){
        this.projectRepository.save(project);
    }

    public Project createProject(ProjectDTO project){
        Project newProject = new Project(project);
        this.saveProject(newProject);
        return newProject;
    }

    public Project editProject(Long id, Project project){
        Project editProject = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Projeto não encontrado"));
        this.saveProject(editProject);
        return editProject;
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

    //public List<Card> generateCard(int amount){}

    //public void startBingo(){}

}
