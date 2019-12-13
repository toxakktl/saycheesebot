package kz.ata.saycheese.managers;

import kz.ata.saycheese.constants.SaycheeseConstants;
import kz.ata.saycheese.model.CheesecakeModel;
import kz.ata.saycheese.service.CheesecakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class CakeManager {

    @Autowired
    private CheesecakeService cheesecakeService;

    public ReplyKeyboard createCookKeyboard(){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        List<CheesecakeModel> cheesecakes = cheesecakeService.findAll();
        for (CheesecakeModel cheesecake : cheesecakes){
            KeyboardRow row = new KeyboardRow();
            row.add(cheesecake.getName());
            keyboard.add(row);
        }
        KeyboardRow row = new KeyboardRow();
        row.add(SaycheeseConstants.HOME);
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

}
