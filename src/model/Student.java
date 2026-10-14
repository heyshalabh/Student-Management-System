package model;

/**
 * ============================================================================
 * JAVA CONCEPTS DEMONSTRATED:
 * 1. Classes and Objects: Models a college Student.
 * 2. Inheritance: 'Student extends Person' (reuses Person's name and email).
 * 3. Constructors & Constructor Chaining: Uses super(...) and this(...).
 * 4. Static Members: collegeName, studentCount, and static counter methods.
 * 5. Access Specifiers:
 *      - private: fields encapsulated inside Student.
 *      - protected: inherited access to Person's fields.
 *      - default: package-private batchId.
 *      - public: methods exposed for external packages.
 * ============================================================================
 */
public class Student extends Person {

    // -------------------------------------------------------------
    // STATIC MEMBERS: Shared by all Student instances
    // -------------------------------------------------------------
    public static final String COLLEGE_NAME = "MATS UNIVERSITY RAIPUR";
    private static int totalStudentsCount = 0;

    // -------------------------------------------------------------
    // ACCESS SPECIFIER: 'private' (Encapsulated instance variables)
    // -------------------------------------------------------------
    private String studentId;
    private String course;
    private int semester;
    private double subject1Marks;
    private double subject2Marks;
    private double subject3Marks;
    private Result result;

    // -------------------------------------------------------------
    // ACCESS SPECIFIER: Default / Package-Private
    // Visible only within the 'model' package.
    // -------------------------------------------------------------
    String batchTag;

    // -------------------------------------------------------------
    // CONSTRUCTOR 1: Default Constructor
    // -------------------------------------------------------------
    public Student() {
        super(); // Calls Person default constructor
        this.studentId = "STU-000";
        this.course = "General";
        this.semester = 1;
        this.subject1Marks = 0.0;
        this.subject2Marks = 0.0;
        this.subject3Marks = 0.0;
        this.result = new Result(0, 0, 0);
        this.batchTag = "BATCH-REGULAR";
        totalStudentsCount++;
    }

    // -------------------------------------------------------------
    // CONSTRUCTOR 2: Parameterized Constructor without marks
    // Chained using this(...)
    // -------------------------------------------------------------
    public Student(String studentId, String name, String email, String course, int semester) {
        this(studentId, name, email, course, semester, 0.0, 0.0, 0.0);
    }

    // -------------------------------------------------------------
    // CONSTRUCTOR 3: Full Parameterized Constructor
    // Invokes super(...) for Person fields
    // -------------------------------------------------------------
    public Student(String studentId, String name, String email, String course, int semester,
                   double sub1, double sub2, double sub3) {
        super(name, email); // Demonstrates super keyword for Inheritance
        this.studentId = studentId;
        this.course = course;
        this.semester = semester;
        this.subject1Marks = sub1;
        this.subject2Marks = sub2;
        this.subject3Marks = sub3;
        this.batchTag = "BATCH-" + course.toUpperCase();
        this.result = new Result(sub1, sub2, sub3);
        totalStudentsCount++;
    }

    // -------------------------------------------------------------
    // STATIC METHODS
    // -------------------------------------------------------------
    public static int getTotalStudentsCount() {
        return totalStudentsCount;
    }

    public static void setTotalStudentsCount(int count) {
        totalStudentsCount = count;
    }

    // -------------------------------------------------------------
    // GETTERS AND SETTERS
    // -------------------------------------------------------------
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
        this.batchTag = "BATCH-" + course.toUpperCase();
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public double getSubject1Marks() {
        return subject1Marks;
    }

    public double getSubject2Marks() {
        return subject2Marks;
    }

    public double getSubject3Marks() {
        return subject3Marks;
    }

    public Result getResult() {
        return result;
    }

    public void setMarks(double sub1, double sub2, double sub3) {
        this.subject1Marks = sub1;
        this.subject2Marks = sub2;
        this.subject3Marks = sub3;
        this.result.computeResult(sub1, sub2, sub3);
    }

    public void recalculateResult() {
        this.result.computeResult(this.subject1Marks, this.subject2Marks, this.subject3Marks);
    }

    // -------------------------------------------------------------
    // METHOD OVERRIDING (Polymorphism & Inheritance)
    // Overrides displayDetails() declared in Person
    // -------------------------------------------------------------
    @Override
    public void displayDetails() {
        // Demonstrates direct access to protected member 'name' from Person
        System.out.println("ID        : " + studentId);
        System.out.println("Name      : " + this.name);
        System.out.println("Email     : " + this.email);
        System.out.println("Course    : " + course + " (Sem: " + semester + ")");
        System.out.println("Marks     : [" + subject1Marks + ", " + subject2Marks + ", " + subject3Marks + "]");
        System.out.println("Total     : " + result.getTotalMarks() + " / 300");
        System.out.printf("Percent   : %.2f%%\n", result.getPercentage());
        System.out.println("Grade     : " + result.getGrade() + " (" + result.getStatus() + ")");
    }

    // -------------------------------------------------------------
    // FILE PERSISTENCE HELPERS (CSV format)
    // -------------------------------------------------------------
    public String toCsvString() {
        return String.join(",",
                studentId,
                escapeCsv(name),
                escapeCsv(email),
                escapeCsv(course),
                String.valueOf(semester),
                String.valueOf(subject1Marks),
                String.valueOf(subject2Marks),
                String.valueOf(subject3Marks)
        );
    }

    public static Student fromCsvString(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        String[] tokens = line.split(",", -1);
        if (tokens.length < 8) {
            return null;
        }
        try {
            String id = tokens[0].trim();
            String name = unescapeCsv(tokens[1].trim());
            String email = unescapeCsv(tokens[2].trim());
            String course = unescapeCsv(tokens[3].trim());
            int sem = Integer.parseInt(tokens[4].trim());
            double m1 = Double.parseDouble(tokens[5].trim());
            double m2 = Double.parseDouble(tokens[6].trim());
            double m3 = Double.parseDouble(tokens[7].trim());

            return new Student(id, name, email, course, sem, m1, m2, m3);
        } catch (Exception e) {
            System.err.println("Error parsing student record: " + line);
            return null;
        }
    }

    private static String escapeCsv(String str) {
        if (str == null) return "";
        return str.replace(",", ";"); // simple sanitizer
    }

    private static String unescapeCsv(String str) {
        if (str == null) return "";
        return str.replace(";", ",");
    }
}
