package com.example.ReviewEngine.ai;

import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import com.anthropic.models.messages.Message;
import com.anthropic.models.messages.MessageCreateParams;
import com.anthropic.models.messages.Model;
import com.anthropic.models.messages.TextBlock;
import com.example.ReviewEngine.model.Product;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AIClient {

    private final AnthropicClient client = AnthropicOkHttpClient.fromEnv();

    public String  generateReview(Product product) {

        MessageCreateParams params = MessageCreateParams.builder()
                .model(Model.CLAUDE_3_HAIKU_20240307)
                .maxTokens(400)
                .temperature(1.0)
                .system("""
                        Du ska generera exakt 5 recensioner av en produkt i följande JSON-format:
                        [
                          {
                            "review": "[En recension på max 1 mening]",
                            "writer": "[Ett påhittat namn]",
                            "rating": [Heltal mellan 1 och 5]
                          },
                          ...
                        ]
                       ⚠️ VIKTIGT:
                            - Returnera **enbart** JSON-arrayen – inga förklaringar, ingen inledning, ingen text.
                            - JSON måste vara strikt korrekt (komma mellan fält, citattecken runt strängar, etc).
                            - Undvik svenska tecken som kan sabba JSON-parsning (t.ex. använd \\" istället för ” och ”)""")
                .addUserMessage("Produkt: " + product.getName() +
                        ", Kategori: " + product.getCategory() +
                        ", Taggar: " +   product.getTags())
                .build();

        try {
            Message message = client.messages().create(params);
            Optional<TextBlock> textBlockOptional = message.content().getFirst().text();

            if (textBlockOptional.isPresent()) {
                return textBlockOptional.get().text();
            } else {
                System.out.println("Kunde inte extrahera text från svaret.");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Fel vid meddelande skapande: " + e.getMessage());
            return null;
        }
    }
}