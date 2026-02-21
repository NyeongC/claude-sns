package com.ccn.sns.sns_project.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.data")
public record AppDataProperties(String defaultPassword) {
}
