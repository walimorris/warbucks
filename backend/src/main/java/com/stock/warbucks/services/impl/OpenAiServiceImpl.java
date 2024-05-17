package com.stock.warbucks.services.impl;

import com.stock.warbucks.configurations.ApplicationPropertiesConfiguration;
import com.stock.warbucks.services.OpenAiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OpenAiServiceImpl implements OpenAiService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenAiServiceImpl.class);

    private final ApplicationPropertiesConfiguration configuration;

    @Autowired
    public OpenAiServiceImpl(ApplicationPropertiesConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String PromptGptForStockPrediction(Map<String, String> stockData) {
        OpenAiChatOptions options = new OpenAiChatOptions.Builder()
                .withModel("gpt-4o")
                .withTemperature(0.4F)
                .withMaxTokens(1000)
                .build();
        OpenAiChatClient client = new OpenAiChatClient(configuration.openAiKey(), options);

        String userContent = """
                Give your predictions for each stock and provide how you would imagine\s
                these stocks will do in the next three years. Your response should include\s
                your opinion if the stock should be bought or sold. You should respond in 
                a professional conversational tone. Use this stock ticker data and mention 
                each stock:
                """ + stockData;

        String systemContent = """
                You are a expert stock analysis.\s
                """;

        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemContent);
        Message userMessage = new UserMessage(userContent);
        Message systemMessage = systemPromptTemplate.createMessage();
        Prompt prompt = new Prompt(List.of(userMessage, systemMessage));

        ChatResponse response = client.call(prompt);
        return response.getResult().getOutput().getContent();
    }
}
