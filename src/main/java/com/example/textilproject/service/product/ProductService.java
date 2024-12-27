package com.example.textilproject.service.product;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    ResponseEntity<Object> createProduct(String jsonProduct , List<MultipartFile> files)throws IOException;

    ResponseEntity<Object> updateProduct(Long productId ,String productDTO , List<MultipartFile> files)throws IOException;

    ResponseEntity<Object> deleteProductById(Long productId)throws IOException;
}
