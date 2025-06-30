package com.inca.mc_main.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class WebFluxValidationConfig implements WebFluxConfigurer {

    @Override
    public Validator getValidator() {
        return new LocalValidatorFactoryBean();
    }
}
