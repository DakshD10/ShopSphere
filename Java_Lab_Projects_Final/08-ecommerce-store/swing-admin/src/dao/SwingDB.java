package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SwingDB {
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/shopsphere?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DEFAULT_USER = "shopsphere_user";
    private static final String DEFAULT_PASSWORD = "ShopSphere@2027!";

    private static final java.util.Properties fileProps = new java.util.Properties();

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ignored) {}

        for (String path : new String[]{"db.properties", "../db.properties", "../../db.properties", "src/main/resources/db.properties"}) {
            java.io.File f = new java.io.File(path);
            if (f.exists() && f.isFile()) {
                try (java.io.FileInputStream fis = new java.io.FileInputStream(f)) {
                    fileProps.load(fis);
                    break;
                } catch (Exception ignored) {}
            }
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = System.getenv("DB_URL");
        if (url == null || url.isEmpty()) url = fileProps.getProperty("db.url", DEFAULT_URL);

        String user = System.getenv("DB_USER");
        if (user == null || user.isEmpty()) user = fileProps.getProperty("db.user", DEFAULT_USER);

        String pass = System.getenv("DB_PASSWORD");
        if (pass == null) pass = fileProps.getProperty("db.password", DEFAULT_PASSWORD);

        return DriverManager.getConnection(url, user, pass);
    }

    public static boolean isConnected() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (Exception e) {
            return false;
        }
    }
}
