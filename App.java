import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Todo todo = new Todo();
        Scanner input = new Scanner(System.in);
        String option;

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

                    todo.addTask(title, description, date, category, level);
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

                default:
                    System.out.println("Invalid choice. Returning to main menu...");
                    break;
            }

        } while (!option.equals("0"));
        input.close();
    }
}