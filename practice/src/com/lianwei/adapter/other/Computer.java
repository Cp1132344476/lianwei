package com.lianwei.adapter.other;

public class Computer {
    public void net(NetToUsb adapter){
        // 上网的具体实现
        adapter.handleRequest();
    }

    public static void main(String[] args) {
        Computer computer = new Computer();
        Adaptee adaptee = new Adaptee();
        Adapter2 adapter = new Adapter2(adaptee);

        computer.net(adapter);
    }
}
