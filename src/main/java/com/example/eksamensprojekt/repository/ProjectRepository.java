package com.example.eksamensprojekt.repository;

import com.example.eksamensprojekt.models.Project;
import com.example.eksamensprojekt.models.SubProject;
import com.example.eksamensprojekt.models.Tasks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class ProjectRepository {
    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String user_id;
    @Value("${spring.datasource.password}")
    String user_pwd;

public void createProject(Project project) {
    try(Connection con = DriverManager.getConnection(url,user_id,user_pwd)) {
        String SQL = "INSERT INTO Project(title, deadline, budget, description, user_id) VALUES(?, ?, ?, ?,?)";
        PreparedStatement pstmt = con.prepareStatement(SQL);
        pstmt.setString(1, project.getTitle());
        LocalDate deadline = project.getDeadline();
        java.sql.Date sqlDeadLine = java.sql.Date.valueOf(deadline);
        pstmt.setDate(2, sqlDeadLine);
        pstmt.setInt(3, project.getBudget());
        pstmt.setString(4, project.getDescription());
        pstmt.setInt(5, project.getUserId());

        pstmt.executeUpdate();

    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}
    public List<Project> getProjectList(int usersId){
        List<Project> projectList = new ArrayList<>();
        try(Connection con = DriverManager.getConnection(url,user_id,user_pwd)) {
            String SQL = "SELECT * FROM project WHERE user_id = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, usersId);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                int projectId = rs.getInt("id");
                String title = rs.getString("title");
                java.sql.Date sqlDeadLine = rs.getDate("deadline");
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = formatter.format(sqlDeadLine);
                LocalDate deadline = sqlDeadLine.toLocalDate();
                int budget = rs.getInt("budget");
                int id = rs.getInt("user_id");
                String description = rs.getString("description");

                projectList.add(new Project(projectId,title, deadline, budget, id, description));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return projectList;
    }

    public Project getSpecificProject(int projectID){
    Project project = null;

        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)){
            String SQL = "SELECT * FROM project WHERE id = ?";
            PreparedStatement pstm = con.prepareStatement(SQL);
            pstm.setInt(1, projectID);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()){
                String title = rs.getString("title");
                java.sql.Date sqlDeadLine = rs.getDate("deadline");
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = formatter.format(sqlDeadLine);
                LocalDate deadline = sqlDeadLine.toLocalDate();
                int budget = rs.getInt("budget");
                int id = rs.getInt("user_id");
                String description = rs.getString("description");

                project = new Project(projectID, title, deadline, budget, id, description);
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
        return project;
    }

    public void updateProject(Project project){
        try(Connection con = DriverManager.getConnection(url,user_id,user_pwd)) {
            String SQL = "UPDATE Project SET title=?, deadline=?, budget=?, description =? WHERE id = ?;";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setString(1,project.getTitle());
            LocalDate deadline = project.getDeadline();
            java.sql.Date sqlDeadline = java.sql.Date.valueOf(deadline);
            pstmt.setDate(2, sqlDeadline);
            pstmt.setInt(3, project.getBudget());
            pstmt.setString(4, project.getDescription());
            pstmt.setInt(5, project.getId());
            pstmt.executeUpdate();

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public List<Project> getAssignedProjects(int userId) {
        List<Project> assignedProjects = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT * FROM project JOIN project_user ON project_user.project_id = project.id WHERE project_user.user_id = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Project project = new Project();
                project.setId(rs.getInt("id"));
                project.setTitle(rs.getString("title"));
                project.setDeadline(rs.getDate("deadline").toLocalDate());
                // Set other project attributes as needed

                assignedProjects.add(project);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return assignedProjects;
    }
    public Project getSpecificAssignedProject(int projectID){
        Project assignedProject = null;

        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)){
            String SQL = "SELECT * FROM project JOIN project_user ON project_user.project_id = project.id WHERE project_user.user_id = ?";
            PreparedStatement pstm = con.prepareStatement(SQL);
            pstm.setInt(1, projectID);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()){
                String title = rs.getString("title");
                java.sql.Date sqlDeadLine = rs.getDate("deadline");
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = formatter.format(sqlDeadLine);
                LocalDate deadline = sqlDeadLine.toLocalDate();
                int budget = rs.getInt("budget");
                int id = rs.getInt("user_id");
                String description = rs.getString("description");

                assignedProject = new Project(projectID, title, deadline, budget, id, description);
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
        return assignedProject;
    }
    public void deleteProject(int projectId) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "DELETE FROM project WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, projectId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
