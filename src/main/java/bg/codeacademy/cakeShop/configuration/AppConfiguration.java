package bg.codeacademy.cakeShop.configuration;

import bg.codeacademy.cakeShop.mapper.Mapper;
import bg.codeacademy.cakeShop.shedule.TransactionTaskExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {
    @Bean
    public Mapper modelMapper() {
        return new Mapper();
    }

    @Bean
    public Thread reader(TransactionTaskExecutor exec) {
        Thread thread = new Thread(exec);
        thread.start();
        return thread;
    }
}