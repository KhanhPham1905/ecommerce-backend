package com.ghtk.ecommercewebsite.entity;

import jakarta.persistence.Id;

import java.sql.Timestamp;
import java.util.List;

public class Product {


    @Id
    private long id;

    private Timestamp createdAt;

    private String description;

    private Timestamp modifiedAt;

    private String name;

    private long price;


    private long salePrice;


    private String slug;

    private int status;


    private long totalSold;

    private int view;

    private Brand brand;


    private List<Shop> shopList;




}
