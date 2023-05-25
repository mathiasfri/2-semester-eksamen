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
            String SQL = "INSERT INTO tasks(title, deadline, time_spent, sub_id) VALUES(?, ?, ?, ?)";
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
                int taskId = rs.getInt("id");
                String title = rs.getString("title");
                java.sql.Date sqlDeadLine = rs.getDate("deadline");
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
    public Tasks getSpecificTask(int taskId){
        Tasks task = null;

        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)){
            String SQL = "SELECT * FROM tasks WHERE id = ?";
            PreparedStatement pstm = con.prepareStatement(SQL);
            pstm.setInt(1, taskId);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()){
                String title = rs.getString("title");
                java.sql.Date sqlDeadLine = rs.getDate("deadline");
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = formatter.format(sqlDeadLine);
                LocalDate deadline = sqlDeadLine.toLocalDate();
                int timeSpent = rs.getInt("time_spent");
                int subId = rs.getInt("sub_id");

                task = new Tasks(taskId, title, deadline, timeSpent, subId);
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
        return task;
    }
    public void updateTask(Tasks task){
        try(Connection con = DriverManager.getConnection(url,user_id,user_pwd)) {
            String SQL = "UPDATE tasks SET title=?, deadline=?, time_spent=? WHERE id = ?;";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setString(1,task.getTitle());
            LocalDate deadline = task.getDeadline();
            java.sql.Date sqlDeadline = java.sql.Date.valueOf(deadline);
            pstmt.setDate(2, sqlDeadline);
            pstmt.setDouble(3, task.getTimeSpent());
            pstmt.setInt(4, task.getId());
            pstmt.executeUpdate();

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public List<Tasks> getAssignedTasks(int userId) {
        List<Tasks> assignedTasks = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT * FROM tasks JOIN tasks_user ON tasks_user.task_id = tasks.id WHERE tasks_user.user_id = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Tasks task = new Tasks();
                task.setId(rs.getInt("id"));
                task.setTitle(rs.getString("title"));
                task.setDeadline(rs.getDate("deadline").toLocalDate());
                task.setTimeSpent(rs.getDouble("time_spent"));
                // Set other project attributes as needed

                assignedTasks.add(task);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return assignedTasks;
    }
    public void deleteTask(int taskId) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "DELETE FROM tasks WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, taskId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
