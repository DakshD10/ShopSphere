import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DB.java
 *
 * Centralized database utility for establishing JDBC connections to MySQL.
 * Loads the Type-4 MySQL Connector/J driver and uses DriverManager with
 * credentials supplied via environment variables or safe local defaults.
 */
public class DB {

    // Default configuration constants (no hardcoded production credentials)
    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_PORT = "3306";
    private static final String DEFAULT_DB = "java_lab";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "";

    // JDBC Driver Class Name for MySQL Connector/J 8.x / 9.x
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";

    static {
        // Step 7 from Lab Manual: Explicitly load JDBC driver into JVM memory
        try {
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            System.err.println("[DB Error] MySQL JDBC Driver not found in classpath!");
            System.err.println("Ensure mysql-connector-j.jar is in the classpath (e.g., in lib/ folder).");
        }
    }

    /**
     * Resolves the active database connection URL.
     * Uses the DB_URL environment variable if set, otherwise falls back to local default.
     */
    public static String getUrl() {
        return System.getenv().getOrDefault(
            "DB_URL",
            "jdbc:mysql://" + DEFAULT_HOST + ":" + DEFAULT_PORT + "/" + DEFAULT_DB +
            "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
        );
    }

    /**
     * Resolves the active database username.
     */
    public static String getUser() {
        return System.getenv().getOrDefault("DB_USER", DEFAULT_USER);
    }

    /**
     * Resolves the active database password.
     */
    public static String getPassword() {
        return System.getenv().getOrDefault("DB_PASSWORD", DEFAULT_PASSWORD);
    }

    /**
     * Opens and returns a new JDBC Connection using DriverManager.
     *
     * @return active java.sql.Connection
     * @throws SQLException if a database access error occurs
     */
    public static Connection connect() throws SQLException {
        try {
            return DriverManager.getConnection(getUrl(), getUser(), getPassword());
        } catch (SQLException e) {
            // If DB_PASSWORD was not explicitly set and empty password failed with Access Denied (28000), try 'root'
            if (System.getenv("DB_PASSWORD") == null && "28000".equals(e.getSQLState())) {
                try {
                    return DriverManager.getConnection(getUrl(), getUser(), "root");
                } catch (SQLException ignored) {
                    // Fall through to throw original exception
                }
            }
            throw e;
        }
    }

    /**
     * Overloaded helper to connect to a specific database (such as 'college' from the professor's PDF).
     *
     * @param dbName Target database schema name
     * @return active java.sql.Connection
     * @throws SQLException if connection fails
     */
    public static Connection connect(String dbName) throws SQLException {
        String baseHost = System.getenv().getOrDefault("DB_HOST", DEFAULT_HOST);
        String basePort = System.getenv().getOrDefault("DB_PORT", DEFAULT_PORT);
        String customUrl = "jdbc:mysql://" + baseHost + ":" + basePort + "/" + dbName +
                           "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        try {
            return DriverManager.getConnection(customUrl, getUser(), getPassword());
        } catch (SQLException e) {
            if (System.getenv("DB_PASSWORD") == null && "28000".equals(e.getSQLState())) {
                try {
                    return DriverManager.getConnection(customUrl, getUser(), "root");
                } catch (SQLException ignored) {
                }
            }
            throw e;
        }
    }

    /**
     * Displays active connection metadata (excluding password for security).
     */
    public static void printConfig() {
        System.out.println("--------------------------------------------------");
        System.out.println("  JDBC Connection Configuration");
        System.out.println("--------------------------------------------------");
        System.out.println("  URL      : " + getUrl());
        System.out.println("  User     : " + getUser());
        System.out.println("  Password : " + (getPassword().isEmpty() ? "(empty/none)" : "********"));
        System.out.println("--------------------------------------------------");
    }
}
