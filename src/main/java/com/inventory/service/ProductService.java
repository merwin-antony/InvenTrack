package com.inventory.service;

import com.inventory.config.DatabaseConfig;
import com.inventory.dao.ProductDAO;
import com.inventory.model.InventoryStats;
import com.inventory.model.Product;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for product management.
 * Enforces business logic, data validation, and bridges UI with Data Access Object.
 */
public class ProductService {

    private final ProductDAO productDAO;

    public ProductService() {
        this.productDAO = new ProductDAO();
    }

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public void validateProduct(Product product) throws ValidationException {
        if (product == null) {
            throw new ValidationException("Product data cannot be null.");
        }

        // Validate product name
        if (product.getProductName() == null || product.getProductName().trim().isEmpty()) {
            throw new ValidationException("Product name is required and cannot be empty.");
        }
        if (product.getProductName().trim().length() > 100) {
            throw new ValidationException("Product name exceeds the maximum length of 100 characters.");
        }

        // Validate category
        if (product.getCategory() != null && product.getCategory().trim().length() > 50) {
            throw new ValidationException("Category exceeds the maximum length of 50 characters.");
        }

        // Validate quantity
        if (product.getQuantity() < 0) {
            throw new ValidationException("Quantity cannot be negative (got " + product.getQuantity() + ").");
        }

        // Validate price
        if (product.getPrice() == null) {
            throw new ValidationException("Price is required.");
        }
        if (product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Price cannot be negative (got ₹" + product.getPrice() + ").");
        }
        if (product.getPrice().compareTo(new BigDecimal("99999999.99")) > 0) {
            throw new ValidationException("Price exceeds maximum allowable value (₹99,999,999.99).");
        }
    }

    /**
     * Adds a new product after strict validation.
     */
    public int addProduct(Product product) throws ValidationException, SQLException {
        validateProduct(product);
        return productDAO.addProduct(product);
    }

    /**
     * Updates an existing product after validation.
     */
    public boolean updateProduct(Product product) throws ValidationException, SQLException {
        if (product.getProductId() == null || product.getProductId() <= 0) {
            throw new ValidationException("Cannot update product: Invalid Product ID.");
        }
        validateProduct(product);
        return productDAO.updateProduct(product);
    }

    /**
     * Deletes a product by ID.
     */
    public boolean deleteProduct(int productId) throws SQLException {
        return productDAO.deleteProduct(productId);
    }

    /**
     * Updates the stock quantity directly with validation.
     */
    public boolean updateStock(int productId, int newQuantity) throws ValidationException, SQLException {
        if (newQuantity < 0) {
            throw new ValidationException("Stock quantity cannot be negative.");
        }
        return productDAO.updateStock(productId, newQuantity);
    }

    /**
     * Adjusts the stock quantity by a delta (e.g., +5 or -2).
     */
    public boolean adjustStock(int productId, int delta) throws ValidationException, SQLException {
        Optional<Product> opt = productDAO.getProductById(productId);
        if (opt.isEmpty()) {
            throw new ValidationException("Product with ID " + productId + " was not found.");
        }
        Product current = opt.get();
        int newQty = current.getQuantity() + delta;
        if (newQty < 0) {
            throw new ValidationException("Cannot reduce stock by " + Math.abs(delta) + ". Only " + current.getQuantity() + " units available.");
        }
        return productDAO.updateStock(productId, newQty);
    }

    public List<Product> getAllProducts() throws SQLException {
        return productDAO.getAllProducts();
    }

    public Optional<Product> getProductById(int productId) throws SQLException {
        return productDAO.getProductById(productId);
    }

    public List<Product> searchProducts(String keyword, String category) throws SQLException {
        return productDAO.searchProducts(keyword, category);
    }

    public List<String> getAllCategories() throws SQLException {
        return productDAO.getAllCategories();
    }

    public List<Product> getLowStockProducts() throws SQLException {
        return productDAO.getLowStockProducts(DatabaseConfig.getLowStockThreshold());
    }

    public InventoryStats getInventoryStats() throws SQLException {
        return productDAO.getInventoryStats(DatabaseConfig.getLowStockThreshold());
    }
}
