package kz.ata.saycheese.managers;

import kz.ata.saycheese.constants.SaycheeseConstants;
import kz.ata.saycheese.enums.OrderState;
import kz.ata.saycheese.enums.State;
import kz.ata.saycheese.exceptions.OrderException;
import kz.ata.saycheese.model.CheesecakeModel;
import kz.ata.saycheese.model.OrderModel;
import kz.ata.saycheese.service.CheesecakeService;
import kz.ata.saycheese.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class OrderManager {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CheesecakeService cheesecakeService;


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

    @Transactional
    public SendMessage sendOrdersDialog(Long chat_id, State state) {
        String out;
        if (state.equals(State.ALL_ORDER)){
            out = constructAllOrdersText();
        }else if (state.equals(State.ACTIVE_ORDERS)){
            out = constructActiveOrderText();
        }else if (state.equals(State.ADD_ORDER)){
            out = constructAddOrderText();
        }else if (state.equals(State.DELETE_ORDER)){
            out = constructDeleteOrderText();
        }else if (state.equals(State.COMPLETE_ORDER)){
            out = constructCompleteOrderText();
        }else {
            out = constructDefaultOrderText();
        }
        SendMessage message = new SendMessage();
        message.enableMarkdown(true);
        message.setChatId(chat_id);
        message.setText(out);
        return message;
    }

    public void addOrder(String message) throws OrderException {
        String[] fields = message.split("\\r?\\n");
        validate(fields);
        OrderModel order = new OrderModel();
        try {
            CheesecakeModel cheesecakeModel = cheesecakeService.findByName(fields[3]);
            if (cheesecakeModel == null){
                throw new OrderException("Введите верное название чизкейка. ");
            }
            order.setCustomerName(fields[0]);
            order.setAddress(fields[1]);
            order.setDeliveryDate(SaycheeseConstants.SIMPLE_DATE_FORMAT.parse(fields[2]));
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

    public void deleteOrder(String message) throws OrderException {
        Long orderId = Long.valueOf(message);
        Optional<OrderModel> order = orderService.findById(orderId);
        if (order.isPresent()){
            orderService.deleteOrder(order.get());
        }else{
            throw new OrderException("Заказ с номером " + orderId + " не найден.");
        }
    }

    public void completeOrder(String message) throws OrderException {
        Long orderId = Long.valueOf(message);
        Optional<OrderModel> order = orderService.findById(orderId);
        if (order.isPresent()){
            OrderModel o = order.get();
            if (!o.getOrderState().equals(OrderState.ACTIVE)){
                throw new OrderException("Заказ не Активный");
            }
            o.setOrderState(OrderState.COMPLETED);
            orderService.saveOrder(o);
        }else{
            throw new OrderException("Заказ с номером " + orderId + " не найден.");
        }
    }

    private String constructAllOrdersText() {
        List<OrderModel> activeOrders = orderService.findAllOrders();
        if (CollectionUtils.isEmpty(activeOrders)){
            return "Нет заказов.";
        }else{
            return constructTextForOrders(activeOrders);
        }
    }

    private String constructActiveOrderText() {
        List<OrderModel> activeOrders = orderService.findAllActiveOrders();
        if (CollectionUtils.isEmpty(activeOrders)){
            return "Нет активных заказов.";
        }else{
            return constructTextForOrders(activeOrders);
        }
    }

    private String constructAddOrderText() {
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

    private String constructTextForOrders(List<OrderModel> orders){
        StringBuilder sb = new StringBuilder();
        for (OrderModel order: orders){
            sb.append("*ID*: ").append(order.getId()).append("\n")
                    .append("*Имя клиента*: ").append(order.getCustomerName()).append("\n")
                    .append("*Адрес*: ").append(order.getAddress()).append("\n")
                    .append("*Дата доставки*: ").append(SaycheeseConstants.SIMPLE_DATE_FORMAT.format(order.getDeliveryDate())).append("\n")
                    .append("*Цена*: ").append(order.getPrice()).append("\n")
                    .append("*Чизкейк*: ").append(order.getCheesecake().getName()).append("\n")
                    .append("*Количество*: ").append(order.getWeight()).append("\n")
                    .append("--------------------").append("\n");
        }
        return sb.toString();
    }

    private String constructDeleteOrderText() {
        return "Введите ID заказа.";
    }

    private String constructCompleteOrderText() {
        return "Введите ID заказа.";
    }

    private String constructDefaultOrderText() {
        return "Выберите операцию с заказами";
    }

    private void validate(String[] fields) throws OrderException {
        if (fields.length <= 0 || fields.length > 5){
            throw new OrderException("Неверный формат полей. Необходимо, чтобы было 5 полей.");
        }
//        if (!fields[2].matches(SaycheeseConstants.DATE_REGEX)){
//            throw new TelegramApiException("Неверный формат даты. Введите дату в формате дд.мм.гггг");
//        }
    }

}
