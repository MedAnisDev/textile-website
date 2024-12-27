package com.example.textilproject.service.file;

import com.example.textilproject.exceptions.custom.ResourceNotFoundCustomException;
import com.example.textilproject.model.ImageFile;
import com.example.textilproject.model.Product;
import com.example.textilproject.repository.ImageFileRepository;
import com.example.textilproject.repository.ProductRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImagesFileServiceImpl implements ImagesFileService {

    private final ImageFileRepository imageFileRepository ;
    private final ProductRepository productRepository ;

    private final String  FILE_PATH= Paths.get("").toAbsolutePath().resolve("src").resolve("main").resolve("resources").resolve("static") +"/";

    public ImagesFileServiceImpl(ImageFileRepository imageFileRepository, ProductRepository productRepository) {
        this.imageFileRepository = imageFileRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<ImageFile> uploadMultipleFiles(List<MultipartFile> files , final Product currentProduct) throws IOException {
        List<ImageFile> productFiles = new ArrayList<>() ;
        for(var file : files ){
            ImageFile currentFile = handleFile(file , currentProduct) ;
            productFiles.add(currentFile ) ;
        }
        return productFiles ;
    }

    @Override
    public ImageFile handleFile(final MultipartFile file  , final Product currentProduct) throws IOException {

        String originalFileName = file.getOriginalFilename();
        String fileName = originalFileName.substring(0, originalFileName.indexOf('.'));
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        String fullName = fileName + System.currentTimeMillis() + extension ;

        String path = FILE_PATH + fileName + System.currentTimeMillis() + extension;

        //saving file
        final ImageFile imageFile = ImageFile.builder()
                .name(fullName)
                .type(file.getContentType())
                .fileUrl(path)
                .createdAt(LocalDateTime.now())
                .product(currentProduct)
                .build();
        imageFileRepository.save(imageFile);
        file.transferTo(new File(path));

        return imageFile;
    }
    @Override
    public ImageFile uploadFile(final MultipartFile file ) throws IOException {

        String originalFileName = file.getOriginalFilename();
        String fileName = originalFileName.substring(0, originalFileName.indexOf('.'));
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        String fullName = fileName + System.currentTimeMillis() + extension ;

        String path = FILE_PATH + fileName + System.currentTimeMillis() + extension;

        //saving file
        Product prod = Product.builder()
                .category("aaa")
                .name("prod")
                .price(4875)
                .description("asasasasasa")
                .build();
        productRepository.save(prod);
        final ImageFile imageFile = ImageFile.builder()
                .name(fullName)
                .type(file.getContentType())
                .fileUrl(path)
                .createdAt(LocalDateTime.now())
                .product(prod)
                .build();
        imageFileRepository.save(imageFile);
        file.transferTo(new File(path));

        return imageFile;
    }
    @Override
    public ImageFile getFileById(@NotNull final Long fileId) {
        return imageFileRepository.findById(fileId)
                .orElseThrow(()-> new ResourceNotFoundCustomException("File not found "));
    }


    @Override
    public void deleteImagesFromSystem(@NotNull final List<ImageFile> productImages)throws IOException {

        for(ImageFile imageToDelete : productImages){
            //delete file metadata from database
            imageFileRepository.delete(imageToDelete);
            //delete file from server using NIO
            Path imagePath = Paths.get(imageToDelete.getFileUrl()) ;
            Files.delete(imagePath);
        }
    }

}
