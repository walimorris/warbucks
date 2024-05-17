package com.stock.warbucks.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.warbucks.models.StockTickers;
import com.stock.warbucks.services.impl.OpenAiServiceImpl;
import com.stock.warbucks.services.impl.StockServiceImpl;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/warbucks/api")
public class StockPredictionsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(StockPredictionsController.class);

    private final StockServiceImpl stockService;
    private final OpenAiServiceImpl openAiService;

    public StockPredictionsController(StockServiceImpl stockService, OpenAiServiceImpl openAiService) {
        this.stockService = stockService;
        this.openAiService = openAiService;
    }

    @GetMapping("/predict")
    public ResponseEntity<?> predictStockOptions(@RequestParam List<String> tickers) {
        StockTickers stockTickers = StockTickers.builder()
                .tickerSymbols(tickers)
                .build();

        Map<String, String> tickerInfo = stockService.getStockTickersList(stockTickers);
        String promptResponse = openAiService.PromptGptForStockPrediction(tickerInfo);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(promptResponse);
    }

    public static JSONObject getJSONFromObject(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(object, JSONObject.class);
    }
}
