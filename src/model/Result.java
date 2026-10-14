package model;

/**
 * ============================================================================
 * JAVA CONCEPTS DEMONSTRATED:
 * 1. Classes and Objects: Models academic outcome of a student.
 * 2. Static Members: Static constants (MAX_MARKS, PASSING_MARK) and static utility methods.
 * 3. Method Overloading: calculateGrade(percentage) vs calculateGrade(m1, m2, m3).
 * 4. Access Specifiers: 'private', 'public', and default (package-private).
 * 5. Constructors: Default and Parameterized.
 * ============================================================================
 */
public class Result {
    // -------------------------------------------------------------
    // STATIC MEMBERS: Belong to the class itself, shared across all objects
    // -------------------------------------------------------------
    public static final double MAX_MARKS_PER_SUBJECT = 100.0;
    public static final int TOTAL_SUBJECTS = 3;
    public static final double TOTAL_MAX_MARKS = MAX_MARKS_PER_SUBJECT * TOTAL_SUBJECTS; // 300.0
    public static final double PASS_PERCENTAGE = 40.0;
    public static final double PASS_SUBJECT_MARK = 40.0;

    // -------------------------------------------------------------
    // ACCESS SPECIFIER: 'private' (Encapsulation)
    // -------------------------------------------------------------
    private double totalMarks;
    private double percentage;
    private String grade;
    private boolean isPassed;

    // -------------------------------------------------------------
    // ACCESS SPECIFIER: Default / Package-Private (No access modifier keyword)
    // Accessible only within classes inside the 'model' package.
    // -------------------------------------------------------------
    String evaluationNote;

    // Default Constructor
    public Result() {
        this.totalMarks = 0.0;
        this.percentage = 0.0;
        this.grade = "N/A";
        this.isPassed = false;
        this.evaluationNote = "Not Evaluated";
    }

    // Parameterized Constructor
    public Result(double m1, double m2, double m3) {
        computeResult(m1, m2, m3);
    }

    // Core calculation logic
    public void computeResult(double m1, double m2, double m3) {
        this.totalMarks = m1 + m2 + m3;
        this.percentage = (this.totalMarks / TOTAL_MAX_MARKS) * 100.0;
        
        // Pass condition: At least 40% in each subject and overall >= 40%
        if (m1 >= PASS_SUBJECT_MARK && m2 >= PASS_SUBJECT_MARK && m3 >= PASS_SUBJECT_MARK && this.percentage >= PASS_PERCENTAGE) {
            this.isPassed = true;
            this.grade = calculateGrade(this.percentage);
            this.evaluationNote = "Successfully Cleared";
        } else {
            this.isPassed = false;
            this.grade = "F";
            this.evaluationNote = "Needs Improvement (Failed)";
        }
    }

    // -------------------------------------------------------------
    // METHOD OVERLOADING: Version 1
    // Calculates grade purely based on percentage.
    // -------------------------------------------------------------
    public static String calculateGrade(double percentage) {
        if (percentage >= 90.0) return "A+";
        if (percentage >= 80.0) return "A";
        if (percentage >= 70.0) return "B";
        if (percentage >= 60.0) return "C";
        if (percentage >= 50.0) return "D";
        if (percentage >= 40.0) return "E";
        return "F";
    }

    // -------------------------------------------------------------
    // METHOD OVERLOADING: Version 2
    // Calculates grade by taking individual subject marks, verifying passing criteria.
    // Demonstrates different parameter list for the same method name.
    // -------------------------------------------------------------
    public static String calculateGrade(double m1, double m2, double m3) {
        if (m1 < PASS_SUBJECT_MARK || m2 < PASS_SUBJECT_MARK || m3 < PASS_SUBJECT_MARK) {
            return "F"; // Immediate fail if any subject is below passing threshold
        }
        double avg = (m1 + m2 + m3) / 3.0;
        return calculateGrade(avg); // Reuses the overloaded percentage-based method
    }

    // Getters
    public double getTotalMarks() {
        return totalMarks;
    }

    public double getPercentage() {
        return percentage;
    }

    public String getGrade() {
        return grade;
    }

    public boolean isPassed() {
        return isPassed;
    }

    public String getStatus() {
        return isPassed ? "PASSED" : "FAILED";
    }

    public String getEvaluationNote() {
        return evaluationNote;
    }
}
