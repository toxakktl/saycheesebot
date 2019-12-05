package kz.ata.saycheese.service;

import kz.ata.saycheese.constants.SaycheeseConstants;
import kz.ata.saycheese.enums.State;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StateService {

    public void handleStateMain(String message, Long chat_id, Map<Long, State> states) {
        switch (message){
            case SaycheeseConstants.ORDERS:
                states.put(chat_id, State.ORDERS);
                break;
            case SaycheeseConstants.STORAGE:
                states.put(chat_id, State.STORAGE);
                break;
            case SaycheeseConstants.SELL:
                states.put(chat_id, State.SELL);
                break;
            case SaycheeseConstants.REPORTS:
                states.put(chat_id, State.REPORTS);
                break;
            default:
                states.put(chat_id, State.MAIN);
                break;
        }
    }

    public void handleStateOrders(String message, long chat_id, Map<Long, State> states) {
        switch (message){
            case SaycheeseConstants.ACTIVE_ORDERS:
                states.put(chat_id, State.ACTIVE_ORDERS);
                break;
            case SaycheeseConstants.ADD_ORDER:
                states.put(chat_id, State.ADD_ORDER);
                break;
            case SaycheeseConstants.DELETE_ORDER:
                states.put(chat_id, State.DELETE_ORDER);
                break;
            default:
                states.put(chat_id, State.MAIN);
                break;
        }
    }

    public void handleStateStorage(String message, long chat_id, Map<Long, State> states) {
        switch (message){
            case SaycheeseConstants.ALL_STORAGE:
                states.put(chat_id, State.ALL_STORAGE);
                break;
            case SaycheeseConstants.ADD_FOOD:
                states.put(chat_id, State.ADD_FOOD);
                break;
            case SaycheeseConstants.DELETE_FOOD:
                states.put(chat_id, State.DELETE_FOOD);
                break;
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
            case SaycheeseConstants.ADD_FOOD:
                states.put(chat_id, State.ADD_FOOD);
                break;
            case SaycheeseConstants.DELETE_FOOD:
                states.put(chat_id, State.DELETE_FOOD);
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
