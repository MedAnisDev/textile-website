package com.example.textilproject.service;

import com.example.textilproject.DTO.productDTO;
import org.springframework.http.ResponseEntity;

public interface ProductService {
    ResponseEntity<Object> createProduct(productDTO productDTO);

    ResponseEntity<Object> updateProduct(Long productId ,productDTO productDTO);

    ResponseEntity<Object> deleteProductById(Long productId);
}
