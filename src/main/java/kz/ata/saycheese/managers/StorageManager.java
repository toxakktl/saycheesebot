package kz.ata.saycheese.managers;

import kz.ata.saycheese.constants.SaycheeseConstants;
import kz.ata.saycheese.enums.State;
import kz.ata.saycheese.enums.Unit;
import kz.ata.saycheese.exceptions.StorageException;
import kz.ata.saycheese.model.FoodModel;
import kz.ata.saycheese.service.FoodService;
import kz.ata.saycheese.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
public class StorageManager {

    @Autowired
    private StorageService storageService;

    @Autowired
    private FoodService foodService;

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

    public SendMessage sendStorageDialog(Long chat_id, State state) {
        String out = "";
        if (state.equals(State.ALL_STORAGE)){
            out = constructAllStorageText();
        }else if (state.equals(State.UPDATE_STORAGE)){
            out = constructUpdateStorageText();
        }else {
            out = constructDefaultStorageText();
        }
        SendMessage message = new SendMessage();
        message.enableMarkdown(true);
        message.setChatId(chat_id);
        message.setText(out);
        return message;
    }

    public void updateStorage(String message) throws StorageException {
        String[] fields = message.split("\\r?\\n");
        validateStorageFields(fields);
        FoodModel foodModel = foodService.findByName(fields[0]);
        if (foodModel == null){
            throw new StorageException("Продукт не найден. ");
        }
        foodModel.setQuantity(new BigDecimal(fields[1]));
        foodModel.setUnit(Unit.valueOf(fields[2]));
        foodService.save(foodModel);
    }

    private void validateStorageFields(String[] fields) throws StorageException {
        if (fields.length <= 0 || fields.length > 3){
            throw new StorageException("Неверный формат полей. Необходимо, чтобы было 5 полей.");
        }
    }

    public String constructAllStorageText() {
        StringBuilder sb = new StringBuilder();
        List<FoodModel> storage = storageService.findAll();
        for (FoodModel food: storage){
            sb.append(food.getName()).append(": ").append(food.getQuantity()).append(" ").append(food.getUnit().getValue()).append("\n");
        }
        return sb.toString();
    }

    public String constructUpdateStorageText() {
        StringBuilder sb = new StringBuilder();
        sb.append("*Обновите количество продуктов в формате*: ").append("\n").
                append("Название продукта (название должно совпадать с названием с результата Все продукты)\n").
                append("Количсетво\n").
                append("Единица измерения (KG, G, ML, L)\n\n").
                append("*Пример: *\n").
                append("Бананы\n3\nKG");
        return sb.toString();
    }

    public String constructDefaultStorageText() {
        return "Выберите операцию со складом";
    }

}
