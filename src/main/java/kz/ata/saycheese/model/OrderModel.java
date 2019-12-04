package kz.ata.saycheese.model;

import kz.ata.saycheese.enums.OrderState;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "orders")
@SequenceGenerator(name = "id_gen", sequenceName = "order_seq", initialValue = 1000, allocationSize = 1)
public class    OrderModel extends BaseModel{

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "address")
    private String address;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "delivery_date")
    private Date deliveryDate;

    @Column(name = "price")
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "cheesecake_id")
    private CheesecakeModel cheesecake;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private OrderState orderState;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public CheesecakeModel getCheesecake() {
        return cheesecake;
    }

    public void setCheesecake(CheesecakeModel cheesecake) {
        this.cheesecake = cheesecake;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }
}
