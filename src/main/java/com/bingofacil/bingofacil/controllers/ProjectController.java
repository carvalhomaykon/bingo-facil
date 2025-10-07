package com.bingofacil.bingofacil.controllers;

import com.bingofacil.bingofacil.dtos.CardDTO;
import com.bingofacil.bingofacil.dtos.ProjectDTO;
import com.bingofacil.bingofacil.model.card.Card;
import com.bingofacil.bingofacil.model.project.Project;
import com.bingofacil.bingofacil.model.user.User;
import com.bingofacil.bingofacil.services.card.CardService;
import com.bingofacil.bingofacil.services.project.ProjectService;
import com.bingofacil.bingofacil.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody ProjectDTO project, Principal principal){
        Project newProject = this.projectService.createProject(project, principal);
        return new ResponseEntity<>(newProject, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Project>> findAllProjects(Principal principal){
        List<Project> projects = this.projectService.findAllProjects(principal);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> findProjectById(@PathVariable Long id){
        Project project = this.projectService.findProjectById(id);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void removeProject(@PathVariable Long id){
        this.projectService.removeProject(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> editProject(@PathVariable Long id, @RequestBody ProjectDTO project){
        Project projectEdit = this.projectService.editProject(id, project);

        return new ResponseEntity<>(projectEdit, HttpStatus.OK);
    }

}
