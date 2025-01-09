/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package todolistapps;

/**
 *
 * @author YenPishz
 */
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;



public class Todo {
     public ArrayList<String> tasks;
    private Connection connection;
    

    public Todo() {
        tasks = new ArrayList<>();
        
        connectToDatabase();
    }

    private void connectToDatabase() {
    try {
        // Replace this with your MySQL connection string
        String url = "jdbc:mysql://localhost:3306/todolist";
        String user = "root";
        String password = "";
        connection = DriverManager.getConnection(url, user, password);
        System.out.println("Connected to MySQL database successfully.");
    } catch (SQLException e) {
        System.out.println("Connection error: " + e.getMessage());
    }
}

    public void addTask(String title, String description, Date date, String category, String level) {
    String sql = "INSERT INTO tasks (title, description, due_date, category, priority_level, status) VALUES (?, ?, ?, ?, ?, ?)";
    try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        pstmt.setString(1, title);
        pstmt.setString(2, description);
        pstmt.setDate(3, date);
        pstmt.setString(4, category);
        pstmt.setString(5, level);
        pstmt.setString(6, "Incomplete"); // Default status
        
        pstmt.executeUpdate();

        // Retrieve the generated ID for the task
        try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                Task newTask = new Task(generatedId, title, description, date.toString(), "Incomplete", category, level, null, null);
                // Add the task to your local list (or handle it as per your logic)
                
                tasks.add(newTask.toString());
                System.out.println("Task added successfully with ID: " + generatedId);
            }
        }
    } catch (SQLException e) {
        System.out.println("Error adding task: " + e.getMessage());
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

    public ArrayList<Task> fetchTasks() {
    ArrayList<Task> tasks = new ArrayList<>();
    String sql = "SELECT * FROM tasks";
    try (PreparedStatement pstmt = connection.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {

        while (rs.next()) {
            int id = rs.getInt("id");
            String title = rs.getString("title");
            String description = rs.getString("description");
            String dueDate = rs.getString("due_date");
            String status = rs.getString("status");
            String category = rs.getString("category");
            String priorityLevel = rs.getString("priority_level");

            Task task = new Task(id, title, description, dueDate, status, category, priorityLevel, null, null);
            tasks.add(task);
        }
    } catch (SQLException e) {
        System.out.println("Error fetching tasks: " + e.getMessage());
    }
    return tasks;
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
