package com.rem.springaifunctions.service;

import com.rem.springaifunctions.model.*;

import java.util.List;

public interface OpenAIService {

    Answer getAnswer(Question question);

    Answer getStockPrice(Question question);

    public Answer getCountryInflation(Question question);

    WeatherResponse toNinja(WeatherRequest weatherRequest);
}
