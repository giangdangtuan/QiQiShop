package com.app85soft.qiqishop.dto.config;

import lombok.Data;

@Data
public class ConnectionConfig {
    private String apiUrl;
    private int maxRequest = 200;
    private int maxRequestPerHost = 50;
    private long connectTimeout = 30;

}
