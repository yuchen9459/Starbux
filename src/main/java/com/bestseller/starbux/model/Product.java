package com.bestseller.starbux.model;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Basic(optional = false)
    private String name;

    @NotNull
    private Double price;

    @NotNull
    private String categoryType;

    private long salesNum = 0;

    public Product() {

    }

    public Product(long id, String name, double price, String categoryType, long salesNum) {
        this.setId(id);
        this.setName(name);
        this.setPrice(price);
        this.setCategoryType(categoryType);
        this.setSalesNum(salesNum);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public long getSalesNum() {
        return salesNum;
    }

    public void setSalesNum(long salesNum) {
        this.salesNum = salesNum;
    }
}