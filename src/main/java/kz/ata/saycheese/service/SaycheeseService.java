package kz.ata.saycheese.service;

import kz.ata.saycheese.constants.SaycheeseConstants;
import kz.ata.saycheese.model.CheesecakeModel;
import kz.ata.saycheese.model.OrderModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaycheeseService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CheesecakeService cheesecakeService;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public ReplyKeyboard createMainKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add(SaycheeseConstants.ORDERS);
        row.add(SaycheeseConstants.STORAGE);
        keyboard.add(row);
        row = new KeyboardRow();
        row.add(SaycheeseConstants.SELL);
        row.add(SaycheeseConstants.REPORTS);
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    public ReplyKeyboard createSubordersKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add(SaycheeseConstants.ACTIVE_ORDERS);
        row.add(SaycheeseConstants.ALL_ORDERS);
        keyboard.add(row);
        row = new KeyboardRow();
        row.add(SaycheeseConstants.ADD_ORDER);
        row.add(SaycheeseConstants.DELETE_ORDER);
        keyboard.add(row);
        row = new KeyboardRow();
        row.add(SaycheeseConstants.BACK);
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    public ReplyKeyboard createSubreportsKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add(SaycheeseConstants.ALL_TIME_REPORT);
        row.add(SaycheeseConstants.PERIOD_REPORT);
        keyboard.add(row);
        row = new KeyboardRow();
        row.add(SaycheeseConstants.BACK);
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    public ReplyKeyboard createSubstorageKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add(SaycheeseConstants.ALL_STORAGE);
        keyboard.add(row);
        row = new KeyboardRow();
        row.add(SaycheeseConstants.ADD_FOOD);
        row.add(SaycheeseConstants.DELETE_FOOD);
        keyboard.add(row);
        row = new KeyboardRow();
        row.add(SaycheeseConstants.BACK);
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    public ReplyKeyboard createSellKeyboard(){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        List<CheesecakeModel> cheesecakes = cheesecakeService.findAll();
        for (CheesecakeModel cheesecake : cheesecakes){
            KeyboardRow row = new KeyboardRow();
            row.add(cheesecake.getName());
            keyboard.add(row);
        }
        KeyboardRow row = new KeyboardRow();
        row.add(SaycheeseConstants.BACK);
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }


    public String constructActiveOrdersText() {
        StringBuilder sb = new StringBuilder();
        List<OrderModel> orders = orderService.findAllOrders();
        for (OrderModel order: orders){
            sb.append("*ID*: ").append(order.getId()).append("\n")
                    .append("*Имя клиента*: ").append(order.getCustomerName()).append("\n")
                    .append("*Адрес*: ").append(order.getAddress()).append("\n")
                    .append("*Дата доставки*: ").append(simpleDateFormat.format(order.getDeliveryDate())).append("\n")
                    .append("*Цена*: ").append(order.getPrice()).append("\n")
                    .append("*Чизкейк*: ").append(order.getCheesecake().getName()).append("\n")
                    .append("*Количество*: ").append(order.getQuantity()).append("\n")
                    .append("--------------------").append("\n");
        }
        return sb.toString();
    }

    public String constructAddOrderText() {
        StringBuilder sb = new StringBuilder();
        sb.append("*Введите заказ в формате*: ").append("\n");
        return sb.toString();
    }

    public String constructDefaultText() {
        return "Команда не найдена";
    }
}
