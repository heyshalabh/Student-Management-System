package interfaces;

import model.Student;
import java.util.List;

/**
 * ============================================================================
 * JAVA CONCEPTS DEMONSTRATED:
 * 1. Interfaces: Defines a strict contract for Student Management operations.
 * 2. Packages: interfaces package separate from model and service.
 * 3. Access Specifiers: Public interface methods.
 * ============================================================================
 */
public interface StudentOperations {

    /**
     * Adds a new student to the system.
     * @param student the student object to add
     * @return true if added successfully, false if duplicate ID exists
     */
    boolean addStudent(Student student);

    /**
     * Searches for a student by ID.
     * @param studentId the unique student identifier
     * @return Student object if found, or null
     */
    Student searchStudent(String studentId);

    /**
     * Updates details of an existing student.
     * @param studentId the unique student identifier
     * @param updatedStudent student object containing updated details
     * @return true if updated, false if student not found
     */
    boolean updateStudent(String studentId, Student updatedStudent);

    /**
     * Deletes a student by ID.
     * @param studentId the unique student identifier
     * @return true if deleted, false if student not found
     */
    boolean deleteStudent(String studentId);

    /**
     * Retrieves all student records.
     * @return list of all students
     */
    List<Student> getAllStudents();

    /**
     * Displays all student records in a formatted tabular layout.
     */
    void displayAllStudents();

    /**
     * Saves student records to the local file.
     * @return true if save succeeded
     */
    boolean saveToFile();

    /**
     * Loads student records from the local file.
     * @return true if load succeeded
     */
    boolean loadFromFile();
}
