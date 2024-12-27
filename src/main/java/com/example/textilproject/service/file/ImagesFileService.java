package com.example.textilproject.service.file;

import com.example.textilproject.model.ImageFile;
import com.example.textilproject.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImagesFileService {
    List<ImageFile> uploadMultipleFiles(List<MultipartFile> files , final Product currentProduct) throws IOException ;
    ImageFile handleFile(final MultipartFile file  , final Product currentProduct) throws IOException ;
    ImageFile uploadFile(final MultipartFile file ) throws IOException ;
    ImageFile getFileById(final Long fileId);
    void deleteImagesFromSystem(List<ImageFile> productImages ) throws IOException ;
}
