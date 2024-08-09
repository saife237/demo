package com.example.demo.controller;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api") // Prefix all routes with /api
public class FileUploadController {

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("path") String path, @RequestParam("files") MultipartFile[] files) {
        if (files.length == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please select files to upload.");
        }

        try {
            Path uploadPath = Paths.get(path);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            StringBuilder extractedText = new StringBuilder();

            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    continue; // Skip empty files
                }

                Path filePath = uploadPath.resolve(file.getOriginalFilename());
                Files.write(filePath, file.getBytes());

                PDDocument document = PDDocument.load(filePath.toFile());
                PDFTextStripper pdfStripper = new PDFTextStripper();
                String text = pdfStripper.getText(document);
                document.close();

                extractedText.append("File: ").append(file.getOriginalFilename()).append("\n")
                        .append(text).append("\n\n");
            }

            return ResponseEntity.ok(extractedText.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while uploading the files: " + e.getMessage());
        }
    }
}
