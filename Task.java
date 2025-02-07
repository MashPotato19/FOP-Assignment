/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package todolistapps;
import java.util.*;

/**
 *
 * @author YenPishz
 */
public class Task {
    private int id;
    private String title;
    private String description;
    private String dueDate;
    private String status; // "complete" or "incomplete"
    private String category; // e.g., Homework, Personal, Work
    private String priority; // e.g., Low, Medium, High
    private Integer dependentTaskId; // ID of the task this one depends on
    private boolean isRecurring;
    private String recurrenceInterval;
    


    // Constructor with dependentTaskId
    public Task(int id, String title, String description, String dueDate, String status, String category, String priority, Integer dependentTaskId, String recurrenceInterval) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
        this.category = category;
        this.priority = priority;
        this.dependentTaskId = dependentTaskId;
        this.recurrenceInterval = recurrenceInterval;
    
        this.recurrenceInterval = recurrenceInterval;
        
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getStatus() {
        return status;
    }

    public String getCategory() {
        return category;
    }

    public String getPriority() {
        return priority;
    }

    public Integer getDependentTaskId() {
        return dependentTaskId;
    }
    
    // Setters
    public void setId(Integer id) {
        this.id = id;    
    }
    
    public void setTitle(String title) {
        this.title = title;   
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
    
    public void setStatus(String status) {
        
        //this if else statement is to ensure that the only acceptable input for status is either Complete or Incomplete
        if (status.equalsIgnoreCase("complete") || status.equalsIgnoreCase("incomplete"))
        {
            this.status = status;
        }
        else
        {
            throw new IllegalArgumentException("Status must only be 'complete' or 'incomplete'.");
        }
    }

    public void setCategory(String category) {
        this.category = category;
    }
    
    //This is to establish a fixed priority which is low,mid,high
    public void setPriority(String priority) {
        if (priority.equalsIgnoreCase("low") || priority.equalsIgnoreCase("mid") || priority.equalsIgnoreCase("high"))
        {
            this.priority = priority;
        }
        else
        {
            throw new IllegalArgumentException("Priority must be set to 'low', 'mid', or 'high'.");
        }
    }
    
    public void setDependentTaskId(Integer dependentTaskId) {
        this.dependentTaskId = dependentTaskId;
    }
    
    // Recurring Task
    public boolean isRecurring() {
        return isRecurring;
    }

    public void setRecurring(boolean isRecurring) {
        this.isRecurring = isRecurring;
    }

    public String getRecurrenceInterval() {
        return recurrenceInterval;
    }

     private Task findTaskById(int taskId, List<Task> allTasks) {
        for (Task task : allTasks) {
            if (task.getId() == taskId) {
                return task;
            }
        }
        return null;
     }
    
    @Override   // To ensure that the created task will be displayed with proper format
    public String toString() {
        return "Task ID: " + id +
               "\nTitle: " + title +
               "\nDescription: " + description +
               "\nDue Date: " + dueDate +
               "\nStatus: " + status +
               "\nCategory: " + category +
               "\nPriority: " + priority +
               "\nDepends on Task ID: " + (dependentTaskId != null ? dependentTaskId : "None");
    }
}


class TaskCreation {
    private ArrayList<Task> tasks = new ArrayList<>();
    private int nextId = 1; // To give unique ID to each task created

    private Todo todo = new Todo(); // Initialize the Todo instance

    public static void main(String[] args) {
        TaskCreation taskCreation = new TaskCreation();
        taskCreation.initializeTasks();
        taskCreation.runMenu();
    }

    // Load tasks from the database into memory
    public void initializeTasks() {
    try {
        // Now it will correctly fetch and store the list of tasks
        tasks = todo.fetchTasks();

        // If tasks are loaded, print them to verify
        if (!tasks.isEmpty()) {
            System.out.println("Tasks loaded successfully from the database:");
            for (Task task : tasks) {
                System.out.println(task);  // Ensure Task class has a meaningful toString() method
            }
        } else {
            System.out.println("No tasks found in the database.");
        }
    } catch (Exception e) {
        System.out.println("Error loading tasks: " + e.getMessage());
    }
}
    // Methods that assist in Task Creation
    public void runMenu() {
        Scanner scanner = new Scanner(System.in);
        String option;

        do {
            System.out.println("\n=== To-Do List Menu ===");
            System.out.println("1. Create a New Task");
            System.out.println("2. View All Tasks");
            System.out.println("3. Task Management (Mark As Complete)");
            System.out.println("4. Task Deletion");
            System.out.println("5. Task Sort");
            System.out.println("6. Task Search (By Keyword)");
            System.out.println("7. Add a Recurring Task");
            System.out.println("8. Set Task Dependency");
            System.out.println("9. Edit Task");
            System.out.println("10. View Analytics Dashboard");
            System.out.println("0. Exit");
            System.out.print("Choose An Option: ");
            option = scanner.nextLine();

            switch (option) {
                case "1":
                    createTask(scanner);
                    break;
                case "2":
                    viewTasks();
                    break;
                case "3":
                    markTaskAsComplete(scanner);
                    break;
                case "4":
                    deleteTask(scanner);
                    break;
                case "5":
                    sortTasks(scanner);
                    break;
                case "6":
                    searchTasks(scanner);
                    break;
                case "7":
                    addRecurringTask(scanner);
                    break;
                case "8":
                    setTaskDependency(scanner);
                    break;
                case "9":
                    editTask(scanner);
                    break;
                case "10":
                    viewAnalyticsDashboard();
                    break;
                case "0":
                    System.out.println("Exiting Program");
                    break;
                default:
                    System.out.println("Invalid option. Please enter valid option.");
            }
        } while (!option.equals("0"));

        scanner.close();
    }

    // Task Creation (1)
    public void createTask(Scanner scanner) {
        try {
            System.out.println("\n=== Create a New Task ===");

            System.out.print("Enter task title: ");
            String title = scanner.nextLine();

            System.out.print("Enter task description: ");
            String description = scanner.nextLine();

            System.out.print("Enter due date (YYYY-MM-DD): ");
            String dueDate = scanner.nextLine();

            System.out.print("Enter task status (complete/incomplete): ");
            String status = scanner.nextLine();

            System.out.print("Enter task category (Homework, Personal, Work): ");
            String category = scanner.nextLine();

            System.out.print("Priority Level (Low, Mid, High): ");
            String priority = scanner.nextLine();

            System.out.print("Enter dependent task ID (Press Enter if none): ");
            String dependentTaskInput = scanner.nextLine();
            Integer dependentTaskId = dependentTaskInput.isEmpty() ? null : Integer.parseInt(dependentTaskInput);

            Task newTask = new Task(nextId++, title, description, dueDate, status, category, priority, dependentTaskId, null);
            tasks.add(newTask);
            
            // Convert dueDate to java.sql.Date
            java.sql.Date sqlDueDate = java.sql.Date.valueOf(dueDate);

            // Save task to the database using the Todo instance
            todo.addTask(title, description, sqlDueDate, category, priority);

            System.out.println("\nTask created successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
    // View Task (2)
    public void viewTasks() {
        if (tasks.isEmpty()) {
            System.out.println("\nNo tasks available.");
            return;
        }

        System.out.println("\n=== List of Tasks ===");
        for (Task task : tasks) {
            System.out.println(task);
            System.out.println("-------------------");
        }
    }

    // Task Management (3) - Mark task as complete
    public void markTaskAsComplete(Scanner scanner) {
        System.out.print("\n=== Mark Task as Complete ===");
        System.out.print("\nEnter the task ID you want to mark as complete: ");
        int taskId = Integer.parseInt(scanner.nextLine());

        Task task = findTaskById(taskId); // The method findTaskById is declared below
        if (task != null) {
            // Check if the task has a dependency
            if (task.getDependentTaskId() != null) {
                Task dependentTask = findTaskById(task.getDependentTaskId());
                if (dependentTask != null && dependentTask.getStatus().equals("incomplete")) {
                    System.out.println("Warning: Task \"" + task.getTitle() + "\" cannot be marked as complete because it depends on \"" + dependentTask.getTitle() + "\". Please complete \"" + dependentTask.getTitle() + "\" first.");
                    return;
                }
            }
            task.setStatus("complete");
            System.out.println("Task \"" + task.getTitle() + "\" marked as complete!");
        } else {
            System.out.println("Task not found with ID: " + taskId);
        }
    }

    // Task Deletion (4)
    public void deleteTask(Scanner scanner) {
        if (tasks.isEmpty()) {
            System.out.println("\nNo tasks available.");
            return;
        }
        try {
            System.out.println("\n=== Delete a Task ===");
            System.out.print("Enter the task ID you want to delete: ");
            int taskId = Integer.parseInt(scanner.nextLine());
            
            // Delete the task from the database
            todo.deleteTask(taskId);

            // Fetch and display updated tasks
            todo.fetchTasks();
            

            // Find the task by ID
            Task task = findTaskById(taskId);

            if (task != null) {
                tasks.remove(task);  // Remove the task from the arraylist
                System.out.println("Task \"" + task.getTitle() + "\" deleted successfully!");
            } else {
                System.out.println("No task found with ID: " + taskId);
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid task ID format.");
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    // Task Sort (5)
    public void sortTasks(Scanner scanner) {
        if (tasks.isEmpty()) {
            System.out.println("\nNo tasks available to sort.");
            return;
        }

        System.out.println("\n=== Sort Tasks ===");
        System.out.println("1. Due Date (Ascending)");
        System.out.println("2. Due Date (Descending)");
        System.out.println("3. Priority (High to Low)");
        System.out.println("4. Priority (Low to High)");
        System.out.print("> ");
        int choice = Integer.parseInt(scanner.nextLine()); // To convert string input into integer

        switch (choice) {
            case 1: // Due Date (Ascending)
                tasks.sort(Comparator.comparing(Task::getDueDate));
                System.out.println("Tasks sorted by Due Date (Ascending)!");
                break;
            case 2: // Due Date (Descending)
                tasks.sort(Comparator.comparing(Task::getDueDate).reversed());
                System.out.println("Tasks sorted by Due Date (Descending)!");
                break;
            case 3: // Priority (High to Low)
                tasks.sort(Comparator.comparing(Task::getPriority).reversed());
                System.out.println("Tasks sorted by Priority (High to Low)!");
                break;
            case 4: // Priority (Low to High)
                tasks.sort(Comparator.comparing(Task::getPriority));
                System.out.println("Tasks sorted by Priority (Low to High)!");
                break;
            default:
                System.out.println("Invalid option! Please enter valid input.");
        }
        viewTasks();
    }

    // Task Search (6)
    public void searchTasks(Scanner scanner) {
        if (tasks.isEmpty()) {
            System.out.println("\nNo tasks available.");
            return;
        }

        System.out.println("\n=== Search Tasks ===");
        System.out.print("Enter a keyword to search by title or description: ");
        String keyword = scanner.nextLine().toLowerCase();

        // Flag to check if we found any results
        boolean found = false;

        System.out.println("\n=== Search Results ===");

        for (Task task : tasks) {
            // Check if the keyword exists in the title or description (case-insensitive)
            if (task.getTitle().toLowerCase().contains(keyword) ||
                task.getDescription().toLowerCase().contains(keyword)) {

                found = true;
                System.out.println(task.getId() + ". [" + task.getStatus() + "] " +
                        task.getTitle() + " - Due: " + task.getDueDate() +
                        " - Category: " + task.getCategory() +
                        " - Priority: " + task.getPriority() +
                        "\nDescription: " + task.getDescription());
            }
        }
        if (!found) {
            System.out.println("No tasks match the search keyword.");
        }
    }

    // Add a Recurring Task (7)
    public void addRecurringTask(Scanner scanner) {
        try {
            System.out.println("\n=== Add a Recurring Task ===");
            System.out.print("Enter task title: ");
            String title = scanner.nextLine();

            System.out.print("Enter task description: ");
            String description = scanner.nextLine();

            System.out.print("Enter recurrence interval (daily, weekly, monthly): ");
            String recurrenceInterval = scanner.nextLine();

            Task recurringTask = new Task(nextId++, title, description, "2025-01-01", "incomplete", "General", "Low", null, recurrenceInterval);
            recurringTask.setRecurring(true);  // Mark task as recurring
            tasks.add(recurringTask);

            System.out.println("Recurring Task \"" + recurringTask.getTitle() + "\" created successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Create Recurring Task if already complete
    private void createRecurringTask(Task task) {
        String recurrenceInterval = task.getRecurrenceInterval();

        // Simulate creating a new task based on the recurrence interval
        String newDueDate = getNextRecurrenceDate(recurrenceInterval);
        Task recurringTask = new Task(nextId++, task.getTitle(), task.getDescription(), newDueDate, "incomplete", task.getCategory(), task.getPriority(), task.getDependentTaskId(), recurrenceInterval);
        tasks.add(recurringTask);
        System.out.println("New recurring task \"" + recurringTask.getTitle() + "\" created with due date: " + newDueDate);
    }

    // Calculate next due date
    private String getNextRecurrenceDate(String recurrenceInterval) {
    // This date is just for simulation, thats why a fixed date is added
        if (recurrenceInterval.equals("daily")) {
            return "2025-01-02";
        } else if (recurrenceInterval.equals("weekly")) {
            return "2025-01-08";
        } else if (recurrenceInterval.equals("monthly")) {
            return "2025-02-01";
        }
        return "2025-01-01";  // Default
    }
    
    // Set Task Dependency (8)
    public void setTaskDependency(Scanner scanner) {
        
        if (tasks.isEmpty()) {
            System.out.println("\nNo tasks available.");
            return;
        }
        
        System.out.println("\n=== Set Task Dependency ===");
        try {
            System.out.print("Enter the task number that depends on another task: ");
            int dependentTaskId = Integer.parseInt(scanner.nextLine());

            Task dependentTask = findTaskById(dependentTaskId);
            if (dependentTask == null) {
                System.out.println("Task not found with ID: " + dependentTaskId);
                return;
            }

            System.out.print("Enter the task number it depends on: ");
            int taskId = Integer.parseInt(scanner.nextLine());

            Task task = findTaskById(taskId);
            if (task == null) {
                System.out.println("Task not found with ID: " + taskId);
                return;
            }

            // Check for dependency cycle
            if (hasCycle(dependentTask, task)) {
                System.out.println("Error: Dependency cycle detected! Task cannot depend on itself or create a circular dependency.");
                return;
            }

            dependentTask.setDependentTaskId(taskId);
            System.out.println("Task \"" + dependentTask.getTitle() + "\" now depends on \"" + task.getTitle() + "\".");
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid input format.");
        }
    }

    // Cycle detection (checks if a task's dependencies form a cycle)
    private boolean hasCycle(Task dependentTask, Task task) {
        Set<Integer> visited = new HashSet<>();
        return hasCycleHelper(dependentTask, visited, task);
    }

    private boolean hasCycleHelper(Task task, Set<Integer> visited, Task originalTask) {
        if (visited.contains(task.getId())) {
            return true;
        }

        visited.add(task.getId());

        if (task.getDependentTaskId() != null) {
            Task dependentTask = findTaskById(task.getDependentTaskId());
            if (dependentTask != null && dependentTask.getId() != originalTask.getId() && hasCycleHelper(dependentTask, visited, originalTask)) {
                return true;
            }
        }
        visited.remove(task.getId());
        return false;
    }
    
    // Edit Task (9)
    public void editTask(Scanner scanner) {
        System.out.print("\n=== Edit Task ===");
        System.out.print("\nEnter the task ID you want to edit: ");
        int taskId = Integer.parseInt(scanner.nextLine());

        Task task = findTaskById(taskId);
        if (task == null) {
            System.out.println("Task not found with ID: " + taskId);
            return;
        }

        System.out.println("What would you like to edit?");
        System.out.println("1. Title");
        System.out.println("2. Description");
        System.out.println("3. Due Date");
        System.out.println("4. Category");
        System.out.println("5. Priority");
        System.out.println("6. Set Task Dependency");
        System.out.println("7. Cancel");
        System.out.print("> ");
        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1:
                System.out.print("Enter the new title: ");
                task.setTitle(scanner.nextLine());
                System.out.println("Task title updated to: " + task.getTitle());
                break;
            case 2:
                System.out.print("Enter the new description: ");
                task.setDescription(scanner.nextLine());
                System.out.println("Task description updated to: " + task.getDescription());
                break;
            case 3:
                System.out.print("Enter the new due date (YYYY-MM-DD): ");
                task.setDueDate(scanner.nextLine());
                System.out.println("Task due date updated to: " + task.getDueDate());
                break;
            case 4:
                System.out.print("Enter the new category: ");
                task.setCategory(scanner.nextLine());
                System.out.println("Task category updated to: " + task.getCategory());
                break;
            case 5:
                System.out.print("Enter the new priority level (Low, Mid, High): ");
                task.setPriority(scanner.nextLine());
                System.out.println("Task priority updated to: " + task.getPriority());
                break;
            case 6:
                setTaskDependency(scanner);
                break;
            case 7:
                System.out.println("Editing canceled.");
                break;
            default:
                System.out.println("Invalid option.");
        }
    }
    
    // Analytics Dashboard (10)
    public void viewAnalyticsDashboard() {
        System.out.println("\n=== Analytics Dashboard ===");

        int totalTasks = tasks.size();
        int completedTasks = 0;
        int pendingTasks = 0;
        Map<String, Integer> categoryCounts = new HashMap<>();

        // Calculate the number of completed, pending tasks and category distribution
        for (Task task : tasks) {
            if (task.getStatus().equalsIgnoreCase("complete")) {
                completedTasks++;
            } else if (task.getStatus().equalsIgnoreCase("incomplete")) {
                pendingTasks++;
            }

            // Count the tasks per category
            categoryCounts.put(task.getCategory(), categoryCounts.getOrDefault(task.getCategory(), 0) + 1);
        }

        // Calculate completion rate
        double completionRate = totalTasks > 0 ? ((double) completedTasks / totalTasks) * 100 : 0;

        // Display the dashboard
        System.out.println("Total Tasks: " + totalTasks);
        System.out.println("Completed: " + completedTasks);
        System.out.println("Pending: " + pendingTasks);
        System.out.printf("Completion Rate: %.2f%%\n", completionRate);
        System.out.println("Task Categories:");

        // Display task categories and their counts
        for (Map.Entry<String, Integer> entry : categoryCounts.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    private Task findTaskById(int taskId) {
        for (Task task : tasks) {
            if (task.getId() == taskId) {
                return task;
            }
        }
        return null;
    }
}

