package com.lianwei.factory.abstract1;

public class XiaoMiFactory implements IProductFactory{
    @Override
    public IphoneProduct iphoneProduct() {
        return new XiaoMiPhone();
    }

    @Override
    public IRouterProduct iRouterProduct() {
        return new XiaoMiRouter();
    }
}
