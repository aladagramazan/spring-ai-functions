package com.rem.springaifunctions.controller;

import com.rem.springaifunctions.model.*;
import com.rem.springaifunctions.service.OpenAIService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class QuestionController {

    private final OpenAIService openAIService;

    @PostMapping("/weather")
    public Answer askQuestion(@RequestBody Question question) {
        return openAIService.getAnswer(question);
    }

    @PostMapping("/stock")
    public Answer getStockPrice(@RequestBody Question question) {
        return openAIService.getStockPrice(question);
    }

    @PostMapping("/inflation")
    public Answer getInflation(@RequestBody Question question) {
        return openAIService.getCountryInflation(question);
    }

    @GetMapping("/toNinja")
    public WeatherResponse toNinja(@RequestBody WeatherRequest weatherRequest) {
        return openAIService.toNinja(weatherRequest);
    }
}
