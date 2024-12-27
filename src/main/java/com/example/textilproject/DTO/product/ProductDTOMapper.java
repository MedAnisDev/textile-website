package com.example.textilproject.DTO.product;

import com.example.textilproject.model.Product;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ProductDTOMapper implements Function<Product , ProductDTO> {
    private final FileDTOMapper fileDTOMapper ;

    public ProductDTOMapper(FileDTOMapper fileDTOMapper) {
        this.fileDTOMapper = fileDTOMapper;
    }

    @Override
    public ProductDTO apply(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                product.getCreatedAt(),
                product.getUpdatedAt(),
                product.getImageFiles().stream().map(fileDTOMapper).toList()
        );
    }
}
