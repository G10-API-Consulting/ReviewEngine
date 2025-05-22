package com.example.ReviewEngine.dto;

import jakarta.validation.constraints.NotBlank;

public class ProductIdRequest {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
