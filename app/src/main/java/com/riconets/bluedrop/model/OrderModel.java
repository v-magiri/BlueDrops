package com.riconets.bluedrop.model;

public class OrderModel {
    String OrderId,OrderStatus,ShippingAddress
            ,OrderLatitude,OrderLongitude,OrderCost,OrderTime,OrderTo,OrderBy;

    public OrderModel(String orderId, String orderStatus, String shippingAddress, String orderLatitude,
                      String orderLongitude, String orderCost, String orderTime, String orderTo, String orderBy) {
        this.OrderId = orderId;
        this.OrderStatus = orderStatus;
        this.ShippingAddress = shippingAddress;
        this.OrderLatitude = orderLatitude;
        this.OrderLongitude = orderLongitude;
        this.OrderCost = orderCost;
        this.OrderTime = orderTime;
        this.OrderTo = orderTo;
        this.OrderBy = orderBy;
    }

    public OrderModel() {
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public String getShippingAddress() {
        return ShippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        ShippingAddress = shippingAddress;
    }

    public String getOrderLatitude() {
        return OrderLatitude;
    }

    public void setOrderLatitude(String orderLatitude) {
        OrderLatitude = orderLatitude;
    }

    public String getOrderLongitude() {
        return OrderLongitude;
    }

    public void setOrderLongitude(String orderLongitude) {
        OrderLongitude = orderLongitude;
    }

    public String getOrderCost() {
        return OrderCost;
    }

    public void setOrderCost(String orderCost) {
        OrderCost = orderCost;
    }

    public String getOrderTime() {
        return OrderTime;
    }

    public void setOrderTime(String orderTime) {
        OrderTime = orderTime;
    }

    public String getOrderTo() {
        return OrderTo;
    }

    public void setOrderTo(String orderTo) {
        OrderTo = orderTo;
    }

    public String getOrderBy() {
        return OrderBy;
    }

    public void setOrderBy(String orderBy) {
        OrderBy = orderBy;
    }
}
