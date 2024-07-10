package com.example.demo.service;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import com.google.api.client.util.Value;

@Service
public class PdfService {

    @Value("${pdf.path}") // Load PDF path from application.properties
    private String pdfPath;

    public String getPdfText() throws IOException {
        PDDocument pdDocument = PDDocument.load(pdfPath);
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(pdDocument);
        pdDocument.close();
        return text;
    }
}
