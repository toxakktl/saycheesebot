package kz.ata.saycheese.service;

import kz.ata.saycheese.constants.SaycheeseConstants;
import kz.ata.saycheese.enums.OrderState;
import kz.ata.saycheese.enums.Unit;
import kz.ata.saycheese.model.CheesecakeModel;
import kz.ata.saycheese.model.FoodModel;
import kz.ata.saycheese.model.OrderModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SaycheeseService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private CheesecakeService cheesecakeService;

    @Autowired
    private FoodService foodService;

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
        row.add(SaycheeseConstants.COMPLETE_ORDER);
        row.add(SaycheeseConstants.HOME);
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
        row.add(SaycheeseConstants.HOME);
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
        row.add(SaycheeseConstants.UPDATE_STORAGE);
        keyboard.add(row);
        row = new KeyboardRow();
        row.add(SaycheeseConstants.HOME);
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
        row.add(SaycheeseConstants.HOME);
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }


    public String constructAllOrdersText() {
        List<OrderModel> activeOrders = orderService.findAllOrders();
        if (CollectionUtils.isEmpty(activeOrders)){
            return "Нет заказов.";
        }else{
            return constructTextForOrders(activeOrders);
        }
    }

    public String constructAddOrderText() {
        StringBuilder sb = new StringBuilder();
        sb.append("*Введите заказ в формате*: ").append("\n").
                append("Имя кклиента\n").
                append("Адрес\n").
                append("Дата доставки в формате дд.мм.гггг\n").
                append("Название чизкейка маленькими буквами\n").
                append("Вес в кг.\n\n").
                append("*Пример: *\n").
                append("Тохтар\nЕгизбаева 7/5 кв. 29\n12.12.2019\nфисташковый\n1.5");
        return sb.toString();
    }

    public String constructDeleteOrderText() {
        return "Введите ID заказа.";
    }

    public String constructCompleteOrderText() {
        return "Введите ID заказа.";
    }

    public String constructDefaultOrderText() {
        return "Выберите операцию с заказами";
    }

    public String constructDefaultStorageText() {
        return "Выберите операцию со складом";
    }

    public String constructActiveOrderText() {
        List<OrderModel> activeOrders = orderService.findAllActiveOrders();
        if (CollectionUtils.isEmpty(activeOrders)){
            return "Нет активных заказов.";
        }else{
            return constructTextForOrders(activeOrders);
        }
    }

    private String constructTextForOrders(List<OrderModel> orders){
        StringBuilder sb = new StringBuilder();
        for (OrderModel order: orders){
            sb.append("*ID*: ").append(order.getId()).append("\n")
                    .append("*Имя клиента*: ").append(order.getCustomerName()).append("\n")
                    .append("*Адрес*: ").append(order.getAddress()).append("\n")
                    .append("*Дата доставки*: ").append(simpleDateFormat.format(order.getDeliveryDate())).append("\n")
                    .append("*Цена*: ").append(order.getPrice()).append("\n")
                    .append("*Чизкейк*: ").append(order.getCheesecake().getName()).append("\n")
                    .append("*Количество*: ").append(order.getWeight()).append("\n")
                    .append("--------------------").append("\n");
        }
        return sb.toString();
    }

    public void addOrder(String[] fields) throws TelegramApiException {
        validate(fields);
        OrderModel order = new OrderModel();
        try {
            CheesecakeModel cheesecakeModel = cheesecakeService.findByName(fields[3]);
            if (cheesecakeModel == null){
                throw new TelegramApiException("Введите верное название чизкейка. ");
            }
            order.setCustomerName(fields[0]);
            order.setAddress(fields[1]);
            order.setDeliveryDate(simpleDateFormat.parse(fields[2]));
            order.setCheesecake(cheesecakeModel);
            order.setWeight(new BigDecimal(fields[4]));
            order.setCreatedDate(new Date());
            order.setOrderState(OrderState.ACTIVE);
            order.setPrice(cheesecakeModel.getPricePerKg().multiply(order.getWeight()));
            orderService.saveOrder(order);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void deleteOrder(String message) throws TelegramApiException {
        Long orderId = Long.valueOf(message);
        Optional<OrderModel> order = orderService.findById(orderId);
        if (order.isPresent()){
            orderService.deleteOrder(order.get());
        }else{
            throw new TelegramApiException("Заказ с номером " + orderId + " не найден.");
        }
    }

    public void completeOrder(String message) throws TelegramApiException {
        Long orderId = Long.valueOf(message);
        Optional<OrderModel> order = orderService.findById(orderId);
        if (order.isPresent()){
            OrderModel o = order.get();
            if (!o.getOrderState().equals(OrderState.ACTIVE)){
                throw new TelegramApiException("Заказ не Активный");
            }
            o.setOrderState(OrderState.COMPLETED);
            orderService.saveOrder(o);
        }else{
            throw new TelegramApiException("Заказ с номером " + orderId + " не найден.");
        }
    }

    private void validate(String[] fields) throws TelegramApiException {
        if (fields.length <= 0 || fields.length > 5){
            throw new TelegramApiException("Неверный формат полей. Необходимо, чтобы было 5 полей.");
        }
//        if (!fields[2].matches(SaycheeseConstants.DATE_REGEX)){
//            throw new TelegramApiException("Неверный формат даты. Введите дату в формате дд.мм.гггг");
//        }
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

    public String constructDeleteFoodText() {
        return "none";
    }

    public void updateStorage(String[] fields) throws TelegramApiException {
        validateStorageFields(fields);
        FoodModel foodModel = foodService.findByName(fields[0]);
        if (foodModel == null){
            throw new TelegramApiException("Продукт не найден. ");
        }
        foodModel.setQuantity(new BigDecimal(fields[1]));
        foodModel.setUnit(Unit.valueOf(fields[2]));
        foodService.save(foodModel);
    }

    private void validateStorageFields(String[] fields) throws TelegramApiException {
        if (fields.length <= 0 || fields.length > 3){
            throw new TelegramApiException("Неверный формат полей. Необходимо, чтобы было 5 полей.");
        }
    }
}

