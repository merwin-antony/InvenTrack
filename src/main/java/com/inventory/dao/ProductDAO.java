package com.inventory.dao;

import com.inventory.config.DatabaseConfig;
import com.inventory.model.InventoryStats;
import com.inventory.model.Product;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) for the 'products' table.
 * 
 */
public class ProductDAO {

    /**
     * Inserts a new product into the database.
     * Generates and assigns the auto-incremented product_id back to the Product object.
     *
     * @param product the Product to insert
     * @return the generated product_id
     * @throws SQLException if a database error occurs
     */
    public int addProduct(Product product) throws SQLException {
        String sql = "INSERT INTO products (product_name, category, quantity, price) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getCategory());
            stmt.setInt(3, product.getQuantity());
            stmt.setBigDecimal(4, product.getPrice());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    product.setProductId(generatedId);
                    return generatedId;
                } else {
                    throw new SQLException("Creating product failed, no ID obtained.");
                }
            }
        }
    }

    /**
     * Retrieves all products ordered by product_id ascending (S.No. 1, 2, 3...).
     *
     * @return List of all products
     * @throws SQLException if a database error occurs
     */
    public List<Product> getAllProducts() throws SQLException {
        String sql = "SELECT product_id, product_name, category, quantity, price FROM products ORDER BY product_id ASC";
        List<Product> list = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapRowToProduct(rs));
            }
        }
        return list;
    }

    /**
     * Finds a single product by its primary key ID.
     *
     * @param productId the ID to look up
     * @return Optional containing the Product if found, or empty Optional
     * @throws SQLException if a database error occurs
     */
    public Optional<Product> getProductById(int productId) throws SQLException {
        String sql = "SELECT product_id, product_name, category, quantity, price FROM products WHERE product_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToProduct(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Updates an existing product's details in the database.
     *
     * @param product the Product with updated fields
     * @return true if updated, false if not found
     * @throws SQLException if a database error occurs
     */
    public boolean updateProduct(Product product) throws SQLException {
        String sql = "UPDATE products SET product_name = ?, category = ?, quantity = ?, price = ? WHERE product_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getCategory());
            stmt.setInt(3, product.getQuantity());
            stmt.setBigDecimal(4, product.getPrice());
            stmt.setInt(5, product.getProductId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Updates only the stock quantity of a product.
     *
     * @param productId the ID of the product
     * @param newQuantity the updated stock quantity
     * @return true if updated, false if product not found
     * @throws SQLException if a database error occurs
     */
    public boolean updateStock(int productId, int newQuantity) throws SQLException {
        String sql = "UPDATE products SET quantity = ? WHERE product_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, Math.max(0, newQuantity));
            stmt.setInt(2, productId);

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Deletes a product from the database by its ID.
     *
     * @param productId the ID of the product to delete
     * @return true if deleted, false if not found
     * @throws SQLException if a database error occurs
     */
    public boolean deleteProduct(int productId) throws SQLException {
        String sql = "DELETE FROM products WHERE product_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productId);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Searches and filters products dynamically.
     * Supports searching by product ID (if numeric) or product name (wildcard search),
     * and filtering by category.
     *
     * @param keyword search keyword (matches name or ID)
     * @param category category to filter by (or "All Categories" / null for no filter)
     * @return filtered list of products
     * @throws SQLException if a database error occurs
     */
    public List<Product> searchProducts(String keyword, String category) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT product_id, product_name, category, quantity, price FROM products WHERE 1=1");
        List<Object> params = new ArrayList<>();

        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        boolean hasCategory = category != null && !category.trim().isEmpty() && !category.equalsIgnoreCase("All Categories");

        if (hasKeyword) {
            String trimmed = keyword.trim();
            boolean isNumeric = trimmed.matches("\\d+");
            if (isNumeric) {
                sql.append(" AND (product_id = ? OR LOWER(product_name) LIKE ?)");
                params.add(Integer.parseInt(trimmed));
                params.add("%" + trimmed.toLowerCase() + "%");
            } else {
                sql.append(" AND LOWER(product_name) LIKE ?");
                params.add("%" + trimmed.toLowerCase() + "%");
            }
        }

        if (hasCategory) {
            sql.append(" AND category = ?");
            params.add(category.trim());
        }

        sql.append(" ORDER BY product_id ASC");

        List<Product> results = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                Object p = params.get(i);
                if (p instanceof Integer) {
                    stmt.setInt(i + 1, (Integer) p);
                } else if (p instanceof String) {
                    stmt.setString(i + 1, (String) p);
                }
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRowToProduct(rs));
                }
            }
        }
        return results;
    }

    /**
     * Retrieves all distinct categories currently stored in the products table.
     * Useful for dynamically populating the category filter dropdown and dialog choices.
     *
     * @return sorted list of unique category names
     * @throws SQLException if a database error occurs
     */
    public List<String> getAllCategories() throws SQLException {
        String sql = "SELECT DISTINCT category FROM products WHERE category IS NOT NULL AND category != '' ORDER BY category ASC";
        List<String> categories = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        }
        return categories;
    }

    /**
     * Retrieves products that are at or below the low stock threshold.
     *
     * @param threshold maximum quantity to qualify as low stock
     * @return List of products requiring restocking
     * @throws SQLException if a database error occurs
     */
    public List<Product> getLowStockProducts(int threshold) throws SQLException {
        String sql = "SELECT product_id, product_name, category, quantity, price FROM products WHERE quantity <= ? ORDER BY product_id ASC";
        List<Product> list = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, threshold);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToProduct(rs));
                }
            }
        }
        return list;
    }

    /**
     * Calculates the aggregated dashboard metrics directly from the database using SQL aggregates:
     * - COUNT(product_id)
     * - SUM(quantity)
     * - COUNT(CASE WHEN quantity <= threshold)
     * - SUM(price * quantity)
     *
     * @param threshold low-stock quantity threshold
     * @return InventoryStats summary object
     * @throws SQLException if a database error occurs
     */
    public InventoryStats getInventoryStats(int threshold) throws SQLException {
        String sql = "SELECT " +
                "  COUNT(product_id) AS total_distinct, " +
                "  COALESCE(SUM(quantity), 0) AS total_units, " +
                "  COALESCE(SUM(CASE WHEN quantity <= ? THEN 1 ELSE 0 END), 0) AS low_stock_count, " +
                "  COALESCE(SUM(price * quantity), 0.00) AS total_value " +
                "FROM products";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, threshold);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int distinct = rs.getInt("total_distinct");
                    int totalUnits = rs.getInt("total_units");
                    int lowStock = rs.getInt("low_stock_count");
                    BigDecimal totalVal = rs.getBigDecimal("total_value");

                    return new InventoryStats(distinct, totalUnits, lowStock, totalVal);
                }
            }
        }
        return InventoryStats.empty();
    }

    /**
     * Maps a single row from a ResultSet to a Product instance.
     */
    private Product mapRowToProduct(ResultSet rs) throws SQLException {
        return new Product(
                rs.getInt("product_id"),
                rs.getString("product_name"),
                rs.getString("category"),
                rs.getInt("quantity"),
                rs.getBigDecimal("price")
        );
    }
}
