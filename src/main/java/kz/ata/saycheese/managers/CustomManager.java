package kz.ata.saycheese.managers;

import kz.ata.saycheese.constants.SaycheeseConstants;
import kz.ata.saycheese.enums.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomManager {

    @Autowired
    private OrderManager orderManager;
    @Autowired
    private StorageManager storageManager;
    @Autowired
    private CakeManager cakeManager;
    @Autowired
    private ReportManager reportManager;

    public SendMessage sendCustomKeyboard(long chatId, State type, String msg) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(msg);
        if (type.equals(State.ORDERS)){
            message.setReplyMarkup(orderManager.createSubordersKeyboard());
        }else if(type.equals(State.STORAGE)){
            message.setReplyMarkup(storageManager.createSubstorageKeyboard());
        }else if(type.equals(State.COOK)){
            message.setReplyMarkup(cakeManager.createCookKeyboard());
        }else if(type.equals(State.REPORTS)){
            message.setReplyMarkup(reportManager.createSubreportsKeyboard());
        }else {
            message.setReplyMarkup(createMainKeyboard());
        }
        return message;
    }

    public ReplyKeyboard createMainKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add(SaycheeseConstants.ORDERS);
        row.add(SaycheeseConstants.STORAGE);
        keyboard.add(row);
        row = new KeyboardRow();
        row.add(SaycheeseConstants.COOK);
        row.add(SaycheeseConstants.REPORTS);
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }
}
