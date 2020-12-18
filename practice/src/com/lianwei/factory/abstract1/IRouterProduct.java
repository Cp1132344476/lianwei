package com.lianwei.factory.abstract1;

/**
 * 路由器产品接口
 * @author chenpeng
 */
public interface IRouterProduct {
    void start();
    void shutdown();
    void openWifi();
    void setting();
}
