package kz.ata.saycheese.bot;

import kz.ata.saycheese.config.BotConfig;
import kz.ata.saycheese.constants.SaycheeseConstants;
import kz.ata.saycheese.enums.State;
import kz.ata.saycheese.service.SaycheeseService;
import kz.ata.saycheese.service.StateService;
import kz.ata.saycheese.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
            sendCustomKeyboard(update.getMessage().getChatId(), State.MAIN, SaycheeseConstants.ACCESS_DENIED);
            return;
        }
        if (update.hasMessage() && update.getMessage().hasText()){
            String message = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            State state = getState(chat_id);
            if (handleHomeButton(chat_id, message))
                return;
            String out = "";
            switch (state){
                case MAIN:
                    out = stateService.handleStateMain(message, chat_id, states);
                    sendCustomKeyboard(chat_id, states.get(chat_id), out);
                    break;
                //Orders part
                case ORDERS:
                case ACTIVE_ORDERS:
                case ALL_ORDER:
                    stateService.handleStateOrders(message, chat_id, states);
                    sendOrdersDialog(chat_id, states.get(chat_id));
                    break;
                case ADD_ORDER:
                    try {
                        processNewOrder(message);
                        sendSimpleMessage(chat_id, SaycheeseConstants.ORDER_SAVED);
                        states.put(chat_id, State.ORDERS);
                    } catch (TelegramApiException e) {
                        sendSimpleMessage(chat_id, e.getMessage());
                        states.put(chat_id, State.ORDERS);
                    }
                    break;
                case DELETE_ORDER:
                    try {
                        processDeleteOrder(message);
                        sendSimpleMessage(chat_id, SaycheeseConstants.ORDER_DELETED);
                        states.put(chat_id, State.ORDERS);
                    } catch (TelegramApiException e) {
                        sendSimpleMessage(chat_id, e.getMessage());
                        states.put(chat_id, State.ORDERS);
                    }
                    break;
                case COMPLETE_ORDER:
                    try {
                        processCompleteOrder(message);
                        sendSimpleMessage(chat_id, SaycheeseConstants.ORDER_COMPETED);
                        states.put(chat_id, State.ORDERS);
                    } catch (TelegramApiException e) {
                        sendSimpleMessage(chat_id, e.getMessage());
                        states.put(chat_id, State.ORDERS);
                    }
                    break;

                 //Storage part
                case STORAGE:
                case ALL_STORAGE:
                    stateService.handleStateStorage(message, chat_id, states);
                    sendStorageDialog(chat_id, states.get(chat_id));
                    break;
                case UPDATE_STORAGE:
                    try {
                        processUpdateStorage(message);
                        states.put(chat_id, State.STORAGE);
                    } catch (TelegramApiException e) {
                        sendSimpleMessage(chat_id, e.getMessage());
                        states.put(chat_id, State.STORAGE);
                    }
                    break;

                case SELL:
                    out = "Выберите чизкейк";
                    states.put(chat_id, State.SELL_PROCESSING);
                    sendCustomKeyboard(chat_id, states.get(chat_id), out);
                    break;
                case REPORTS:
                    out = "Выберите период отчетности";
                    stateService.handleStateReports(message, chat_id, states);
                    sendCustomKeyboard(chat_id, State.SUBREPORTS, out);
                    break;

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
                    out = "Выберите действие";
                    stateService.handleStateMain(message, chat_id, states);
                    sendCustomKeyboard(chat_id, State.MAIN, out);
                    break;
            }
        }
    }

    private boolean handleHomeButton(long chat_id, String message) {
        if (message.equals(SaycheeseConstants.HOME)){
            states.put(chat_id, State.MAIN);
            sendCustomKeyboard(chat_id, State.MAIN, "Выберите действие");
            return true;
        }
        return false;
    }

    private void processUpdateStorage(String message) throws TelegramApiException {
        String[] fields = message.split("\\r?\\n");
        saycheeseService.updateStorage(fields);
    }

    private void processCompleteOrder(String message) throws TelegramApiException {
        saycheeseService.completeOrder(message);
    }

    private void processDeleteOrder(String message) throws TelegramApiException {
        saycheeseService.deleteOrder(message);
    }

    private void processNewOrder(String message) throws TelegramApiException {
        String[] fields = message.split("\\r?\\n");
        saycheeseService.addOrder(fields);
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

    private void sendOrdersDialog(Long chat_id, State state) {
        String out = "";
        if (state.equals(State.ALL_ORDER)){
            out = saycheeseService.constructAllOrdersText();
        }else if (state.equals(State.ACTIVE_ORDERS)){
            out = saycheeseService.constructActiveOrderText();
        }else if (state.equals(State.ADD_ORDER)){
            out = saycheeseService.constructAddOrderText();
        }else if (state.equals(State.DELETE_ORDER)){
            out = saycheeseService.constructDeleteOrderText();
        }else if (state.equals(State.COMPLETE_ORDER)){
            out = saycheeseService.constructCompleteOrderText();
        }else {
            out = saycheeseService.constructDefaultOrderText();
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

    private void sendStorageDialog(Long chat_id, State state) {
        String out = "";
        if (state.equals(State.ALL_STORAGE)){
            out = saycheeseService.constructAllStorageText();
        }else if (state.equals(State.UPDATE_STORAGE)){
            out = saycheeseService.constructUpdateStorageText();
        }else {
            out = saycheeseService.constructDefaultStorageText();
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

    public void sendCustomKeyboard(long chatId, State type, String msg) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(msg);
        if (type.equals(State.ORDERS)){
            message.setReplyMarkup(saycheeseService.createSubordersKeyboard());
        }else if(type.equals(State.STORAGE)){
            message.setReplyMarkup(saycheeseService.createSubstorageKeyboard());
        }else if(type.equals(State.SELL)){
            message.setReplyMarkup(saycheeseService.createSellKeyboard());
        }else if(type.equals(State.REPORTS)){
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
