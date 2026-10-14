package main;

import interfaces.StudentOperations;
import model.Student;
import service.StudentManager;

import java.util.Scanner;

/**
 * ============================================================================
 * JAVA CONCEPTS DEMONSTRATED:
 * 1. Classes and Objects: Instantiating Scanner, StudentManager, Student.
 * 2. Packages: package main; importing from model, interfaces, and service.
 * 3. Static Members: Application constants, helper validator methods, main().
 * 4. Interfaces: Programming to interface 'StudentOperations'.
 * 5. Access Specifiers: public entry point, private helper UI methods.
 * ============================================================================
 * 
 * Project Name : Student Management System
 * Developer    : Shalabh Suman
 * Architecture : Layered Architecture (model, interfaces, service, main)
 */
public class Main {

    // -------------------------------------------------------------
    // STATIC CONSTANTS & ANSI COLOR CODES (With Terminal Fallback)
    // -------------------------------------------------------------
    private static final String APP_TITLE = "STUDENT MANAGEMENT SYSTEM";
    private static final String AUTHOR = "Built by Shalabh Suman";
    private static final String VERSION = "v1.0.0 (Java Mini Project)";

    // ANSI Colors
    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String BLUE = "\u001B[34m";

    // -------------------------------------------------------------
    // MAIN METHOD (Application Entry Point)
    // -------------------------------------------------------------
    public static void main(String[] args) {
        // Programming to interface
        StudentOperations operations = new StudentManager();
        StudentManager manager = (StudentManager) operations;

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        printBanner();

        while (running) {
            printMenu();
            int choice = readIntInput(scanner, "  Enter your choice (1-8): ");

            switch (choice) {
                case 1:
                    handleAddStudent(manager, scanner);
                    break;
                case 2:
                    handleSearchStudent(manager, scanner);
                    break;
                case 3:
                    handleUpdateStudent(manager, scanner);
                    break;
                case 4:
                    handleDeleteStudent(manager, scanner);
                    break;
                case 5:
                    handleDisplayAllStudents(manager);
                    break;
                case 6:
                    handleCalculateGrades(manager);
                    break;
                case 7:
                    handleGenerateResult(manager, scanner);
                    break;
                case 8:
                    running = false;
                    printExitMessage();
                    break;
                default:
                    printError("Invalid choice! Please select an option between 1 and 8.");
            }

            if (running) {
                System.out.println();
                System.out.print(CYAN + "  Press [Enter] to return to the main menu..." + RESET);
                scanner.nextLine();
            }
        }

        scanner.close();
    }

    // -------------------------------------------------------------
    // UI: BANNER & MENU
    // -------------------------------------------------------------
    private static void printBanner() {
        System.out.println(CYAN + "+=================================================================+" + RESET);
        System.out.println(CYAN + "|                                                                 |" + RESET);
        System.out.println(CYAN + "|        " + BOLD + YELLOW + center(APP_TITLE, 49) + RESET + CYAN + "        |" + RESET);
        System.out.println(CYAN + "|        " + BLUE + center(VERSION, 49) + RESET + CYAN + "        |" + RESET);
        System.out.println(CYAN + "|        " + GREEN + center(AUTHOR, 49) + RESET + CYAN + "        |" + RESET);
        System.out.println(CYAN + "|                                                                 |" + RESET);
        System.out.println(CYAN + "+=================================================================+" + RESET);
    }

    private static void printMenu() {
        System.out.println();
        System.out.println(BOLD + "+----------------------- MAIN MENU -------------------------------+" + RESET);
        System.out.println("|  [1] Add New Student                                            |");
        System.out.println("|  [2] Search Student by ID                                       |");
        System.out.println("|  [3] Update Student Details                                     |");
        System.out.println("|  [4] Delete Student Record                                      |");
        System.out.println("|  [5] Display All Students                                       |");
        System.out.println("|  [6] Calculate Student Grades (Class Analytics)                 |");
        System.out.println("|  [7] Generate Individual Student Result Card                    |");
        System.out.println("|  [8] Exit Application                                           |");
        System.out.println("+-----------------------------------------------------------------+");
        
        System.out.println("+-----------------------------------------------------------------+");
    }

    private static void printSectionHeader(String title) {
        System.out.println();
        System.out.println(BOLD + BLUE + ">>> " + title.toUpperCase() + " <<<" + RESET);
        System.out.println("-----------------------------------------------------------------");
    }

    private static void printSuccess(String message) {
        System.out.println(BOLD + GREEN + "  [SUCCESS] " + message + RESET);
    }

    private static void printError(String message) {
        System.out.println(BOLD + RED + "  [ERROR] " + message + RESET);
    }

    private static void printInfo(String message) {
        System.out.println(BOLD + YELLOW + "  [INFO] " + message + RESET);
    }

    private static void printExitMessage() {
        System.out.println();
        System.out.println(CYAN + "+=================================================================+" + RESET);
        System.out.println(CYAN + "|   Thank you for using the Student Management System!            |" + RESET);
        System.out.println(CYAN + "|   All records have been saved successfully to local storage.    |" + RESET);
        System.out.println(CYAN + "|   " + GREEN + center(AUTHOR, 61) + RESET + CYAN + " |" + RESET);
        System.out.println(CYAN + "+=================================================================+" + RESET);
    }

    // -------------------------------------------------------------
    // FEATURE HANDLERS
    // -------------------------------------------------------------

    private static void handleAddStudent(StudentManager manager, Scanner scanner) {
        printSectionHeader("Add New Student");

        String studentId;
        while (true) {
            System.out.print("  Enter Student ID (e.g. S101): ");
            studentId = scanner.nextLine().trim();
            if (studentId.isEmpty()) {
                printError("Student ID cannot be empty!");
                continue;
            }
            if (manager.searchStudent(studentId) != null) {
                printError("Student ID '" + studentId + "' already exists! Please use a unique ID.");
                return;
            }
            break;
        }

        String name;
        while (true) {
            System.out.print("  Enter Full Name: ");
            name = scanner.nextLine().trim();
            if (!name.isEmpty()) break;
            printError("Student name cannot be empty!");
        }

        System.out.print("  Enter Email Address: ");
        String email = scanner.nextLine().trim();
        if (email.isEmpty()) email = "student@college.edu";

        String course;
        while (true) {
            System.out.print("  Enter Course (e.g., Computer Science, IT, BCA): ");
            course = scanner.nextLine().trim();
            if (!course.isEmpty()) break;
            printError("Course cannot be empty!");
        }

        int semester = readIntInput(scanner, "  Enter Current Semester (1-8): ");
        while (semester < 1 || semester > 12) {
            printError("Semester must be between 1 and 12.");
            semester = readIntInput(scanner, "  Enter Current Semester (1-8): ");
        }

        System.out.println("  Enter Marks out of 100 for three subjects:");
        double sub1 = readMarkInput(scanner, "    Subject 1 Marks: ");
        double sub2 = readMarkInput(scanner, "    Subject 2 Marks: ");
        double sub3 = readMarkInput(scanner, "    Subject 3 Marks: ");

        // Create Student Object (Demonstrating Object instantiation & Constructors)
        Student newStudent = new Student(studentId, name, email, course, semester, sub1, sub2, sub3);

        boolean added = manager.addStudent(newStudent);
        if (added) {
            printSuccess("Student record added and saved successfully!");
            System.out.println();
            // Demonstrating Method Overloading: compact display
            manager.displayStudent(newStudent);
        } else {
            printError("Failed to add student. A record with this ID already exists.");
        }
    }

    private static void handleSearchStudent(StudentManager manager, Scanner scanner) {
        printSectionHeader("Search Student");
        System.out.println("  [1] Search by Student ID");
        System.out.println("  [2] Search by Course");
        int subChoice = readIntInput(scanner, "  Select search option (1-2): ");

        if (subChoice == 1) {
            System.out.print("  Enter Student ID to search: ");
            String id = scanner.nextLine().trim();
            // Calls searchStudent(String)
            Student s = manager.searchStudent(id);
            if (s != null) {
                printSuccess("Student found!");
                System.out.println();
                // Demonstrates Method Overloading: detailed report card display
                manager.displayStudent(s, true);
            } else {
                printError("No student found with ID '" + id + "'.");
            }
        } else if (subChoice == 2) {
            System.out.print("  Enter Course Name: ");
            String course = scanner.nextLine().trim();
            // Demonstrates Method Overloading: searchStudent(String, boolean)
            var list = manager.searchStudent(course, true);
            if (list.isEmpty()) {
                printInfo("No students found enrolled in course: " + course);
            } else {
                printSuccess("Found " + list.size() + " student(s) in " + course + ":");
                System.out.println("+------------+----------------------+---------------+-----+-------+");
                System.out.printf("| %-10s | %-20s | %-13s | %-3s | %-5s |\n", "ID", "NAME", "COURSE", "SEM", "GRADE");
                System.out.println("+------------+----------------------+---------------+-----+-------+");
                for (Student s : list) {
                    System.out.printf("| %-10s | %-20s | %-13s | %-3d | %-5s |\n",
                            s.getStudentId(), s.getName(), s.getCourse(), s.getSemester(), s.getResult().getGrade());
                }
                System.out.println("+------------+----------------------+---------------+-----+-------+");
            }
        } else {
            printError("Invalid search option selected.");
        }
    }

    private static void handleUpdateStudent(StudentManager manager, Scanner scanner) {
        printSectionHeader("Update Student Details");
        System.out.print("  Enter Student ID to update: ");
        String id = scanner.nextLine().trim();

        Student existing = manager.searchStudent(id);
        if (existing == null) {
            printError("Student with ID '" + id + "' not found.");
            return;
        }

        printInfo("Current Details for " + existing.getName() + " (" + id + "):");
        manager.displayStudent(existing, false);
        System.out.println();
        System.out.println("  (Leave input empty / enter 0 to retain current value where applicable)");

        System.out.print("  Enter New Full Name [" + existing.getName() + "]: ");
        String newName = scanner.nextLine().trim();
        if (newName.isEmpty()) newName = existing.getName();

        System.out.print("  Enter New Email [" + existing.getEmail() + "]: ");
        String newEmail = scanner.nextLine().trim();
        if (newEmail.isEmpty()) newEmail = existing.getEmail();

        System.out.print("  Enter New Course [" + existing.getCourse() + "]: ");
        String newCourse = scanner.nextLine().trim();
        if (newCourse.isEmpty()) newCourse = existing.getCourse();

        System.out.print("  Enter New Semester [" + existing.getSemester() + "] (or press Enter to keep): ");
        String semInput = scanner.nextLine().trim();
        int newSem = existing.getSemester();
        if (!semInput.isEmpty()) {
            try {
                int parsed = Integer.parseInt(semInput);
                if (parsed >= 1 && parsed <= 12) newSem = parsed;
            } catch (NumberFormatException ignored) {}
        }

        System.out.print("  Do you wish to update marks? (y/N): ");
        String updateMarksChoice = scanner.nextLine().trim();
        double sub1 = existing.getSubject1Marks();
        double sub2 = existing.getSubject2Marks();
        double sub3 = existing.getSubject3Marks();

        if (updateMarksChoice.equalsIgnoreCase("y") || updateMarksChoice.equalsIgnoreCase("yes")) {
            sub1 = readMarkInput(scanner, "    New Subject 1 Marks: ");
            sub2 = readMarkInput(scanner, "    New Subject 2 Marks: ");
            sub3 = readMarkInput(scanner, "    New Subject 3 Marks: ");
        }

        Student updated = new Student(id, newName, newEmail, newCourse, newSem, sub1, sub2, sub3);
        boolean success = manager.updateStudent(id, updated);
        if (success) {
            printSuccess("Student details updated and saved successfully!");
            System.out.println();
            manager.displayStudent(existing, true);
        } else {
            printError("Failed to update student details.");
        }
    }

    private static void handleDeleteStudent(StudentManager manager, Scanner scanner) {
        printSectionHeader("Delete Student Record");
        System.out.print("  Enter Student ID to delete: ");
        String id = scanner.nextLine().trim();

        Student target = manager.searchStudent(id);
        if (target == null) {
            printError("Student with ID '" + id + "' not found.");
            return;
        }

        System.out.println("  Student Found: " + target.getName() + " | Course: " + target.getCourse());
        System.out.print(RED + "  Are you sure you want to permanently delete this student? (y/N): " + RESET);
        String confirm = scanner.nextLine().trim();

        if (confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")) {
            boolean deleted = manager.deleteStudent(id);
            if (deleted) {
                printSuccess("Student record deleted successfully!");
            } else {
                printError("Could not delete student record.");
            }
        } else {
            printInfo("Deletion cancelled.");
        }
    }

    private static void handleDisplayAllStudents(StudentManager manager) {
        printSectionHeader("All Registered Students");
        manager.displayAllStudents();
    }

    private static void handleCalculateGrades(StudentManager manager) {
        printSectionHeader("Calculate Student Grades & Analytics");
        manager.displayGradeStatistics();
    }

    private static void handleGenerateResult(StudentManager manager, Scanner scanner) {
        printSectionHeader("Generate Individual Student Result Card");
        System.out.print("  Enter Student ID: ");
        String id = scanner.nextLine().trim();

        Student student = manager.searchStudent(id);
        if (student == null) {
            printError("Student with ID '" + id + "' not found.");
            return;
        }

        student.recalculateResult();
        manager.generateReportCard(student);
    }

    // -------------------------------------------------------------
    // INPUT VALIDATION HELPERS
    // -------------------------------------------------------------
    private static int readIntInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                printError("Invalid input! Please enter a valid number.");
            }
        }
    }

    private static double readMarkInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                double mark = Double.parseDouble(line);
                if (StudentManager.isValidMark(mark)) {
                    return mark;
                } else {
                    printError("Marks must be between 0 and 100! Please re-enter.");
                }
            } catch (NumberFormatException e) {
                printError("Invalid numeric input! Please enter a valid number.");
            }
        }
    }

    private static String center(String text, int width) {
        if (text == null) text = "";
        if (text.length() >= width) return text.substring(0, width);
        int pad = (width - text.length()) / 2;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pad; i++) sb.append(' ');
        sb.append(text);
        while (sb.length() < width) sb.append(' ');
        return sb.toString();
    }
}
