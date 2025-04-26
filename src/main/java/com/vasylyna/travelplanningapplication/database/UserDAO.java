package com.vasylyna.travelplanningapplication.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public boolean registerUser(String username, String email, String password) {

        if(isUserExists(username, email)) {
            return false;
        }

        String query = "INSERT INTO users (username, email, password_hash) VALUES(?,?,?)";

        try (
                Connection databaseConnection = DatabaseManager.getInstance().getConnection();
                PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private boolean isUserExists(String username, String email) {
        String query = "SELECT * FROM users WHERE username = ? OR email = ?";

        try (
                Connection databaseConnection = DatabaseManager.getInstance().getConnection();
                PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);

            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            return false;
        }
    }

    public String getPasswordHash(String usernameOrEmail) {
        String query = "SELECT password_hash FROM users WHERE username = ? OR email = ?";

        try (
                Connection databaseConnection = DatabaseManager.getInstance().getConnection();
                PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, usernameOrEmail);
            preparedStatement.setString(2, usernameOrEmail);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("password_hash");
            } else {
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }
}
