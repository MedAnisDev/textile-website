package com.example.textilproject.DTO.product;

import java.time.LocalDateTime;

public record FileDTO (
        String fileUrl,
        String name,
        String type,
        LocalDateTime createdAt){

}
