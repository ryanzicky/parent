package com.miniservices.demo.kafka;

import lombok.Data;
import lombok.ToString;

/**
 * @Author zhourui
 * @Date 2021/9/29 19:44
 */
@Data
@ToString
public class Order {

    private Integer orderId;
    private Integer orderVal;
    private Integer orderSort;
    private Double orderPrice;

    public Order(int orderId, int orderVal, int orderSort, double orderPrice) {
        this.orderId = orderId;
        this.orderVal = orderVal;
        this.orderSort = orderSort;
        this.orderPrice = orderPrice;
    }
}
