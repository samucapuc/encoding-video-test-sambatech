package br.com.samuel.sambatech.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomBeansConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}