package com.lianwei.factory.abstract1;

public class Client {
    public static void main(String[] args) {
        System.out.println("===========小米系列产品==========");
        XiaoMiFactory xiaoMiFactory = new XiaoMiFactory();
        IphoneProduct iphoneProduct = xiaoMiFactory.iphoneProduct();
        iphoneProduct.start();
        iphoneProduct.callUp();
        iphoneProduct.sendMessage();
        iphoneProduct.shutdown();
        IRouterProduct iRouterProduct = xiaoMiFactory.iRouterProduct();
        iRouterProduct.start();
        iRouterProduct.openWifi();
        iRouterProduct.setting();
        iRouterProduct.shutdown();
        System.out.println("===========华为系列产品==========");
        HuaWeiFactory huaWeiFactory = new HuaWeiFactory();
        IphoneProduct iphoneProduct1 = huaWeiFactory.iphoneProduct();
        IRouterProduct iRouterProduct1 = huaWeiFactory.iRouterProduct();
        iphoneProduct1.start();
        iphoneProduct1.callUp();
        iphoneProduct1.sendMessage();
        iphoneProduct1.shutdown();
        iRouterProduct1.start();
        iRouterProduct1.openWifi();
        iRouterProduct1.setting();
        iRouterProduct1.shutdown();
    }
}
