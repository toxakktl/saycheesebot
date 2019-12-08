package kz.ata.saycheese.service;

import kz.ata.saycheese.enums.OrderState;
import kz.ata.saycheese.model.OrderModel;
import kz.ata.saycheese.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<OrderModel> findAllOrders(){
        return orderRepository.findAll();
    }

    public List<OrderModel> findAllActiveOrders(){
        return orderRepository.finAllActiveOrders(OrderState.ACTIVE);
    }

    public void saveOrder(OrderModel orderModel){
        orderRepository.save(orderModel);
    }
    public void deleteOrder(OrderModel orderModel){
        orderRepository.delete(orderModel);
    }

    Optional<OrderModel> findById(Long id){
        return orderRepository.findById(id);
    }
}
