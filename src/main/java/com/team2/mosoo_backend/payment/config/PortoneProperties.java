package com.team2.mosoo_backend.payment.config;


import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "portone")
@Getter
public class PortoneProperties {
    private String impkey;
    private String impSecret;
}
