package com.stock.warbucks.services;

import java.util.Map;

public interface OpenAiService {

    /**
     * Prompt GPT for stock predictions based on given stock data.
     *
     * @param stockData {@link String} json string stock data
     * @return {@link String} GPT response
     */
    String PromptGptForStockPrediction(Map<String, String> stockData);
}
