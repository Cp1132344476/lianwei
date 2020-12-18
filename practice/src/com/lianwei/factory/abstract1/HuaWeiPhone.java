package com.lianwei.factory.abstract1;

public class HuaWeiPhone implements IphoneProduct{
    @Override
    public void start() {
        System.out.println("开启华为手机");
    }

    @Override
    public void shutdown() {
        System.out.println("关闭华为手机");
    }

    @Override
    public void callUp() {
        System.out.println("华为打电话");
    }

    @Override
    public void sendMessage() {
        System.out.println("华为发短信");
    }
}
