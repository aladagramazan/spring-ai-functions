package com.rem.springaifunctions.functions;

import com.rem.springaifunctions.model.StockPriceRequest;
import com.rem.springaifunctions.model.StockPriceResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.function.Function;

public class StockPriceFunction implements Function<StockPriceRequest, StockPriceResponse> {

    public static final String STOCK_PRICE_URL = "https://api.api-ninjas.com/v1/stockprice";

    private final String apiNinjasKey;

    public StockPriceFunction(String apiNinjasKey) {
        this.apiNinjasKey = apiNinjasKey;
    }

    @Override
    public StockPriceResponse apply(StockPriceRequest stockPriceRequest) {
        WebClient webClient = WebClient.builder()
                .baseUrl(STOCK_PRICE_URL)
                .defaultHeader("X-Api-Key", apiNinjasKey)
                .defaultHeader("Accept", "application/json")
                .defaultHeader("Content-Type", "application/json")
                .build();
        Flux<StockPriceResponse> responseFlux = webClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam("ticker", stockPriceRequest.ticker()).build())
                .retrieve()
                .bodyToFlux(StockPriceResponse.class);
        if (responseFlux.collectList().block() == null) {
            return StockPriceResponse.builder().build();
        } else {
            return responseFlux.blockFirst();
        }
    }
}
