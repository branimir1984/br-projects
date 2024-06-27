package bg.codeacademy.cakeShop.configuration;

import bg.codeacademy.cakeShop.mapper.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {
    @Bean
    public Mapper modelMapper() {
        return new Mapper();
    }
}