package model;

public class Order {
    private int id;
    private String clientEmail;
    private String productName;
    private int quantity;

    public Order() {}

    public Order(int id, String clientEmail, String productName, int quantity) {
        this.id = id;
        this.clientEmail = clientEmail;
        this.productName = productName;
        this.quantity = quantity;
    }

    public Order(String clientEmail, String productName, int quantity) {
        this.clientEmail = clientEmail;
        this.productName = productName;
        this.quantity = quantity;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getClientEmail() { return clientEmail; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }

}
