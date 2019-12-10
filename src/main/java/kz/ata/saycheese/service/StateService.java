package kz.ata.saycheese.service;

import kz.ata.saycheese.constants.SaycheeseConstants;
import kz.ata.saycheese.enums.State;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StateService {

    public String handleStateMain(String message, Long chat_id, Map<Long, State> states) {
        String out = "";
        switch (message){
            case SaycheeseConstants.ORDERS:
                out = "Выберите заказы";
                states.put(chat_id, State.ORDERS);
                break;
            case SaycheeseConstants.STORAGE:
                out = "Выберите действие со складом";
                states.put(chat_id, State.STORAGE);
                break;
            case SaycheeseConstants.SELL:
                out = "Выберите чизкейк";
                states.put(chat_id, State.SELL);
                break;
            case SaycheeseConstants.REPORTS:
                out = "Выберите период отчетности";
                states.put(chat_id, State.REPORTS);
                break;
            case SaycheeseConstants.HOME:
            default:
                out = "Выберите действие";
                states.put(chat_id, State.MAIN);
                break;
        }
        return out;
    }

    public void handleStateOrders(String message, long chat_id, Map<Long, State> states) {
        switch (message){
            case SaycheeseConstants.ALL_ORDERS:
                states.put(chat_id, State.ALL_ORDER);
                break;
            case SaycheeseConstants.ACTIVE_ORDERS:
                states.put(chat_id, State.ACTIVE_ORDERS);
                break;
            case SaycheeseConstants.ADD_ORDER:
                states.put(chat_id, State.ADD_ORDER);
                break;
            case SaycheeseConstants.DELETE_ORDER:
                states.put(chat_id, State.DELETE_ORDER);
                break;
            case SaycheeseConstants.COMPLETE_ORDER:
                states.put(chat_id, State.COMPLETE_ORDER);
                break;
            default:
                states.put(chat_id, State.ORDERS_DEFAULT);
                break;
        }
    }

    public void handleStateStorage(String message, long chat_id, Map<Long, State> states) {
        switch (message){
            case SaycheeseConstants.ALL_STORAGE:
                states.put(chat_id, State.ALL_STORAGE);
                break;
            case SaycheeseConstants.UPDATE_STORAGE:
                states.put(chat_id, State.UPDATE_STORAGE);
                break;
            case SaycheeseConstants.HOME:
            default:
                states.put(chat_id, State.MAIN);
                break;
        }
    }

    public void handleStateSell(String message, long chat_id, Map<Long, State> states) {
        switch (message){
            case SaycheeseConstants.ALL_STORAGE:
                states.put(chat_id, State.ALL_STORAGE);
                break;
            default:
                states.put(chat_id, State.MAIN);
                break;
        }
    }
    public void handleStateReports(String message, long chat_id, Map<Long, State> states) {
        switch (message){
            case SaycheeseConstants.ALL_TIME_REPORT:
                states.put(chat_id, State.ALL_TIME_REPORT);
                break;
            case SaycheeseConstants.PERIOD_REPORT:
                states.put(chat_id, State.PERIOD_REPORT);
                break;
            default:
                states.put(chat_id, State.MAIN);
                break;
        }
    }
}
