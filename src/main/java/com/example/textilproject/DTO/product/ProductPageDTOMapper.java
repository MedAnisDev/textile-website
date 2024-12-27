package com.example.textilproject.DTO.product;

import com.example.textilproject.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ProductPageDTOMapper implements Function<Page<Product>, ProductPageDTO> {

    private final ProductDTOMapper productDTOMapper ;

    public ProductPageDTOMapper(ProductDTOMapper productDTOMapper) {
        this.productDTOMapper = productDTOMapper;
    }

    @Override
    public ProductPageDTO apply(Page<Product> products) {
        return new ProductPageDTO(
                products.getContent().stream().map(productDTOMapper).toList(),
                products.getNumber(),
                products.getSize(),
                products.getTotalElements(),
                products.getTotalPages(),
                products.hasNext(),
                products.hasPrevious()
        );
    }
}
