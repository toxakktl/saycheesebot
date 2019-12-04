package kz.ata.saycheese.bot;

import kz.ata.saycheese.config.BotConfig;
import kz.ata.saycheese.constants.SaycheeseConstants;
import kz.ata.saycheese.enums.InputType;
import kz.ata.saycheese.service.SaycheeseService;
import kz.ata.saycheese.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class SaycheeseBot extends TelegramLongPollingBot {

    @Autowired
    private BotConfig botConfig;

    @Autowired
    private UserService userService;

    @Autowired
    private SaycheeseService saycheeseService;

    @Override
    public void onUpdateReceived(Update update) {
        if (!checkAccessRights(update.getMessage().getChatId())){
            sendCustomKeyboard(update.getMessage().getChatId(), InputType.BASE, SaycheeseConstants.ACCESS_DENIED);
            return;
        }
        if (update.hasMessage() && update.getMessage().hasText()){
            System.out.println(update.getMessage().getText());
            String message = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            String out = "";
            switch (message){
                case SaycheeseConstants.ORDERS:
                    out = "Выберите заказы";
                    sendCustomKeyboard(chat_id, InputType.SUBORDERS, out);
                    break;
                case SaycheeseConstants.STORAGE:
                    out = "Выберите действие со складом";
                    sendCustomKeyboard(chat_id, InputType.SUBSTORAGE, out);
                    break;
                case SaycheeseConstants.SELL:
                    out = "Выберите чизкейк";
                    sendCustomKeyboard(chat_id, InputType.SUBSELLS, out);
                    break;
                case SaycheeseConstants.REPORTS:
                    out = "Выберите период отчетности";
                    sendCustomKeyboard(chat_id, InputType.SUBREPORTS, out);
                    break;
                default:
                    out = "Выберите действие из меню";
                    sendCustomKeyboard(chat_id, InputType.BASE, out);
                    break;
            }
        }
    }

    public void sendCustomKeyboard(long chatId, InputType type, String msg) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(msg);
        if (type.equals(InputType.SUBORDERS)){
            message.setReplyMarkup(saycheeseService.createSubordersKeyboard());
        }else if(type.equals(InputType.SUBSTORAGE)){
            message.setReplyMarkup(saycheeseService.createSubstorageKeyboard());
        }else if(type.equals(InputType.SUBSELLS)){
            message.setReplyMarkup(saycheeseService.createSellKeyboard());
        }else if(type.equals(InputType.SUBREPORTS)){
            message.setReplyMarkup(saycheeseService.createSubreportsKeyboard());
        }else {
            message.setReplyMarkup(saycheeseService.createMainKeyboard());
        }
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private boolean checkAccessRights(Long chatId) {
        return userService.findAllUserIds().contains(chatId);
    }

    @Override
    public String getBotUsername() {
        return botConfig.getUsername();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }
}
