import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * Main.java
 *
 * Experiment 7: Conventional Beginner-Level JDBC Console Practical
 * Demonstrates complete CRUD (Create, Read, Update, Delete) operations
 * using PreparedStatement on a MySQL 'students' table.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  Experiment 7: JDBC with MySQL CRUD Demonstration");
        System.out.println("==================================================");

        DB.printConfig();

        // Connect using try-with-resources to ensure connection is safely closed
        try (Connection con = DB.connect()) {
            System.out.println("[SUCCESS] Connected to MySQL database successfully!\n");

            // Ensure the students table exists
            initTable(con);

            try (Scanner scanner = new Scanner(System.in)) {
                boolean running = true;
                while (running) {
                    printMenu();
                    System.out.print("Enter choice (0-5): ");
                    String choice = scanner.nextLine().trim();

                    switch (choice) {
                        case "1" -> handleCreate(con, scanner);
                        case "2" -> listStudents(con);
                        case "3" -> handleUpdate(con, scanner);
                        case "4" -> handleDelete(con, scanner);
                        case "5" -> runAutomatedDemo(con);
                        case "0" -> {
                            System.out.println("Exiting JDBC application. Goodbye!");
                            running = false;
                        }
                        default -> System.out.println("[Warning] Invalid option. Please enter 0 through 5.");
                    }
                }
            }

        } catch (SQLException e) {
            handleSqlException(e);
        }
    }

    /**
     * Prints the interactive console menu.
     */
    private static void printMenu() {
        System.out.println("\n--------------------------------------------------");
        System.out.println("  STUDENT MANAGEMENT MENU (JDBC CRUD)");
        System.out.println("--------------------------------------------------");
        System.out.println("  1. Add Student         (INSERT / CREATE)");
        System.out.println("  2. View All Students   (SELECT / READ)");
        System.out.println("  3. Update Course       (UPDATE)");
        System.out.println("  4. Delete Student      (DELETE)");
        System.out.println("  5. Run Automated Demo  (All CRUD in sequence)");
        System.out.println("  0. Exit");
        System.out.println("--------------------------------------------------");
    }

    /**
     * Ensures the students table is created in the database.
     */
    private static void initTable(Connection con) {
        String createSql = """
            CREATE TABLE IF NOT EXISTS students (
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                email VARCHAR(160) NOT NULL UNIQUE,
                course VARCHAR(100) NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(createSql);
        } catch (SQLException e) {
            System.err.println("[Warning] Table initialization note: " + e.getMessage());
        }
    }

    /**
     * 1. CREATE / INSERT operation using PreparedStatement.
     */
    public static int insertStudent(Connection con, String name, String email, String course) throws SQLException {
        String sql = "INSERT INTO students (name, email, course) VALUES (?, ?, ?)";
        // PreparedStatement precompiles the query and protects against SQL injection
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Bind placeholder parameters (1-indexed)
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, course);

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
            return affectedRows;
        }
    }

    /**
     * 2. READ / SELECT operation using PreparedStatement and ResultSet.
     */
    public static void listStudents(Connection con) throws SQLException {
        String sql = "SELECT id, name, email, course FROM students ORDER BY id ASC";
        System.out.println("\n--- Current Students in Database ---");
        System.out.printf("%-6s | %-20s | %-25s | %-20s%n", "ID", "Name", "Email", "Course");
        System.out.println("-----------------------------------------------------------------------------");

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            int count = 0;
            // Iterate through the rows pointed to by the ResultSet cursor
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String course = rs.getString("course");

                System.out.printf("%-6d | %-20s | %-25s | %-20s%n", id, name, email, course);
                count++;
            }

            if (count == 0) {
                System.out.println("  (No student records found in table)");
            } else {
                System.out.println("-----------------------------------------------------------------------------");
                System.out.println("Total records: " + count);
            }
        }
    }

    /**
     * 3. UPDATE operation using PreparedStatement.
     */
    public static int updateStudentCourse(Connection con, int id, String newCourse) throws SQLException {
        String sql = "UPDATE students SET course = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newCourse);
            ps.setInt(2, id);
            return ps.executeUpdate();
        }
    }

    /**
     * 4. DELETE operation using PreparedStatement.
     */
    public static int deleteStudent(Connection con, int id) throws SQLException {
        String sql = "DELETE FROM students WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }

    // --- Interactive Handlers ---

    private static void handleCreate(Connection con, Scanner scanner) {
        System.out.print("Enter Student Name   : ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Student Email  : ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter Enrolled Course: ");
        String course = scanner.nextLine().trim();

        if (name.isEmpty() || email.isEmpty() || course.isEmpty()) {
            System.out.println("[Error] All fields (name, email, course) are required.");
            return;
        }

        try {
            int newId = insertStudent(con, name, email, course);
            System.out.println("[SUCCESS] Student record inserted! (ID: " + newId + ")");
        } catch (SQLException e) {
            System.err.println("[Error] Failed to insert student: " + e.getMessage());
        }
    }

    private static void handleUpdate(Connection con, Scanner scanner) {
        System.out.print("Enter Student ID to update: ");
        String idStr = scanner.nextLine().trim();
        System.out.print("Enter New Course Name     : ");
        String newCourse = scanner.nextLine().trim();

        try {
            int id = Integer.parseInt(idStr);
            int rows = updateStudentCourse(con, id, newCourse);
            if (rows > 0) {
                System.out.println("[SUCCESS] Student ID " + id + " updated successfully (" + rows + " row affected).");
            } else {
                System.out.println("[Warning] No student found with ID " + id + ".");
            }
        } catch (NumberFormatException e) {
            System.out.println("[Error] Invalid ID format. Must be an integer.");
        } catch (SQLException e) {
            System.err.println("[Error] Failed to update student: " + e.getMessage());
        }
    }

    private static void handleDelete(Connection con, Scanner scanner) {
        System.out.print("Enter Student ID to delete: ");
        String idStr = scanner.nextLine().trim();

        try {
            int id = Integer.parseInt(idStr);
            int rows = deleteStudent(con, id);
            if (rows > 0) {
                System.out.println("[SUCCESS] Student ID " + id + " deleted (" + rows + " row removed).");
            } else {
                System.out.println("[Warning] No student found with ID " + id + ".");
            }
        } catch (NumberFormatException e) {
            System.out.println("[Error] Invalid ID format. Must be an integer.");
        } catch (SQLException e) {
            System.err.println("[Error] Failed to delete student: " + e.getMessage());
        }
    }

    /**
     * 5. Automated full CRUD demonstration.
     * Executes Create, Read, Update, and Delete in order, ideal for viva evaluations.
     */
    private static void runAutomatedDemo(Connection con) {
        System.out.println("\n==================================================");
        System.out.println("  STARTING AUTOMATED CRUD WORKFLOW DEMONSTRATION  ");
        System.out.println("==================================================");

        try {
            // Step A: Insert a test student
            String testEmail = "temp.demo" + System.currentTimeMillis() + "@example.com";
            System.out.println("\n[DEMO 1/4] INSERTING temporary record: 'Demo User' (" + testEmail + ")...");
            int demoId = insertStudent(con, "Demo User", testEmail, "Data Science");
            System.out.println("-> Result: Record inserted with generated ID: " + demoId);

            // Step B: Read / List
            System.out.println("\n[DEMO 2/4] READING all records from database...");
            listStudents(con);

            // Step C: Update
            System.out.println("\n[DEMO 3/4] UPDATING Course of ID " + demoId + " to 'Artificial Intelligence'...");
            int updated = updateStudentCourse(con, demoId, "Artificial Intelligence");
            System.out.println("-> Result: " + updated + " row(s) updated.");

            // Step D: Delete
            System.out.println("\n[DEMO 4/4] DELETING temporary record ID " + demoId + "...");
            int deleted = deleteStudent(con, demoId);
            System.out.println("-> Result: " + deleted + " row(s) deleted.");

            System.out.println("\n[DEMO COMPLETE] Full CRUD cycle executed successfully!");

        } catch (SQLException e) {
            System.err.println("[Demo Error] " + e.getMessage());
        }
    }

    /**
     * Helper to provide student-friendly diagnostics based on common JDBC exceptions.
     */
    private static void handleSqlException(SQLException e) {
        System.err.println("\n[DATABASE ERROR] Connection failed: " + e.getMessage());
        System.err.println("SQLState: " + e.getSQLState() + " | ErrorCode: " + e.getErrorCode());
        System.err.println("\nTroubleshooting tips:");
        System.err.println(" 1. Is MySQL Server running locally? (Check 'brew services list' or MySQL Workbench)");
        System.err.println(" 2. Has the database been created? Run: mysql -u root -p < schema.sql");
        System.err.println(" 3. Are credentials correct? Set environment variables:");
        System.err.println("      export DB_URL=\"jdbc:mysql://localhost:3306/java_lab\"");
        System.err.println("      export DB_USER=\"root\"");
        System.err.println("      export DB_PASSWORD=\"your_actual_password\"");
        System.err.println(" 4. Classpath: Ensure mysql-connector-j.jar is referenced when running.");
    }
}
