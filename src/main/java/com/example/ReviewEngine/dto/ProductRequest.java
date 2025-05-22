package com.example.ReviewEngine.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

public class ProductRequest {

    @NotBlank(message = "Product name is required")
    @Schema(description = "Enter product name", example = "iphone 15")
    private String name;

    @NotBlank(message = "A category for the product is required")
    @Schema(description = "Enter category", example = "Phones")
    private String category;

    @Schema(description = "Enter tags", example = "[\"Smartphone\", \"Apple\"]")
    private List<String> tags = new ArrayList<>();

    @Schema(description = "Enter customer id", example = "1")
    private Long customerId;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
