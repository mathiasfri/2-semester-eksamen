package com.example.eksamensprojekt.tests;

import com.example.eksamensprojekt.models.Project;
import com.example.eksamensprojekt.repository.ProjectRepository;
import com.example.eksamensprojekt.repository.SubProjectRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    private ProjectDBTest projectTestDB;
    private Project project1;
    private Project project2;
    private Project project3;
    private Project project4;

    @BeforeEach
    void setUp(){
        projectTestDB = new ProjectDBTest();
        projectTestDB.projectTestDB();

        project1 = new Project(1, "T-Project1", LocalDate.now(), 1, 1, "Description 1", 0.0);
        project2 = new Project(2, "T-Project2", LocalDate.now(), 2, 2, "Description 2", 0.0);
        project3 = new Project(3, "T-Project3", LocalDate.now(), 3, 2, "Description 3", 0.0);
        project4 = new Project(4, "T-Project4", LocalDate.now(), 4, 2, "Description 4", 0.0);
    }


    @Test
    void createProject(){
        projectRepository.createProject(project1);

        Project projectFound = projectRepository.getSpecificProject(project1.getId());

        assertEquals(project1.getTitle(), projectFound.getTitle());
    }
    @Test
    public void testGetProjectList() {
        // Get the project list for a specific user
        List<Project> projectList = projectRepository.getProjectList(1);

        // Perform assertions on the retrieved project list
        assertNotNull(projectList);
        // Add additional assertions as needed
    }

    @Test
    public void testGetSpecificProject() {
        // Create a new project
        Project project = new Project(1, "T-Project1", LocalDate.now(), 1, 1, "Description 1", 0.0);;
        projectRepository.createProject(project);

        // Retrieve the project by its ID
        Project retrievedProject = projectRepository.getSpecificProject(project.getId());

        // Verify that the retrieved project matches the created project
        assertNotNull(retrievedProject);
        assertEquals(project.getTitle(), retrievedProject.getTitle());
        assertEquals(project.getDescription(), retrievedProject.getDescription());
        assertEquals(project.getDeadline(), retrievedProject.getDeadline());
        assertEquals(project.getBudget(), retrievedProject.getBudget());
        assertEquals(project.getTimeSpent(), retrievedProject.getTimeSpent());
        assertEquals(project.getUserId(), retrievedProject.getUserId());
    }

    @Test
    public void testUpdateProject() {
        // Create a new project
        Project project = new Project(1, "T-Project1", LocalDate.now(), 1, 1, "Description 1", 0.0);;
        projectRepository.createProject(project);

        // Update the project
        project.setTitle("Updated Title");
        project.setDescription("Updated Description");
        project.setDeadline(LocalDate.now().plusDays(7));
        project.setBudget(2000);
        project.setTimeSpent(10);
        projectRepository.updateProject(project);

        // Retrieve the updated project
        Project updatedProject = projectRepository.getSpecificProject(project.getId());

        // Verify that the project was updated successfully
        assertNotNull(updatedProject);
        assertEquals(project.getTitle(), updatedProject.getTitle());
        assertEquals(project.getDescription(), updatedProject.getDescription());
        assertEquals(project.getDeadline(), updatedProject.getDeadline());
        assertEquals(project.getBudget(), updatedProject.getBudget());
        assertEquals(project.getTimeSpent(), updatedProject.getTimeSpent());
        assertEquals(project.getUserId(), updatedProject.getUserId());
    }
}