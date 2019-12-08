package kz.ata.saycheese.repository;

import kz.ata.saycheese.enums.OrderState;
import kz.ata.saycheese.model.OrderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderModel, Long> {

    @Query("select o from OrderModel o where o.orderState = ?1")
    List<OrderModel> finAllActiveOrders(OrderState state);
}
