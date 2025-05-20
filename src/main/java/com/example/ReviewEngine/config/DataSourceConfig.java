package com.example.ReviewEngine.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        Dotenv dotenv = Dotenv.load();

        String host = dotenv.get("DB_HOST");
        String port = dotenv.get("DB_PORT");
        String dbname = dotenv.get("DB_NAME");
        String username = dotenv.get("DB_USER");
        String password = dotenv.get("DB_PASSWORD");

        String url = "jdbc:mysql://" + host + ":" + port + "/" + dbname +
                "?useSSL=true&requireSSL=true&verifyServerCertificate=false";

        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }
}
