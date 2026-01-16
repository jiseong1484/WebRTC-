package com.fermi.signaling.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String frontendBaseUrl;
    private long sessionTtlMinutes = 30;

    public String getFrontendBaseUrl() { return frontendBaseUrl; }
    public void setFrontendBaseUrl(String frontendBaseUrl) { this.frontendBaseUrl = frontendBaseUrl; }

    public long getSessionTtlMinutes() { return sessionTtlMinutes; }
    public void setSessionTtlMinutes(long sessionTtlMinutes) { this.sessionTtlMinutes = sessionTtlMinutes; }
}