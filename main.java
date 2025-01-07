/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

public class main {

    public static void main(String[] args) {
        Todo todo = new Todo();
        Scanner input = new Scanner(System.in);
        String option;

        // User input for email & password of sender
        System.out.print("Enter your email: ");
        String senderEmail = input.nextLine();

        System.out.print("Enter your email password: ");
        String senderPassword = input.nextLine();

        do {
            System.out.println("Welcome to To-Do");
            System.out.println("\nChoose an action:");
            System.out.println("1. Create a new task");
            System.out.println("2. Complete a task");
            System.out.println("3. Delete a task");
            System.out.println("4. Sort all tasks");
            System.out.println("5. Search tasks");
            System.out.println("6. Add a recurring task");
            System.out.println("7. Set task dependency");
            System.out.println("8. Edit a task");
            System.out.println("9. View all tasks");
            System.out.println("10. View Analytics Dashboard");
            System.out.println("11. Send Reminders for Tasks Due Tomorrow");
            System.out.println("\nChoice of action: ");
            option = input.nextLine();

            switch (option) {
                case "1":
                    System.out.println("Enter task title: ");
                    String title = input.nextLine();

                    System.out.println("Enter task description: ");
                    String description = input.nextLine();

                    System.out.println("Enter due date (YYYY-MM-DD): ");
                    String dateString = input.nextLine();
                    Date date = null;
                    try {
                        date = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(dateString).getTime());
                    } catch (ParseException e) {
                        System.out.println("Invalid date format. Please use YYYY-MM-DD.");
                        break;
                    }

                    System.out.println("Enter task category (Homework, Personal, Work): ");
                    String category = input.nextLine();

                    System.out.println("Priority level (Low, Medium, High): ");
                    String level = input.nextLine();

                    try {
                        // Add the task to the database
                        todo.addTask(title, description, date, category, level);
                        System.out.println("\nTask created successfully!");
                    } catch (Exception e) {
                        System.out.println("Error adding task: " + e.getMessage());
                    }
                    break;

                case "2":
                    System.out.println("Enter the title of the task to mark as complete: ");
                    String titleToComplete = input.nextLine();
                    todo.completeTask(titleToComplete);
                    break;

                case "3":
                    todo.viewTasks();
                    System.out.print("Enter the task number you want to delete: ");
                    int delete = Integer.parseInt(input.nextLine()) - 1;
                    todo.deleteTask(delete);
                    break;

                case "4":
                    System.out.println("\nChoose sorting method:");
                    System.out.println("1. Due date ascending");
                    System.out.println("2. Due date descending");
                    System.out.println("3. Priority high to low");
                    System.out.println("4. Priority low to high");
                    System.out.print("Enter your choice (1-4): ");
                    int choice = Integer.parseInt(input.nextLine());

                    String sortBy = "";
                    boolean ascending = true;

                    switch (choice) {
                        case 1:
                            sortBy = "date";
                            ascending = true;
                            System.out.println("\nTasks sorted by Due Date in Ascending order");
                            break;

                        case 2:
                            sortBy = "date";
                            ascending = false;
                            System.out.println("\nTasks sorted by Due Date in Descending order");
                            break;

                        case 3:
                            sortBy = "level";
                            ascending = false;
                            System.out.println("\nTasks sorted by Priority from High to Low");
                            break;

                        case 4:
                            sortBy = "level";
                            ascending = true;
                            System.out.println("\nTasks sorted by Priority from Low to High");
                            break;

                        default:
                            System.out.println("Invalid choice. Returning to main menu...");
                            continue;
                    }

                    todo.sortTasks(sortBy, ascending);
                    break;

                case "9":
                    todo.viewTasks();
                    break;

                case "11":
                    sendReminder(senderEmail, senderPassword, todo);
                    break;

                default:
                    System.out.println("Invalid choice. Returning to main menu...");
                    break;
            }

        } while (!option.equals("0"));
        input.close();
    }

    private static void sendReminder(String senderEmail, String senderPassword, Todo todo) {
        todo.fetchTasks();
        Calendar calendar = Calendar.getInstance();
        Date now = new Date(calendar.getTimeInMillis());
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date within24Hours = new Date(calendar.getTimeInMillis());

        StringBuilder emailMessage = new StringBuilder("=== Email Notification ===\n\nTasks due in the next 24 hours:\n\n");

        boolean hasTasks = false;
        for (String task : todo.tasks) {
            String[] taskParts = task.split(" - ");
            if (taskParts[2].equals(within24Hours.toString())) {
                hasTasks = true;
                emailMessage.append("- ").append(taskParts[1]).append(": ").append(taskParts[3]).append("\n");
            }
        }

        if (hasTasks) {
            System.out.println(emailMessage.toString());
            EmailNotiSystem.sendEmail(senderEmail, senderPassword, senderEmail, "Task Reminder: Tasks Due in 24 Hours", emailMessage.toString());
        } else {
            System.out.println("No tasks are due in the next 24 hours.");
        }
    }
}
