package bg.codeacademy.cakeShop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CakeShopApplication {
	private static final Logger logger = LoggerFactory.getLogger(CakeShopApplication.class);
	public static void main(String[] args) {
		logger.info("Test log message");
		SpringApplication.run(CakeShopApplication.class, args);
	}

}
