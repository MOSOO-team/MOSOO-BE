package com.team2.mosoo_backend.payment.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Configuration
@ConfigurationProperties(prefix = "portone")
@Getter
public class PortoneProperties {
    private String impKey;
    private String impSecret;
}
