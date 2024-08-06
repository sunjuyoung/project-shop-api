package com.project.shop.cartItem.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartItemListDTO {


    private Long cartId;
    private Long customerId;
    private Long productId;
    private String productName;
    private int price;
    private int quantity;
}
