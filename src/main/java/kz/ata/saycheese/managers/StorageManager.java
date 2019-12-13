package kz.ata.saycheese.managers;

import kz.ata.saycheese.constants.SaycheeseConstants;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class StorageManager {

    public ReplyKeyboard createSubstorageKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add(SaycheeseConstants.ALL_STORAGE);
        keyboard.add(row);
        row = new KeyboardRow();
        row.add(SaycheeseConstants.UPDATE_STORAGE);
        keyboard.add(row);
        row = new KeyboardRow();
        row.add(SaycheeseConstants.HOME);
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

}
