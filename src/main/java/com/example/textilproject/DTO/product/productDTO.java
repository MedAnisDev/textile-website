package com.example.textilproject.DTO.product;


import jakarta.validation.constraints.NotBlank;

public record productDTO (
    Long id,
    @NotBlank
    String name,

    @NotBlank
    String description,

    @NotBlank
    double price,

    @NotBlank
    String category
){}