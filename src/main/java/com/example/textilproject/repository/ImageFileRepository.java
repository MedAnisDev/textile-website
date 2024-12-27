package com.example.textilproject.repository;

import com.example.textilproject.model.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageFileRepository extends JpaRepository<ImageFile , Long> {
}
