package com.example.eksamensprojekt.repository;

import com.example.eksamensprojekt.models.Project;
import com.example.eksamensprojekt.models.Tasks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TasksRepository {
    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String user_id;
    @Value("${spring.datasource.password}")
    String user_pwd;

    public void createTask(Tasks task) {
        try(Connection con = DriverManager.getConnection(url,user_id,user_pwd)) {
            String SQL = "INSERT INTO tasks(task_title, task_deadline, time_spent, sub_id) VALUES(?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setString(1, task.getTitle());
            LocalDate deadline = task.getDeadline();
            java.sql.Date sqlDeadLine = java.sql.Date.valueOf(deadline);
            pstmt.setDate(2, sqlDeadLine);
            pstmt.setDouble(3, task.getTimeSpent());
            pstmt.setInt(4, task.getSubId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Tasks> getTaskList(int sub_id){
        List<Tasks> taskList = new ArrayList<>();
        try(Connection con = DriverManager.getConnection(url,user_id,user_pwd)) {
            String SQL = "SELECT * FROM tasks WHERE sub_id = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, sub_id);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                int taskId = rs.getInt("task_id");
                String title = rs.getString("task_title");
                java.sql.Date sqlDeadLine = rs.getDate("task_deadline");
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = formatter.format(sqlDeadLine);
                LocalDate deadline = sqlDeadLine.toLocalDate();
                double timeSpent = rs.getInt("time_spent");
                int id = rs.getInt("sub_id");

                taskList.add(new Tasks(taskId,title, deadline, timeSpent, id));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return taskList;
    }
}
