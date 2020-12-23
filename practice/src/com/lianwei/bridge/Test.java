package com.lianwei.bridge;

public class Test {
    public static void main(String[] args) {
        Computer computer = new Laptop(new Apple());
        computer.info();

        Computer computer1 = new Desktop(new Lenovo());
        computer1.info();
    }
}
