package bg.codeacademy.cakeShop;

import bg.codeacademy.cakeShop.shedule.TransactionTaskExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import javax.naming.Context;

@SpringBootApplication
public class CakeShopApplication {
    private static ApplicationContext context = null;
    public CakeShopApplication(ApplicationContext context) {
       CakeShopApplication.context = context;
    }
    public static void main(String[] args) {
        SpringApplication.run(CakeShopApplication.class, args);
        TransactionTaskExecutor taskExecutor = context.getBean(TransactionTaskExecutor.class);
        taskExecutor.start();
    }
}
