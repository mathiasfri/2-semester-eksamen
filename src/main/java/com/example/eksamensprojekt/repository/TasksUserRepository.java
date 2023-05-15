package com.example.eksamensprojekt.repository;

import com.example.eksamensprojekt.models.Tasks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TasksUserRepository {
    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String user_id;
    @Value("${spring.datasource.password}")
    String user_pwd;
    public void assignUsersToTaska(int taskId, List<Integer> userIds) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "INSERT INTO tasks_user(task_id, user_id) VALUES(?, ?)";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            for (int userId : userIds) {
                pstmt.setInt(1, taskId);
                pstmt.setInt(2, userId);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void assignUsersToTask(int taskId, List<Integer> userIds) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String selectQuery = "SELECT COUNT(*) FROM tasks_user WHERE task_id = ? AND user_id = ?";
            String insertQuery = "INSERT INTO tasks_user (task_id, user_id) VALUES (?, ?)";

            for (int userId : userIds) {
                PreparedStatement selectStmt = con.prepareStatement(selectQuery);
                selectStmt.setInt(1, taskId);
                selectStmt.setInt(2, userId);
                ResultSet rs = selectStmt.executeQuery();
                rs.next();
                int count = rs.getInt(1);

                if (count == 0) {
                    PreparedStatement insertStmt = con.prepareStatement(insertQuery);
                    insertStmt.setInt(1, taskId);
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

}
