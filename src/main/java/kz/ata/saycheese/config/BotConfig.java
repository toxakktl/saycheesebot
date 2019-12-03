package kz.ata.saycheese.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BotConfig {

    @Value("${bot.token}")
    private String token;

    @Value("${bot.username}")
    private String username;

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }
}
