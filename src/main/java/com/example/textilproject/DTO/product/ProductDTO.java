package com.example.textilproject.DTO.product;


import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.List;

public record ProductDTO (
    Long id,
    @NotBlank
    String name,

    @NotBlank
    String description,

    @NotBlank
    double price,

    @NotBlank
    String category,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<FileDTO> productImages

){
    public ProductDTO(String name, String description, double price, String category, LocalDateTime createdAt, LocalDateTime updatedAt, List<FileDTO> productImages) {
        this(null, name, description, price, category, createdAt, updatedAt, productImages);
    }
}
