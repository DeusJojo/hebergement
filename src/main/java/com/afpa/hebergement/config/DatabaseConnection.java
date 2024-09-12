package com.afpa.hebergement.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
@AllArgsConstructor
public class DatabaseConnection implements CommandLineRunner {

    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            if (connection != null) {
                System.out.println("Database is connected!! :) :) ");
            } else {
                System.out.println("Failed to make connection!! :( :(");
            }
        } catch (SQLException e) {
            System.out.println("Failed to make connection!: " + e.getMessage());
            e.printStackTrace();
        }
    }

}