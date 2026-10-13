package model;

import java.io.Serializable;

/**
 * NetworkProduct: Serializable model for Java network socket data transfer.
 * Demonstrates ObjectOutputStream / ObjectInputStream across TCP network.
 */
public class NetworkProduct implements Serializable {
    private static final long serialVersionUID = 1L;

    private int productId;
    private String name;
    private String category;
    private double price;
    private int stockQuantity;
    private String sku;

    public NetworkProduct() {}

    public NetworkProduct(int productId, String name, String category, double price, int stockQuantity, String sku) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.sku = sku;
    }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    @Override
    public String toString() {
        return String.format("[#%d] %s (SKU: %s) | Category: %s | Price: $%.2f | Stock: %d",
                productId, name, sku, category, price, stockQuantity);
    }
}
