package com.example.demo.config;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonSesConfiguration {

    @Bean
    public AmazonSimpleEmailService amazonSimpleEmailService(@Value("${aws.region}") String awsRegion) {
        return AmazonSimpleEmailServiceClientBuilder.standard()
                .withRegion(awsRegion)
                .build();
    }
}
