package service;

import interfaces.StudentOperations;
import model.Student;
import model.Result;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * JAVA CONCEPTS DEMONSTRATED:
 * 1. Interfaces: Implements StudentOperations interface.
 * 2. Classes and Objects: Service layer managing the Student collection.
 * 3. Method Overloading: 
 *      - searchStudent(String studentId) vs searchStudent(String query, boolean isCourseSearch)
 *      - displayStudent(Student student) vs displayStudent(Student student, boolean showReportCard)
 * 4. Static Members: DEFAULT_FILE_PATH, static validation methods.
 * 5. Access Specifiers:
 *      - private: internal student list and file path.
 *      - public: interface and service operations.
 * 6. File Handling: Uses File, BufferedReader, PrintWriter to store/load CSV data.
 * ============================================================================
 */
public class StudentManager implements StudentOperations {

    // -------------------------------------------------------------
    // STATIC MEMBERS
    // -------------------------------------------------------------
    public static final String DEFAULT_FILE_PATH = "data/students.txt";

    // -------------------------------------------------------------
    // ACCESS SPECIFIER: 'private' (Encapsulation)
    // -------------------------------------------------------------
    private List<Student> studentList;
    private String filePath;

    // -------------------------------------------------------------
    // CONSTRUCTORS
    // -------------------------------------------------------------
    public StudentManager() {
        this(DEFAULT_FILE_PATH);
    }

    public StudentManager(String filePath) {
        this.filePath = filePath;
        this.studentList = new ArrayList<>();
        // Automatically load existing records from file on startup
        loadFromFile();
    }

    // -------------------------------------------------------------
    // STATIC UTILITY METHOD: Validates marks in range [0, 100]
    // -------------------------------------------------------------
    public static boolean isValidMark(double mark) {
        return mark >= 0.0 && mark <= Result.MAX_MARKS_PER_SUBJECT;
    }

    // -------------------------------------------------------------
    // INTERFACE METHOD IMPLEMENTATION: addStudent
    // -------------------------------------------------------------
    @Override
    public boolean addStudent(Student student) {
        if (student == null || student.getStudentId() == null) {
            return false;
        }

        // Validate unique student ID
        if (searchStudent(student.getStudentId()) != null) {
            return false; // Duplicate ID found
        }

        studentList.add(student);
        // Synchronize static student counter
        Student.setTotalStudentsCount(studentList.size());
        saveToFile();
        return true;
    }

    // -------------------------------------------------------------
    // INTERFACE METHOD IMPLEMENTATION: searchStudent by ID
    // -------------------------------------------------------------
    @Override
    public Student searchStudent(String studentId) {
        if (studentId == null) return null;
        for (Student s : studentList) {
            if (s.getStudentId().equalsIgnoreCase(studentId.trim())) {
                return s;
            }
        }
        return null;
    }

    // -------------------------------------------------------------
    // METHOD OVERLOADING: Version 2 of searchStudent
    // Searches students filtered by course name.
    // -------------------------------------------------------------
    public List<Student> searchStudent(String courseQuery, boolean isCourseSearch) {
        List<Student> results = new ArrayList<>();
        if (courseQuery == null) return results;

        for (Student s : studentList) {
            if (isCourseSearch && s.getCourse().equalsIgnoreCase(courseQuery.trim())) {
                results.add(s);
            }
        }
        return results;
    }

    // -------------------------------------------------------------
    // INTERFACE METHOD IMPLEMENTATION: updateStudent
    // -------------------------------------------------------------
    @Override
    public boolean updateStudent(String studentId, Student updatedStudent) {
        Student existing = searchStudent(studentId);
        if (existing == null || updatedStudent == null) {
            return false;
        }

        // Update fields
        existing.setName(updatedStudent.getName());
        existing.setEmail(updatedStudent.getEmail());
        existing.setCourse(updatedStudent.getCourse());
        existing.setSemester(updatedStudent.getSemester());
        existing.setMarks(
                updatedStudent.getSubject1Marks(),
                updatedStudent.getSubject2Marks(),
                updatedStudent.getSubject3Marks()
        );

        saveToFile();
        return true;
    }

    // -------------------------------------------------------------
    // INTERFACE METHOD IMPLEMENTATION: deleteStudent
    // -------------------------------------------------------------
    @Override
    public boolean deleteStudent(String studentId) {
        Student target = searchStudent(studentId);
        if (target == null) {
            return false;
        }

        studentList.remove(target);
        Student.setTotalStudentsCount(studentList.size());
        saveToFile();
        return true;
    }

    // -------------------------------------------------------------
    // INTERFACE METHOD IMPLEMENTATION: getAllStudents
    // -------------------------------------------------------------
    @Override
    public List<Student> getAllStudents() {
        return new ArrayList<>(studentList);
    }

    // -------------------------------------------------------------
    // METHOD OVERLOADING: displayStudent (Compact Summary)
    // -------------------------------------------------------------
    public void displayStudent(Student student) {
        displayStudent(student, false);
    }

    // -------------------------------------------------------------
    // METHOD OVERLOADING: displayStudent (Detailed Report Card)
    // -------------------------------------------------------------
    public void displayStudent(Student student, boolean showReportCard) {
        if (student == null) {
            System.out.println("  [!] Student record is null.");
            return;
        }

        if (!showReportCard) {
            System.out.println("  ID     : " + student.getStudentId());
            System.out.println("  Name   : " + student.getName());
            System.out.println("  Course : " + student.getCourse() + " (Sem " + student.getSemester() + ")");
            System.out.println("  Result : " + student.getResult().getGrade() + " (" + student.getResult().getStatus() + ")");
        } else {
            generateReportCard(student);
        }
    }

    // -------------------------------------------------------------
    // INTERFACE METHOD IMPLEMENTATION: displayAllStudents
    // -------------------------------------------------------------
    @Override
    public void displayAllStudents() {
        if (studentList.isEmpty()) {
            System.out.println("  [!] No student records found. Add students first.");
            return;
        }

        System.out.println("+------------+----------------------+---------------+-----+-------+-------+-------+--------+--------+-------+");
        System.out.printf("| %-10s | %-20s | %-13s | %-3s | %-5s | %-5s | %-5s | %-6s | %-6s | %-5s |\n",
                "ID", "NAME", "COURSE", "SEM", "SUB1", "SUB2", "SUB3", "TOTAL", "PERC %", "GRADE");
        System.out.println("+------------+----------------------+---------------+-----+-------+-------+-------+--------+--------+-------+");

        for (Student s : studentList) {
            Result r = s.getResult();
            System.out.printf("| %-10s | %-20s | %-13s | %-3d | %-5.1f | %-5.1f | %-5.1f | %-6.1f | %-6.2f | %-5s |\n",
                    truncate(s.getStudentId(), 10),
                    truncate(s.getName(), 20),
                    truncate(s.getCourse(), 13),
                    s.getSemester(),
                    s.getSubject1Marks(),
                    s.getSubject2Marks(),
                    s.getSubject3Marks(),
                    r.getTotalMarks(),
                    r.getPercentage(),
                    r.getGrade()
            );
        }
        System.out.println("+------------+----------------------+---------------+-----+-------+-------+-------+--------+--------+-------+");
        System.out.println("  Total Students: " + studentList.size() + " | Institute: " + Student.COLLEGE_NAME);
    }

    // -------------------------------------------------------------
    // FEATURE: Generate Individual Student Result Card (Report Card)
    // -------------------------------------------------------------
    public void generateReportCard(Student student) {
        if (student == null) return;
        Result r = student.getResult();

        System.out.println("+---------------------------------------------------------------------+");
        System.out.println("| " + center("OFFICIAL ACADEMIC REPORT CARD", 67) + " |");
        System.out.println("| " + center(Student.COLLEGE_NAME, 67) + " |");
        System.out.println("+---------------------------------------------------------------------+");
        System.out.printf("| Student ID : %-20s Course   : %-21s |\n", truncate(student.getStudentId(), 20), truncate(student.getCourse(), 21));
        System.out.printf("| Full Name  : %-20s Semester : %-21d |\n", truncate(student.getName(), 20), student.getSemester());
        System.out.printf("| Email      : %-52s |\n", truncate(student.getEmail(), 52));
        System.out.println("+---------------------------+-----------+------------+----------------+");
        System.out.println("| SUBJECT                   | MAX MARKS | PASS MARKS | MARKS OBTAINED |");
        System.out.println("+---------------------------+-----------+------------+----------------+");
        System.out.printf("| Subject 1 (Core Theory)   | 100.0     | 40.0       | %-14.2f |\n", student.getSubject1Marks());
        System.out.printf("| Subject 2 (Practical)     | 100.0     | 40.0       | %-14.2f |\n", student.getSubject2Marks());
        System.out.printf("| Subject 3 (Elective)      | 100.0     | 40.0       | %-14.2f |\n", student.getSubject3Marks());
        System.out.println("+---------------------------+-----------+------------+----------------+");
        System.out.printf("| GRAND TOTAL               | 300.0     |            | %-14.2f |\n", r.getTotalMarks());
        System.out.printf("| PERCENTAGE                | %-39s |\n", String.format("%.2f%%", r.getPercentage()));
        System.out.printf("| FINAL GRADE               | %-39s |\n", r.getGrade());
        System.out.printf("| STATUS                    | %-39s |\n", r.getStatus());
        System.out.printf("| REMARKS                   | %-39s |\n", r.getEvaluationNote());
        System.out.println("+---------------------------------------------------------------------+");
    }

    // -------------------------------------------------------------
    // FEATURE: Calculate & Display Grade Statistics
    // -------------------------------------------------------------
    public void displayGradeStatistics() {
        if (studentList.isEmpty()) {
            System.out.println("  [!] No students available to calculate grade statistics.");
            return;
        }

        int countA = 0, countB = 0, countC = 0, countD = 0, countE = 0, countF = 0;
        int passed = 0, failed = 0;
        double sumPercentage = 0.0;
        double highest = -1.0;
        double lowest = 101.0;
        String topScorer = "N/A";

        for (Student s : studentList) {
            s.recalculateResult(); // ensure up to date
            Result r = s.getResult();
            double p = r.getPercentage();
            sumPercentage += p;

            if (p > highest) {
                highest = p;
                topScorer = s.getName() + " (" + s.getStudentId() + ")";
            }
            if (p < lowest) {
                lowest = p;
            }

            if (r.isPassed()) {
                passed++;
            } else {
                failed++;
            }

            switch (r.getGrade()) {
                case "A+":
                case "A": countA++; break;
                case "B": countB++; break;
                case "C": countC++; break;
                case "D": countD++; break;
                case "E": countE++; break;
                default: countF++; break;
            }
        }

        double avgPercentage = sumPercentage / studentList.size();

        System.out.println("+-------------------------------------------------------------+");
        System.out.println("|                 CLASS GRADE SUMMARY & ANALYTICS             |");
        System.out.println("+-------------------------------------------------------------+");
        System.out.printf("| Total Students Evaluated : %-32d |\n", studentList.size());
        System.out.printf("| Passed Students          : %-32d |\n", passed);
        System.out.printf("| Failed Students          : %-32d |\n", failed);
        System.out.printf("| Class Average Percentage : %-32s |\n", String.format("%.2f%%", avgPercentage));
        System.out.printf("| Highest Percentage       : %-32s |\n", String.format("%.2f%%", (highest < 0 ? 0 : highest)));
        System.out.printf("| Lowest Percentage        : %-32s |\n", String.format("%.2f%%", (lowest > 100 ? 0 : lowest)));
        System.out.printf("| Top Performer            : %-32s |\n", truncate(topScorer, 32));
        System.out.println("+-------------------------------------------------------------+");
        System.out.println("| Grade Distribution:                                         |");
        System.out.printf("|   - Grade A+/A (>= 80%%) : %-34d |\n", countA);
        System.out.printf("|   - Grade B    (>= 70%%) : %-34d |\n", countB);
        System.out.printf("|   - Grade C    (>= 60%%) : %-34d |\n", countC);
        System.out.printf("|   - Grade D    (>= 50%%) : %-34d |\n", countD);
        System.out.printf("|   - Grade E    (>= 40%%) : %-34d |\n", countE);
        System.out.printf("|   - Grade F     (< 40%%) : %-34d |\n", countF);
        System.out.println("+-------------------------------------------------------------+");
    }

    // -------------------------------------------------------------
    // FILE HANDLING: saveToFile
    // -------------------------------------------------------------
    @Override
    public boolean saveToFile() {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (Student s : studentList) {
                writer.println(s.toCsvString());
            }
            return true;
        } catch (IOException e) {
            System.err.println("  [!] Error saving data to file: " + e.getMessage());
            return false;
        }
    }

    // -------------------------------------------------------------
    // FILE HANDLING: loadFromFile
    // -------------------------------------------------------------
    @Override
    public boolean loadFromFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            return false; // File doesn't exist yet, clean start
        }

        studentList.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    Student s = Student.fromCsvString(line);
                    if (s != null) {
                        studentList.add(s);
                    }
                }
            }
            Student.setTotalStudentsCount(studentList.size());
            return true;
        } catch (IOException e) {
            System.err.println("  [!] Error reading data from file: " + e.getMessage());
            return false;
        }
    }

    // -------------------------------------------------------------
    // STRING HELPER UTILITIES
    // -------------------------------------------------------------
    private static String truncate(String text, int maxLen) {
        if (text == null) return "";
        if (text.length() <= maxLen) return text;
        return text.substring(0, maxLen - 2) + "..";
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
