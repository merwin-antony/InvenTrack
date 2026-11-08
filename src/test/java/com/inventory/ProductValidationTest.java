package com.inventory;

import com.inventory.model.Product;
import com.inventory.service.ProductService;
import com.inventory.service.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Product model calculations and ProductService validation rules.
 */
public class ProductValidationTest {

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService();
    }

    @Test
    @DisplayName("Product total value calculation: price * quantity using BigDecimal")
    void testTotalValueCalculation() {
        Product p1 = new Product("Mechanical Keyboard", "Electronics", 10, new BigDecimal("89.99"));
        assertEquals(new BigDecimal("899.90"), p1.getTotalValue());

        Product p2 = new Product("Mouse Pad", "Accessories", 0, new BigDecimal("15.50"));
        assertEquals(new BigDecimal("0.00"), p2.getTotalValue());

        Product p3 = new Product("Free Sticker", "Promotional", 5, BigDecimal.ZERO);
        assertEquals(new BigDecimal("0.00"), p3.getTotalValue());
    }

    @Test
    @DisplayName("Low stock and out-of-stock indicators")
    void testStockThresholds() {
        Product inStock = new Product("USB Cable", "Accessories", 25, new BigDecimal("4.99"));
        assertFalse(inStock.isOutOfStock());
        assertFalse(inStock.isLowStock(5));

        Product lowStock = new Product("HDMI Adapter", "Accessories", 3, new BigDecimal("12.50"));
        assertFalse(lowStock.isOutOfStock());
        assertTrue(lowStock.isLowStock(5));

        Product outOfStock = new Product("Monitor Stand", "Hardware", 0, new BigDecimal("45.00"));
        assertTrue(outOfStock.isOutOfStock());
        assertFalse(outOfStock.isLowStock(5)); // 0 is considered out-of-stock
    }

    @Test
    @DisplayName("Validation rejects null or empty product name")
    void testValidateEmptyName() {
        Product nullName = new Product(null, "Hardware", 5, new BigDecimal("10.00"));
        assertThrows(ValidationException.class, () -> productService.validateProduct(nullName));

        Product blankName = new Product("   ", "Hardware", 5, new BigDecimal("10.00"));
        assertThrows(ValidationException.class, () -> productService.validateProduct(blankName));
    }

    @Test
    @DisplayName("Validation rejects negative quantity")
    void testValidateNegativeQuantity() {
        Product product = new Product("Test Product", "General", 0, new BigDecimal("10.00"));
        product.setQuantity(-5); // Force negative quantity
        assertThrows(ValidationException.class, () -> productService.validateProduct(product));
    }

    @Test
    @DisplayName("Validation rejects negative price")
    void testValidateNegativePrice() {
        Product product = new Product("Test Product", "General", 5, new BigDecimal("-1.50"));
        assertThrows(ValidationException.class, () -> productService.validateProduct(product));
    }

    @Test
    @DisplayName("Validation accepts valid product data")
    void testValidateValidProduct() {
        Product valid = new Product("Gaming Monitor", "Electronics", 15, new BigDecimal("249.99"));
        assertDoesNotThrow(() -> productService.validateProduct(valid));
    }
}
