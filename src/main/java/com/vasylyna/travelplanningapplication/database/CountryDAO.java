package com.vasylyna.travelplanningapplication.database;

import com.vasylyna.travelplanningapplication.controllers.LoginController;
import com.vasylyna.travelplanningapplication.database.entity.Country;
import com.vasylyna.travelplanningapplication.util.AlertDialogUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CountryDAO {

    public static String getCountryCodeByName (String countryName) {
        String query = "SELECT code FROM countries WHERE name = ?" ;

        try (
                Connection connection = DatabaseManager.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, countryName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("code");
            }
        } catch (SQLException e) {
            AlertDialogUtil.showErrorDialog("Помилка", "Помилка з'єднання з базою даних");
        }
        return null;
    }

    public static List<Country> getAllCountries (int userId, String table) {
        List<Country> countries = new ArrayList<>();
        String query = "SELECT id, name, code FROM " + table + " WHERE user_id = ? ORDER BY id";

        try (
                Connection connection = DatabaseManager.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String code = resultSet.getString("code");
                countries.add(new Country(id, name, code));
            }

        } catch (SQLException e) {
            AlertDialogUtil.showErrorDialog("Помилка", "Помилка з'єднання з базою даних");
        }
        return countries;
    }

    public static boolean addCountry(String countryName, String countryCode, String table) {
        String checkQuery = "SELECT COUNT(*) FROM " + table +  " WHERE name = ? AND user_id = ?" ;
        String insertQuery = "INSERT INTO " + table + " (name, code, user_id) VALUES (?, ?, ?)";

        try (
                Connection connection = DatabaseManager.getInstance().getConnection();
                PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery)

        ) {
            if (table.equals("desired_countries")) {
                boolean existsInVisited = isCountryInTable(countryName);
                if (existsInVisited) {
                    AlertDialogUtil.showErrorDialog("Помилка", "Ви вже відвідали цю країну, якщо хочете додати ще раз, то видаліть її із списку відвіданих.");
                    return false;
                }
            }

            checkStatement.setString(1, countryName);
            checkStatement.setInt(2, LoginController.currentUserId);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                AlertDialogUtil.showErrorDialog("Помилка", "Цю країну вже було додано у ваш список.");
                return false;
            }
            insertStatement.setString(1, countryName);
            insertStatement.setString(2, countryCode);
            insertStatement.setInt(3, LoginController.currentUserId);
            insertStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            AlertDialogUtil.showErrorDialog("Помилка", "Помилка з'єднання з базою даних");
            return false;
        }
    }

    private static boolean isCountryInTable(String countryName) {
        String query = "SELECT COUNT(*) FROM visited_countries WHERE name = ? AND user_id = ?";

        try (
            Connection connection = DatabaseManager.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, countryName);
            preparedStatement.setInt(2, LoginController.currentUserId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            AlertDialogUtil.showErrorDialog("Помилка", "Помилка з'єднання з базою даних");
        }
        return false;
    }

    public static void deleteCountry(int countryId, String table) {
        String query = "DELETE FROM " + table + " WHERE user_id = ? AND id = ?";
        try (
             Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setInt(1, LoginController.currentUserId);
            preparedStatement.setInt(2, countryId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            AlertDialogUtil.showErrorDialog("Помилка", "Помилка з'єднання з базою даних");
        }
    }

    public static int countAllCountriesInWorld() {
        String query = "SELECT COUNT(DISTINCT code) FROM countries";
        int count = 0;

        try (
             Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            AlertDialogUtil.showErrorDialog("Помилка", "Помилка з'єднання з базою даних");
        }
        return count;
    }

    public static int countVisitedCountries() {
        String query = "SELECT COUNT(DISTINCT code) FROM visited_countries WHERE user_id = ?";
        int count = 0;

        try (
            Connection connection = DatabaseManager.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setInt(1, LoginController.currentUserId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            AlertDialogUtil.showErrorDialog("Помилка", "Помилка з'єднання з базою даних");
        }
        return count;
    }
}
