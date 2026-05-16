package com.Medilabo_solutions.Frontend_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;
/**
 * Configuration class to enable support for HTTP methods like PUT and DELETE in HTML forms.
 * This is necessary because HTML forms only support GET and POST methods by default.
 */
@Configuration
public class WebConfig {
    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }
}
