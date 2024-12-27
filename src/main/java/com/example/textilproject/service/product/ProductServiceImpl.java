package com.example.textilproject.service.product;

import com.example.textilproject.DTO.product.ProductDTOMapper;
import com.example.textilproject.model.Product;
import com.example.textilproject.repository.ProductRepository;
import com.example.textilproject.service.file.ImagesFileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.lang.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository ;
    private final ImagesFileService imagesFileService ;

    private final ProductDTOMapper productDTOMapper ;
    public ProductServiceImpl(ProductRepository productRepository, ImagesFileService imagesFileService, ProductDTOMapper productDTOMapper) {
        this.productRepository = productRepository;
        this.imagesFileService = imagesFileService;
        this.productDTOMapper = productDTOMapper;
    }

    @Transactional
    @Override
    public ResponseEntity<Object> createProduct(
            @NonNull final String jsonProduct,
            @NonNull final List<MultipartFile> files
    )throws IOException {
        log.info(files.get(0).getName());

        final Product currentProduct = new ObjectMapper().readValue(jsonProduct , Product.class);
        productRepository.save(currentProduct);

        currentProduct.setImageFiles(imagesFileService.uploadMultipleFiles(files , currentProduct));

        productRepository.save(currentProduct);

        return new ResponseEntity<>(productDTOMapper.apply(currentProduct), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> updateProduct(
            final Long productId ,
            final String jsonProduct ,
            final List<MultipartFile> files
    ) throws IOException{
        final Product currentProduct = getProductById(productId);

        final Product updatedProduct = new ObjectMapper().readValue(jsonProduct , Product.class);


        productRepository.save(currentProduct);

        productRepository.save(currentProduct);
        return new ResponseEntity<>(productDTOMapper.apply(currentProduct) , HttpStatus.OK) ;
    }

    @Transactional
    @Override
    public ResponseEntity<Object> deleteProductById(final Long productId) {
        final Product currentProduct = getProductById(productId) ;

        productRepository.deleteById(productId);
        final String successResponse = String.format("product with ID :  %d deleted successfully", productId);
        return new ResponseEntity<>(successResponse , HttpStatus.OK);
    }

    public Product getProductById(Long productId){
        return productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("product not found"));
    }
}
