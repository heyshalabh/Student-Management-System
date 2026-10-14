package model;

/**
 * ============================================================================
 * JAVA CONCEPT DEMONSTRATED: 
 * 1. Classes and Objects: Represents a generic human being.
 * 2. Inheritance (Base Class): Parent class for Student.
 * 3. Access Specifiers: Demonstrates 'protected' and 'public'.
 * 4. Constructors: Parameterized and Default constructors.
 * ============================================================================
 */
public class Person {
    // -------------------------------------------------------------
    // ACCESS SPECIFIER: 'protected'
    // Can be accessed directly by subclasses (e.g., Student)
    // and classes within the same package ('model').
    // -------------------------------------------------------------
    protected String name;
    protected String email;

    // Default Constructor
    public Person() {
        this.name = "Unknown";
        this.email = "not_provided@example.com";
    }

    // Parameterized Constructor
    public Person(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Getter and Setter methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Base method to be overridden by subclasses (Polymorphism)
    public void displayDetails() {
        System.out.println("Name  : " + name);
        System.out.println("Email : " + email);
    }
}
