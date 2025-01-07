/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Todo {
     public ArrayList<String> tasks;
    private Connection connection;

    public Todo() {
        tasks = new ArrayList<>();
        connectToDatabase();
    }

    private void connectToDatabase() {
        try {
            String url = "jdbc:postgresql://ep-hidden-haze-a1ez7q11-pooler.ap-southeast-1.aws.neon.tech/neondb";
            String user = "neondb_owner";
            String password = "CIvVZNslA31L";
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addTask(String title, String description, Date date, String category, String level) {
        String sql = "INSERT INTO tasks (title, description, due_date, category, priority_level, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.setDate(3, date);
            pstmt.setString(4, category);
            pstmt.setString(5, level);
            pstmt.setString(6, "Incomplete");
            pstmt.executeUpdate();
            System.out.println("Task added successfully!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void completeTask(String title) {
        String sql = "UPDATE tasks SET status = '[X]' WHERE title = ? AND status = '[ ]'";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, title);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Task marked as complete!");
            } else {
                System.out.println("Task not found or already completed.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteTask(int index) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, index);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Task deleted successfully!");
            } else {
                System.out.println("Invalid number. Returning to menu...");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void fetchTasks() {
        String sql = "SELECT * FROM tasks";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            tasks.clear();
            while (rs.next()) {
                String task = rs.getString("status") + " " + rs.getString("title") + " - " + rs.getString("description")
                        + " - " + rs.getString("due_date") + " - " + rs.getString("category") + " - "
                        + rs.getString("priority_level");
                tasks.add(task);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void viewTasks() {
        fetchTasks();
        System.out.println("\nCurrent Tasks:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    public void sortTasks(String sortBy, boolean ascending) {
        fetchTasks();
        selectionSort(tasks, sortBy, ascending);
    }

    private int getPriorityValue(String level) {
        switch (level.trim().toLowerCase()) {
            case "high":
                return 3;
            case "medium":
                return 2;
            case "low":
                return 1;
        }
        return 0;
    }

    private boolean compareTasks(String task1, String task2, String sortBy, boolean ascending) {
        String[] task1Parts = task1.split(" - ");
        String[] task2Parts = task2.split(" - ");

        String value1 = sortBy.equals("date") ? task1Parts[2] : task1Parts[4];
        String value2 = sortBy.equals("date") ? task2Parts[2] : task2Parts[4];

        int comparison;
        if (sortBy.equals("level")) {
            int priority1 = getPriorityValue(value1);
            int priority2 = getPriorityValue(value2);
            comparison = Integer.compare(priority1, priority2);
        } else {
            comparison = value1.compareTo(value2);
        }
        return ascending ? comparison < 0 : comparison > 0;
    }

    private void selectionSort(ArrayList<String> list, String sortBy, boolean ascending) {
        int n = tasks.size();

        for (int i = 0; i < n - 1; i++) {
            int selectedIndex = i;

            for (int j = i + 1; j < n; j++) {
                if (compareTasks(tasks.get(j), tasks.get(selectedIndex), sortBy, ascending)) {
                    selectedIndex = j;
                }
            }

            if (selectedIndex != i) {
                String temp = tasks.get(i);
                tasks.set(i, tasks.get(selectedIndex));
                tasks.set(selectedIndex, temp);
            }
        }
    }


    public ArrayList<String> getTasks() {
    return tasks;
}

    
    
}
