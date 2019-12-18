package kz.ata.saycheese.bot;

import kz.ata.saycheese.config.BotConfig;
import kz.ata.saycheese.constants.SaycheeseConstants;
import kz.ata.saycheese.enums.State;
import kz.ata.saycheese.exceptions.CookException;
import kz.ata.saycheese.exceptions.OrderException;
import kz.ata.saycheese.exceptions.StorageException;
import kz.ata.saycheese.managers.CakeManager;
import kz.ata.saycheese.managers.CustomManager;
import kz.ata.saycheese.managers.OrderManager;
import kz.ata.saycheese.managers.StorageManager;
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
    private StateService stateService;
    @Autowired
    private CustomManager customManager;
    @Autowired
    private OrderManager orderManager;
    @Autowired
    private StorageManager storageManager;
    @Autowired
    private CakeManager cakeManager;

    private Map<Long, State> states = new ConcurrentHashMap<>();
    private Map<Long, String> cheesecakeType = new ConcurrentHashMap<>();

    @Override
    public void onUpdateReceived(Update update) {
        if (!userService.checkAccessRights(update.getMessage().getChatId())){
            sendMessage(customManager.sendCustomKeyboard(update.getMessage().getChatId(), State.MAIN, SaycheeseConstants.ACCESS_DENIED));
            return;
        }
        if (update.hasMessage() && update.getMessage().hasText()){
            String message = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            State state = getState(chat_id);
            if (handleHomeButton(chat_id, message))
                return;
            String out = "";
            try {
                switch (state){
                    case MAIN:
                        out = stateService.handleStateMain(message, chat_id, states);
                        sendMessage(customManager.sendCustomKeyboard(chat_id, states.get(chat_id), out));
                        break;
                    //Orders part
                    case ORDERS:
                    case ACTIVE_ORDERS:
                    case ALL_ORDER:
                        stateService.handleStateOrders(message, chat_id, states);
                        sendMessage(orderManager.sendOrdersDialog(chat_id, states.get(chat_id)));
                        break;
                    case ADD_ORDER:
                        orderManager.addOrder(message);
                        sendSimpleMessage(chat_id, SaycheeseConstants.ORDER_SAVED);
                        states.put(chat_id, State.ORDERS);
                        break;
                    case DELETE_ORDER:
                        orderManager.deleteOrder(message);
                        sendSimpleMessage(chat_id, SaycheeseConstants.ORDER_DELETED);
                        states.put(chat_id, State.ORDERS);
                        break;
                    case COMPLETE_ORDER:
                        orderManager.completeOrder(message);
                        sendSimpleMessage(chat_id, SaycheeseConstants.ORDER_COMPETED);
                        states.put(chat_id, State.ORDERS);
                        break;
                    //Storage part
                    case STORAGE:
                    case ALL_STORAGE:
                        stateService.handleStateStorage(message, chat_id, states);
                        sendMessage(storageManager.sendStorageDialog(chat_id, states.get(chat_id)));
                        break;
                    case UPDATE_STORAGE:
                        storageManager.updateStorage(message);
                        states.put(chat_id, State.STORAGE);
                        break;
                    //cook
                    case COOK:
                        stateService.handleStateCook(message, chat_id, states);
                        cheesecakeType.put(chat_id, message);
                        sendSimpleMessage(chat_id, SaycheeseConstants.CHESECAKE_SELECTED);
                        break;
                    case COOK_PROCESSING:
                        sendMessage(cakeManager.constructRecipe(message, cheesecakeType.get(chat_id), chat_id));
                        states.put(chat_id, State.COOK);
                        break;
                    default:
                        sendMessage(customManager.sendCustomKeyboard(chat_id, states.get(chat_id), "Неверное сообщение."));
                        break;
                }
            } catch (OrderException e) {
                sendSimpleMessage(chat_id, e.getMessage());
                states.put(chat_id, State.ORDERS);
            } catch (StorageException e){
                sendSimpleMessage(chat_id, e.getMessage());
                states.put(chat_id, State.STORAGE);
            } catch (CookException e){
                sendSimpleMessage(chat_id, e.getMessage());
                states.put(chat_id, State.COOK);
            }
        }
    }

    private boolean handleHomeButton(long chat_id, String message) {
        if (message.equals(SaycheeseConstants.HOME)){
            states.put(chat_id, State.MAIN);
            sendMessage(customManager.sendCustomKeyboard(chat_id, State.MAIN, "Выберите действие"));
            return true;
        }
        return false;
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


    private void sendMessage(SendMessage msg){
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
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
