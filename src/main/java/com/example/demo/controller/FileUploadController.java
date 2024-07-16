package com.example.demo.controller;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

// import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class FileUploadController {
    
    private static final String UPLOAD_DIR = "uploads/";

    @GetMapping("/")
    public String index() {
        return "upload";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("path") String path, @RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a file to upload.");
            return "upload";
        }

        try {
            // Determine the path to the upload directory provided by the user
            Path uploadPath = Paths.get(UPLOAD_DIR+path);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Save the file to the user-specified upload directory
            Path filePath = uploadPath.resolve(file.getOriginalFilename());
            Files.write(filePath, file.getBytes());

            // Extract text from the uploaded PDF file
            PDDocument document = PDDocument.load(filePath.toFile());
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            document.close();

            // Add the extracted text to the model
            model.addAttribute("message", "File uploaded successfully!");
            model.addAttribute("text", text);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "An error occurred while uploading the file: " + e.getMessage());
        }

        return "upload";
    }
}
