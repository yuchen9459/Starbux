package com.bestseller.starbux.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Cart implements Serializable {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long cartId;

    @NotNull
    private long userId;

    @JsonManagedReference(value = "cart")
    @OneToMany(mappedBy = "cart", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<CartItem> items = new HashSet<>();

    private double priceOriginal;
    private double priceDiscount;


    public Cart() {

    }

    public Cart(Set<CartItem> items, long userId) {
        this.setItems(items);
        this.setUserId(userId);
    }

    public long getCartId() {
        return cartId;
    }

    public void setCartId(long cartId) {
        this.cartId = cartId;
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
