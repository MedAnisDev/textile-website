package com.example.textilproject.DTO.product;

import com.example.textilproject.model.ImageFile;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class FileDTOMapper implements Function<ImageFile ,FileDTO > {

    @Override
    public FileDTO apply(ImageFile imageFile) {
        return new FileDTO(
              imageFile.getName(),
                imageFile.getType(),
                imageFile.getFileUrl(),
                imageFile.getCreatedAt()
        );
    }
}