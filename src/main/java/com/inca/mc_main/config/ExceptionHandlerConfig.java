package com.inca.mc_main.config;

import com.inca.mc_main.exception.GlobalErrorWebExceptionHandler;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;

@Configuration
public class ExceptionHandlerConfig {

    @Bean
    public WebProperties.Resources resources() {
        return new WebProperties.Resources();
    }

    @Bean
    public GlobalErrorWebExceptionHandler globalErrorWebExceptionHandler(
            ErrorAttributes errorAttributes,
            WebProperties.Resources resources,
            ApplicationContext applicationContext,
            ServerCodecConfigurer codecConfigurer) {
        return new GlobalErrorWebExceptionHandler(errorAttributes, resources, applicationContext, codecConfigurer);
    }
}
