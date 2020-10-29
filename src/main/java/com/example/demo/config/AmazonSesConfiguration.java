package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;

@Configuration
public class AmazonSesConfiguration {

    @Bean
    public SesClient amazonSimpleEmailService(@Value("${aws.region}") String awsRegion) {
        return SesClient.builder()
                .region(Region.of(awsRegion))
                .build();
    }
}
