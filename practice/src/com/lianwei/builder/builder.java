package com.lianwei.builder;

public abstract class builder {

    abstract void buildA();     // 地基
    abstract void buildB();     // 钢筋工程
    abstract void buildC();     // 铺电线
    abstract void buildD();     // 粉刷

    /**
     * 得到产品
     * @return
     */
    abstract Product getProduct();
}
