package com.example.demo.service;

import java.util.concurrent.CompletionException;

public class OpenAIService {

    private final  openai; // Initialize OpenAI client

    public OpenAIService(String apiKey) {
        this.openai = new OpenAI(apiKey);
    }

    public String askQuestion(String question, String context) throws CompletionException {
        completionResponse completionResponse = openai.completion("Answer the following question about the PDF content:\nQuestion: " + question + "\nContext: " + context, 
                maxTokens=100, // Adjust maximum response length
                n=1, // Number of generated responses
                stop=null, // Define a stop sequence for the generation
                temperature=0.7); // Adjust response creativity
        return completionResponse.getChoices().get(0).getText().trim();
    }
}
