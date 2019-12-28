package com.zhacky.ninjapos.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "PRODUCTS")
public class Product {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", length = 50)
    @NotNull
    private String name;

    @Column(name = "CODE")
    private String code;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PRICE")
    private double price;

    @Column(name = "COST")
    private double cost;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "UNIT_ID")
    private Unit unit;

    @Column(name = "OLD_PRICE")
    private double oldPrice;

    @Column(name = "LAST_UPDATED")
    private Date lastUpdated;


    public Product() {

    }

    public Product(String name) {
        this(name, "", "", 0.0, 0.0, null, 0.0, new Date());
    }

    public Product(@NotNull String name, String code, String description, double price, double cost, Unit unit, double oldPrice, Date lastUpdated) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.price = price;
        this.cost = cost;
        this.unit = unit;
        this.oldPrice = oldPrice;
        this.lastUpdated = lastUpdated;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n<u>Product Information</u>");
        sb.append("\nID: " + id);
        sb.append("\nName: " + name);
        sb.append("\nCode: " + code);
        sb.append("\nDescription: " + description);
        sb.append("\nPrice: " + String.format("%,.2f", price));
        return sb.toString();
    }
}
