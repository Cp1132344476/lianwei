package com.lianwei.builder;

/**
 * 指挥:核心。负责指挥构建一个工程；工程如何构建由他来决定
 * @author chenpeng
 */
public class Director {
    public Product build(builder builder){
        builder.buildA();
        builder.buildB();
        builder.buildC();
        builder.buildD();

        return builder.getProduct();
    }
}
