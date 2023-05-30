package com.example.eksamensprojekt.repository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
public class SubProjectUserRepository {
    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String user_id;
    @Value("${spring.datasource.password}")
    String user_pwd;

    // Oprindelige funktion
    public void assignUsersToSubProject(int projectId, List<Integer> userIds) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "INSERT INTO subproject_user(sub_id, user_id) VALUES(?, ?)";
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
    public void assignUsersToaSubProject(int subProjectId, List<Integer> userIds) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String selectQuery = "SELECT COUNT(*) FROM subproject_user WHERE sub_id = ? AND user_id = ?";
            String insertQuery = "INSERT INTO subproject_user (sub_id, user_id) VALUES (?, ?)";

            for (int userId : userIds) {
                PreparedStatement selectStmt = con.prepareStatement(selectQuery);
                selectStmt.setInt(1, subProjectId);
                selectStmt.setInt(2, userId);
                ResultSet rs = selectStmt.executeQuery();
                rs.next();
                int count = rs.getInt(1);

                if (count == 0) {
                    PreparedStatement insertStmt = con.prepareStatement(insertQuery);
                    insertStmt.setInt(1, subProjectId);
                    insertStmt.setInt(2, userId);
                    insertStmt.executeUpdate();
                } else {
                    // Lav en boolean her og brug det i html eventuelt?
                    System.out.println("User with ID " + userId + " is already assigned to the sub-project.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isAssignedToSubproject(int userID, int subID){
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)){
            String SQL = "SELECT * FROM subproject_user WHERE user_id = ? AND sub_id = ?";
            PreparedStatement pstm = con.prepareStatement(SQL);
            pstm.setInt(1, userID);
            pstm.setInt(2, subID);
            ResultSet rs = pstm.executeQuery();

            return rs.next();

        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }


}

