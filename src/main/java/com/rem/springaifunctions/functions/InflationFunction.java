package com.rem.springaifunctions.functions;

import com.rem.springaifunctions.model.InflationRequest;
import com.rem.springaifunctions.model.InflationResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.function.Function;

public class InflationFunction implements Function<InflationRequest, InflationResponse> {

    public static final String INFLATION_URL = "https://api.api-ninjas.com/v1/inflation";

    private final String apiNinjasKey;

    public InflationFunction(String apiNinjasKey) {
        this.apiNinjasKey = apiNinjasKey;
    }

    @Override
    public InflationResponse apply(InflationRequest inflationRequest) {
        WebClient webClient = WebClient.builder()
                .baseUrl(INFLATION_URL)
                .defaultHeader("X-Api-Key", apiNinjasKey)
                .defaultHeader("Accept", "application/json")
                .defaultHeader("Content-Type", "application/json")
                .build();
        Flux<InflationResponse> responseFlux = webClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam("country", inflationRequest.country()).build())
                .retrieve()
                .bodyToFlux(InflationResponse.class);
        if (responseFlux.collectList().block() == null) {
            return InflationResponse.builder().build();
        } else {
            return responseFlux.blockFirst();
        }
    }
}
