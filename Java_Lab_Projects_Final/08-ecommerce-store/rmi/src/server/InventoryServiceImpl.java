package server;

import remote.InventoryService;
import remote.ProductDTO;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * InventoryServiceImpl: Remote Object implementation extending UnicastRemoteObject.
 * Manages distributed inventory operations with thread-safe persistence.
 */
public class InventoryServiceImpl extends UnicastRemoteObject implements InventoryService {
    private static final long serialVersionUID = 1L;
    private final Map<Integer, ProductDTO> catalog = new ConcurrentHashMap<>();

    public InventoryServiceImpl() throws RemoteException {
        super();
        initCatalog();
    }

    private void initCatalog() {
        boolean dbLoaded = false;
        String dbUrl = System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/shopsphere?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
        String dbUser = System.getenv().getOrDefault("DB_USER", "shopsphere_user");
        String dbPassword = System.getenv().getOrDefault("DB_PASSWORD", "ShopSphere@2027!");

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT p.product_id, p.name, c.name AS category_name, p.price, p.stock " +
                     "FROM products p LEFT JOIN categories c ON p.category_id = c.category_id")) {

            while (rs.next()) {
                ProductDTO dto = new ProductDTO(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("category_name"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                );
                catalog.put(dto.getProductId(), dto);
            }
            if (!catalog.isEmpty()) {
                System.out.println("[RMI Server] Loaded " + catalog.size() + " products from MySQL.");
                dbLoaded = true;
            }
        } catch (Exception e) {
            System.out.println("[RMI Server] MySQL not reachable (" + e.getMessage() + "), using in-memory inventory.");
        }

        if (!dbLoaded) {
            catalog.put(1, new ProductDTO(1, "Smartphone Pro Max", "Electronics", 999.99, 45));
            catalog.put(2, new ProductDTO(2, "Wireless Noise-Canceling Headphones", "Electronics", 199.99, 80));
            catalog.put(3, new ProductDTO(3, "Classic Oxford Cotton Shirt", "Apparel", 49.99, 120));
            catalog.put(4, new ProductDTO(4, "Ergonomic Mechanical Keyboard", "Accessories", 129.50, 30));
            catalog.put(5, new ProductDTO(5, "Java Programming: Enterprise Edition", "Books", 59.00, 200));
            System.out.println("[RMI Server] Seeded " + catalog.size() + " in-memory products.");
        }
    }

    @Override
    public ProductDTO getProduct(int productId) throws RemoteException {
        System.out.println("[RMI Call] getProduct(productId=" + productId + ") invoked by remote client.");
        return catalog.get(productId);
    }

    @Override
    public int getStock(int productId) throws RemoteException {
        System.out.println("[RMI Call] getStock(productId=" + productId + ") invoked by remote client.");
        ProductDTO p = catalog.get(productId);
        return p != null ? p.getStockQuantity() : -1;
    }

    @Override
    public boolean updateStock(int productId, int quantity) throws RemoteException {
        System.out.println("[RMI Call] updateStock(productId=" + productId + ", quantity=" + quantity + ") invoked by remote client.");
        ProductDTO p = catalog.get(productId);
        if (p != null) {
            p.setStockQuantity(quantity);
            return true;
        }
        return false;
    }

    @Override
    public List<ProductDTO> getAllProducts() throws RemoteException {
        System.out.println("[RMI Call] getAllProducts() invoked by remote client (" + catalog.size() + " returned).");
        return new ArrayList<>(catalog.values());
    }
}
