package com.example.textilproject.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "files")
public class ImageFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id" ,unique = true , nullable = false)
    private Long id;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "created_at" ,updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
