package com.example.eksamensprojekt.repository;

import com.example.eksamensprojekt.models.Project;
import com.example.eksamensprojekt.models.SubProject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SubProjectRepository {
    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String user_id;
    @Value("${spring.datasource.password}")
    String user_pwd;

    public void createProject(SubProject subProject) {
        try(Connection con = DriverManager.getConnection(url,user_id,user_pwd)) {
            String SQL = "INSERT INTO subProject(sub_title, sub_deadline, project_id) VALUES(?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setString(1, subProject.getTitle());
            LocalDate deadline = subProject.getDeadline();
            java.sql.Date sqlDeadLine = java.sql.Date.valueOf(deadline);
            pstmt.setDate(2, sqlDeadLine);
            pstmt.setInt(3, subProject.getProjectId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<SubProject> getSubProjects(int project_id){
        List<SubProject> subProjects = new ArrayList<>();
        try(Connection con = DriverManager.getConnection(url,user_id,user_pwd)) {
            String SQL = "SELECT * FROM subProject WHERE project_id = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, project_id);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                int sub_id = rs.getInt("sub_id");
                String title = rs.getString("sub_title");
                java.sql.Date sqlDeadLine = rs.getDate("sub_deadline");
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = formatter.format(sqlDeadLine);
                LocalDate deadline = sqlDeadLine.toLocalDate();

                subProjects.add(new SubProject(sub_id,title, deadline, project_id));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return subProjects;
    }
    public SubProject getSpecificSubProject(int subProjectID){
        SubProject subProject = null;

        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)){
            String SQL = "SELECT * FROM subProject WHERE sub_id = ?";
            PreparedStatement pstm = con.prepareStatement(SQL);
            pstm.setInt(1, subProjectID);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()){
                String title = rs.getString("sub_title");
                java.sql.Date sqlDeadLine = rs.getDate("sub_deadline");
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = formatter.format(sqlDeadLine);
                LocalDate deadline = sqlDeadLine.toLocalDate();
                int projectID = rs.getInt("project_id");

                subProject = new SubProject(subProjectID, title, deadline, projectID);
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
        return subProject;
    }
    public void updateSubProject(SubProject subProject){
        try(Connection con = DriverManager.getConnection(url,user_id,user_pwd)) {
            String SQL = "UPDATE subProject SET sub_title=?, sub_deadline=? WHERE sub_id = ?;";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setString(1,subProject.getTitle());
            LocalDate deadline = subProject.getDeadline();
            java.sql.Date sqlDeadline = java.sql.Date.valueOf(deadline);
            pstmt.setDate(2, sqlDeadline);
            pstmt.setInt(3, subProject.getId());
            pstmt.executeUpdate();

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public List<SubProject> getAssignedSubProjects(int userId) {
        List<SubProject> assignedSubProjects = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT * FROM subproject JOIN subproject_user ON subproject_user.sub_id = subproject.sub_id WHERE subproject_user.user_id = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                SubProject subProject = new SubProject();
                subProject.setId(rs.getInt("sub_id"));
                subProject.setTitle(rs.getString("sub_title"));
                subProject.setDeadline(rs.getDate("sub_deadline").toLocalDate());
                // Set other project attributes as needed

                assignedSubProjects.add(subProject);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return assignedSubProjects;
    }
}
