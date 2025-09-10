package com.bingofacil.bingofacil.repositories.project;

import com.bingofacil.bingofacil.model.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
