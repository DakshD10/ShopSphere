package model;

public class AdminProduct {
    private int id;
    private String name;
    private String category;
    private double price;
    private int stock;
    private boolean active;

    public AdminProduct() {}

    public AdminProduct(int id, String name, String category, double price, int stock, boolean active) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.active = active;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
