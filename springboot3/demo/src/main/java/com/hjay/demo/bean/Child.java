package com.hjay.demo.bean;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Child {

    private String name;
    private int  age;
    private Date birthday;
    private List<String> test;
}
