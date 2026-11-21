package com.hjay.sdp.create.factory.prototype;

import lombok.Data;

import java.io.Serializable;

@Data
public class Student implements Cloneable, Serializable {

    private String name;

    @Override
    protected Student clone() throws CloneNotSupportedException {
        return (Student) super.clone();
    }
}
