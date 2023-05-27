package com.example.eksamensprojekt.tests;
import com.example.eksamensprojekt.repository.DB_conTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
@ActiveProfiles("test")
public class ProjectDBTest {
    public void projectTestDB(){
        try {
            Connection connection = DB_conTest.getConnection();

            Statement statement = connection.createStatement();

            connection.setAutoCommit(false);

            statement.addBatch("SET foreign_key_checks = 0;");
            statement.addBatch("DROP TABLE IF EXISTS project;");

            statement.addBatch("CREATE TABLE project\n" +
                    "(\n" +
                    "    id    INTEGER NOT NULL AUTO_INCREMENT,\n" +
                    "    title VARCHAR(50),\n" +
                    "    description VARCHAR(999),\n" +
                    "    deadline DATE,\n" +
                    "    budget DOUBLE,\n" +
                    "    time_spent DOUBLE,\n" +
                    "    user_id INTEGER,\n" +
                    "    PRIMARY KEY (id),\n" +
                    "    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE\n" +
                    ");");

            statement.executeBatch();
            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

