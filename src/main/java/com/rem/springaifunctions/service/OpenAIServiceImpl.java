package com.rem.springaifunctions.service;

import com.rem.springaifunctions.functions.InflationFunction;
import com.rem.springaifunctions.functions.StockPriceFunction;
import com.rem.springaifunctions.functions.WeatherServiceFunction;
import com.rem.springaifunctions.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OpenAIServiceImpl implements OpenAIService {

    @Value("${sfg.aiapp.apiNinjasKey}")
    private String apiNinjasKey;

    private final OpenAiChatModel openAiChatModel;

    @Override
    public Answer getStockPrice(Question question) {
        var promptOptions = OpenAiChatOptions.builder()
                .functionCallbacks(List.of(FunctionCallback.builder()
                        .function("CurrentStockPrice", new StockPriceFunction(apiNinjasKey))
                        .description("Get the current stock price for a stock symbol")
                                .inputType(StockPriceRequest.class)
                                .responseConverter(response -> {
                                    Object schema = ModelOptionsUtils.getJsonSchema(StockPriceResponse.class, false);
                                    Object json = ModelOptionsUtils.toJsonString(response);
                                    return schema + "\n" + json;
                                })
                        .build()))
                .build();

        Message userMessage = new PromptTemplate(question.question()).createMessage();
        Message systemMessage = new SystemPromptTemplate("You are an agent which returns back a stock price for the given company name, and I ask you question in Turkish").createMessage();

        var response = openAiChatModel.call(new Prompt(List.of(userMessage, systemMessage), promptOptions));

        return new Answer(response.getResult().getOutput().getContent());
    }

    @Override
    public Answer getCountryInflation(Question question) {
        var promptOptions = OpenAiChatOptions.builder()
                .functionCallbacks(List.of(FunctionCallback.builder()
                        .function("Inflation", new InflationFunction(apiNinjasKey))
                        .description("Get the current inflation for a country")
                        .inputType(InflationRequest.class)
                        .responseConverter(response -> {
                            Object schema = ModelOptionsUtils.getJsonSchema(InflationResponse.class, false);
                            Object json = ModelOptionsUtils.toJsonString(response);
                            return schema + "\n" + json;
                        })
                        .build()))
                .build();

        Message userMessage = new PromptTemplate(question.question()).createMessage();
        Message systemMessage = new SystemPromptTemplate("You are an agent which returns back inflation and for the given country, " +
                "and I ask you question in Turkish, Can you please answer me in Turkish?").createMessage();

        var response = openAiChatModel.call(new Prompt(List.of(userMessage, systemMessage), promptOptions));

        return new Answer(response.getResult().getOutput().getContent());
    }

    @Override
    public Answer getAnswer(Question question) {
        var promptOptions = OpenAiChatOptions.builder()
                .functionCallbacks(List.of(FunctionCallback.builder()
                        .function("CurrentWeather", new WeatherServiceFunction(apiNinjasKey))
                        .description("Get the current weather for a location")
                        .inputType(WeatherRequest.class)
                        .responseConverter(response -> {
                            String schema = ModelOptionsUtils.getJsonSchema(WeatherResponse.class, false);
                            String json = ModelOptionsUtils.toJsonString(response);
                            return schema + "\n" + json;
                        })
                        .build()))
                .build();

        Message userMessage = new PromptTemplate(question.question()).createMessage();

        Message systemMessage = new SystemPromptTemplate("You are a weather service. You receive weather information from a service which gives you the information based on the metrics system." +
                " When answering the weather in an imperial system country, you should convert the temperature to Fahrenheit and the wind speed to miles per hour. ").createMessage();

        var response = openAiChatModel.call(new Prompt(List.of(userMessage, systemMessage), promptOptions));

        return new Answer(response.getResult().getOutput().getContent());
    }

    @Override
    public WeatherResponse toNinja(WeatherRequest weatherRequest) {
        return new WeatherServiceFunction(apiNinjasKey).apply(weatherRequest);
    }
}
