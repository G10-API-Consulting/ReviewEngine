package com.example.ReviewEngine.ai;

import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import com.anthropic.models.messages.Message;
import com.anthropic.models.messages.MessageCreateParams;
import com.anthropic.models.messages.Model;
import org.springframework.stereotype.Component;

@Component
public class AIClient {

    public void generateReview(){
        // Använd från miljövariabeln
        AnthropicClient client = AnthropicOkHttpClient.fromEnv(); // Klienten hämtar nyckeln från miljövariabeln

        // Kontrollera att klienten är korrekt instansierad
        if (client == null) {
            System.out.println("Kunde inte skapa klienten. Kontrollera miljövariabler.");
            return;
        }

        // Skapa parametrar för meddelandet
        MessageCreateParams params = MessageCreateParams.builder()
                .model(Model.CLAUDE_3_HAIKU_20240307)
                .maxTokens(50)
                .temperature(1.0)
                .system("Du är en person som har köpt en produkt")
                .addUserMessage("Skrev ett recension")
                .build();

        // Skicka förfrågan
        try {
            Message message = client.messages().create(params);
            System.out.println(message.content());
        } catch (Exception e) {
            System.out.println("Fel vid meddelande skapande: " + e.getMessage());
        }
    }
}
