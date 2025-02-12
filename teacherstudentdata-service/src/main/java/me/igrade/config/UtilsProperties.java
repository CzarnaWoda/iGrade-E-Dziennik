package me.igrade.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "utils")
public record UtilsProperties(boolean enabledDeveloperMessages, String timeFormat) {
}
