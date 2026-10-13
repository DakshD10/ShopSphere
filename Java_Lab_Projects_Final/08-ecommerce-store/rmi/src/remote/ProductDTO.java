package remote;

import java.io.Serializable;

/**
 * ProductDTO: Data Transfer Object for Distributed Java RMI.
 * Must implement Serializable for Java RMI marshaling / unmarshaling over IIOP/TCP.
 */
public class ProductDTO implements Serializable {
    private static final long serialVersionUID = 20261014L;

    private int productId;
    private String name;
    private String category;
    private double price;
    private int stockQuantity;

    public ProductDTO() {}

    public ProductDTO(int productId, String name, String category, double price, int stockQuantity) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
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

    @Override
    public String toString() {
        return String.format("ProductDTO[ID=%d, '%s', Category='%s', Price=$%.2f, Stock=%d]",
                productId, name, category, price, stockQuantity);
    }
}
