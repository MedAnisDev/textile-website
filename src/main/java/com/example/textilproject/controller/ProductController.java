package com.example.textilproject.controller;

import com.example.textilproject.DTO.product.productDTO;
import com.example.textilproject.service.ProductService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createProduct(@NotNull productDTO productDTO){
        return productService.createProduct(productDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateProduct(final Long productId ,@NotNull final productDTO productDTO){
        return productService.updateProduct(productId ,productDTO);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteProductById(final Long productId){
        return productService.deleteProductById(productId);
    }

}
