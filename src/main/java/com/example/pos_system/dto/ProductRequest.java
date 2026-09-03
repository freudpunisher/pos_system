package com.example.pos_system.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductRequest {

    @NotBlank(message = "SKU is required")
    @Size(max = 20, message = "SKU cannot exceed 20 characters")
    private String sku;

    @NotBlank(message = "Product name is required")
    @Size(max = 100, message = "Product name cannot exceed 100 characters")
    private String name;

    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than zero")
    private BigDecimal price;

    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantityInStock;

    @Min(value = 0, message = "Reorder level cannot be negative")
    private Integer reorderLevel;

    private Long categoryId;
}