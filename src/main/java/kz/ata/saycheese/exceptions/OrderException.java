package kz.ata.saycheese.exceptions;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class OrderException extends TelegramApiException {

    public OrderException() {
    }
    public OrderException(String message) {
        super(message);
    }
}
