package com.example.ReviewEngine.service;

import com.example.ReviewEngine.model.City;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;


import java.io.InputStream;
import java.util.Random;


@Service
public class CityLoader {

    public String getCity() throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        ClassPathResource resource = new ClassPathResource("city.list.json");
        try (InputStream inputStream = resource.getInputStream()) {

            City[] cityArray = mapper.readValue(inputStream, City[].class);
            int rnd = new Random().nextInt(cityArray.length);
            return cityArray[rnd].name;
        }
    }

}
