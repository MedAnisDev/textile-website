package com.example.textilproject.service.product;

import com.example.textilproject.DTO.product.ProductDTOMapper;
import com.example.textilproject.DTO.product.ProductPageDTO;
import com.example.textilproject.DTO.product.ProductPageDTOMapper;
import com.example.textilproject.model.ImageFile;
import com.example.textilproject.model.Product;
import com.example.textilproject.repository.ProductRepository;
import com.example.textilproject.service.file.ImagesFileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.lang.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository ;
    private final ImagesFileService imagesFileService ;

    private final ProductDTOMapper productDTOMapper ;
    private final ProductPageDTOMapper productPageDTOMapper;
    public ProductServiceImpl(ProductRepository productRepository, ImagesFileService imagesFileService, ProductDTOMapper productDTOMapper, ProductPageDTOMapper productPageDTOMapper) {
        this.productRepository = productRepository;
        this.imagesFileService = imagesFileService;
        this.productDTOMapper = productDTOMapper;
        this.productPageDTOMapper = productPageDTOMapper;
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

    @Transactional
    @Override
    public ResponseEntity<Object> updateProduct(
            final Long productId ,
            final String jsonProduct ,
            final List<MultipartFile> files
    ) throws IOException{

        final Product currentProduct = getProductById(productId);
        final Product updatedProduct = new ObjectMapper().readValue(jsonProduct , Product.class);

        currentProduct.setName(updatedProduct.getName());
        currentProduct.setUpdatedAt(LocalDateTime.now());
        currentProduct.setDescription(updatedProduct.getDescription());
        currentProduct.setPrice(updatedProduct.getPrice());
        currentProduct.setCategory(updatedProduct.getCategory());

        if(!files.isEmpty()){
            imagesFileService.deleteImagesFromSystem(currentProduct.getImageFiles());
            currentProduct.setImageFiles(imagesFileService.uploadMultipleFiles(files , currentProduct));
        }
        productRepository.save(currentProduct);

        return new ResponseEntity<>(productDTOMapper.apply(currentProduct) , HttpStatus.OK) ;
    }

    @Transactional
    @Override
    public ResponseEntity<Object> deleteProductById(final Long productId) throws IOException {
        final Product currentProduct = getProductById(productId) ;
        List < ImageFile> productImages = currentProduct.getImageFiles() ;

        imagesFileService.deleteImagesFromSystem(productImages);
        productRepository.deleteById(productId);

        final String successResponse = String.format("product with ID :  %d deleted successfully", productId);
        return new ResponseEntity<>(successResponse , HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> fetchProducts(final int pageNumber) {
        Pageable pageable = PageRequest.of(
                pageNumber -1 ,
                10
        );
        Page<Product> productPage  = productRepository.findAll(pageable);
        ProductPageDTO productPageDTO = productPageDTOMapper.apply(productPage);

        return new ResponseEntity<>(productPageDTO , HttpStatus.OK) ;
    }

    @Override
    public ResponseEntity<Object> fetchProductsByCategory(final int pageNumber,final String category) {
        Pageable pageable = PageRequest.of(
                pageNumber -1 ,
                10
        );
        Page<Product> productPage  = productRepository.findAllByCategory(category ,pageable);
        ProductPageDTO productPageDTO = productPageDTOMapper.apply(productPage);

        return new ResponseEntity<>(productPageDTO , HttpStatus.OK) ;
    }

    public Product getProductById(Long productId){
        return productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("product not found"));
    }
}
