package com.vasylyna.travelplanningapplication.database;

import com.vasylyna.travelplanningapplication.controllers.LoginController;
import com.vasylyna.travelplanningapplication.database.entity.ChecklistItem;
import com.vasylyna.travelplanningapplication.util.AlertDialogUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChecklistDAO {

    public static List<ChecklistItem> getAllItems() {
        List<ChecklistItem> items = new ArrayList<>();
        String query = "SELECT id, item_name, is_checked FROM checklist_items WHERE user_id = ? ORDER BY id";

        try (
             Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setInt(1, LoginController.currentUserId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                items.add(new ChecklistItem(
                        resultSet.getInt("id"),
                        resultSet.getString("item_name"),
                        resultSet.getBoolean("is_checked")
                ));
            }

        } catch (SQLException e) {
            AlertDialogUtil.showErrorDialog("Помилка", "Помилка з'єднання з базою даних");
        }
        return items;
    }

    public static void toggleItemCheck(int itemId, boolean isChecked) {
        String query = "UPDATE checklist_items SET is_checked = ? WHERE id = ?";
        try (
                Connection connection = DatabaseManager.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setBoolean(1, isChecked);
            preparedStatement.setInt(2, itemId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            AlertDialogUtil.showErrorDialog("Помилка", "Помилка з'єднання з базою даних");
        }
    }

    public static boolean addItem(String item_name) {
        String query = "INSERT INTO checklist_items (item_name, is_checked, user_id) VALUES (?, false, ?)";
        try (
             Connection connection = DatabaseManager.getInstance().getConnection();
              PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, item_name);
            preparedStatement.setInt(2, LoginController.currentUserId);
            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            AlertDialogUtil.showErrorDialog("Помилка", "Помилка з'єднання з базою даних");
        }
        return false;
    }

    public static void deleteItem(int itemId) {
        String query = "DELETE FROM checklist_items WHERE id = ?";
        try (
             Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setInt(1, itemId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            AlertDialogUtil.showErrorDialog("Помилка", "Помилка з'єднання з базою даних");
        }
    }

    public static void createDefaultChecklist(int userId) {
        String query = "INSERT INTO checklist_items (item_name, is_checked, user_id) VALUES (?, false, ?)";
        List<String> defaultItems = List.of(
                "Паспорт",
                "Квитки",
                "Зарядка",
                "Засоби гігієни (зубна щітка, паста, мило)",
                "Сонцезахисний крем",
                "Аптечка (основні ліки)",
                "Засоби від комарів",
                "Серветки (вологі та сухі)",
                "Антисептик для рук",
                "Жувальна гумка / льодяники"
        );

        try (
             Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            for (String item : defaultItems) {
                preparedStatement.setString(1, item);
                preparedStatement.setInt(2, userId);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            AlertDialogUtil.showErrorDialog("Помилка", "Помилка з'єднання з базою даних");
            e.printStackTrace();
        }
    }
}
