package com.shopsphere.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Centralized JDBC Connection provider for ShopSphere.
 * Reads environment variables (DB_URL, DB_USER, DB_PASSWORD) with sensible local defaults.
 */
public final class DB {

    private static final String DEFAULT_URL = 
            "jdbc:mysql://localhost:3306/shopsphere?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DEFAULT_USER = "shopsphere_user";
    private static final String DEFAULT_PASSWORD = "ShopSphere@2027!";
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";

    private static final java.util.Properties fileProps = new java.util.Properties();

    static {
        try {
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            System.err.println("[DB Error] MySQL Connector/J driver class not found in classpath: " + e.getMessage());
        }

        // 1. Try loading from classpath (packaged inside WAR or JAR)
        try (java.io.InputStream in = DB.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                fileProps.load(in);
            }
        } catch (Exception ignored) {}

        // 2. Try loading from local file in working directory if not loaded yet
        if (!fileProps.containsKey("db.password")) {
            for (String path : new String[]{"db.properties", "../db.properties", "src/main/resources/db.properties"}) {
                java.io.File f = new java.io.File(path);
                if (f.exists() && f.isFile()) {
                    try (java.io.FileInputStream fis = new java.io.FileInputStream(f)) {
                        fileProps.load(fis);
                        break;
                    } catch (Exception ignored) {}
                }
            }
        }
    }

    private DB() {}

    public static String getUrl() {
        String env = System.getenv("DB_URL");
        if (env != null && !env.isEmpty()) return env;
        String sys = System.getProperty("db.url");
        if (sys != null && !sys.isEmpty()) return sys;
        return fileProps.getProperty("db.url", DEFAULT_URL);
    }

    public static String getUser() {
        String env = System.getenv("DB_USER");
        if (env != null && !env.isEmpty()) return env;
        String sys = System.getProperty("db.user");
        if (sys != null && !sys.isEmpty()) return sys;
        return fileProps.getProperty("db.user", DEFAULT_USER);
    }

    public static String getPassword() {
        String env = System.getenv("DB_PASSWORD");
        if (env != null) return env;
        String sys = System.getProperty("db.password");
        if (sys != null) return sys;
        return fileProps.getProperty("db.password", DEFAULT_PASSWORD);
    }

    /**
     * Obtains a new JDBC Connection from DriverManager.
     *
     * @return active java.sql.Connection
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        String url = getUrl();
        String user = getUser();
        String password = getPassword();

        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException ex) {
            // If empty password failed with Access Denied (28000) and no explicit DB_PASSWORD was set, try 'root'
            if (System.getenv("DB_PASSWORD") == null && "28000".equals(ex.getSQLState())) {
                try {
                    return DriverManager.getConnection(url, user, "root");
                } catch (SQLException ignored) {
                    // Fall through and throw original exception
                }
            }
            throw ex;
        }
    }

    /**
     * Safely closes one or more JDBC resources without throwing checked exceptions.
     */
    public static void closeQuietly(AutoCloseable... resources) {
        if (resources != null) {
            for (AutoCloseable res : resources) {
                if (res != null) {
                    try {
                        res.close();
                    } catch (Exception ignored) {}
                }
            }
        }
    }
}
