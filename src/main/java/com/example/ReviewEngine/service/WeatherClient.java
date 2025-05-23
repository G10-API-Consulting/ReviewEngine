package com.example.ReviewEngine.service;


import com.example.ReviewEngine.model.WeatherResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class WeatherClient {

    @Value("${weather.api.key}")
    private String API_KEY;
    private final CityLoader cityLoader;

    public WeatherClient(CityLoader cityLoader) {
        this.cityLoader = cityLoader;
    }

    public String getCurrentWeather() throws Exception {
        String city = cityLoader.getCity();

        String uri = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY;

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        String result = restTemplate.getForObject(uri, String.class);
        WeatherResponse response = objectMapper.readValue(result, WeatherResponse.class);
        return response.weather[0].description;

    }
}
