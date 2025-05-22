package com.example.ReviewEngine.ai;
import com.anthropic.client.AnthropicClient;
import com.anthropic.models.messages.MessageCreateParams;
import com.anthropic.services.blocking.MessageService;
import com.example.ReviewEngine.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.lang.reflect.Field;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AIClientTest {

    @Mock
    private AnthropicClient mockClient;

    @Mock
    private MessageService mockService;

    @InjectMocks
    private AIClient aiClient;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        Field f = AIClient.class.getDeclaredField("client");
        f.setAccessible(true);
        f.set(aiClient, mockClient);
        when(mockClient.messages()).thenReturn(mockService);
    }


    @Test
    void generateReview_returnsNull_onException() {
        when(mockService.create(any(MessageCreateParams.class)))
                .thenThrow(new RuntimeException("boom"));

        String result = aiClient.generateReview(new Product());
        assertThat(result).isNull();
        verify(mockService).create(any(MessageCreateParams.class));
    }
}
