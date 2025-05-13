package com.example.ReviewEngine.ai;

import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import com.anthropic.models.messages.Message;
import com.anthropic.models.messages.MessageCreateParams;
import com.anthropic.models.messages.Model;
import com.anthropic.models.messages.TextBlock;
import com.example.ReviewEngine.model.Review;
import com.example.ReviewEngine.model.Product;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AIClient {

    private final AnthropicClient client = AnthropicOkHttpClient.fromEnv();
    private final ReviewParser reviewParser;

    public AIClient(ReviewParser reviewParser) {
        this.reviewParser = reviewParser;
    }

    public Review generateReview(Product product) {

        MessageCreateParams params = MessageCreateParams.builder()
                .model(Model.CLAUDE_3_HAIKU_20240307)
                .maxTokens(100)
                .temperature(1.0)
                .system("""
                    Du ska skriva en kort recension av en produkt. Generera följande i exakt detta format:
                    REVIEW: [En recension på max 1 mening]
                    WRITER: [Ett påhittat namn på recensenten]
                    RATING: [Ett heltal mellan 1-5]
                    VIKTIGT: Följ exakt det här formatet med rubrikerna i versaler följt av kolon och svaret. Inkludera inga andra förklaringar eller text.
                    """)
                .addUserMessage("Produkt: " + product.getName() +
                        ", Kategori: " + product.getCategory() +
                        ", Taggar: " +   product.getTags())
                .build();

        try {
            Message message = client.messages().create(params);
            Optional<TextBlock> textBlockOptional = message.content().getFirst().text();

            if (textBlockOptional.isPresent()) {
                String responseText = textBlockOptional.get().text();
                return reviewParser.parse(responseText, product);
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