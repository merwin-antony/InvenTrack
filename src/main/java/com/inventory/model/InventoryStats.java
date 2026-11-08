package com.inventory.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Model representing the aggregated inventory metrics displayed on the Dashboard.
 * 
 * Required metrics:
 * 1. Total distinct products count (COUNT(product_id))
 * 2. Total units in stock (SUM(quantity))
 * 3. Low-stock products count (COUNT where quantity <= threshold)
 * 4. Total inventory valuation (SUM(price * quantity))
 */
public class InventoryStats {

    private final int totalDistinctProducts;
    private final int totalUnitsInStock;
    private final int lowStockCount;
    private final BigDecimal totalInventoryValue;

    public InventoryStats(int totalDistinctProducts, int totalUnitsInStock, int lowStockCount, BigDecimal totalInventoryValue) {
        this.totalDistinctProducts = totalDistinctProducts;
        this.totalUnitsInStock = totalUnitsInStock;
        this.lowStockCount = lowStockCount;
        this.totalInventoryValue = totalInventoryValue != null
                ? totalInventoryValue.setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }

    public static InventoryStats empty() {
        return new InventoryStats(0, 0, 0, BigDecimal.ZERO);
    }

    public int getTotalDistinctProducts() {
        return totalDistinctProducts;
    }

    public int getTotalUnitsInStock() {
        return totalUnitsInStock;
    }

    public int getLowStockCount() {
        return lowStockCount;
    }

    public BigDecimal getTotalInventoryValue() {
        return totalInventoryValue;
    }

    @Override
    public String toString() {
        return "InventoryStats{" +
                "distinctProducts=" + totalDistinctProducts +
                ", totalUnits=" + totalUnitsInStock +
                ", lowStockCount=" + lowStockCount +
                ", totalValue=" + totalInventoryValue +
                '}';
    }
}
