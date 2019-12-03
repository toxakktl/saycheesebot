package kz.ata.saycheese;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class SaycheeseApplication {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(SaycheeseApplication.class, args);
    }
}
