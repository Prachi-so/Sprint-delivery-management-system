package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

	private final String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;

    public String saveFile(MultipartFile file) throws IOException {

        File folder = new File(uploadDir);

        if (!folder.exists()) {
            folder.mkdirs();   
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        String filePath = uploadDir + fileName;

        file.transferTo(new File(filePath));

       
        return "uploads/" + fileName;
    }
}
