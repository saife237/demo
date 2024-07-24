package com.example.demo.controller;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class FileUploadController {

    private final Tika tika = new Tika();

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
            Path uploadPath = Paths.get(path);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            StringBuilder extractedText = new StringBuilder();

            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    continue;
                }

                Path filePath = uploadPath.resolve(file.getOriginalFilename());
                Files.write(filePath, file.getBytes());

                // Extract text using Apache Tika
                String text = tika.parseToString(filePath.toFile());
                
                extractedText.append("File: ").append(file.getOriginalFilename()).append("\n")
                        .append(text).append("\n\n");
            }

            model.addAttribute("message", "Files uploaded successfully!");
            model.addAttribute("text", extractedText.toString());
        } catch (IOException | TikaException e) {
            e.printStackTrace();
            model.addAttribute("message", "An error occurred while uploading the files: " + e.getMessage());
        }

        return "upload";
    }
}
