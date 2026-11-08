package com.inventory.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Product Entity POJO (Plain Old Java Object)
 * Represents a row in the 'products' table of 'inventory_db'.
 * 
 * Schema:
 * - product_id INT AUTO_INCREMENT PRIMARY KEY
 * - product_name VARCHAR(100) NOT NULL
 * - category VARCHAR(50)
 * - quantity INT NOT NULL DEFAULT 0
 * - price DECIMAL(10,2) NOT NULL
 */
public class Product {

    private Integer productId;
    private String productName;
    private String category;
    private int quantity;
    private BigDecimal price;

    public Product() {
        this.quantity = 0;
        this.price = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Constructor for creating a new product before saving to the database (no ID yet).
     */
    public Product(String productName, String category, int quantity, BigDecimal price) {
        this(null, productName, category, quantity, price);
    }

    /**
     * Full constructor including productId (for records retrieved from the database).
     */
    public Product(Integer productId, String productName, String category, int quantity, BigDecimal price) {
        this.productId = productId;
        this.productName = productName != null ? productName.trim() : "";
        this.category = category != null ? category.trim() : "General";
        this.quantity = quantity;
        this.price = price != null ? price.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }

    // Getters and Setters

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName != null ? productName.trim() : "";
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category != null ? category.trim() : "General";
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price != null ? price.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calculates total inventory value for this product: price * quantity.
     * Uses BigDecimal for accurate financial computation without floating-point precision loss.
     */
    public BigDecimal getTotalValue() {
        if (price == null || quantity <= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return price.multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Checks if this product is out of stock.
     */
    public boolean isOutOfStock() {
        return quantity <= 0;
    }

    /**
     * Checks if this product is low in stock based on a given threshold.
     */
    public boolean isLowStock(int threshold) {
        return quantity > 0 && quantity <= threshold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(productId, product.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + productId +
                ", name='" + productName + '\'' +
                ", category='" + category + '\'' +
                ", qty=" + quantity +
                ", price=" + price +
                '}';
    }
}
