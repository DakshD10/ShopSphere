package server;

import model.NetworkProduct;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * InventoryServer: Multi-threaded Java TCP ServerSocket implementation.
 * Listens on port 6060 and handles remote client requests for ShopSphere inventory.
 * Supports commands: GET_PRODUCT, GET_STOCK, UPDATE_STOCK, SEARCH_PRODUCT, LIST_ALL, QUIT.
 * Demonstrates ObjectOutputStream serialization and multi-threaded connection handling.
 */
public class InventoryServer {
    public static final int DEFAULT_PORT = 6060;
    private final int port;
    private final Map<Integer, NetworkProduct> inMemoryCatalog = new ConcurrentHashMap<>();
    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    private volatile boolean running = true;
    private ServerSocket serverSocket;

    public InventoryServer(int port) {
        this.port = port;
        initCatalog();
    }

    private void initCatalog() {
        // First try to load products from MySQL database
        boolean dbLoaded = false;
        String dbUrl = System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/shopsphere?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
        String dbUser = System.getenv().getOrDefault("DB_USER", "shopsphere_user");
        String dbPassword = System.getenv().getOrDefault("DB_PASSWORD", "ShopSphere@2027!");

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT p.product_id, p.name, c.name AS category_name, p.price, p.stock, CONCAT('SKU-', p.product_id) AS sku " +
                     "FROM products p LEFT JOIN categories c ON p.category_id = c.category_id")) {

            while (rs.next()) {
                NetworkProduct np = new NetworkProduct(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("category_name"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        rs.getString("sku")
                );
                inMemoryCatalog.put(np.getProductId(), np);
            }
            if (!inMemoryCatalog.isEmpty()) {
                System.out.println("[InventoryServer] Loaded " + inMemoryCatalog.size() + " products from MySQL database.");
                dbLoaded = true;
            }
        } catch (Exception e) {
            System.out.println("[InventoryServer] Database not reachable (" + e.getMessage() + "), initializing default catalog.");
        }

        if (!dbLoaded) {
            seedFallbackProducts();
        }
    }

    private void seedFallbackProducts() {
        inMemoryCatalog.put(1, new NetworkProduct(1, "Smartphone Pro Max", "Electronics", 999.99, 45, "ELEC-SP-001"));
        inMemoryCatalog.put(2, new NetworkProduct(2, "Wireless Noise-Canceling Headphones", "Electronics", 199.99, 80, "ELEC-HP-002"));
        inMemoryCatalog.put(3, new NetworkProduct(3, "Classic Oxford Cotton Shirt", "Apparel", 49.99, 120, "APP-SH-001"));
        inMemoryCatalog.put(4, new NetworkProduct(4, "Ergonomic Mechanical Keyboard", "Accessories", 129.50, 30, "ACC-KB-001"));
        inMemoryCatalog.put(5, new NetworkProduct(5, "Java Programming: Enterprise Edition", "Books", 59.00, 200, "BK-JAVA-001"));
        System.out.println("[InventoryServer] Seeded " + inMemoryCatalog.size() + " fallback catalog products.");
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("==================================================");
            System.out.println("  ShopSphere TCP Inventory Server Started");
            System.out.println("  Listening on Port: " + port);
            System.out.println("  Available Items in Memory: " + inMemoryCatalog.size());
            System.out.println("==================================================");

            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("[+] Accepted connection from: " + clientSocket.getRemoteSocketAddress());
                    threadPool.execute(new ClientHandler(clientSocket));
                } catch (SocketException se) {
                    if (!running) break;
                }
            }
        } catch (IOException e) {
            System.err.println("[-] Server error: " + e.getMessage());
        } finally {
            stop();
        }
    }

    public void stop() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException ignored) {}
        threadPool.shutdown();
        System.out.println("[*] InventoryServer stopped.");
    }

    private class ClientHandler implements Runnable {
        private final Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                ObjectOutputStream objOut = new ObjectOutputStream(socket.getOutputStream())
            ) {
                writer.println("200 WELCOME ShopSphere Inventory TCP Server v1.0. Type HELP for commands.");

                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) continue;
                    System.out.println("  [" + socket.getRemoteSocketAddress() + "] Command: " + line);

                    String[] parts = line.split("\\s+");
                    String command = parts[0].toUpperCase();

                    if ("QUIT".equals(command) || "EXIT".equals(command)) {
                        writer.println("200 BYE Connection closed.");
                        break;
                    } else if ("HELP".equals(command)) {
                        writer.println("200 COMMANDS: GET_PRODUCT <id> | GET_STOCK <id> | UPDATE_STOCK <id> <qty> | SEARCH_PRODUCT <kw> | LIST_ALL | SERIALIZE_PRODUCT <id> | QUIT");
                    } else if ("GET_STOCK".equals(command)) {
                        handleGetStock(parts, writer);
                    } else if ("GET_PRODUCT".equals(command)) {
                        handleGetProduct(parts, writer);
                    } else if ("SERIALIZE_PRODUCT".equals(command)) {
                        handleSerializeProduct(parts, writer, objOut);
                    } else if ("UPDATE_STOCK".equals(command)) {
                        handleUpdateStock(parts, writer);
                    } else if ("SEARCH_PRODUCT".equals(command)) {
                        handleSearchProduct(parts, line, writer);
                    } else if ("LIST_ALL".equals(command)) {
                        handleListAll(writer);
                    } else {
                        writer.println("400 UNKNOWN_COMMAND Unrecognized command: " + command + ". Type HELP.");
                    }
                }
            } catch (IOException e) {
                System.out.println("[-] Client disconnected: " + socket.getRemoteSocketAddress() + " (" + e.getMessage() + ")");
            } finally {
                try { socket.close(); } catch (IOException ignored) {}
            }
        }

        private void handleGetStock(String[] parts, PrintWriter writer) {
            if (parts.length < 2) {
                writer.println("400 MISSING_PARAM Usage: GET_STOCK <productId>");
                return;
            }
            try {
                int id = Integer.parseInt(parts[1]);
                NetworkProduct p = inMemoryCatalog.get(id);
                if (p != null) {
                    writer.println("200 STOCK productId=" + id + " stock=" + p.getStockQuantity());
                } else {
                    writer.println("404 NOT_FOUND Product " + id + " does not exist.");
                }
            } catch (NumberFormatException e) {
                writer.println("400 INVALID_NUMBER " + parts[1]);
            }
        }

        private void handleGetProduct(String[] parts, PrintWriter writer) {
            if (parts.length < 2) {
                writer.println("400 MISSING_PARAM Usage: GET_PRODUCT <productId>");
                return;
            }
            try {
                int id = Integer.parseInt(parts[1]);
                NetworkProduct p = inMemoryCatalog.get(id);
                if (p != null) {
                    writer.println("200 PRODUCT " + p.toString());
                } else {
                    writer.println("404 NOT_FOUND Product " + id + " does not exist.");
                }
            } catch (NumberFormatException e) {
                writer.println("400 INVALID_NUMBER " + parts[1]);
            }
        }

        private void handleSerializeProduct(String[] parts, PrintWriter writer, ObjectOutputStream objOut) throws IOException {
            if (parts.length < 2) {
                writer.println("400 MISSING_PARAM Usage: SERIALIZE_PRODUCT <productId>");
                return;
            }
            try {
                int id = Integer.parseInt(parts[1]);
                NetworkProduct p = inMemoryCatalog.get(id);
                if (p != null) {
                    writer.println("200 SENDING_OBJECT");
                    objOut.writeObject(p);
                    objOut.flush();
                } else {
                    writer.println("404 NOT_FOUND Product " + id + " does not exist.");
                }
            } catch (NumberFormatException e) {
                writer.println("400 INVALID_NUMBER " + parts[1]);
            }
        }

        private void handleUpdateStock(String[] parts, PrintWriter writer) {
            if (parts.length < 3) {
                writer.println("400 MISSING_PARAM Usage: UPDATE_STOCK <productId> <newQuantity>");
                return;
            }
            try {
                int id = Integer.parseInt(parts[1]);
                int qty = Integer.parseInt(parts[2]);
                NetworkProduct p = inMemoryCatalog.get(id);
                if (p != null) {
                    p.setStockQuantity(qty);
                    writer.println("200 UPDATED productId=" + id + " newStock=" + qty);
                } else {
                    writer.println("404 NOT_FOUND Product " + id + " does not exist.");
                }
            } catch (NumberFormatException e) {
                writer.println("400 INVALID_NUMBER Parameters must be integers.");
            }
        }

        private void handleSearchProduct(String[] parts, String line, PrintWriter writer) {
            if (parts.length < 2) {
                writer.println("400 MISSING_PARAM Usage: SEARCH_PRODUCT <keyword>");
                return;
            }
            String kw = line.substring(parts[0].length()).trim().toLowerCase();
            List<NetworkProduct> matches = new ArrayList<>();
            for (NetworkProduct p : inMemoryCatalog.values()) {
                if (p.getName().toLowerCase().contains(kw) ||
                    (p.getCategory() != null && p.getCategory().toLowerCase().contains(kw))) {
                    matches.add(p);
                }
            }
            writer.println("200 FOUND " + matches.size() + " matches:");
            for (NetworkProduct m : matches) {
                writer.println("  * " + m.toString());
            }
            writer.println("200 END_OF_LIST");
        }

        private void handleListAll(PrintWriter writer) {
            writer.println("200 CATALOG_COUNT " + inMemoryCatalog.size() + " items:");
            for (NetworkProduct p : inMemoryCatalog.values()) {
                writer.println("  - " + p.toString());
            }
            writer.println("200 END_OF_LIST");
        }
    }

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            try { port = Integer.parseInt(args[0]); } catch (Exception ignored) {}
        }
        InventoryServer server = new InventoryServer(port);
        server.start();
    }
}
