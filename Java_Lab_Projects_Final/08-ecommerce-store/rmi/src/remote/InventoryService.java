package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * InventoryService: Remote Interface for ShopSphere Distributed Inventory Management.
 * Extends java.rmi.Remote, all methods declare throws RemoteException.
 */
public interface InventoryService extends Remote {

    /**
     * Retrieve a product by ID.
     */
    ProductDTO getProduct(int productId) throws RemoteException;

    /**
     * Get real-time stock level for a product.
     */
    int getStock(int productId) throws RemoteException;

    /**
     * Update product stock quantity.
     * Returns true if successful.
     */
    boolean updateStock(int productId, int quantity) throws RemoteException;

    /**
     * Retrieve all products from remote catalog.
     */
    List<ProductDTO> getAllProducts() throws RemoteException;
}
