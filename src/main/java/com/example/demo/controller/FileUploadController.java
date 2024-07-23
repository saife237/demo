package com.example.demo.controller;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class FileUploadController {

    @GetMapping("/")
    public String index() {
        return "upload.html";
    }

    @PostMapping("/upload")
    @ResponseBody
    public String uploadFile(@RequestParam("path") String path, @RequestParam("files") MultipartFile[] files) {
        if (files.length == 0) {
            return "Please select files to upload.";
        }

        StringBuilder extractedText = new StringBuilder();
        try {
            Path uploadPath = Paths.get(path);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

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
        } catch (IOException e) {
            e.printStackTrace();
            return "An error occurred while uploading the files: " + e.getMessage();
        }

        return extractedText.toString();
    }
}
