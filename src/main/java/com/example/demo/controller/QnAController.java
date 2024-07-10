package com.example.demo.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.service.OpenAIService;
import com.example.demo.service.PdfService;

@Controller
public class QnAController {

    @Autowired
    private PdfService pdfService;

    @Autowired
    private OpenAIService openAIService; // Replace with your GenAI service

    @GetMapping("/qna")
    public String getQuestionForm() {
        return "question";
    }

    @PostMapping("/qna")
    public String answerQuestion(@RequestParam String question, Model model) throws IOException {
        String pdfText = pdfService.getPdfText();
        String answer = openAIService.askQuestion(question, pdfText);
        model.addAttribute("question", question);
        model.addAttribute("answer", answer);
        return "question";
    }
}

