package kz.ata.saycheese.service;

import kz.ata.saycheese.constants.SaycheeseConstants;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class SaycheeseService {

    public ReplyKeyboard createMainKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add(SaycheeseConstants.ORDERS);
        row.add(SaycheeseConstants.STORAGE);
        keyboard.add(row);
        row = new KeyboardRow();
        row.add(SaycheeseConstants.SELL);
        row.add(SaycheeseConstants.REPORTS);
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    public ReplyKeyboard createSubordersKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add(SaycheeseConstants.ACTIVE_ORDERS);
        row.add(SaycheeseConstants.ALL_ORDERS);
        keyboard.add(row);
        row = new KeyboardRow();
        row.add(SaycheeseConstants.BACK);
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    public ReplyKeyboard createSubreportsKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add(SaycheeseConstants.ALL_TIME_REPORT);
        row.add(SaycheeseConstants.PERIOD_REPORT);
        keyboard.add(row);
        row = new KeyboardRow();
        row.add(SaycheeseConstants.BACK);
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    public ReplyKeyboard createSubstorageKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add(SaycheeseConstants.ALL_STORAGE);
        keyboard.add(row);
        row = new KeyboardRow();
        row.add(SaycheeseConstants.ADD_FOOD);
        row.add(SaycheeseConstants.DELETE_FOOD);
        keyboard.add(row);
        row = new KeyboardRow();
        row.add(SaycheeseConstants.BACK);
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    public ReplyKeyboard createSellKeyboard(){
        return null;
    }


}
