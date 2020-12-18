package com.lianwei.factory.abstract1;

/**
 * @author chenpeng
 * 抽象产品工厂
 */
public interface IProductFactory {
    // 生产手机
    IphoneProduct iphoneProduct();
    // 生产路由器
    IRouterProduct iRouterProduct();
}
