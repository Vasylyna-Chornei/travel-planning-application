package com.vasylyna.travelplanningapplication.database;

import com.vasylyna.travelplanningapplication.controllers.LoginController;
import com.vasylyna.travelplanningapplication.database.entity.Transaction;
import com.vasylyna.travelplanningapplication.util.AlertDialogUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public static boolean addTransaction(double amount, String category, String description, int userId, String transactionType, Connection connection) {
        String query = "INSERT INTO transactions (amount, category, description, user_id, transaction_type) VALUES (?, ?, ?, ?, ?::transaction_enum)";

        try (
            PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setDouble(1, amount);
            preparedStatement.setString(2, category);
            preparedStatement.setString(3, description);
            preparedStatement.setInt(4, userId);
            preparedStatement.setString(5, transactionType);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            AlertDialogUtil.showErrorDialog("Помилка", "Помилка з'єднання з базою даних");
            return false;
        }
    }



    public static double getCurrentBudget(int userId, String budgetType) {
        String columnToCheck = budgetType.equals("total") ? "total_budget" : "remaining_budget";
        String query = "SELECT " + columnToCheck + " FROM budgets WHERE user_id = ?";

        try (
             Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble(columnToCheck);
            }
        } catch (SQLException e) {
            AlertDialogUtil.showErrorDialog("Помилка", "Помилка з'єднання з базою даних");
        }
        return 0.0;
    }

    public static boolean updateRemainingBudget(double amount, int userId, String transactionType, Connection connection) {
        double currentBudget = getCurrentBudget(userId, "remaining_budget");

        if ("ADD".equals(transactionType)) {
            currentBudget += amount;
        } else if ("WITHDRAW".equals(transactionType)) {
            currentBudget -= amount;
        }

        String query = "UPDATE budgets SET remaining_budget = ? WHERE user_id = ?";

        try (
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setDouble(1, currentBudget);
            preparedStatement.setInt(2, userId);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            AlertDialogUtil.showErrorDialog("Помилка", "Помилка з'єднання з базою даних");
            return false;
        }
    }

    public static List<Transaction> getTransactions(int userId) {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions WHERE user_id = ? ORDER BY id DESC";

        try (
                Connection connection = DatabaseManager.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Transaction transaction = new Transaction(
                        resultSet.getInt("id"),
                        resultSet.getDouble("amount"),
                        resultSet.getString("category"),
                        resultSet.getString("description"),
                        resultSet.getTimestamp("date").toLocalDateTime(),
                        resultSet.getString("transaction_type")
                );
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            AlertDialogUtil.showErrorDialog("Помилка", "Помилка з'єднання з базою даних");
        }
        return transactions;
    }

    public static boolean updateBudgetDirectly(double amount, int userId, String budgetType) {
        String columnToCheck = budgetType.equals("total") ? "total_budget" : "remaining_budget";
        String query = "UPDATE budgets SET " + columnToCheck + " = ? WHERE user_id = ?";

        try (
                Connection connection = DatabaseManager.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setDouble(1, amount);
            preparedStatement.setInt(2, userId);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            AlertDialogUtil.showErrorDialog("Помилка", "Помилка з'єднання з базою даних");
            return false;
        }
    }

    public static void createInitialBudgetForUser(int userId) {
        String checkQuery = "SELECT COUNT(*) FROM budgets WHERE user_id = ?";
        String insertQuery = "INSERT INTO budgets (user_id, remaining_budget, total_budget) VALUES (?, 0, 0)";

        try (
             Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
             PreparedStatement insertStatement = connection.prepareStatement(insertQuery)
        ) {
            checkStatement.setInt(1, userId);
            ResultSet resultSet = checkStatement.executeQuery();
            if (resultSet.next() && resultSet.getInt(1) == 0) {
                insertStatement.setInt(1, userId);
                insertStatement.executeUpdate();
            }
        } catch (SQLException e) {
            AlertDialogUtil.showErrorDialog("Помилка", "Помилка з'єднання з базою даних");
        }
    }

    public static void deleteTransaction(Transaction transaction) {
        String query = "DELETE FROM transactions WHERE id = ?";

        try (
             Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            connection.setAutoCommit(false);

            boolean updated = updateRemainingBudget(
                    transaction.getAmount(),
                    LoginController.currentUserId,
                    reverseTransactionType(transaction.getTransactionType()),
                    connection
            );

            if (!updated) {
                connection.rollback();
                AlertDialogUtil.showErrorDialog("Помилка", "Не вдалося оновити залишок");
                return;
            }

            preparedStatement.setInt(1, transaction.getId());
            preparedStatement.executeUpdate();
            AlertDialogUtil.showInfoDialog("Успіх", "Транзакцію видалено", "Операцію виконано успішно");

            connection.commit();
        } catch (SQLException e) {
            AlertDialogUtil.showErrorDialog("Помилка", "Не вдалося видалити транзакцію");
        }
    }

    private static String reverseTransactionType(String transactionType) {
        if ("ADD".equals(transactionType)) {
            return "WITHDRAW";
        } else {
            return "ADD";
        }
    }
}
