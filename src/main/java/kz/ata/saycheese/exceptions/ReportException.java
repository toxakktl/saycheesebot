package kz.ata.saycheese.exceptions;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class ReportException extends TelegramApiException {

    public ReportException() {
    }

    public ReportException(String message) {
        super(message);
    }
    
}
