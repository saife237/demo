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
// import java.util.List;
// import java.util.stream.Collectors;

@Controller
public class FileUploadController {

    // private static final String UPLOAD_DIR = "main/resources";


    @GetMapping("/")
    public String index() {
        return "upload";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("path") String path, @RequestParam("files") MultipartFile[] files, Model model) {
        if (files.length == 0) {
            model.addAttribute("message", "Please select files to upload.");
            return "upload";
        }

        try {
            // Determine the path to the upload directory provided by the user
            Path uploadPath = Paths.get(path);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // StringBuilder to collect extracted text from all PDFs
            StringBuilder extractedText = new StringBuilder();

            // Process each uploaded file
            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    continue; // Skip empty files
                }

                // Save the file to the user-specified upload directory
                Path filePath = uploadPath.resolve(file.getOriginalFilename());
                Files.write(filePath, file.getBytes());

                // Extract text from the uploaded PDF file
                PDDocument document = PDDocument.load(filePath.toFile());
                PDFTextStripper pdfStripper = new PDFTextStripper();
                String text = pdfStripper.getText(document);
                document.close();

                // Append the extracted text to the StringBuilder
                extractedText.append("File: ").append(file.getOriginalFilename()).append("\n")
                        .append(text).append("\n\n");
            }

            // Add the extracted text to the model
            model.addAttribute("message", "Files uploaded successfully!");
            model.addAttribute("text", extractedText.toString());
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "An error occurred while uploading the files: " + e.getMessage());
        }

        return "upload";
    }
}
