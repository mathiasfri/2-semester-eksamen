package com.example.eksamensprojekt.repository;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProjectUserRepository {
    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String user_id;
    @Value("${spring.datasource.password}")
    String user_pwd;


    public void assignUsersToProject(int projectId, List<Integer> userIds) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "INSERT INTO Project_User(project_id, user_id) VALUES(?, ?)";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            for (int userId : userIds) {
                pstmt.setInt(1, projectId);
                pstmt.setInt(2, userId);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getUserIdByEmail(String email) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT user_id FROM users WHERE email = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    public List<Integer> checkIfAssignedToProject(int projectID) {
        List<Integer> listOfAssignedUsers = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT * FROM project_user WHERE project_id = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, projectID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int userID = rs.getInt("user_id");

                listOfAssignedUsers.add(userID);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listOfAssignedUsers;
    }
}

