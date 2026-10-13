import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * JdbcDemo.java
 *
 * Direct implementation of the step-by-step lab demonstration from:
 * "JDBC ( Java Database Connectivity ).pdf"
 *
 * Demonstrates:
 * - Step 7: Load Driver (Class.forName)
 * - Step 8: Make Connection (DriverManager.getConnection)
 * - Step 9: Create PreparedStatement ("insert into student values(?,?,?)")
 * - Step 10: Set Values (ps.setInt, ps.setString)
 * - Step 11: Execute Update (ps.executeUpdate)
 * - Step 12: Print confirmation
 * - Step 13: Create Statement (con.createStatement)
 * - Step 14: Execute Query ("select * from student")
 * - Step 15-16: Read ResultSet cursor (rs.next, rs.getInt, rs.getString)
 * - Step 17: Display formatted output
 * - Step 18: Close connection (con.close)
 */
public class JdbcDemo {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  Professor PDF Lab Demo: JdbcDemo.java");
        System.out.println("  Reference: 'JDBC ( Java Database Connectivity ).pdf'");
        System.out.println("==================================================");

        // Step 7: Load Driver into JVM Memory
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Step 7: MySQL JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver class not found. Ensure mysql-connector-j.jar is in classpath.");
            return;
        }

        // Resolving connection URL and credentials (with environment overrides)
        String dbUrl = System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/college?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
        String dbUser = System.getenv().getOrDefault("DB_USER", "root");
        String envPass = System.getenv("DB_PASSWORD");
        String dbPass = (envPass != null) ? envPass : "";

        Connection con = null;
        PreparedStatement ps = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            // Step 8: Make Connection
            System.out.println("Step 8: Connecting to " + dbUrl + " as user '" + dbUser + "'...");
            try {
                con = DriverManager.getConnection(dbUrl, dbUser, dbPass);
            } catch (SQLException e) {
                if (envPass == null && "28000".equals(e.getSQLState())) {
                    // Try fallback password "root" as specified in PDF
                    con = DriverManager.getConnection(dbUrl, dbUser, "root");
                } else {
                    throw e;
                }
            }
            System.out.println("Connection opened successfully.");

            // Ensure table exists in 'college' database if running fresh
            try (Statement setupStmt = con.createStatement()) {
                setupStmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS student(
                        id INT PRIMARY KEY,
                        name VARCHAR(30),
                        branch VARCHAR(20)
                    )
                """);
            }

            // Step 9: Create PreparedStatement with placeholders (?)
            String insertSql = "INSERT INTO student (id, name, branch) VALUES (?, ?, ?) " +
                               "ON DUPLICATE KEY UPDATE name=VALUES(name), branch=VALUES(branch)";
            ps = con.prepareStatement(insertSql);

            // Step 10: Set Values
            ps.setInt(1, 101);
            ps.setString(2, "Rahul");
            ps.setString(3, "CSE");

            // Step 11: Execute Update
            ps.executeUpdate();

            // Step 12: Print status
            System.out.println("Step 11-12: Record Inserted / Updated (101, Rahul, CSE)");

            // Step 13: Create Statement for querying
            st = con.createStatement();

            // Step 14: Execute Query
            rs = st.executeQuery("SELECT * FROM student");

            // Step 15-16: Read ResultSet row by row
            System.out.println("\n--- Query Output (select * from student) ---");
            while (rs.next()) {
                // Column index based retrieval (Step 16)
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String branch = rs.getString(3);

                // Step 17: Output
                System.out.println(id + " " + name + " " + branch);
            }

        } catch (SQLException e) {
            System.err.println("\n[SQL Exception Occurred]");
            System.err.println("Message: " + e.getMessage());
            System.err.println("SQLState: " + e.getSQLState());
        } finally {
            // Step 18: Close Connection and resources
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (ps != null) ps.close();
                if (con != null) {
                    con.close();
                    System.out.println("\nStep 18: Database Connection closed successfully.");
                }
            } catch (SQLException ex) {
                System.err.println("Error closing resources: " + ex.getMessage());
            }
        }
    }
}
