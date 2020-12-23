package com.lianwei.adapter;

/**
 * 创建实现了 AdvancedMediaPlayer 接口的实体类。
 * @author chenpeng
 */
public class VlcPlayer implements AdvancedMediaPlayer{
    @Override
    public void playVlc(String fileName) {
        System.out.println("Playing vlc file. Name: "+ fileName);
    }

    @Override
    public void playMp4(String fileName) {

    }
}
