package kz.ata.saycheese.exceptions;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class CookException extends TelegramApiException {

    public CookException() {
    }

    public CookException(String message) {
        super(message);
    }
}
