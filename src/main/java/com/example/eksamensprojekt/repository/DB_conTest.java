package com.example.eksamensprojekt.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class DB_conTest {
    private static String URL;
    private static String USER;
    private static String PASS;

    private static Connection connection;

    @Value("${spring.datasource.url}")
    public void setUrl(String url) {
        URL = url;
    }

    @Value("${spring.datasource.username}")
    public void setUser(String user) {
        USER = user;
    }

    @Value("${spring.datasource.password}")
    public void setPass(String pass) {
        PASS = pass;
    }

    public static Connection getConnection() {
        try {
            if (connection == null) connection = DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
