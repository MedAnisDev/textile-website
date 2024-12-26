package com.example.textilproject.service;

import com.example.textilproject.DTO.productDTO;
import com.example.textilproject.model.Product;
import com.example.textilproject.repository.ProductRepository;
import io.micrometer.common.lang.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository ;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ResponseEntity<Object> createProduct(@NonNull final productDTO productDTO) {
        final Product product = Product.builder()
                .name(productDTO.name())
                .description(productDTO.description())
                .price(productDTO.price())
                .category(productDTO.category())
                .build();
        productRepository.save(product);
        return new ResponseEntity<>("product saved successfully", HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> updateProduct(final Long productId ,@NonNull final productDTO productDTO) {
        final Product currentProduct = getProductById(productId);
        currentProduct.setName(productDTO.name());
        currentProduct.setPrice(productDTO.price());
        currentProduct.setDescription(productDTO.description());
        currentProduct.setCategory(productDTO.category());

        productRepository.save(currentProduct);
        return new ResponseEntity<>("product updated successfully" , HttpStatus.OK) ;
    }

    @Transactional
    @Override
    public ResponseEntity<Object> deleteProductById(Long productId) {
        final Product currentProduct = getProductById(productId) ;

        productRepository.deleteById(productId);
        final String successResponse = String.format("product with ID :  %d deleted successfully", productId);
        return new ResponseEntity<>(successResponse , HttpStatus.OK);
    }

    public Product getProductById(Long productId){
        return productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("product not found"))
    }
}
