package com.bestseller.starbux.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
public class OrderInfo implements Serializable {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long orderId;

    @NotNull
    private long userId;

    @JsonManagedReference(value = "order")
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<CartItem> items = new HashSet<>();

    private double priceOriginal;
    private double priceDiscount;

    public OrderInfo() {

    }

    public OrderInfo(long orderId, long userId, Set<CartItem> items, double priceOriginal, double priceDiscount) {
        this.orderId = orderId;
        this.userId = userId;
        this.items = items;
        this.priceOriginal = priceOriginal;
        this.priceDiscount = priceDiscount;
    }

    public OrderInfo(long userId) {
        this.setUserId(userId);
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Set<CartItem> getItems() {
        return items;
    }

    public void setItems(Set<CartItem> items) {
        this.items = items;
    }

    public double getPriceOriginal() {
        return priceOriginal;
    }

    public void setPriceOriginal(double priceOriginal) {
        this.priceOriginal = priceOriginal;
    }

    public double getPriceDiscount() {
        return priceDiscount;
    }

    public void setPriceDiscount(double priceDiscount) {
        this.priceDiscount = priceDiscount;
    }
}
