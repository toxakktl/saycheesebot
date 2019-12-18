package kz.ata.saycheese.exceptions;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class StorageException extends TelegramApiException {
    public StorageException() {
    }

    public StorageException(String message) {
        super(message);
    }
}
