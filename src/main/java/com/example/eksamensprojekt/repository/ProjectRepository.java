package com.example.eksamensprojekt.repository;

import com.example.eksamensprojekt.models.Project;
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
        String SQL = "INSERT INTO Project(project_title, project_deadline, project_budget, project_description, user_id) VALUES(?, ?, ?, ?,?)";
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
                int projectId = rs.getInt("project_id");
                String title = rs.getString("project_title");
                java.sql.Date sqlDeadLine = rs.getDate("project_deadline");
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = formatter.format(sqlDeadLine);
                LocalDate deadline = sqlDeadLine.toLocalDate();
                int budget = rs.getInt("project_budget");
                int id = rs.getInt("user_id");
                String description = rs.getString("project_description");

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
            String SQL = "SELECT * FROM project WHERE project_id = ?";
            PreparedStatement pstm = con.prepareStatement(SQL);
            pstm.setInt(1, projectID);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()){
                String title = rs.getString("project_title");
                java.sql.Date sqlDeadLine = rs.getDate("project_deadline");
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = formatter.format(sqlDeadLine);
                LocalDate deadline = sqlDeadLine.toLocalDate();
                int budget = rs.getInt("project_budget");
                int id = rs.getInt("user_id");
                String description = rs.getString("project_description");

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
            String SQL = "UPDATE Project SET project_title=?, project_deadline=?, project_budget=?, project_description =? WHERE project_id = ?;";
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
}
