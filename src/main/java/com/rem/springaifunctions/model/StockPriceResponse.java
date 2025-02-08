package com.rem.springaifunctions.model;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Builder;

@Builder
public record StockPriceResponse(
        @JsonPropertyDescription("ticker") String ticker,
        @JsonPropertyDescription("name") String name,
        @JsonPropertyDescription("price") double price,
        @JsonPropertyDescription("exchange") String exchange,
        @JsonPropertyDescription("updated") Integer updated,
        @JsonPropertyDescription("currency") String currency) {
}