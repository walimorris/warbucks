package com.stock.warbucks.services;

import com.stock.warbucks.models.StockTickers;

import java.util.Map;

public interface StockService {

    /**
     * Get stock data with ticker as key and response json data as value.
     *
     * @param stockTickers {@link StockTickers}
     * @return {@link Map}
     */
    Map<String, String> getStockTickersList(StockTickers stockTickers);
}
