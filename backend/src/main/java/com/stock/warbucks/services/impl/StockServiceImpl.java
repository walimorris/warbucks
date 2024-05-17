package com.stock.warbucks.services.impl;

import com.stock.warbucks.configurations.ApplicationPropertiesConfiguration;
import com.stock.warbucks.models.StockTickers;
import com.stock.warbucks.services.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class StockServiceImpl implements StockService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StockServiceImpl.class);

    private final ApplicationPropertiesConfiguration configuration;
    private final RestTemplate restTemplate;

    @Autowired
    public StockServiceImpl(ApplicationPropertiesConfiguration configuration, RestTemplate restTemplate) {
        this.configuration = configuration;
        this.restTemplate = restTemplate;
    }

    @Override
    public Map<String, String> getStockTickersList(StockTickers stockTickers) {
        Map<String, String> tickerResults = new HashMap<>();
        String start = getDateNDaysAgo(3);
        String end = getDateNDaysAgo(1);
        for (String ticker : stockTickers.getTickerSymbols()) {
            String url = String.format(
                    "https://api.polygon.io/v2/aggs/ticker/%s/range/1/day/%s/%s?adjusted=true&sort=asc&apiKey=%s",
                    ticker, start, end, configuration.polygonIoKey()
            );
            String response = restTemplate.getForObject(url, String.class);
            LOGGER.info(response);
            tickerResults.put(ticker, response);
        }
        return tickerResults;
    }

    private static String getDateNDaysAgo(int n) {
        LocalDate currentDate = LocalDate.now();
        LocalDate pastDate = currentDate.minusDays(n);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LOGGER.info("DATE: {}", pastDate.format(formatter));
        return pastDate.format(formatter);
    }
}
