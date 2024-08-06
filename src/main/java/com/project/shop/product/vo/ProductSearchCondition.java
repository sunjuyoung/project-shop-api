package com.project.shop.product.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductSearchCondition {

    private String keyword;

    private int priceGoe;

    private int priceLoe;

    private String orderBy;

    private String direction;



}
