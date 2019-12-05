package kz.ata.saycheese.bot;

import kz.ata.saycheese.config.BotConfig;
import kz.ata.saycheese.constants.SaycheeseConstants;
import kz.ata.saycheese.enums.InputType;
import kz.ata.saycheese.enums.State;
import kz.ata.saycheese.service.SaycheeseService;
import kz.ata.saycheese.service.StateService;
import kz.ata.saycheese.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class SaycheeseBot extends TelegramLongPollingBot {

    @Autowired
    private BotConfig botConfig;

    @Autowired
    private UserService userService;

    @Autowired
    private SaycheeseService saycheeseService;

    @Autowired
    private StateService stateService;

    private Map<Long, State> states = new ConcurrentHashMap<>();
    private Map<Long, String> cheesecakeType = new ConcurrentHashMap<>();

    @Override
    public void onUpdateReceived(Update update) {
        if (!checkAccessRights(update.getMessage().getChatId())){
            sendCustomKeyboard(update.getMessage().getChatId(), InputType.BASE, SaycheeseConstants.ACCESS_DENIED);
            return;
        }
        if (update.hasMessage() && update.getMessage().hasText()){
            String message = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            State state = getState(chat_id);
            String out = "";
            switch (state){
                case MAIN:
                    out = "Выберите действие";
                    stateService.handleStateMain(message, chat_id, states);
                    sendCustomKeyboard(chat_id, InputType.MAIN, out);
                    break;
                case ORDERS:
                    out = "Выберите заказы";
                    stateService.handleStateOrders(message, chat_id, states);
                    sendCustomKeyboard(chat_id, InputType.SUBORDERS, out);
                    break;
                case STORAGE:
                    out = "Выберите действие со складом";
                    stateService.handleStateStorage(message, chat_id, states);
                    sendCustomKeyboard(chat_id, InputType.SUBSTORAGE, out);
                    break;
                case SELL:
                    out = "Выберите чизкейк";
                    states.put(chat_id, State.SELL_PROCESSING);
                    sendCustomKeyboard(chat_id, InputType.SUBSELLS, out);
                    break;
                case REPORTS:
                    out = "Выберите период отчетности";
                    stateService.handleStateReports(message, chat_id, states);
                    sendCustomKeyboard(chat_id, InputType.SUBREPORTS, out);
                    break;
//
//                case SELL_PROCESSING:
//                    out = "Выберан " + message + ". Введите вес в кг. Пример: 1.4";
//                    cheesecakeType.put(chat_id, message);
//                    states.put(chat_id, State.SELL_WEIGHT);
//                    sendSimpleMessage(chat_id, out);
//                    break;
//                case SELL_WEIGHT:
//                    out = "Выберан " + message + ". Введите вес в кг. Пример: 1.4";
//                    cheesecakeType.put(chat_id, message);
//                    states.put(chat_id, State.SELL_WEIGHT);
//                    sendCustomKeyboard(chat_id, InputType.SUBSELLS, out);
//                    break;
                default:
                    break;
            }
        }
    }

    private State getState(long chat_id) {
        if (states.get(chat_id) == null){
            states.put(chat_id, State.MAIN);
        }
        return states.get(chat_id);
    }

    private void sendSimpleMessage(Long chat_id, String text){
        try {
            SendMessage message = new SendMessage();
            message.enableMarkdown(true);
            message.setChatId(chat_id);
            message.setText(text);
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendCommandMessage(Long chat_id, String command) {
        String out = "";
        if (command.equalsIgnoreCase(SaycheeseConstants.ACTIVE_ORDERS)){
            out = saycheeseService.constructActiveOrdersText();
        }else if (command.equalsIgnoreCase(SaycheeseConstants.ADD_ORDER)){
            out = saycheeseService.constructAddOrderText();
        }else{
            out = saycheeseService.constructDefaultText();
        }
        try {
            SendMessage message = new SendMessage();
            message.enableMarkdown(true);
            message.setChatId(chat_id);
            message.setText(out);
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
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
