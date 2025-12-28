package com.bingofacil.bingofacil.services;

import com.bingofacil.bingofacil.dtos.ProjectDTO;
import com.bingofacil.bingofacil.dtos.user.UserDTO;
import com.bingofacil.bingofacil.model.award.StyleAward;
import com.bingofacil.bingofacil.model.project.StatusProject;
import com.bingofacil.bingofacil.model.user.User;
import com.bingofacil.bingofacil.repositories.user.UserRepository;
import com.bingofacil.bingofacil.services.project.ProjectService;
import com.bingofacil.bingofacil.services.user.UserService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;

    @Autowired
    UserService userService;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Project registration successful")
    @WithMockUser(username = "maykon@gmail.com")
    void createProjectSuccess(){
        UserDTO dtoUser = new UserDTO("Maykon", "maykon@gmail.com", "maykon", "91999999999", null);
        User user = userService.createUser(dtoUser);

        ProjectDTO dtoProject = new ProjectDTO(
                "Bingo Beneficiente3",
                2,
                java.time.LocalDateTime.parse("2025-01-15T19:30:00"),
                "Bingo beneficiente para fulano",
                java.math.BigDecimal.valueOf(15.00),
                StatusProject.IN_PROGRESS,
                null
        );

        java.security.Principal mockPrincipal = () -> "maykon@gmail.com";

        var project = projectService.createProject(dtoProject, mockPrincipal);

        assertNotNull(project.getId());
        assertEquals("Bingo Beneficiente3", project.getName());
        assertEquals(user.getId(), project.getOrganizer().getId());

    }

}
