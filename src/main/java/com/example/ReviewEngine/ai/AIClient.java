package com.example.ReviewEngine.ai;

import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import com.anthropic.models.messages.Message;
import com.anthropic.models.messages.MessageCreateParams;
import com.anthropic.models.messages.Model;
import com.anthropic.models.messages.TextBlock;
import com.example.ReviewEngine.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AIClient {

    private final AnthropicClient client = AnthropicOkHttpClient.fromEnv();

    public String  generateReview(Product product, List<String> des) {
        System.out.println("här");
        System.out.println(des);

        StringBuilder descriptions = new StringBuilder();
        for (String description : des) {
            descriptions.append("- ").append(description).append("\n");
        }

        MessageCreateParams params = MessageCreateParams.builder()
                .model(Model.CLAUDE_3_HAIKU_20240307)
                .maxTokens(500)
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
                                    
                                    🧠 Vädret påverkar recensionens ton eller innehåll, du får 5 olika sorters väderförhållanden. Använd de här väderbeskrivningarna som inspiration, en vädertyp per recension.
                                    
                                    Exempel:
                                    - Vid 'clear sky' → recensionen kan vara glad eller positiv.
                                    - Vid 'overcast clouds' → recensionen kan låta trött, neutral eller klagande.
                                    - Vid 'scattered clouds' → recensionen kan vara blandad i ton.
                                    
                                    ⚠️ VIKTIGT:
                                    - Returnera ENDAST en giltig JSON-array – inga förklaringar, ingen inledning.
                                    - JSON måste vara strikt korrekt (komma mellan fält, citattecken runt strängar, etc).
                                    - Undvik svenska citattecken som kan sabba parsning (använd \\" istället för ” och ”).
                                    """)
                .addUserMessage("Produkt: " + product.getName() +
                        ", Kategori: " + product.getCategory() +
                        ", Taggar: " +   product.getTags() +
                        " , Väderförhållanden:" + descriptions)
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