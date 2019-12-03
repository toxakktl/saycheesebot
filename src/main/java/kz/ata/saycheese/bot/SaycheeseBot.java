package kz.ata.saycheese.bot;

import kz.ata.saycheese.config.BotConfig;
import kz.ata.saycheese.constants.SaycheeseConstants;
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

    @Override
    public void onUpdateReceived(Update update) {
        if (!checkAccessRights(update.getMessage().getChatId())){
            SendMessage message =  new SendMessage(update.getMessage().getChatId(), SaycheeseConstants.ACCESS_DENIED);
            send(message);
            return;
        }
        if (update.hasMessage() && update.getMessage().hasText()){
            System.out.println(update.getMessage().getText());
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId());
            message.setText("Hello World");
            send(message);
        }
    }


    private void send(SendMessage message){
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
