package com.lianwei.factory.abstract1;

public class HuaWeiFactory implements IProductFactory{
    @Override
    public IphoneProduct iphoneProduct() {
        return new HuaWeiPhone();
    }

    @Override
    public IRouterProduct iRouterProduct() {
        return new HuaWeiRouter();
    }
}
