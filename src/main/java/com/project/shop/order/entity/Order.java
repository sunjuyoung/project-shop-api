package com.project.shop.order.entity;

import com.project.shop.customer.entity.Customer;
import com.project.shop.global.domain.BaseTime;
import com.project.shop.order.entity.enums.DeliveryStatus;
import com.project.shop.order.entity.enums.OrderState;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
@Entity(name = "orders")
public class Order extends BaseTime {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;


    @Embedded
    private ShippingInfo shippingInfo;

    @Enumerated(EnumType.STRING)
    private OrderState orderState;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Column(name = "total_price")
    private int totalPrice;
}
