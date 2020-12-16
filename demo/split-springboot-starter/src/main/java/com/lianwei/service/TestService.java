package com.lianwei.service;

import com.lianwei.properties.Property;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

public class TestService {
    private Property property;

    @Autowired
    public void setProperty(Property property) {
        this.property = property;
    }

    public void splitString(String name) {
        String[] strings = name.split("\\.");
        System.out.println(Arrays.toString(strings));
    }

    public void print() {
        System.out.println(property.toString());
    }
}
