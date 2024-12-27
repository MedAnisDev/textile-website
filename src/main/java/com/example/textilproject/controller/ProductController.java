package com.example.textilproject.controller;

import com.example.textilproject.model.ImageFile;
import com.example.textilproject.service.file.ImagesFileService;
import com.example.textilproject.service.product.ProductService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    private final ProductService productService;
    private final ImagesFileService imagesFileService ;


    public ProductController(ProductService productService, ImagesFileService imagesFileService) {
        this.productService = productService;
        this.imagesFileService = imagesFileService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createProduct(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("jsonProduct") String jsonProduct
    )throws IOException {
        return productService.createProduct(jsonProduct , files);
    }

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadProduct(
            @NotNull @RequestParam(name = "files") final MultipartFile files
    )throws IOException {
        ImageFile file =imagesFileService.uploadFile(files);
        return new ResponseEntity<>("successfull", HttpStatus.CREATED);
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<Object> updateProduct(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("jsonProduct") String jsonProduct ,
            @PathVariable Long productId
    )throws IOException {
        return productService.updateProduct(productId ,jsonProduct , files);
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<Object> deleteProductById(@PathVariable final Long productId){
        return productService.deleteProductById(productId);
    }

}
