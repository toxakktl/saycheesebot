package kz.ata.saycheese.managers;

import kz.ata.saycheese.constants.SaycheeseConstants;
import kz.ata.saycheese.exceptions.CookException;
import kz.ata.saycheese.model.CheesecakeModel;
import kz.ata.saycheese.model.IngredientModel;
import kz.ata.saycheese.model.RecipeIngredientModel;
import kz.ata.saycheese.model.RecipeModel;
import kz.ata.saycheese.service.CheesecakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.math.BigDecimal;
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

    public SendMessage constructRecipe(String msg, String cheesecake, Long chat_id) throws CookException {
        BigDecimal qty = new BigDecimal(msg);
        CheesecakeModel cheesecakeModel = cheesecakeService.findByName(cheesecake);
        if (cheesecakeModel == null){
            throw new CookException("Выбранный чизкейк не найден.");
        }
        StringBuilder sb = new StringBuilder();
        RecipeModel recipe = cheesecakeModel.getRecipe();
        for (RecipeIngredientModel rec: recipe.getIngredients()){
            IngredientModel ingredient = rec.getIngredient();
            sb.append(ingredient.getFood().getName()).append(" ").append(rec.getQuantiy().multiply(qty)).append(" ").append(rec.getUnit().getValue()).append("\n");
        }
        SendMessage message = new SendMessage();
        message.enableMarkdown(true);
        message.setChatId(chat_id);
        message.setText(sb.toString());
        return message;
    }

}
