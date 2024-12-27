package com.example.textilproject.DTO.product;

import java.util.List;

public record ProductPageDTO (
    List<ProductDTO> product,
    int page,
    int size,
    long totalItems,
    int totalPages,
    boolean hasNextPage,
    boolean hasPrevPage
){}
