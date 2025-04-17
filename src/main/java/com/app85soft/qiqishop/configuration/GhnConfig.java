package com.app85soft.qiqishop.configuration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "ghn")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GhnConfig {
    String token;
    String shopId;
    String baseUrl;
}
