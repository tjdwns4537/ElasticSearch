package com.example.elasticsearch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String[] resourceLocation = {
                "classpath:/static/", "classpath:/resources/", "classpath:/templates/"
        };
        registry.addResourceHandler("/**").addResourceLocations(resourceLocation);
        super.addResourceHandlers(registry);
    }
}
