package kz.ata.saycheese.service;

import kz.ata.saycheese.model.OrderModel;
import kz.ata.saycheese.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<OrderModel> findAllOrders(){
        return orderRepository.findAll();
    }
}
